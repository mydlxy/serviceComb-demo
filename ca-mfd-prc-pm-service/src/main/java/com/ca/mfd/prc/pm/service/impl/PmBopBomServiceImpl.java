package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;

import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.pm.communication.entity.MidMBomEntity;
import com.ca.mfd.prc.pm.dto.WorkshopCodeMaterialRelaDTO;
import com.ca.mfd.prc.pm.entity.PmBopBomEntity;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import com.ca.mfd.prc.pm.entity.PmPfmeaEntity;
import com.ca.mfd.prc.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pm.entity.PmVersionEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pm.mapper.IPmBopBomMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkStationMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkstationMaterialMapper;
import com.ca.mfd.prc.pm.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pm.service.IPmBopBomService;
import com.ca.mfd.prc.pm.service.IPmProductMaterialMasterService;
import com.ca.mfd.prc.pm.service.IPmWorkstationMaterialService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author inkelink
 * @Description: MBOM详情服务实现
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@Service
public class PmBopBomServiceImpl extends AbstractCrudServiceImpl<IPmBopBomMapper, PmBopBomEntity> implements IPmBopBomService {

    public static final String WORKSHOP_CODE = "workshopCode";
    public static final String WORKSTATION_CODE = "workstationCode";

    public static final String FEATURE_ALL_MODELS = "ALLMODELS";

    public static final String MODULE_CODE_DIANCI = "F300101";
    public static final String[] WORKSHOP_CODES = {"WE","PA","GA"};

    @Autowired
    @Qualifier("pmThreadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private IPmWorkstationMaterialMapper pmWorkstationMaterialMapper;
    @Autowired
    private IPmWorkStationMapper pmWorkstationMapper;
    @Autowired
    private IPmWorkstationMaterialService pmWorkstationMaterialService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private IPmProductMaterialMasterService pmProductMaterialMasterService;

    @Override
    public void beforeInsert(PmBopBomEntity entity) {
        upDateMaterialList(entity);
    }

    @Override
    public void beforeUpdate(PmBopBomEntity entity) {
        upDateMaterialList(entity);
    }

    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_BOP_BOM";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmBopBomEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmBopBomEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmBopBomEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmBopBomEntity> updateWrapper) {
        removeCache();
    }


    @Override
    public List<PmBopBomEntity> getAllDatas() {
        List<PmBopBomEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas);
                }
            }
        }
        return datas;
    }

    /**
     * 同步修改物料清单数据
     */
    private void upDateMaterialList(PmBopBomEntity entity) {
        if (StringUtils.isBlank(entity.getRowNum()) || entity.getRowNum().equals("0")) {
            entity.setRowNum(String.valueOf(IdGenerator.getId()));
        }
        if (StringUtils.isBlank(entity.getMaterialCode())) {
            throw new InkelinkException("物料号不能为空");
        }
        validDataUnique(entity.getId(), "ROW_NUM", entity.getRowNum(), "已经存在bom行号为%s的数据,bom行号必须全厂间唯一",null,null);
        QueryWrapper<PmWorkstationMaterialEntity> wlQr = new QueryWrapper<>();
        wlQr.lambda().eq(PmWorkstationMaterialEntity::getIsDelete, false)
                .eq(PmWorkstationMaterialEntity::getRowNumAndMaterialNo, entity.getRowNumAndMaterialNo());
        List<PmWorkstationMaterialEntity> workstationMaterials = pmWorkstationMaterialMapper.selectList(wlQr);
        UpdateWrapper<PmWorkstationMaterialEntity> uw = new UpdateWrapper<>();
        LambdaUpdateWrapper<PmWorkstationMaterialEntity> luw = uw.lambda();
        if (workstationMaterials != null) {
            for (PmWorkstationMaterialEntity et : workstationMaterials) {
                luw.set(PmWorkstationMaterialEntity::getMasterChinese, entity.getMaterialName());
                luw.set(PmWorkstationMaterialEntity::getFeatureCode, entity.getUsageValue());
                luw.set(PmWorkstationMaterialEntity::getMaterialNo, entity.getMaterialCode());
                luw.set(PmWorkstationMaterialEntity::getAttribute1, entity.getRowNum());
                luw.set(PmWorkstationMaterialEntity::getAttribute2, entity.getRdSupplyStatus());
                luw.set(PmWorkstationMaterialEntity::getAttribute3, entity.getQuantity());
                //BOP行号 attribute1
                //供货状态  attribute2
                //用量  attribute3
                luw.eq(PmWorkstationMaterialEntity::getId, et.getId());
                luw.eq(PmWorkstationMaterialEntity::getVersion, et.getVersion());
                luw.eq(PmWorkstationMaterialEntity::getIsDelete, false);
                pmWorkstationMaterialService.update(uw);
            }
        }
    }

    @Override
    public PageData<PmBopBomEntity> page(PageDataDto model) {
        if (!model.getConditions().isEmpty()) {
            List<String> workstationsCodes = model.getConditions().stream().filter(c -> StringUtils.isNotBlank(c.getValue()) &&
                            StringUtils.endsWithIgnoreCase(c.getColumnName(), WORKSTATION_CODE))
                    .map(c -> c.getValue()).distinct().collect(Collectors.toList());
            List<ConditionDto> conditions = model.getConditions().stream().filter(c -> StringUtils.endsWithIgnoreCase(c.getColumnName(), WORKSTATION_CODE))
                    .collect(Collectors.toList());
            if (conditions != null) {
                model.getConditions().removeAll(conditions);
            }
            if (workstationsCodes != null && !workstationsCodes.isEmpty()) {
                //查询物料号
                QueryWrapper<PmWorkstationMaterialEntity> wlQr = new QueryWrapper<>();
                wlQr.lambda().eq(PmWorkstationMaterialEntity::getIsDelete, false)
                        .in(PmWorkstationMaterialEntity::getWorkstationCode, workstationsCodes);
                List<PmWorkstationMaterialEntity> workstationMaterials = pmWorkstationMaterialMapper.selectList(wlQr);
                List<String> materialNos = new ArrayList<>(workstationMaterials.size());
                if (workstationMaterials != null && !workstationMaterials.isEmpty()) {
                    materialNos = workstationMaterials.stream().map(PmWorkstationMaterialEntity::getRowNumAndMaterialNo)
                            .distinct().collect(Collectors.toList());
                }
                //物料号查询
                if (materialNos != null && !materialNos.isEmpty()) {
                    String rowNum = getRowNums(materialNos);
                    ConditionDto metlDto = new ConditionDto("rowNum", rowNum, ConditionOper.In);
                    model.getConditions().add(metlDto);
                } else {
                    PageData<PmBopBomEntity> pages = new PageData<>();
                    pages.setPageIndex(1);
                    pages.setPageSize(20);
                    return pages;
                }
            }
        }
        PageData<PmBopBomEntity> pages = super.page(model);
        List<PmBopBomEntity> boms = pages.getDatas();
        if (!boms.isEmpty()) {
            List<String> materialCodes = new ArrayList<>(boms.size());
            boms.stream().forEach(item -> materialCodes.add(item.getRowNumAndMaterialNo()));
            QueryWrapper<PmWorkstationMaterialEntity> wqWm = new QueryWrapper<>();
            LambdaQueryWrapper<PmWorkstationMaterialEntity> lwq = wqWm.lambda();
            if (!materialCodes.isEmpty()) {
                lwq.in(PmWorkstationMaterialEntity::getRowNumAndMaterialNo, materialCodes);
            }
            lwq.eq(PmWorkstationMaterialEntity::getIsDelete, false);
            lwq.orderByAsc(PmWorkstationMaterialEntity::getWorkstationCode);
            List<ConditionDto> conditions = model.getConditions();
            if (!conditions.isEmpty() && WORKSHOP_CODE.equals(conditions.get(0).getColumnName())) {
                lwq.eq(PmWorkstationMaterialEntity::getWorkshopCode, conditions.get(0).getValue());
            }
            List<PmWorkstationMaterialEntity> workstationMaterials = pmWorkstationMaterialMapper.selectList(wqWm);
            Map<String, List<String>> workstationCodeByMaterialCode = new HashMap<>(workstationMaterials.size() / 2);
            Map<String, String> useTypeByMaterialCode = new HashMap<>(workstationMaterials.size());
            if (!workstationMaterials.isEmpty()) {
                for (PmWorkstationMaterialEntity pmWorkstationMaterialEntity : workstationMaterials) {
                    workstationCodeByMaterialCode.computeIfAbsent(pmWorkstationMaterialEntity.getRowNumAndMaterialNo(), v -> new ArrayList<>()).add(pmWorkstationMaterialEntity.getWorkstationCode() + "[" + pmWorkstationMaterialEntity.getMaterialNum() + "]");
                    useTypeByMaterialCode.put(pmWorkstationMaterialEntity.getRowNumAndMaterialNo(),getUseType(pmWorkstationMaterialEntity));
                }
            }
            for (PmBopBomEntity pmBopBomEntity : boms) {
                List<String> workstationCodes = workstationCodeByMaterialCode.get(pmBopBomEntity.getRowNumAndMaterialNo());
                String useType = useTypeByMaterialCode.get(pmBopBomEntity.getRowNumAndMaterialNo());
                if (workstationCodes != null && !workstationCodes.isEmpty()) {
                    pmBopBomEntity.setWorkstationCodes(String.join(useType, workstationCodes));
                }
                pmBopBomEntity.setBreakPointFlag(StringUtils.isNotBlank(pmBopBomEntity.getBreakPointCode()));
            }

        }
        return pages;
    }

    private String getUseType(PmWorkstationMaterialEntity pmWorkstationMaterialEntity){
        String useType = "";
        if(pmWorkstationMaterialEntity.getUseType() == 0){
            useType = "或";
        }else if(pmWorkstationMaterialEntity.getUseType() == 2){
            useType = "且";
        }
        return useType;
    }

    private String getRowNums(List<String> rowNumMaterialNos) {
        List<String> rowNum = new ArrayList<>(rowNumMaterialNos.size());
        rowNumMaterialNos.stream().forEach(item ->
                rowNum.add(item.split("\\|")[0])
        );
        return String.join("|", rowNum);
    }

    @Override
    public void syncFromMBom(List<MidMBomEntity> datas) {
        if (CollectionUtils.isNotEmpty(datas)) {
            List<PmBopBomEntity> collectFromBopBom = this.getAllDatas();
            Map<String,PmBopBomEntity> rowNumAndMaterialCodeBomBopMap = new HashMap<>(collectFromBopBom.size());
            for(PmBopBomEntity pmBopBomEntity : collectFromBopBom){
                rowNumAndMaterialCodeBomBopMap.put(pmBopBomEntity.getRowNumAndMaterialNo(),pmBopBomEntity);
            }
            List<ComboInfoDTO> comboDatas = sysConfigurationProvider.getComboDatas("MBOM_SHOPCODE");
            List<MidMBomEntity> update = new ArrayList<>(collectFromBopBom.size());
            List<PmBopBomEntity> updateBopBom = new ArrayList<>(collectFromBopBom.size());
            List<MidMBomEntity> insert = new ArrayList<>(collectFromBopBom.size());
            for(MidMBomEntity midMBomEntity : datas){
                String key = midMBomEntity.getLineNumber() + "|" + midMBomEntity.getMaterialCode();
                if(rowNumAndMaterialCodeBomBopMap.keySet().contains(key)){
                    PmBopBomEntity pmBopBomEntity = rowNumAndMaterialCodeBomBopMap.get(key);
                    midMBomEntity.setId(pmBopBomEntity.getId());
                    update.add(midMBomEntity);
                }else{
                    insert.add(midMBomEntity);
                }
            }
            //新增
            this.insertBatch(insert.stream().map(c -> {
                PmBopBomEntity entity = new PmBopBomEntity();
                setBopBomEntity(entity, c, comboDatas);
                return entity;
            }).collect(Collectors.toList()), 200, false, 1);
            this.saveChange();
            //修改
            this.updateBatchById(update.stream().map(c -> {
                PmBopBomEntity entity = new PmBopBomEntity();
                entity.setId(c.getId());
                setBopBomEntity(entity, c, comboDatas);
                updateBopBom.add(entity);
                return entity;
            }).collect(Collectors.toList()), 200, false);
            this.saveChange();

            threadPoolTaskExecutor.submit(() -> {
                try{
                    updateWorkStationMaterial(updateBopBom);
                }catch (Exception e){
                    log.error("同步工位物料关系出错:"+e.getMessage(),e);
                }

            });
        }

    }

    private void updateWorkStationMaterial(List<PmBopBomEntity> datas){
        List<PmWorkstationMaterialEntity> pmWorkstationMaterials = pmWorkstationMaterialService.getData(null);
        if(!pmWorkstationMaterials.isEmpty() && !datas.isEmpty()){
            Map<String,PmBopBomEntity> rowNumAndMaterialCodeMidBopMap = new HashMap<>(datas.size());
            for(PmBopBomEntity pmBopBomEntity : datas){
                String key = pmBopBomEntity.getRowNumAndMaterialNo();
                rowNumAndMaterialCodeMidBopMap.put(key,pmBopBomEntity);
            }
            List<PmWorkstationMaterialEntity> workstationMaterials = new ArrayList<>(pmWorkstationMaterials.size());
            for(PmWorkstationMaterialEntity pmWorkstationMaterialEntity : pmWorkstationMaterials){
                String key = pmWorkstationMaterialEntity.getRowNumAndMaterialNo();
                if(rowNumAndMaterialCodeMidBopMap.containsKey(key)){
                    PmBopBomEntity pmBopBomEntity = rowNumAndMaterialCodeMidBopMap.get(key);
                    pmWorkstationMaterialEntity.setFeatureCode(pmBopBomEntity.getUsageValue());
                    pmWorkstationMaterialEntity.setMasterChinese(pmBopBomEntity.getMaterialName());
                    workstationMaterials.add(pmWorkstationMaterialEntity);
                }
            }
            for(PmWorkstationMaterialEntity workstationMaterial : workstationMaterials){
                pmWorkstationMaterialService.update(workstationMaterial,false);
            }
            this.saveChange();
        }


    }

    private void setBopBomEntity(PmBopBomEntity entity, MidMBomEntity c, List<ComboInfoDTO> comboDatas) {
        entity.setFromMbomId(String.valueOf(c.getMBomId()));
        entity.setMaterialCode(c.getMaterialCode());
        entity.setMasterMaterialCode(c.getParentMaterialCode());
        entity.setQuantity(c.getQuantity());
        entity.setMeasureUnit(c.getMeasureUnit());
        entity.setMaterialTypeCode(c.getMaterialType());
        entity.setRdSupplyStatus(c.getRdSupplyStatus());
        entity.setManufacturingSupplyStatus(c.getManufacturingSupplyStatus());
        entity.setUsageDesc(c.getUsageDesc());
        String workShop = "";
        if (!CollectionUtils.isEmpty(comboDatas)) {
            ComboInfoDTO comboInfoDTO = comboDatas.stream().filter(x -> c.getUseProcessTypeCode().equals(x.getValue())).findFirst().orElse(null);
            if (comboInfoDTO == null) {
                workShop = "";
            } else {
                workShop = comboInfoDTO.getText();
            }
        }
        entity.setUseWorkShop(workShop);
        String usageValue;
        String bomRoom = getBomRoom(entity,c);
        if (StringUtils.isBlank(c.getUsageValue())
                || c.getUsageValue().equals("ALL")) {
            usageValue = bomRoom + ":ALLTYPES;";
        } else {
            usageValue = bomRoom + ":" + c.getUsageValue();
        }
        entity.setUsageValue(usageValue);
        entity.setModuleCode(c.getModuleCode());
        entity.setModuleName(c.getModuleName());
        entity.setCompositesNum(c.getComponentGroupNumber());
        entity.setReplaceGroup(c.getReplaceGroup());
        entity.setSupportGroup(c.getSupportGroup());
        entity.setEmployeeidDisplayName(c.getEmployeeIdDisplayName());
        entity.setUsePlant(c.getUsePlant());
        entity.setManufacturingPlant(c.getManufacturingPlant());
        String manufacturingWorkShop = "";
        if (!CollectionUtils.isEmpty(comboDatas)) {
            ComboInfoDTO comboInfoDTO = comboDatas.stream().filter(x -> c.getManufacturingProcessTypeCode().equals(x.getValue())).findFirst().orElse(null);
            if (comboInfoDTO == null) {
                manufacturingWorkShop = "";
            } else {
                manufacturingWorkShop = comboInfoDTO.getText();
            }
        }
        entity.setManufacturingWorkshop(manufacturingWorkShop);
        entity.setProcessLevel(c.getProcessLevel());
        entity.setMbomEngineerName(c.getMbomEngineerIdDisplayName());
        entity.setMbomEngineerUpdatedate(DateUtils.format(c.getMbomEngineerUpdateDate(), DateUtils.DATE_TIME_PATTERN));
        entity.setTechnicsEngineerName(c.getTechnicsEngineerIdDisplayName());
        entity.setTechnicsEngineerUpdatedate(DateUtils.format(c.getTechnicsEngineerUpdateDate(), DateUtils.DATE_TIME_PATTERN));
        entity.setSourcEchangeCode(c.getSourceChangeCode());
        entity.setChangeCode(c.getChangeCode());
        entity.setBreakPointCode(c.getBreakPointCode());
        entity.setBreakPointChangesTatus(c.getBreakPointChangeStatus());
        entity.setEffectiveFrom(c.getEffectiveFrom());
        entity.setEffectiveTo(c.getEffectiveTo());
        entity.setRowNum(c.getLineNumber());
        entity.setMaterialName(c.getMaterialNameCh());
        ClassUtil.defaultValue(entity);
    }

    private String getBomRoom(PmBopBomEntity entity,MidMBomEntity c){
        String bomRoom = FEATURE_ALL_MODELS;
        String workShopCode = entity.getUseWorkShop();
        if(Arrays.asList(WORKSHOP_CODES).contains(workShopCode)
            && !MODULE_CODE_DIANCI.equals(c.getModuleCode())){
            if(StringUtils.isNotBlank(c.getAttribute1())){
                bomRoom = c.getAttribute1();
            }
        }else if(StringUtils.isNotBlank(c.getParentMaterialCode())){
            bomRoom = c.getParentMaterialCode();
        }
        return bomRoom;
    }

    @Override
    public List<String> getFeatureFromBom(String workshopCode, String workStationCode) {
        List<String> rowNums = new ArrayList<>();
        if (StringUtils.isNotBlank(workStationCode)) {
            QueryWrapper<PmWorkstationMaterialEntity> workstationMaterialQw = new QueryWrapper<>();
            workstationMaterialQw.lambda().eq(PmWorkstationMaterialEntity::getWorkstationCode, workStationCode);
            pmWorkstationMaterialService.getData(workstationMaterialQw, false).stream()
                    .map(PmWorkstationMaterialEntity::getRowNumAndMaterialNo).forEach(item -> {
                        if (item.indexOf("|") > 0) {
                            rowNums.add(item.split("\\|")[0]);
                        }
                    });
        }
        QueryWrapper<PmBopBomEntity> wlQr = new QueryWrapper<>();
        if (StringUtils.isNotBlank(workshopCode)) {
            wlQr.lambda().eq(PmBopBomEntity::getUseWorkShop, workshopCode);
        }
        if (!rowNums.isEmpty()) {
            wlQr.lambda().in(PmBopBomEntity::getRowNum, rowNums);
        }
        return this.getData(wlQr, false).stream().map(PmBopBomEntity::getUsageValue).distinct().collect(Collectors.toList());
    }


    public PmBopBomEntity getBopBomByRowNumAndMaterialNo(String bomRowNum, String materialNo) {
        QueryWrapper<PmBopBomEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmBopBomEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmBopBomEntity::getMaterialCode, materialNo);
        lambdaQueryWrapper.eq(PmBopBomEntity::getRowNum, bomRowNum);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public void copyProductMaterialMasterToMBom(WorkshopCodeMaterialRelaDTO workshopCodeMaterialRelaDTO) {
        QueryWrapper<PmProductMaterialMasterEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmProductMaterialMasterEntity> lqw = qw.lambda();
        lqw.in(PmProductMaterialMasterEntity::getMaterialNo, workshopCodeMaterialRelaDTO.getMaterialNos());
        List<PmProductMaterialMasterEntity> productMaterials = pmProductMaterialMasterService.getData(qw, false);
        if (productMaterials.isEmpty()) {
            throw new InkelinkException("没有查询到任何物料");
        }
        for (PmProductMaterialMasterEntity pmProductMaterialMasterEntity : productMaterials) {
            this.save(copyProductMaterialMasterToMBom(pmProductMaterialMasterEntity, workshopCodeMaterialRelaDTO.getShopCode()));
        }
        this.saveChange();
    }

    private PmBopBomEntity copyProductMaterialMasterToMBom(PmProductMaterialMasterEntity pmProductMaterialMasterEntity, String workshopCode) {
        PmBopBomEntity pmBopBomEntity = new PmBopBomEntity();
        pmBopBomEntity.setRowNum(String.valueOf(IdGenerator.getId()));
        pmBopBomEntity.setUseWorkShop(workshopCode);
        pmBopBomEntity.setUsePlant(pmProductMaterialMasterEntity.getOrg());
        pmBopBomEntity.setMaterialCode(pmProductMaterialMasterEntity.getMaterialNo());
        pmBopBomEntity.setMaterialName(pmProductMaterialMasterEntity.getMaterialCn());
        pmBopBomEntity.setMeasureUnit(pmProductMaterialMasterEntity.getUnit());
        pmBopBomEntity.setUsageValue("ALLMODELS:ALLTYPES");
        pmBopBomEntity.setQuantity(String.valueOf(pmProductMaterialMasterEntity.getPackageCount()));
        pmBopBomEntity.setModuleCode(pmProductMaterialMasterEntity.getPartGroup());
        return pmBopBomEntity;
    }

    @Override
    public void delete(Serializable[] ids) {
        if (ids.length == 0) {
            throw new InkelinkException("请选择要删除的bom");
        }
        QueryWrapper<PmBopBomEntity> qwBom = new QueryWrapper<>();
        LambdaQueryWrapper<PmBopBomEntity> lqwBom = qwBom.lambda();
        lqwBom.in(PmBopBomEntity::getId, ids);
        List<PmBopBomEntity> pmBopBomEntities = this.getData(qwBom, false);
        if (pmBopBomEntities.isEmpty()) {
            throw new InkelinkException("mbom不存在或已被删除");
        }
        List<String> rowNumAndMaterialNos = pmBopBomEntities.stream().map(PmBopBomEntity::getRowNumAndMaterialNo).collect(Collectors.toList());
        deleteWorkStationMaterialsByRowNumAndMaterialNo(rowNumAndMaterialNos);
        this.delete(ids, true);
    }

    private void deleteWorkStationMaterialsByRowNumAndMaterialNo(List<String> rowNumAndMaterialNos) {
        UpdateWrapper<PmWorkstationMaterialEntity> qw = new UpdateWrapper<>();
        LambdaUpdateWrapper<PmWorkstationMaterialEntity> lqw = qw.lambda();
        lqw.set(PmWorkstationMaterialEntity::getIsDelete, true);
        lqw.in(PmWorkstationMaterialEntity::getRowNumAndMaterialNo, rowNumAndMaterialNos);
        lqw.eq(PmWorkstationMaterialEntity::getIsDelete, false);
        pmWorkstationMaterialService.update(qw);
    }

    public void dealExcelDatas(List<Map<String, Object>> datas) {
        Map<String,String> workstationMaterialNums = getWorkstationMaterialNums();
        for(Map<String,Object> eachItem : datas){
            String rowAndMaterialNo = eachItem.get("rowNum") + "|" + eachItem.get("materialCode");
            String materialNums = workstationMaterialNums.get(rowAndMaterialNo);
            eachItem.put("workstationCodes",StringUtils.isBlank(materialNums) ? "" : materialNums);
        }
    }

    private Map<String,String> getWorkstationMaterialNums(){
        QueryWrapper<PmWorkstationMaterialEntity> qw = new QueryWrapper();
        List<PmWorkstationMaterialEntity> pmWorkstationMaterialEntities = pmWorkstationMaterialService.getData(qw,false);
        Map<String,List<String>> rowNumAndMaterialNoAndMaterialNum = new HashMap<>(pmWorkstationMaterialEntities.size());
        Map<String,String> rowNumAndMaterialNoAndMaterialNumStr = new HashMap<>(pmWorkstationMaterialEntities.size());
        Map<String, String> useTypeByMaterialCode = new HashMap<>(pmWorkstationMaterialEntities.size());
        for(PmWorkstationMaterialEntity pmWorkstationMaterialEntity : pmWorkstationMaterialEntities){
            String workstationMaterialNums = pmWorkstationMaterialEntity.getWorkstationCode() + "[" + pmWorkstationMaterialEntity.getMaterialNum() + "]";
            rowNumAndMaterialNoAndMaterialNum.computeIfAbsent(pmWorkstationMaterialEntity.getRowNumAndMaterialNo(),v -> new ArrayList<>()).add(workstationMaterialNums);
            useTypeByMaterialCode.put(pmWorkstationMaterialEntity.getRowNumAndMaterialNo(),getUseType(pmWorkstationMaterialEntity));
        }
        for(Map.Entry<String,List<String>> eachItem : rowNumAndMaterialNoAndMaterialNum.entrySet()){
            String useType = useTypeByMaterialCode.get(eachItem.getKey());
            rowNumAndMaterialNoAndMaterialNumStr.put(eachItem.getKey(),String.join(useType,eachItem.getValue()));
        }
        return rowNumAndMaterialNoAndMaterialNumStr;
    }

    @Override
    public void importExcel(InputStream is) throws Exception {
        Map<String, String> fieldParam = getExcelColumnNames();
        List<Map<String, String>> dicDatasFromExcel = InkelinkExcelUtils.importExcel(is, fieldParam);
        validImportDatas(dicDatasFromExcel, fieldParam);
        List<PmBopBomEntity> entities = convertExcelDataToEntity(dicDatasFromExcel);
        insertBatch(entities, 500, false, 1);
    }

}