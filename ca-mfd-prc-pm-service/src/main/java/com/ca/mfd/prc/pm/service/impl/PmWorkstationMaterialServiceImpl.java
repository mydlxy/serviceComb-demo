package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pm.mapper.IPmLineMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkShopMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkStationMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkstationMaterialMapper;
import com.ca.mfd.prc.pm.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pm.service.IPmCharacteristicsDataService;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsVersionsService;
import com.ca.mfd.prc.pm.service.IPmWorkstationMaterialService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.ca.mfd.prc.common.constant.Constant.TRUE_BOOL;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_CHINESE;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_NUM;


/**
 * @author inkelink
 * @Description: 工位物料清单服务实现
 * @date 2023年09月26日
 * @变更说明 BY inkelink At 2023年09月26日
 */
@Service("pmWorkstationMaterialService")
public class PmWorkstationMaterialServiceImpl extends AbstractPmCrudServiceImpl<IPmWorkstationMaterialMapper, PmWorkstationMaterialEntity> implements IPmWorkstationMaterialService {

    private static final Object lockObj = new Object();
    private static final Map<String, String> BOOLEAN_FIELDS_MAPPING = new HashMap<>(1);
    @Autowired
    private SysConfigurationProvider pmSysConfigurationService;
    @Autowired
    private IPmCharacteristicsDataService pmCharacteristicsDataService;
    @Autowired
    private IPmWorkShopMapper workShopMapper;
    @Autowired
    private IPmLineMapper pmLineMapper;
    @Autowired
    private IPmWorkStationMapper pmWorkStationMapper;
    @Autowired
    private IPmWorkstationMaterialMapper pmWorkstationMaterialMapper;
    @Autowired
    IPmProductCharacteristicsVersionsService pmProductCharacteristicsVersionsService;

//    @Autowired
//    IPmVersionService pmVersionService;

    @Autowired
    private LocalCache localCache;

    private final String cacheName = "PRC_PM_WORKSTATION_MATERIAL";

    private final String cacheNameVersions = "PRC_PM_WORKSTATION_MATERIAL_VESION";

    static {
        BOOLEAN_FIELDS_MAPPING.put("deleteFlag", "isDelete");
    }

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmWorkstationMaterialEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmWorkstationMaterialEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmWorkstationMaterialEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmWorkstationMaterialEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void update(PmWorkstationMaterialEntity pmWorkstationMaterialEntity) {
       this.update(pmWorkstationMaterialEntity,true);
    }

    @Override
    public void update(PmWorkstationMaterialEntity pmWorkstationMaterialEntity,boolean very) {
        if(very){
            beforeUpdate(pmWorkstationMaterialEntity);
        }
        LambdaUpdateWrapper<PmWorkstationMaterialEntity> luw = new LambdaUpdateWrapper<>();
        luw.set(PmWorkstationMaterialEntity::getMaterialNo, pmWorkstationMaterialEntity.getMaterialNo());
        luw.set(PmWorkstationMaterialEntity::getMaterialNum, pmWorkstationMaterialEntity.getMaterialNum());
        luw.set(PmWorkstationMaterialEntity::getMaterialUnit, pmWorkstationMaterialEntity.getMaterialUnit());
        luw.set(PmWorkstationMaterialEntity::getMasterChinese, pmWorkstationMaterialEntity.getMasterChinese());
        luw.set(PmWorkstationMaterialEntity::getMasterEnglish, pmWorkstationMaterialEntity.getMasterEnglish());
        luw.set(PmWorkstationMaterialEntity::getCustomizedFlag, pmWorkstationMaterialEntity.getCustomizedFlag());
        luw.set(PmWorkstationMaterialEntity::getSupplyStatus, pmWorkstationMaterialEntity.getSupplyStatus());
        luw.set(PmWorkstationMaterialEntity::getSwitchPartsFlag, pmWorkstationMaterialEntity.getSwitchPartsFlag());
        luw.set(PmWorkstationMaterialEntity::getConfirmFlag, pmWorkstationMaterialEntity.getConfirmFlag());
        luw.set(PmWorkstationMaterialEntity::getFeatureCode, pmWorkstationMaterialEntity.getFeatureCode());
        luw.eq(PmWorkstationMaterialEntity::getId, pmWorkstationMaterialEntity.getId());
        luw.eq(PmWorkstationMaterialEntity::getVersion, pmWorkstationMaterialEntity.getVersion());
        this.update(luw);
    }

    @Override
    public List<PmWorkstationMaterialEntity> getAllDatas() {
        List<PmWorkstationMaterialEntity> datas = localCache.getObject(cacheName);
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

    @Override
    public List<PmWorkstationMaterialEntity> getWorkstationMaterialsByMaterialNoAndShopCode(String materialNo, String workstationCode, String shopCode) {
        QueryWrapper<PmWorkstationMaterialEntity> wq = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkstationMaterialEntity> lqw = wq.lambda();
        if(StringUtils.isNotBlank(materialNo)){
            lqw.eq(PmWorkstationMaterialEntity :: getMaterialNo,materialNo);
        }
        if(StringUtils.isNotBlank(workstationCode)){
            lqw.eq(PmWorkstationMaterialEntity :: getWorkstationCode,workstationCode);
        }
        if(StringUtils.isBlank(shopCode)){
            lqw.eq(PmWorkstationMaterialEntity :: getWorkshopCode,shopCode);
        }
        return getData(wq,false);
    }

    @Override
    public List<PmWorkstationMaterialEntity> getPmWorkstationEntityByVersion(Long shopId, int version, Boolean isDeleted) {
        QueryWrapper<PmWorkstationMaterialEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkstationMaterialEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkstationMaterialEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmWorkstationMaterialEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmWorkstationMaterialEntity::getIsDelete, isDeleted);
        return pmWorkstationMaterialMapper.selectList(queryWrapper);
    }

    @Override
    public List<PmWorkstationMaterialEntity> getPmWorkstationEntityByMaterialNo(Long stationId, String materialNo, Boolean isDeleted) {
        QueryWrapper<PmWorkstationMaterialEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkstationMaterialEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkstationMaterialEntity::getPrcPmWorkstationId, stationId);
        lambdaQueryWrapper.eq(PmWorkstationMaterialEntity::getMaterialNo, materialNo);
        lambdaQueryWrapper.eq(PmWorkstationMaterialEntity::getIsDelete, isDeleted);
        return pmWorkstationMaterialMapper.selectList(queryWrapper);
    }

    @Override
    public void beforeUpdate(PmWorkstationMaterialEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(PmWorkstationMaterialEntity model) {
        validData(model);
    }

    private void validData(PmWorkstationMaterialEntity model) {
        ClassUtil.validNullByNullAnnotation(model);
        //工位信息
        validWorkStation(model);
        //线体信息
        validLine(model);
        //车间信息
        validWorkShop(model);

        validMaterialUniq(model);
        if (Boolean.FALSE.equals(FeatureTool.verifyExpression(model.getFeatureCode()))) {
            throw new InkelinkException(String.format("线体[%s]>工位[%s]>物料号[%s]特征值[%s]无效",
                    model.getLineCode(), model.getWorkstationCode(), model.getMaterialNo(), model.getFeatureCode()));
        }
    }

    private void validMaterialUniq(PmWorkstationMaterialEntity model) {
        QueryWrapper<PmWorkstationMaterialEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkstationMaterialEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkstationMaterialEntity::getWorkshopCode, model.getWorkshopCode());
        lambdaQueryWrapper.eq(PmWorkstationMaterialEntity::getWorkstationCode, model.getWorkstationCode());
        lambdaQueryWrapper.eq(PmWorkstationMaterialEntity::getRowNumAndMaterialNo, model.getRowNumAndMaterialNo());
        lambdaQueryWrapper.ne(PmWorkstationMaterialEntity::getId, model.getId() == null ? 0 : model.getId());
        if (selectCount(queryWrapper) > 0) {
            throw new InkelinkException(String.format("线体[%s]>工位[%s]>BOM行号和物料号[%s]已经存在", model.getLineCode(), model.getWorkstationCode(), model.getRowNumAndMaterialNo()));
        }
    }


    /**
     * 验证特征
     *
     * @param
     * @return
     */
//    private boolean validCharacteristic(String characteristic) {
//        List<SysConfigurationEntity> listOfVehicleModle = this.pmSysConfigurationService.getSysConfigurations(VEHICLE_MODEL);
//        List<String> listOfVehicleMasterFeatures = pmCharacteristicsDataService.getAllDatas().stream()
//                .map(i -> i.getCharacteristicsValue()).collect(Collectors.toList());
//        return BusinessValidUtils.checkVehicleOption(characteristic, listOfVehicleMasterFeatures, listOfVehicleModle);
//    }

    @Override
    public List<PmWorkstationMaterialEntity> getByShopId(Long shopId) {
        return pmWorkstationMaterialMapper.selectList(Wrappers.lambdaQuery(PmWorkstationMaterialEntity.class)
                .eq(PmWorkstationMaterialEntity::getPrcPmWorkshopId, shopId)
                .eq(PmWorkstationMaterialEntity::getVersion, 0)
                .eq(PmWorkstationMaterialEntity::getIsDelete, false));
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> mapping = new HashMap<>(8);
        mapping.put("workshopCode", "车间代码");
        mapping.put("lineCode", "线体代码");
        mapping.put("workstationCode", "工位代码");
        mapping.put("attribute1", "BOM行标识");
        mapping.put("materialNo", "物料编码");
        mapping.put("masterChinese", "物料名称");
        mapping.put("featureCode", "使用规则");
        mapping.put("materialNum", "工位用量");
        mapping.put("deleteFlag", "是否删除");
        return mapping;
    }

    /**
     * 自动填充车间、线体、工位信息
     *
     * @param entity
     */
    private void valid(PmWorkstationMaterialEntity entity) {
        //工位信息
        validWorkStation(entity);
        //线体信息
        validLine(entity);
        //车间信息
        validWorkShop(entity);
    }

    private void validWorkStation(PmWorkstationMaterialEntity entity) {
        //工位信息
        QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkStationEntity> lqw = qw.lambda();
        lqw.eq(PmWorkStationEntity::getId, entity.getPrcPmWorkstationId().toString());
        lqw.eq(PmWorkStationEntity::getVersion, 0);
        PmWorkStationEntity pmWorkStationEntity = pmWorkStationMapper.selectList(qw).stream().findFirst().orElse(null);
        if (pmWorkStationEntity == null) {
            throw new InkelinkException(String.format("工位ID[%s]不存在", entity.getPrcPmWorkstationId()));
        }
        entity.setWorkstationCode(pmWorkStationEntity.getWorkstationCode());
        entity.setPrcPmLineId(pmWorkStationEntity.getPrcPmLineId());
    }

    private void validLine(PmWorkstationMaterialEntity entity) {
        //线体信息
        QueryWrapper<PmLineEntity> lineQry = new QueryWrapper<>();
        lineQry.lambda().eq(PmLineEntity::getId,entity.getPrcPmLineId()).eq(PmLineEntity::getVersion,0);
        PmLineEntity pmLineEntity = pmLineMapper.selectOne(lineQry);
        if (pmLineEntity == null) {
            throw new InkelinkException(String.format("线体ID[%s]不存在", entity.getPrcPmLineId()));
        }
        entity.setLineCode(pmLineEntity.getLineCode());
        entity.setPrcPmWorkshopId(pmLineEntity.getPrcPmWorkshopId());
    }

    private void validWorkShop(PmWorkstationMaterialEntity entity) {
        //车间信息
        List<ConditionDto> shopConditionDtoList = new ArrayList<>(2);
        PmWorkShopEntity pmWorkShopEntity = workShopMapper.getByShopIdAndVersion(entity.getPrcPmWorkshopId(), 0);
        if (pmWorkShopEntity == null) {
            throw new InkelinkException(String.format("车间ID[%s]不存在", entity.getPrcPmWorkshopId()));
        }
        entity.setWorkshopCode(pmWorkShopEntity.getWorkshopCode());
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) {
        List<PmWorkstationMaterialEntity> listOfWorkstationMaterial;
        try {
            listOfWorkstationMaterial = this.convertExcelDataToEntity(listFromExcelSheet,
                    mapSysConfigByCategory, sheetName);
        } catch (Exception e) {
            log.error("工位物料导入失败", e);
            throw new InkelinkException("工位物料导入失败:" + e.getMessage());
        }
        //设置外键
        setForeignKey(listOfWorkstationMaterial, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfWorkstationMaterial, currentUnDeployData);
    }

    private void setForeignKey(List<PmWorkstationMaterialEntity> listOfPmTool, PmAllDTO pmAllDTO) {
        if (listOfPmTool.isEmpty()) {
            return;
        }
        for (PmWorkstationMaterialEntity pmWorkstationMaterialEntity : listOfPmTool) {
            setForeignKey(pmWorkstationMaterialEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmWorkstationMaterialEntity pmWorkstationMaterialEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        //设置车间id
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmWorkstationMaterialEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("物料编号[" + pmWorkstationMaterialEntity.getMaterialNo() + "]对应的车间编码[" + pmWorkstationMaterialEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmWorkstationMaterialEntity.setPrcPmWorkshopId(shop.getId());
        List<PmLineEntity> lines = pmAllDTO.getLines();
        //设置线体id
        PmLineEntity line = lines.stream().filter(item -> Objects.equals(pmWorkstationMaterialEntity.getLineCode(), item.getLineCode())
                        && Objects.equals(pmWorkstationMaterialEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId()))
                .findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("物料编号[" + pmWorkstationMaterialEntity.getMaterialNo() + "]对应的线体编码[" + pmWorkstationMaterialEntity.getLineCode() + "]没有对应任何线体，请检查是否有配置对应编码线体");
        }
        pmWorkstationMaterialEntity.setPrcPmLineId(line.getId());
        List<PmWorkStationEntity> stations = pmAllDTO.getStations();
        //设置工位id
        PmWorkStationEntity workStation = stations.stream().filter(item -> Objects.equals(pmWorkstationMaterialEntity.getWorkstationCode(), item.getWorkstationCode())
                        && Objects.equals(pmWorkstationMaterialEntity.getPrcPmLineId(), item.getPrcPmLineId()))
                .findFirst().orElse(null);
        if (workStation == null) {
            throw new InkelinkException("物料编号[" + pmWorkstationMaterialEntity.getMaterialNo() + "]对应的线体编码[" + pmWorkstationMaterialEntity.getLineCode() + "]对应的工位编码[" + pmWorkstationMaterialEntity.getWorkstationCode() + "]没有对应任何工位，请检查是否有配置对应编码工位");
        }
        pmWorkstationMaterialEntity.setPrcPmWorkstationId(workStation.getId());
    }

    private void verifyAndSaveEntity(List<PmWorkstationMaterialEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        List<PmWorkstationMaterialEntity> insertList = new ArrayList<>(listEntity.size());
        for (PmWorkstationMaterialEntity pmWorkstationMaterialEntity : listEntity) {
            if(StringUtils.isBlank(pmWorkstationMaterialEntity.getMaterialNo())){
                throw new InkelinkException("物料编码不能为空");
            }
            if(pmWorkstationMaterialEntity.getIsDelete()){
                LambdaUpdateWrapper<PmWorkstationMaterialEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmWorkstationMaterialEntity::getIsDelete, true);
                luw.eq(PmWorkstationMaterialEntity::getMaterialNo, pmWorkstationMaterialEntity.getMaterialNo());
                luw.eq(PmWorkstationMaterialEntity::getVersion, 0);
                this.update(luw,false);
                continue;
            }
            if(StringUtils.isBlank(pmWorkstationMaterialEntity.getAttribute1())){
                throw new InkelinkException("Bom行号不能为空");
            }
            pmWorkstationMaterialEntity.setRowNumAndMaterialNo(pmWorkstationMaterialEntity.getAttribute1()
            + "|" + pmWorkstationMaterialEntity.getMaterialNo());
            ClassUtil.validNullByNullAnnotation(pmWorkstationMaterialEntity);
            PmWorkstationMaterialEntity existWorkstationMaterial = currentUnDeployData.getMaterials().stream().filter(
                            item -> Objects.equals(item.getPrcPmWorkstationId(), pmWorkstationMaterialEntity.getPrcPmWorkstationId())
                                    && Objects.equals(item.getRowNumAndMaterialNo(), pmWorkstationMaterialEntity.getRowNumAndMaterialNo()))
                    .findFirst().orElse(null);
            if (existWorkstationMaterial != null) {
                LambdaUpdateWrapper<PmWorkstationMaterialEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmWorkstationMaterialEntity::getMaterialNum, pmWorkstationMaterialEntity.getMaterialNum());
                luw.set(PmWorkstationMaterialEntity::getFeatureCode, pmWorkstationMaterialEntity.getFeatureCode());
                luw.set(PmWorkstationMaterialEntity::getMaterialNo, pmWorkstationMaterialEntity.getMaterialNo());
                luw.set(PmWorkstationMaterialEntity::getMasterChinese, pmWorkstationMaterialEntity.getMasterChinese());
                luw.set(PmWorkstationMaterialEntity::getAttribute1, pmWorkstationMaterialEntity.getAttribute1());
                luw.set(PmWorkstationMaterialEntity::getIsDelete, pmWorkstationMaterialEntity.getIsDelete());
                luw.set(PmWorkstationMaterialEntity::getWorkstationCode, pmWorkstationMaterialEntity.getWorkstationCode());
                luw.set(PmWorkstationMaterialEntity::getPrcPmWorkstationId, pmWorkstationMaterialEntity.getPrcPmWorkstationId());
                luw.set(PmWorkstationMaterialEntity::getPrcPmLineId, pmWorkstationMaterialEntity.getPrcPmLineId());
                luw.set(PmWorkstationMaterialEntity::getLineCode, pmWorkstationMaterialEntity.getLineCode());
                luw.set(PmWorkstationMaterialEntity::getPrcPmWorkshopId, pmWorkstationMaterialEntity.getPrcPmWorkshopId());
                luw.set(PmWorkstationMaterialEntity::getWorkshopCode, pmWorkstationMaterialEntity.getWorkshopCode());
                luw.eq(PmWorkstationMaterialEntity::getId, existWorkstationMaterial.getId());
                luw.eq(PmWorkstationMaterialEntity::getVersion, 0);
                this.update(luw,false);
            } else if (!pmWorkstationMaterialEntity.getIsDelete()) {
                pmWorkstationMaterialEntity.setVersion(0);
                insertList.add(pmWorkstationMaterialEntity);
            }
        }
        if(!insertList.isEmpty()){
            this.insertBatch(insertList,200,false,1);
        }

    }

    @Override
    protected void setBooleanVal(Map<String, String> eachRowData) {
        Set<String> booleanKeySet = BOOLEAN_FIELDS_MAPPING.keySet();
        Set<String> allKeySet = eachRowData.keySet();
        Map<String, String> appendMap = new HashMap<>(1);
        for (String eachField : allKeySet) {
            if (booleanKeySet.contains(eachField)) {
                boolean flag = TRUE_BOOL.equalsIgnoreCase(eachRowData.get(eachField))
                        || TRUE_NUM.equals(eachRowData.get(eachField))
                        || TRUE_CHINESE.equals(eachRowData.get(eachField));
                appendMap.put(BOOLEAN_FIELDS_MAPPING.get(eachField), String.valueOf(flag));
            }
        }
        eachRowData.putAll(appendMap);
    }


}