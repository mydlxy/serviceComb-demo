package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.mapper.IPmLineMapper;
import com.ca.mfd.prc.pm.mapper.IPmToolJobMapper;
import com.ca.mfd.prc.pm.mapper.IPmToolMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkShopMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkStationMapper;
import com.ca.mfd.prc.pm.service.IPmToolJobService;
import com.ca.mfd.prc.pm.service.IPmWoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ca.mfd.prc.common.constant.Constant.TRUE_BOOL;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_CHINESE;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_NUM;

/**
 * @author inkelink ${email}
 * @Description: 作业
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmToolJobServiceImpl extends AbstractPmCrudServiceImpl<IPmToolJobMapper, PmToolJobEntity> implements IPmToolJobService {

    private static final Map<String, String> BOOLEAN_FIELDS_MAPPING = new HashMap<>(2);
    private static final Object lockObj = new Object();

    static {
        BOOLEAN_FIELDS_MAPPING.put("deleteFlag", "isDelete");
    }

    private final IPmToolJobMapper pmToolJobDao;
    private final IPmToolMapper pmToolDao;
    private final IPmWoService pmWoService;
    private final IPmLineMapper pmLineMapper;
    private final IPmToolMapper pmToolMapper;
    private final IPmWorkStationMapper pmWorkStationMapper;

    private final IPmWorkShopMapper pmWorkShopMapper;

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_TOOL_JOB";

    @Autowired
    public PmToolJobServiceImpl(IPmToolJobMapper pmToolJobDao,
                                IPmToolMapper pmToolDao,
                                IPmWoService pmWoService,
                                IPmLineMapper pmLineMapper,
                                IPmToolMapper pmToolMapper,
                                IPmWorkStationMapper pmWorkStationMapper,
                                IPmWorkShopMapper pmWorkShopMapper
    ) {
        this.pmToolJobDao = pmToolJobDao;
        this.pmToolDao = pmToolDao;
        this.pmWoService = pmWoService;
        this.pmLineMapper = pmLineMapper;
        this.pmToolMapper = pmToolMapper;
        this.pmWorkStationMapper = pmWorkStationMapper;
        this.pmWorkShopMapper = pmWorkShopMapper;
    }

    @Override
    public PageData<PmToolJobEntity> page(PageDataDto model) {
        List<ConditionDto> conditions = model.getConditions();
        ConditionDto workshopCodeCondition = conditions.stream().filter(conditionDto -> "workshopCode".equals(conditionDto.getColumnName())).findAny().orElse(null);
        if (Objects.isNull(workshopCodeCondition)) {
            throw new InkelinkException("缺失车间信息无法查询");
        }
        QueryWrapper<PmWorkShopEntity> shopQry = new QueryWrapper<>();
        shopQry.lambda().eq(PmWorkShopEntity::getWorkshopCode, workshopCodeCondition.getValue())
                .eq(PmWorkShopEntity::getVersion, 0)
                .eq(PmWorkShopEntity::getIsDelete, false);
        PmWorkShopEntity pmWorkShopEntity = pmWorkShopMapper.selectList(shopQry).stream().findAny().orElse(null);
        if (Objects.isNull(pmWorkShopEntity)) {
            throw new InkelinkException("缺失车间信息无法查询");
        }
        ConditionDto prcPmWorkstationIdCondition = conditions.stream().filter(conditionDto -> "prcPmWorkstationId".equals(conditionDto.getColumnName())).findAny().orElse(null);
        if (Objects.nonNull(prcPmWorkstationIdCondition)) {
            String prcPmWorkstationId = prcPmWorkstationIdCondition.getValue();
            QueryWrapper<PmWoEntity> woQry = new QueryWrapper<>();
            woQry.lambda().eq(PmWoEntity::getPrcPmWorkstationId, Long.parseLong(prcPmWorkstationId))
                    .eq(PmWoEntity::getVersion, 0).eq(PmWoEntity::getIsDelete, false);
            List<PmWoEntity> wos = pmWoService.getData(woQry,false);
            model.getConditions().remove(prcPmWorkstationIdCondition);
            if (CollectionUtils.isNotEmpty(wos)) {
                List<String> list = wos.stream().map(wo -> String.valueOf(wo.getId())).collect(Collectors.toList());
                model.getConditions().add(new ConditionDto("PRC_PM_WO_ID", String.join("|", list), ConditionOper.In));
            } else {
                model.getConditions().add(new ConditionDto("PRC_PM_WO_ID", "-1", ConditionOper.Equal));
            }
        }
        model.getConditions().remove(workshopCodeCondition);
        model.getConditions().add(new ConditionDto("PRC_PM_WORKSHOP_ID", String.valueOf(pmWorkShopEntity.getId()), ConditionOper.Equal));
        PageData<PmToolJobEntity> page = super.page(model);
        List<Long> woIds = page.getDatas().stream().map(PmToolJobEntity::getPmWoId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(woIds)){
            QueryWrapper<PmWoEntity> qry = new QueryWrapper<>();
            qry.lambda().eq(PmWoEntity::getVersion,0).eq(PmWoEntity::getIsDelete,false)
                    .in(PmWoEntity::getId,woIds);
            List<PmWoEntity> pmWoEntities = pmWoService.getData(qry,false);
            Map<Long, Long> map = pmWoEntities.stream().collect(Collectors.toMap(PmWoEntity::getId, PmWoEntity::getPrcPmWorkstationId));
            page.getDatas().forEach(data -> data.setPrcPmWorkstationId(map.get(data.getPmWoId())));
        }
        return page;
    }


    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmToolJobEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmToolJobEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmToolJobEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmToolJobEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeUpdate(PmToolJobEntity entity) {
        validData(entity);
    }

    @Override
    public void beforeInsert(PmToolJobEntity entity) {
        validData(entity);
    }

    private void validData(PmToolJobEntity model) {
        if (Boolean.FALSE.equals(FeatureTool.verifyExpression(model.getFeatureCode()))) {
            throw new InkelinkException(String.format("job[%s]特征值[%s]无效", model.getJobNo(), model.getFeatureCode()));
        }
        Long prcPmWorkshopId = model.getPrcPmWorkshopId();
        Long prcPmLineId = model.getPrcPmLineId();
        Long prcPmWorkstationId = model.getPrcPmWorkstationId();
        prcPmWorkshopId = prcPmWorkshopId == null ? 0 : prcPmWorkshopId;
        prcPmLineId = prcPmLineId == null ? 0 : prcPmLineId;
        prcPmWorkstationId = prcPmWorkstationId == null ? 0 : prcPmWorkstationId;
        if (prcPmWorkshopId == 0
                ||  prcPmLineId == 0
                ||  prcPmWorkstationId == 0) {
            if(model.getPrcPmToolId() != null && model.getPrcPmToolId() > 0){
                validTool(model);
            }else{
                validWo(model);
            }
        }
    }


    private void validTool(PmToolJobEntity model) {
        QueryWrapper<PmToolEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmToolEntity::getId, model.getPrcPmToolId());
        lambdaQueryWrapper.eq(PmToolEntity::getVersion, 0);
        List<PmToolEntity> tools = this.pmToolMapper.selectList(queryWrapper);
        if (tools.isEmpty()) {
            throw new InkelinkException(String.format("job[%s]对应的工具ID[%s]不存在", model.getJobNo(), model.getPrcPmToolId()));
        }
        PmToolEntity tool = tools.get(0);
        model.setPrcPmWorkshopId(tool.getPrcPmWorkshopId());
        model.setPrcPmLineId(tool.getPrcPmLineId());
        model.setPrcPmWorkstationId(tool.getPrcPmWorkstationId());
    }

    private void validWo(PmToolJobEntity model) {
        QueryWrapper<PmWoEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWoEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWoEntity::getId, model.getPmWoId());
        lambdaQueryWrapper.eq(PmWoEntity::getVersion, 0);
        List<PmWoEntity> wos = this.pmWoService.getData(queryWrapper,false);
        if (wos.isEmpty()) {
            throw new InkelinkException(String.format("job[%s]对应的woId[%s]不存在", model.getJobNo(), model.getPrcPmToolId()));
        }
        PmWoEntity wo = wos.get(0);
        model.setPrcPmWorkshopId(wo.getPrcPmWorkshopId());
        model.setPrcPmLineId(wo.getPrcPmLineId());
        model.setPrcPmWorkstationId(wo.getPrcPmWorkstationId());
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
    public void update(PmToolJobEntity pmToolJobEntity) {
        beforeUpdate(pmToolJobEntity);
        LambdaUpdateWrapper<PmToolJobEntity> luw = new LambdaUpdateWrapper<>();

        luw.set(PmToolJobEntity::getPrcPmToolId, pmToolJobEntity.getPrcPmToolId());
        luw.set(PmToolJobEntity::getJobNo, pmToolJobEntity.getJobNo());
        luw.set(PmToolJobEntity::getPmWoId, pmToolJobEntity.getPmWoId());
        luw.set(PmToolJobEntity::getFeatureCode, pmToolJobEntity.getFeatureCode());
        luw.set(PmToolJobEntity::getAttribute3, pmToolJobEntity.getAttribute3());

        luw.eq(PmToolJobEntity::getId, pmToolJobEntity.getId());
        luw.eq(PmToolJobEntity::getVersion, pmToolJobEntity.getVersion());
        this.update(luw);
    }

    @Override
    public List<PmToolJobEntity> getAllDatas() {
        List<PmToolJobEntity> datas = localCache.getObject(cacheName);
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
    public List<PmWoEntity> getWoInfo(String toolId) {
        List<PmWoEntity> datas = new ArrayList<>();
        PmToolEntity tool = pmToolDao.selectById(toolId);
        if (tool != null) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(new ConditionDto("PRC_PM_WORKSTATION_ID", tool.getPrcPmWorkstationId().toString(), ConditionOper.Equal));
            conditionInfos.add(new ConditionDto("TYPE", "1|3|10", ConditionOper.In));
            datas = pmWoService.getData(conditionInfos);
        }
        return datas;
    }

    @Override
    public List<PmToolEntity> getToolByWoId(Long woId) {
        PmWoEntity wo = pmWoService.get(woId);
        if (wo == null) {
            log.error("工艺不存在");
            return Collections.emptyList();
        }
        QueryWrapper<PmToolJobEntity> qwtj = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolJobEntity> lqwtj = qwtj.lambda();
        lqwtj.eq(PmToolJobEntity::getPmWoId, woId);
        lqwtj.eq(PmToolJobEntity::getFeatureCode, wo.getFeatureCode());
        List<PmToolJobEntity> jobs = getData(qwtj, false);
        if (jobs.isEmpty()) {
            log.error("根据工艺ID和工艺特征未查询到job信息");
            return Collections.emptyList();
        }
        List<Long> toolIds = jobs.stream().map(PmToolJobEntity::getPrcPmToolId).collect(Collectors.toList());
        Map<Long, List<PmToolJobEntity>> jobsBytoolId = new HashMap<>(toolIds.size());
        for (PmToolJobEntity pmToolJobEntity : jobs) {
            jobsBytoolId.computeIfAbsent(pmToolJobEntity.getPrcPmToolId(), v -> new ArrayList<>()).add(pmToolJobEntity);
        }
        QueryWrapper<PmToolEntity> qwtt = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolEntity> lqwt = qwtt.lambda();
        lqwt.eq(PmToolEntity::getPrcPmWorkstationId, wo.getPrcPmWorkstationId());
        lqwt.in(PmToolEntity::getId, toolIds);
        lqwt.eq(PmToolEntity::getIsDelete, false);
        List<PmToolEntity> tools = pmToolMapper.selectList(qwtt);
        for (PmToolEntity pmToolEntity : tools) {
            pmToolEntity.setPmToolJobEntity(jobsBytoolId.get(pmToolEntity.getId()));
        }
        return tools;
    }

    @Override
    public Map<String,Object> getToolAndToolJobByWoCode(String woCode,Long workstationId) {
        QueryWrapper<PmWoEntity> qwWo = new QueryWrapper<>();
        LambdaQueryWrapper<PmWoEntity> lqwWo = qwWo.lambda();
        lqwWo.eq(PmWoEntity::getWoCode,woCode);
        lqwWo.eq(PmWoEntity::getPrcPmWorkstationId,workstationId);
        PmWoEntity wo = pmWoService.getData(qwWo,false).stream().findFirst().orElse(null);
        if (wo == null) {
            log.error("工艺不存在");
            return Collections.emptyMap();
        }
        Map<String,Object> target = new HashMap<>(2);
        QueryWrapper<PmToolJobEntity> qwtj = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolJobEntity> lqwtj = qwtj.lambda();
        lqwtj.eq(PmToolJobEntity::getPmWoId, wo.getId());
        lqwtj.eq(PmToolJobEntity::getFeatureCode, wo.getFeatureCode());
        List<PmToolJobEntity> jobs = getData(qwtj, false);
        target.put("jobs",jobs);
        QueryWrapper<PmToolEntity> qwtt = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolEntity> lqwt = qwtt.lambda();
        lqwt.eq(PmToolEntity::getPrcPmWorkstationId, wo.getPrcPmWorkstationId());
        lqwt.eq(PmToolEntity::getIsDelete, false);
        List<PmToolEntity> tools = pmToolMapper.selectList(qwtt);
        target.put("tools",tools);
        return target;
    }

    @Override
    public List<PmToolJobEntity> getListByParentId(Long parentId) {
        QueryWrapper<PmToolJobEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolJobEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmToolJobEntity::getPrcPmToolId, parentId);
        lambdaQueryWrapper.eq(PmToolJobEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmToolJobEntity::getVersion, 0);
        lambdaQueryWrapper.orderByAsc(PmToolJobEntity::getJobNo);
        return this.pmToolJobDao.selectList(queryWrapper);
    }

    @Override
    public List<PmToolJobEntity> getByShopId(Long shopId) {
        return pmToolJobDao.selectList(Wrappers.lambdaQuery(PmToolJobEntity.class)
                .eq(PmToolJobEntity::getPrcPmWorkshopId, shopId)
                .eq(PmToolJobEntity::getVersion, 0)
                .eq(PmToolJobEntity::getIsDelete, false));
    }

    @Override
    public List<PmToolJobEntity> getPmToolJobEntityByVersion(Long shopId, int version, Boolean flags) {
        QueryWrapper<PmToolJobEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolJobEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmToolJobEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmToolJobEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmToolJobEntity::getIsDelete, flags);
        return pmToolJobDao.selectList(queryWrapper);
    }

    @Override
    public List<PmToolJobEntity> getPmToolJobEntityByJobNo(Long toolId, String jobNo, Boolean flags) {
        QueryWrapper<PmToolJobEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolJobEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmToolJobEntity::getPrcPmToolId, toolId);
        lambdaQueryWrapper.eq(PmToolJobEntity::getJobNo, jobNo);
        lambdaQueryWrapper.eq(PmToolJobEntity::getIsDelete, flags);
        return pmToolJobDao.selectList(queryWrapper);
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> pmToolJobMap = new HashMap<>(8);
        pmToolJobMap.put("workshopCode", "车间代码");
        pmToolJobMap.put("lineCode", "线体代码");
        pmToolJobMap.put("workstationCode", "工位代码");
        pmToolJobMap.put("toolCode", "工具号");
        pmToolJobMap.put("jobNo", "参数号");
        pmToolJobMap.put("woCode", "操作");
        pmToolJobMap.put("featureCode", "特征");
        pmToolJobMap.put("deleteFlag", "是否删除");
        return pmToolJobMap;
    }

    @Override
    public PmToolJobEntity get(Serializable id) {
        QueryWrapper<PmToolJobEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmToolJobEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmToolJobEntity::getId, id);
        lambdaQueryWrapper.eq(PmToolJobEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmToolJobEntity::getVersion, 0);
        List<PmToolJobEntity> pmToolJobEntityList = pmToolJobDao.selectList(queryWrapper);
        PmToolJobEntity pmToolJobEntity = pmToolJobEntityList.stream().findFirst().orElse(null);
        if(Objects.nonNull(pmToolJobEntity)){
            QueryWrapper<PmWoEntity> qry = new QueryWrapper<>();
            qry.lambda().eq(PmWoEntity::getVersion,0).eq(PmWoEntity::getIsDelete,false)
                    .eq(PmWoEntity::getId,pmToolJobEntity.getPmWoId());
            PmWoEntity pmWoEntity = pmWoService.getData(qry,false).stream().findAny().orElse(null);
            if(Objects.nonNull(pmWoEntity)){
                pmToolJobEntity.setPrcPmWorkstationId(pmWoEntity.getPrcPmWorkstationId());
            }
        }
        return pmToolJobEntity;
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception {
        List<PmToolJobEntity> listOfPmToolJob = this.convertExcelDataToEntity(listFromExcelSheet,
                mapSysConfigByCategory, sheetName);
        //设置外键
        setForeignKey(listOfPmToolJob, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfPmToolJob, currentUnDeployData);
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

    private void setForeignKey(List<PmToolJobEntity> listOfPmToolJob, PmAllDTO pmAllDTO) {
        if (listOfPmToolJob.isEmpty()) {
            return;
        }
        for (PmToolJobEntity pmToolJobEntity : listOfPmToolJob) {
            setForeignKey(pmToolJobEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmToolJobEntity pmToolJobEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        //设置车间id
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmToolJobEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("ToolJob编码[" + pmToolJobEntity.getJobNo() + "]所在车间编码(内部编码)[" + pmToolJobEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmToolJobEntity.setPrcPmWorkshopId(shop.getId());
        List<PmLineEntity> lines = pmAllDTO.getLines();
        //设置线体id
        PmLineEntity line = lines.stream().filter(item -> Objects.equals(pmToolJobEntity.getLineCode(), item.getLineCode())
                        && Objects.equals(pmToolJobEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId()))
                .findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("ToolJob编码[" + pmToolJobEntity.getJobNo() + "]对应的线体编码[" + pmToolJobEntity.getLineCode() + "]没有对应任何线体，请检查是否有配置对应编码线体");
        }
        pmToolJobEntity.setPrcPmLineId(line.getId());

        //设置工位id
        List<PmWorkStationEntity> stations = pmAllDTO.getStations();
        PmWorkStationEntity workStation = stations.stream().filter(item -> Objects.equals(pmToolJobEntity.getWorkstationCode(), item.getWorkstationCode())
                        && Objects.equals(pmToolJobEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId())
                        && Objects.equals(pmToolJobEntity.getPrcPmLineId(), item.getPrcPmLineId()))
                .findFirst().orElse(null);
        if (workStation == null) {
            throw new InkelinkException("ToolJob编码[" + pmToolJobEntity.getJobNo() + "]对应的线体编码[" + workStation.getLineCode() + "]工位编码[" + workStation.getWorkstationCode() + "]没有对应任何工位，请检查是否有配置对应编码工位");
        }
        pmToolJobEntity.setPrcPmWorkstationId(workStation.getId());

        //设置工具id
        List<PmToolEntity> tools = pmAllDTO.getTools();
        PmToolEntity tool = tools.stream().filter(item -> Objects.equals(pmToolJobEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId())
                        && Objects.equals(pmToolJobEntity.getPrcPmLineId(), item.getPrcPmLineId())
                        && Objects.equals(pmToolJobEntity.getToolCode(), item.getToolCode())
                )
                .findFirst().orElse(null);
        if (tool == null) {
            throw new InkelinkException("ToolJob编码[" + pmToolJobEntity.getJobNo() + "]对应的线体编码[" + pmToolJobEntity.getLineCode() + "]工具编码[" + pmToolJobEntity.getToolCode() + "]没有对应的工具，请检查是否有配置对应编码工具");
        }
        pmToolJobEntity.setPrcPmToolId(tool.getId());

        //设置wo
        List<PmWoEntity> wos = pmAllDTO.getWos();
        PmWoEntity wo = wos.stream().filter(item -> Objects.equals(pmToolJobEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId())
                        && Objects.equals(pmToolJobEntity.getPrcPmLineId(), item.getPrcPmLineId())
                        && Objects.equals(pmToolJobEntity.getPrcPmWorkstationId(), item.getPrcPmWorkstationId())
                        && Objects.equals(pmToolJobEntity.getWoCode(), item.getWoCode())
                )
                .findFirst().orElse(null);
        if (wo == null) {
            throw new InkelinkException("ToolJob编码[" + pmToolJobEntity.getJobNo() + "]对应的线体编码[" + pmToolJobEntity.getLineCode() + "]工位编码[" + pmToolJobEntity.getWorkstationCode() + "]工具编码[" + pmToolJobEntity.getToolCode() + "]job号[" + pmToolJobEntity.getJobNo() + "]工艺编码[" + pmToolJobEntity.getWoCode() + "]没有对应的工艺，请检查是否有配置对应编码的工艺");
        }
        if (wo.getWoType() != 1) {
            throw new InkelinkException("ToolJob编码[" + pmToolJobEntity.getJobNo() + "]对应的线体编码[" + pmToolJobEntity.getLineCode() + "]工位编码[" + pmToolJobEntity.getWorkstationCode() + "]工具编码[" + pmToolJobEntity.getToolCode() + "]job号[" + pmToolJobEntity.getJobNo() + "]工艺编码[" + pmToolJobEntity.getWoCode() + "]工艺类型不是[SCR-拧紧]类型，请检查工艺编码");
        }
        pmToolJobEntity.setPmWoId(wo.getId());
    }

    private void verifyAndSaveEntity(List<PmToolJobEntity> listEntity,
                                     PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        Map<Long, Set<String>> mapOfToolJobNoByStationId = new HashMap(16);
        Map<Long, Set<String>> mapOfToolJobWoByStationId = new HashMap(16);
        for (PmToolJobEntity toolJob : listEntity) {
            ClassUtil.validNullByNullAnnotation(toolJob);
            //验证jobno
            verifyToolJobNo(toolJob, mapOfToolJobNoByStationId);
            //验证wo
            verifyToolJobWo(toolJob, mapOfToolJobWoByStationId);

            PmToolJobEntity existToolJob = currentUnDeployData.getToolJobs().stream().filter(
                            item -> Objects.equals(item.getPrcPmWorkshopId(), toolJob.getPrcPmWorkshopId())
                                    && Objects.equals(item.getPrcPmLineId(), toolJob.getPrcPmLineId())
                                    && Objects.equals(item.getPrcPmToolId(), toolJob.getPrcPmToolId())
                                    && Objects.equals(item.getJobNo(), toolJob.getJobNo()))
                    .findFirst().orElse(null);
            if (existToolJob != null) {
                LambdaUpdateWrapper<PmToolJobEntity> luw = new LambdaUpdateWrapper();
                luw.set(PmToolJobEntity::getPrcPmToolId, toolJob.getPrcPmToolId());
                luw.set(PmToolJobEntity::getJobNo, toolJob.getJobNo());
                luw.set(PmToolJobEntity::getPmWoId, toolJob.getPmWoId());
                luw.set(PmToolJobEntity::getFeatureCode, toolJob.getFeatureCode());
                luw.set(PmToolJobEntity::getIsDelete, toolJob.getIsDelete());

                luw.eq(PmToolJobEntity::getId, existToolJob.getId());
                luw.eq(PmToolJobEntity::getVersion, 0);
                this.update(luw);
            } else if (!toolJob.getIsDelete()) {
                toolJob.setVersion(0);
                this.insert(toolJob);
            }
        }

    }

    private void verifyToolJobNo(PmToolJobEntity toolJob, Map<Long, Set<String>> mapOfToolJobNoByStationId) {
        Set<String> setOfToolJobNo = mapOfToolJobNoByStationId.computeIfAbsent(toolJob.getPrcPmToolId(), v -> new HashSet<>());
        if (setOfToolJobNo.contains(toolJob.getJobNo())) {
            throw new InkelinkException("车间[" + toolJob.getWorkshopCode() + "]>线体[" + toolJob.getLineCode() + "]>工具[" + toolJob.getToolCode() + "]>Job编号[" + toolJob.getJobNo() + "]重复");
        }
        setOfToolJobNo.add(toolJob.getJobNo());
    }

    private void verifyToolJobWo(PmToolJobEntity toolJob, Map<Long, Set<String>> mapOfToolJobWoByStationId) {
        Set<String> setOfToolJobWo = mapOfToolJobWoByStationId.computeIfAbsent(toolJob.getPrcPmToolId(), v -> new HashSet<>());
        if (setOfToolJobWo.contains(toolJob.getWoCode())) {
            throw new InkelinkException("车间[" + toolJob.getWorkshopCode() + "]>线体[" + toolJob.getLineCode() + "]>Job编号[" + toolJob.getJobNo() + "]>wo编码[" + toolJob.getWoCode() + "]重复");
        }
        setOfToolJobWo.add(toolJob.getWoCode());
    }


}