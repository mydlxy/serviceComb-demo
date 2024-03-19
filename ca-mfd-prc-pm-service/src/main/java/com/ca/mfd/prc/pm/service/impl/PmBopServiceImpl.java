package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.caching.LocalCache;

import com.ca.mfd.prc.common.exception.InkelinkException;

import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.*;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.*;

import com.ca.mfd.prc.pm.mapper.IPmBopMapper;

import com.ca.mfd.prc.pm.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysConfigurationEntity;

import com.ca.mfd.prc.pm.service.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.ca.mfd.prc.common.constant.Constant.TRUE_BOOL;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_CHINESE;
import static com.ca.mfd.prc.common.constant.Constant.TRUE_NUM;

/**
 * @author inkelink
 * @Description: 超级BOP服务实现
 * @date 2023年08月30日
 * @变更说明 BY inkelink At 2023年08月30日
 */
@Service
public class PmBopServiceImpl extends AbstractCrudServiceImpl<IPmBopMapper, PmBopEntity> implements IPmBopService {

    private static final Object lockObj = new Object();
    private static final Map<String, String> BOOLEAN_FIELDS_MAPPING = new HashMap<>(2);
    @Resource
    IPmBopMapper bopDao;
    @Autowired
    private IPmWorkShopService pmWorkShopService;
    @Autowired
    private IPmLineService pmLineService;
    @Autowired
    private IPmWoService pmWoService;
    @Autowired
    private IPmToolService pmToolService;
    @Autowired
    private IPmToolJobService pmToolJobService;
    @Autowired
    private IPmWorkStationService pmWorkStationService;
    @Autowired
    private IPmTraceComponentService pmTraceComponentService;
    @Autowired
    private LocalCache localCache;
    @Autowired
    private SysConfigurationProvider sysConfigurationService;
    @Autowired
    private IPmPfmeaService pmPfmeaService;
    @Autowired
    private IPmBopBomService pmBopBomService;


    private final String cacheName = "PRC_PM_BOP";
    static {
        BOOLEAN_FIELDS_MAPPING.put("deleteFlag", "isDelete");
        BOOLEAN_FIELDS_MAPPING.put("enableFlag", "isEnable");
    }

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmBopEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmBopEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmBopEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmBopEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PmBopEntity> getAllDatas() {
        List<PmBopEntity> datas = localCache.getObject(cacheName);
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
    public List<PmBopEntity> getPmBopEntityByVersion(Long shopId, Boolean aFalse) {
        QueryWrapper<PmBopEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmBopEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmBopEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmBopEntity::getIsDelete, aFalse);
        return bopDao.selectList(queryWrapper);
    }

    @Override
    public void beforeUpdate(PmBopEntity entity) {
        valid(entity);
    }

    @Override
    public void beforeInsert(PmBopEntity entity) {
        valid(entity);
    }

    @Override
    public void beforeUpdate(Wrapper<PmBopEntity> updateWrapper) {
        validFeatureCode(updateWrapper.getEntity());
    }

    /**
     * 自动填充车间、线体、工位信息
     *
     * @param entity
     */
    private void valid(PmBopEntity entity) {
        validFeatureCode(entity);
        //工位信息
        validWorkStation(entity);
        //线体信息
        validLine(entity);
        //车间信息
        validWorkShop(entity);
        //组件信息
        setWoQmDefectComponentCode(entity);
//        validDataUnique(entity.getId(), "PROCESS_STEP", entity.getProcessStep(),
//                "已经存在作业步骤为[%s]的数据,同一工位同一工序作业步骤不能重复",
//                "PROCESS_NO",String.valueOf(entity.getProcessNo()),
//                "PRC_PM_WORKSTATION_ID",String.valueOf(entity.getPrcPmWorkstationId()));

    }

    private void setWoQmDefectComponentCode(PmBopEntity entity){
        if(entity.getQmDefectComponentId() != null && entity.getQmDefectComponentId() > 0){
            PmTraceComponentEntity pmTraceComponentEntity = pmTraceComponentService.get(entity.getQmDefectComponentId());
            if(pmTraceComponentEntity != null){
                entity.setWoQmDefectComponentCode(pmTraceComponentEntity.getTraceComponentCode());
                entity.setWoQmDefectComponentDescription(pmTraceComponentEntity.getTraceComponentDescription());
            }
        }
    }

    private void validWorkStation(PmBopEntity entity) {
        //工位信息
        QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkStationEntity> lwq = qw.lambda();
        lwq.eq(PmWorkStationEntity::getId, entity.getPrcPmWorkstationId());
        lwq.eq(PmWorkStationEntity::getVersion, 0);
        PmWorkStationEntity pmWorkStationEntity = pmWorkStationService.getData(qw,false).stream().findFirst().orElse(null);
        if (pmWorkStationEntity == null) {
            throw new InkelinkException("BOP对应的工位不存在");
        }
        entity.setWorkstationCode(pmWorkStationEntity.getWorkstationCode());
        entity.setWorkstationName(pmWorkStationEntity.getWorkstationName());
        entity.setPrcPmLineId(pmWorkStationEntity.getPrcPmLineId());
    }

    private void validLine(PmBopEntity entity) {
        //线体信息
        QueryWrapper<PmLineEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmLineEntity> lwq = qw.lambda();
        lwq.eq(PmLineEntity::getId, entity.getPrcPmLineId());
        lwq.eq(PmLineEntity::getVersion, 0);
        PmLineEntity pmLineEntity = pmLineService.getData(qw,false).stream().findFirst().orElse(null);
        if (pmLineEntity == null) {
            throw new InkelinkException("BOP对应的线体不存在");
        }
        entity.setLineCode(pmLineEntity.getLineCode());
        entity.setLineName(pmLineEntity.getLineName());
        entity.setPrcPmWorkshopId(pmLineEntity.getPrcPmWorkshopId());
    }

    private void validWorkShop(PmBopEntity entity) {
        //车间信息
        QueryWrapper<PmWorkShopEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkShopEntity> lwq = qw.lambda();
        lwq.eq(PmWorkShopEntity::getId, entity.getPrcPmWorkshopId());
        lwq.eq(PmWorkShopEntity::getVersion, 0);
        PmWorkShopEntity pmWorkShopEntity = pmWorkShopService.getData(qw,false).stream().findFirst().orElse(null);
        if (pmWorkShopEntity == null) {
            throw new InkelinkException("BOP对应的车间不存在");
        }
        entity.setWorkshopCode(pmWorkShopEntity.getWorkshopCode());
    }

    private void validFeatureCode(PmBopEntity entity) {
        if(entity != null && StringUtils.isNotBlank(entity.getFeatureCode())){
            if(!verifyFeatureCode(entity.getFeatureCode(),entity.getWorkshopCode())
            && !FeatureTool.verifyExpression(entity.getFeatureCode())){
                throw new InkelinkException("特征值表达式无效！");
            }
        }
//        List<ConditionDto> shopConditionDtoList = new ArrayList<>(2);
//        shopConditionDtoList.add(new ConditionDto("prcPmWorkstationId", entity.getPrcPmWorkstationId().toString(), ConditionOper.Equal));
//        shopConditionDtoList.add(new ConditionDto("processNo", entity.getProcessNo().toString(), ConditionOper.Equal));
//        shopConditionDtoList.add(new ConditionDto("id", entity.getId() == null ? "0" : entity.getId().toString(), ConditionOper.Unequal));
//        PmBopEntity existPmBopEntity = this.getData(shopConditionDtoList).stream().findFirst().orElse(null);
//        if (existPmBopEntity != null) {
//            throw new InkelinkException("同一个工位下工序不能重复！");
//        }
    }
    @Override
    public List<PmBopEntity> getByWorkStationId(Long stationId){
        return bopDao.selectList(Wrappers.lambdaQuery(PmBopEntity.class)
                .eq(PmBopEntity::getPrcPmWorkstationId, stationId)
                .eq(PmBopEntity::getIsDelete, false))
                .stream()
                .sorted((a, b) -> b.getProcessNo().compareTo(a.getProcessNo()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PmBopEntity> getByShopId(Long shopId) {
        return bopDao.selectList(Wrappers.lambdaQuery(PmBopEntity.class)
                .eq(PmBopEntity::getPrcPmWorkshopId, shopId)
                .eq(PmBopEntity::getIsDelete, false));
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> pmBopMap = new HashMap<>(37);
        pmBopMap.put("workshopCode", "车间代码");
        pmBopMap.put("lineCode", "线体代码");
        pmBopMap.put("workstationCode", "工位代码");
        pmBopMap.put("processNo", "工序");
        pmBopMap.put("processRemark", "工序描述");
        pmBopMap.put("processStep", "作业步骤");
        pmBopMap.put("woCode", "操作编码");
        pmBopMap.put("woOperType", "操作类型");
        pmBopMap.put("woQmDefectAnomalyCode", "缺陷代码");
        pmBopMap.put("woQmDefectComponentCode", "组件代码");
        pmBopMap.put("woTrcByGroup", "批量追溯");
        pmBopMap.put("woGroupName", "分组");
        pmBopMap.put("processType", "工艺类型");

        pmBopMap.put("actionRemark", "动作描述");
        pmBopMap.put("processObjects", "作业对象");
        pmBopMap.put("materialNo", "零件编号");
        pmBopMap.put("materialName", "零件名称");
        pmBopMap.put("materialQuantity", "零件用量");

        pmBopMap.put("equipmentCode", "设备编号");
        pmBopMap.put("equipmentName", "设备名称");

        pmBopMap.put("toolCode", "工具编号");
        pmBopMap.put("toolName", "工具名称");
        pmBopMap.put("toolType", "工具类型");
        pmBopMap.put("toolBrand", "工具品牌");
        pmBopMap.put("toolNetType", "工具交互类型");
        pmBopMap.put("toolIp", "工具IP");
        pmBopMap.put("toolPort", "端口号");

        pmBopMap.put("job", "JOB号");

       // pmBopMap.put("actionImage", "操作图册文件名");
       // pmBopMap.put("materialImage", "零件图册文件名");
        pmBopMap.put("beginTime", "时序开始时间");
        pmBopMap.put("endTime", "时序结束时间");
        pmBopMap.put("processCount", "作业次数");
        pmBopMap.put("specialRequest", "特殊要求");
        pmBopMap.put("pfmeaFile", "PFMEA文件");
        //pmBopMap.put("controlFile", "控制计划");
        pmBopMap.put("featureCode", "特征表达式");
        pmBopMap.put("deleteFlag", "是否删除");
        pmBopMap.put("enableFlag", "是否启用");
        return pmBopMap;
    }


    /**
     * 分离集合
     * @param listOfEachSheet
     * @return
     */
    @Override
    public Map<String,List<Map<String, String>>> getSeparateList(List<Map<String, String>> listOfEachSheet){
        if(listOfEachSheet.isEmpty()){
            return Collections.emptyMap();
        }
        Map<String,List<Map<String, String>>> targetMap = new HashMap<>(4);
        Map<String, String> newDataOfEachRow;
        for(Map<String, String> dataOfEachRow : listOfEachSheet){
            newDataOfEachRow = getWoSeparateMap(dataOfEachRow);
            if(!newDataOfEachRow.isEmpty()){
                targetMap.computeIfAbsent("wo",v -> new ArrayList<>()).add(newDataOfEachRow);
            }
            newDataOfEachRow = getToolSeparateMap(dataOfEachRow);
            if(!newDataOfEachRow.isEmpty()){
                targetMap.computeIfAbsent("tool",v -> new ArrayList<>()).add(newDataOfEachRow);
            }
            newDataOfEachRow = getToolJobSeparateMap(dataOfEachRow);
            if(!newDataOfEachRow.isEmpty()){
                targetMap.computeIfAbsent("toolJob",v -> new ArrayList<>()).add(newDataOfEachRow);
            }
            newDataOfEachRow = getMaterialSeparateMap(dataOfEachRow);
            if(!newDataOfEachRow.isEmpty()){
                targetMap.computeIfAbsent("material",v -> new ArrayList<>()).add(newDataOfEachRow);
            }
            newDataOfEachRow = getOperBookSeparateMap(dataOfEachRow);
            if(!newDataOfEachRow.isEmpty()){
                targetMap.computeIfAbsent("operBook",v -> new ArrayList<>()).add(newDataOfEachRow);
            }
        }
        return targetMap;
    }

    private Map<String, String> getWoSeparateMap(Map<String, String> dataOfEachRow){
        return getSeparateMap(dataOfEachRow,getWoBopMapping(),getWoNotRequiredFields());
    }

    private Map<String, String> getToolSeparateMap(Map<String, String> dataOfEachRow){
        return getSeparateMap(dataOfEachRow,getToolBopMapping(),getToolNotRequiredFields());
    }

    private Map<String, String> getToolJobSeparateMap(Map<String, String> dataOfEachRow){
        return getSeparateMap(dataOfEachRow,getJobBopMapping(),Collections.emptyList());
    }

    private Map<String, String> getMaterialSeparateMap(Map<String, String> dataOfEachRow){
        return getSeparateMap(dataOfEachRow,getMaterialBopMapping(),Collections.emptyList());
    }

    private Map<String, String> getOperBookSeparateMap(Map<String, String> dataOfEachRow){
        return getSeparateMap(dataOfEachRow,getOperBookBopMapping(),getOperBookNotRequiredFields());
    }

    private Map<String, String> getSeparateMap(Map<String, String> dataOfEachRow,
                                               Map<String, String> mapping,
                                               List<String> notRequiredFields){
        Map<String,String> targetMap = new HashMap<>(dataOfEachRow.size());
        for(Map.Entry<String,String> entry : mapping.entrySet()){
            String val = dataOfEachRow.get(entry.getValue());
            if(StringUtils.isBlank(val)
                    && notRequiredFields != null
                    && !notRequiredFields.contains(entry.getKey())){
                return Collections.emptyMap();
            }
            targetMap.put(entry.getKey(),StringUtils.isBlank(val) ? "" : val);
        }
        return targetMap;
    }


    @Override
    public Map<String, String> getWoBopMapping(){
        Map<String, String> mapping = new HashMap<>(17);
        mapping.put("workshopCode", "workshopCode");
        mapping.put("lineCode", "lineCode");
        mapping.put("workstationCode", "workstationCode");
        mapping.put("displayNo","woDisplayNo");
        mapping.put("woCode", "woCode");
        mapping.put("woType", "processType");
        mapping.put("operType", "woOperType");
        mapping.put("qmDefectAnomalyCode", "woQmDefectAnomalyCode");
        mapping.put("qmDefectAnomalyDescription", "woQmDefectAnomalyDescription");

        mapping.put("traceComponentCode", "woQmDefectComponentCode");
        mapping.put("trcByGroup", "woTrcByGroup");
        mapping.put("woGroupName", "woGroupName");
        mapping.put("featureCode", "featureCode");
        mapping.put("woDescription", "processRemark");
        mapping.put("remark", "remark");
        mapping.put("isEnable", "isEnable");
        mapping.put("deleteFlag", "deleteFlag");
        mapping.put("enableFlag", "enableFlag");
        return mapping;
    }

    private List<String> getWoNotRequiredFields(){
        List<String> woNotRequiredFields = new ArrayList<>(1);
        woNotRequiredFields.add("remark");
        return woNotRequiredFields;
    }

    @Override
    public Map<String, String> getToolBopMapping(){
        Map<String, String> mapping = new HashMap<>(13);
        mapping.put("workshopCode", "workshopCode");
        mapping.put("lineCode", "lineCode");
        mapping.put("workstationCode", "workstationCode");
        mapping.put("toolCode", "toolCode");
        mapping.put("toolName", "toolName");
        mapping.put("toolType", "toolType");
        mapping.put("brand", "toolBrand");
        mapping.put("netType", "toolNetType");
        mapping.put("ip", "toolIp");
        mapping.put("port", "toolPort");
        mapping.put("isEnable", "isEnable");
        mapping.put("remark", "remark");
        mapping.put("deleteFlag", "deleteFlag");
        mapping.put("enableFlag", "enableFlag");
        return mapping;
    }

    private List<String> getToolNotRequiredFields(){
        List<String> toolNotRequiredFields = new ArrayList<>(1);
        toolNotRequiredFields.add("remark");
        return toolNotRequiredFields;
    }

    @Override
    public Map<String, String> getJobBopMapping(){
        Map<String, String> mapping = new HashMap<>(7);
        mapping.put("workshopCode", "workshopCode");
        mapping.put("lineCode", "lineCode");
        mapping.put("workstationCode", "workstationCode");
        mapping.put("toolCode", "toolCode");
        mapping.put("jobNo", "job");
        mapping.put("woCode", "woCode");
        mapping.put("featureCode", "featureCode");
        mapping.put("deleteFlag", "deleteFlag");
        return mapping;
    }

    @Override
    public Map<String, String> getOperBookBopMapping() {
        Map<String, String> mapping = new HashMap<>(9);
        mapping.put("workshopCode", "workshopCode");
        mapping.put("lineCode", "lineCode");
        mapping.put("workstationCode", "workstationCode");
        //mapping.put("woBookName","processRemark");
        mapping.put("prcPmProcessNo", "processNo");
        mapping.put("materialName", "materialName");
        mapping.put("materialNo", "materialNo");
        mapping.put("featureCode", "featureCode");
        mapping.put("actionImage", "actionImage");
        mapping.put("materialImage", "materialImage");

        mapping.put("remark", "remark");
        mapping.put("deleteFlag", "deleteFlag");
        return mapping;
    }

    @Override
    public PageData<PmBopEntity> pageGroup(PageDataDto model) {
        List<String> columns = new ArrayList<>(8);
        columns.add("DISTINCT PRC_PM_WORKSHOP_CODE AS WORKSHOP_CODE");
        columns.add("PRC_PM_WORKSHOP_ID AS PRC_PM_WORKSHOP_ID");
        columns.add("PRC_PM_LINE_ID AS PRC_PM_LINE_ID");
        columns.add("PRC_PM_LINE_CODE AS LINE_CODE");
        columns.add("PRC_PM_WORKSTATION_ID AS PRC_PM_WORKSTATION_ID");
        columns.add("PRC_PM_WORKSTATION_CODE AS WORKSTATION_CODE");
        columns.add("PROCESS_NO AS PROCESS_NO");
        columns.add("PROCESS_NAME AS PROCESS_NAME");
        columns.add("FEATURE_CODE AS FEATURE_CODE");
        return page(model,columns);
    }

    @Override
    public void updateByProcessNo(PmBopEntity pmBopEntity) {
        QueryWrapper<PmBopEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmBopEntity> lqw = qw.lambda();
        if(pmBopEntity.getPrcPmWorkstationId() != null && pmBopEntity.getPrcPmWorkstationId() > 0){
            lqw.eq(PmBopEntity :: getPrcPmWorkstationId,pmBopEntity.getPrcPmWorkstationId());
        }else{
            lqw.eq(PmBopEntity :: getWorkstationCode,pmBopEntity.getWorkstationCode());
        }
        lqw.eq(PmBopEntity :: getProcessNo,pmBopEntity.getProcessNo());
        lqw.eq(PmBopEntity :: getFeatureCode,pmBopEntity.getFeatureCode());
        List<PmBopEntity> bops = this.getData(qw,false);
        validFeatureCode(pmBopEntity);
        if(bops.isEmpty()){
            this.insert(pmBopEntity);
        }else{
            UpdateWrapper<PmBopEntity> uw = new UpdateWrapper<>();
            LambdaUpdateWrapper<PmBopEntity> luw = uw.lambda();
            luw.set(PmBopEntity :: getProcessName,pmBopEntity.getProcessName());
            if(pmBopEntity.getPrcPmWorkstationId() != null && pmBopEntity.getPrcPmWorkstationId() >0){
                luw.eq(PmBopEntity :: getPrcPmWorkstationId,pmBopEntity.getPrcPmWorkstationId());
            }else{
                luw.eq(PmBopEntity :: getWorkstationCode,pmBopEntity.getWorkstationCode());
            }
            luw.eq(PmBopEntity :: getProcessNo,pmBopEntity.getProcessNo());
            luw.eq(PmBopEntity :: getFeatureCode,pmBopEntity.getFeatureCode());
            luw.eq(PmBopEntity :: getIsDelete,false);
            this.update(uw);
        }
    }


    private boolean verifyFeatureCode(String featureCode,String shopCode){
        if(StringUtils.indexOf(featureCode,":") == -1 ){
            QueryWrapper<PmBopBomEntity> qw = new QueryWrapper<>();
            LambdaQueryWrapper<PmBopBomEntity> lqw = qw.lambda();
            lqw.eq(PmBopBomEntity :: getUseWorkShop,shopCode);
            lqw.eq(PmBopBomEntity :: getUsageValue,featureCode);
            List<PmBopBomEntity> mboms =  pmBopBomService.getData(qw,false);
            if(mboms.isEmpty()){
                return false;
            }
            return true;
        }
        return false;
    }


    @Override
    public void delete(Serializable[] ids) {
        QueryWrapper<PmBopEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmBopEntity> lqw = new LambdaQueryWrapper();
        lqw.in(PmBopEntity :: getId,ids);
        List<PmBopEntity> bops = this.getData(qw,false);
        if(!bops.isEmpty()){
            List<Long> longIds = new ArrayList<>();
            for(Serializable id : ids){
                longIds.add((Long)id);
            }
            List<PmPfmeaEntity> plans = getPfmea(longIds);
            if(!plans.isEmpty()){
                throw new InkelinkException("该工步下存在未删除的pfmea配置，请先删除pfmea配置");
            }
        }
        this.delete(ids, true);
    }

    @Override
    public void delByProcessNo(PmBopEntity pmBopEntity) {
        List<PmPfmeaEntity> plans = getPfmeaByProcessNos(
                Arrays.asList(pmBopEntity.getProcessNo()),
                pmBopEntity.getPrcPmWorkstationId(),
                pmBopEntity.getWorkstationCode());
        if(!plans.isEmpty()){
            throw new InkelinkException("该工序下存在未删除的PFMEA配置，请先删除控制计划");
        }
        UpdateWrapper<PmBopEntity> uw = new UpdateWrapper<>();
        LambdaUpdateWrapper<PmBopEntity> luw = uw.lambda();
        luw.set(PmBopEntity :: getIsDelete,true);
        if(pmBopEntity.getPrcPmWorkstationId() != null
                && pmBopEntity.getPrcPmWorkstationId() >0){
            luw.eq(PmBopEntity :: getPrcPmWorkstationId,pmBopEntity.getPrcPmWorkstationId());
        }else{
            luw.eq(PmBopEntity :: getWorkstationCode,pmBopEntity.getWorkstationCode());
        }
        luw.eq(PmBopEntity :: getProcessNo,pmBopEntity.getProcessNo());
        luw.eq(PmBopEntity :: getIsDelete,false);
        this.update(uw);
    }


    private List<PmPfmeaEntity> getPfmeaByProcessNos(List<Integer> processNos,
                                                                 Long pmWorkstationId,String workstationCode){
        QueryWrapper<PmBopEntity> qw = new QueryWrapper<>();
        LambdaQueryWrapper<PmBopEntity> lqw = qw.lambda();
        if(pmWorkstationId != null && pmWorkstationId >0){
            lqw.eq(PmBopEntity :: getPrcPmWorkstationId,pmWorkstationId);
        }else{
            lqw.eq(PmBopEntity :: getWorkstationCode,workstationCode);
        }
        if(processNos != null && !processNos.isEmpty()){
            lqw.in(PmBopEntity :: getProcessNo,processNos);
        }
        List<PmBopEntity> bops = this.getData(qw,false);
        if(!bops.isEmpty()){
            List<Long> bopIds = bops.stream().map(PmBopEntity :: getId).collect(Collectors.toList());
            return getPfmea(bopIds);
        }
        return Collections.emptyList();
    }



    private List<PmPfmeaEntity> getPfmea(List<Long> bopIds){
        if(!bopIds.isEmpty()){
            QueryWrapper<PmPfmeaEntity> qwc = new QueryWrapper<>();
            LambdaQueryWrapper<PmPfmeaEntity> lqwc = qwc.lambda();
            lqwc.in(PmPfmeaEntity :: getPrcPmBopId,bopIds);
            return pmPfmeaService.getData(qwc,false);
        }
        return Collections.emptyList();
    }



    private List<String> getOperBookNotRequiredFields(){
        List<String> OperBookNotRequiredFields = new ArrayList<>(1);
        OperBookNotRequiredFields.add("remark");
        return OperBookNotRequiredFields;
    }


    @Override
    public Map<String, String> getMaterialBopMapping(){
        Map<String, String> mapping = new HashMap<>(8);
        mapping.put("workshopCode", "workshopCode");
        mapping.put("lineCode", "lineCode");
        mapping.put("workstationCode", "workstationCode");
        mapping.put("materialNo", "materialNo");
        mapping.put("masterChinese", "materialName");
        mapping.put("featureCode", "featureCode");
        mapping.put("materialNum", "materialQuantity");
        mapping.put("materialUnit", "materialUnit");
        mapping.put("deleteFlag", "deleteFlag");
        return mapping;
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData){
        List<PmBopEntity> listOfPmBop;
        try{
            listOfPmBop = this.convertExcelDataToEntity(listFromExcelSheet,
                    mapSysConfigByCategory, sheetName);
        }catch (Exception e){
            log.error("导入bop失败",e);
            throw new InkelinkException("导入bop失败:" + e.getMessage());
        }
        //设置外键
        setForeignKey(listOfPmBop, currentUnDeployData);
        //验证并保存
        //verifyAndSaveEntity(listOfPmBop,currentUnDeployData);
        verifyAndSaveEntity(listOfPmBop);
    }

    private void setForeignKey(List<PmBopEntity> listOfPmBop, PmAllDTO pmAllDTO) {
        if (listOfPmBop.isEmpty()) {
            return;
        }
        for (PmBopEntity pmBopEntity : listOfPmBop) {
            setForeignKey(pmBopEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmBopEntity pmBopEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        //设置车间id
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmBopEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("工序[" + pmBopEntity.getProcessRemark() + "]对应的车间编码[" + pmBopEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmBopEntity.setPrcPmWorkshopId(shop.getId());
        List<PmLineEntity> lines = pmAllDTO.getLines();
        //设置线体id
        PmLineEntity line = lines.stream().filter(item -> Objects.equals(pmBopEntity.getLineCode(), item.getLineCode())
                        && Objects.equals(pmBopEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId()))
                .findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("工序[" + pmBopEntity.getProcessRemark() + "]对应的线体编码[" + pmBopEntity.getLineCode() + "]没有对应任何线体，请检查是否有配置对应编码线体");
        }
        pmBopEntity.setPrcPmLineId(line.getId());
        pmBopEntity.setLineName(line.getLineName());
        List<PmWorkStationEntity> stations = pmAllDTO.getStations();
        //设置工位id
        PmWorkStationEntity workStation = stations.stream().filter(item -> Objects.equals(pmBopEntity.getWorkstationCode(), item.getWorkstationCode())
                        && Objects.equals(pmBopEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId())
                        && Objects.equals(pmBopEntity.getPrcPmLineId(), item.getPrcPmLineId()))
                .findFirst().orElse(null);
        if (workStation == null) {
            throw new InkelinkException("工序[" + pmBopEntity.getProcessRemark() + "]对应的线体编码[" + pmBopEntity.getLineCode() + "]对应的工位编码[" + pmBopEntity.getWorkstationCode() + "]没有对应任何工位，请检查是否有配置对应编码工位");
        }
        pmBopEntity.setPrcPmWorkstationId(workStation.getId());
        pmBopEntity.setWorkstationName(workStation.getWorkstationName());
    }

    private void verifyAndSaveEntity(List<PmBopEntity> listEntity,PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        for (PmBopEntity pmBopEntity : listEntity) {
            ClassUtil.validNullByNullAnnotation(pmBopEntity);
            doSave(pmBopEntity,currentUnDeployData.getBops());
        }
    }

    private void verifyAndSaveEntity(List<PmBopEntity> listEntity) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        verifyBopEntity(listEntity);
        for (PmBopEntity pmBopEntity : listEntity) {
            ClassUtil.validNullByNullAnnotation(pmBopEntity);
            doSave(pmBopEntity);
        }
    }

    private void verifyBopEntity(List<PmBopEntity> listEntity){
        Set<String> bopGroupByStationCodeAndProcessNoSet = new HashSet<>(listEntity.size());
        for(PmBopEntity pmBopEntity : listEntity){
            String value = pmBopEntity.getWorkstationCode() + ":" + pmBopEntity.getProcessNo() + ":" + pmBopEntity.getProcessStep();
            if(bopGroupByStationCodeAndProcessNoSet.contains(value)){
                throw new InkelinkException(String.format("工位[%s]工序[%s]工步[%s]重复",pmBopEntity.getWorkstationCode(),pmBopEntity.getProcessNo(), pmBopEntity.getProcessStep()));
            }
            bopGroupByStationCodeAndProcessNoSet.add(value);
        }
    }

    private void doSave(PmBopEntity dto,List<PmBopEntity> bops){
        if(StringUtils.isNotBlank(dto.getWoCode())
                && StringUtils.isBlank(dto.getJob())){
            dto.setModelType("wo");
            doSaveWoData(dto,bops);
        }else if(StringUtils.isNotBlank(dto.getMaterialNo())){
            dto.setModelType("material");
            doSaveMaterialData(dto,bops);
        }else if(StringUtils.isNotBlank(dto.getToolCode())
                && StringUtils.isBlank(dto.getJob())){
            dto.setModelType("tool");
            doSaveToolData(dto,bops);
        }else if(StringUtils.isNotBlank(dto.getJob())){
            dto.setModelType("job");
            doSaveJobData(dto,bops);
        }else if(StringUtils.isNotBlank(dto.getPfmeaFile())){
            dto.setModelType("operBook");
            doSaveOperBookData(dto,bops);
        }else{
            dto.setModelType("other");
            insert(dto);
        }
    }

    private void doSave(PmBopEntity dto){
        QueryWrapper<PmBopEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmBopEntity> lqw = qw.lambda();
        lqw.eq(PmBopEntity::getWorkstationCode,dto.getWorkstationCode());
        lqw.eq(PmBopEntity::getProcessNo,dto.getProcessNo());
        lqw.eq(PmBopEntity::getProcessStep,dto.getProcessStep());
        lqw.eq(PmBopEntity::getIsDelete,false);
        PmBopEntity existBop = bopDao.selectList(qw).stream().findFirst().orElse(null);
        if(existBop != null){
            Long id = existBop.getId();
            BeanUtils.copyProperties(dto,existBop);
            existBop.setId(id);
            this.updateById(existBop);
        }else{
            this.insert(dto);
        }
    }

    private void doSaveWoData(PmBopEntity pmBopEntity,List<PmBopEntity> bops){
            //找有woCode但是 又没有job号的数据，因为job数据里也有woCode
            PmBopEntity existsBop = bops.stream().filter(item ->
                    Objects.equals(pmBopEntity.getPrcPmWorkstationId(), item.getPrcPmWorkstationId())
                            && Objects.equals(pmBopEntity.getWoCode(), item.getWoCode())
                            && StringUtils.isBlank(item.getJob())
            ).findFirst().orElse(null);
            saveBopEntity(existsBop,pmBopEntity);
    }

    private void doSaveMaterialData(PmBopEntity pmBopEntity,List<PmBopEntity> bops){
        PmBopEntity existsBop = bops.stream().filter(item ->
                Objects.equals(pmBopEntity.getPrcPmWorkstationId(), item.getPrcPmWorkstationId())
                        && Objects.equals(pmBopEntity.getMaterialNo(), item.getMaterialNo())
        ).findFirst().orElse(null);
        saveBopEntity(existsBop,pmBopEntity);
    }

    private void doSaveToolData(PmBopEntity pmBopEntity,List<PmBopEntity> bops){
        PmBopEntity existsBop = bops.stream().filter(item ->
                Objects.equals(pmBopEntity.getPrcPmWorkstationId(), item.getPrcPmWorkstationId())
                        && Objects.equals(pmBopEntity.getToolCode(), item.getToolCode())
        ).findFirst().orElse(null);
        saveBopEntity(existsBop,pmBopEntity);
    }

    private void doSaveJobData(PmBopEntity pmBopEntity,List<PmBopEntity> bops){
        PmBopEntity existsBop = bops.stream().filter(item ->
                Objects.equals(pmBopEntity.getPrcPmWorkstationId(), item.getPrcPmWorkstationId())
                        && Objects.equals(pmBopEntity.getJob(), item.getJob())
        ).findFirst().orElse(null);
        saveBopEntity(existsBop,pmBopEntity);
    }

    private void doSaveOperBookData(PmBopEntity pmBopEntity,List<PmBopEntity> bops){
        PmBopEntity existsBop = bops.stream().filter(item ->
                Objects.equals(pmBopEntity.getPrcPmWorkstationId(), item.getPrcPmWorkstationId())
                        && StringUtils.isNotBlank(pmBopEntity.getPfmeaFile())
        ).findFirst().orElse(null);
        saveBopEntity(existsBop,pmBopEntity);
    }
    private void saveBopEntity(PmBopEntity oldPmBopEntity,PmBopEntity newPmBopEntity){
        if(oldPmBopEntity != null){
            Long id = oldPmBopEntity.getId();
            BeanUtils.copyProperties(newPmBopEntity,oldPmBopEntity);
            oldPmBopEntity.setId(id);
            this.updateById(oldPmBopEntity);
        }else{
            this.insert(newPmBopEntity);
        }
    }

    @Override
    protected void setBooleanVal(Map<String, String> eachRowData) {
        Set<String> booleanKeySet = BOOLEAN_FIELDS_MAPPING.keySet();
        Set<String> allKeySet = eachRowData.keySet();
        Map<String, String> appendMap = new HashMap<>(2);
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

    @Override
    public void setExcelColumnNames(Map<String, String> columnNames) {
        columnNames.put("lineCode", "线体编码");
        columnNames.put("lineName", "线体名称");
        columnNames.put("workstationCode", "工位代码");
        columnNames.put("workstationName", "工位名称");
        columnNames.put("processNo", "工序标识");
        columnNames.put("processName", "工序名称");
        columnNames.put("featureCode", "使用规则");
        columnNames.put("processStep", "作业步骤");
        columnNames.put("processRemark", "作业描述");
        columnNames.put("processObjects", "作业对象");
        columnNames.put("processCount", "作业次数");
        columnNames.put("attribute1", "作业规范");
        columnNames.put("beginTime", "时序开始时间");
        columnNames.put("endTime", "时序结束时间");
        columnNames.put("materialNo", "物料编码");
        columnNames.put("materialName", "物料名称");
        columnNames.put("materialQuantity", "物料用量");
        columnNames.put("woCode", "工艺代码");
        columnNames.put("processType", "工艺类型");
        columnNames.put("equipmentName", "设备名称");
        columnNames.put("equipmentCode", "设备编码");
        columnNames.put("toolName", "工具名称");
        columnNames.put("toolCode", "工具编码");
        columnNames.put("attribute2", "工装名称");
        columnNames.put("frockCode", "工装编码");
        columnNames.put("job", "JOB号");
        columnNames.put("attribute3", "JOB号描述");
        columnNames.put("attribute4", "扭矩");
        columnNames.put("specialRequest", "特殊要求");
        columnNames.put("controlFile", "控制计划");
    }

    protected class SheetTableName {
        public static final String BOP = "工艺工步";
        public static final String WO = "工艺";
        public static final String TOOL = "工具工装";
        public static final String JOB = "JOB";
        public static final String CONTROL_PLAN = "PFMEA和控制计划";
        public static final String REACTION_PLAN = "反应计划";
    }

    private final Map<String, Map<String, String>> dic = new LinkedHashMap(4);
    {
        Map<String, String> woMap = new LinkedHashMap<>(15);
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getId), "工艺行号(值为0时执行添加)");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getWorkshopCode), "车间编码");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getLineCode), "线体编码");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getWorkstationCode), "工位编码");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getWoCode), "编码");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getWoType), "类型");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getIsEnable), "是否启用");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getDisplayNo), "顺序号");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getTraceComponentCode), "组建编码");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getTrcByGroup), "批量追溯");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getOperType), "操作类型");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getFeatureCode), "特征");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getWoDescription), "工艺描述");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getQmDefectAnomalyCode), "缺陷编码");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getQmDefectAnomalyDescription), "缺陷描述");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getRemark), "备注");
        woMap.put(MpSqlUtils.getColumnName(PmWoEntity::getIsDelete), "是否删除");
        dic.put(SheetTableName.WO, woMap);

        Map<String, String> toolMap = new LinkedHashMap<>(15);
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getId), "工具行号(新增时此项为空或为0)");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getWorkshopCode), "车间编码");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getLineCode), "线体编码");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getWorkstationCode), "工位编码");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getToolCode), "工具号");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getToolName), "工具名称");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getIsEnable), "是否启用");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getDisplayNo), "顺序号");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getToolType), "工具类型");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getBrand), "品牌");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getNetType), "交付方式");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getIp), "ip地址");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getPort), "DB端口");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getRemark), "备注");
        toolMap.put(MpSqlUtils.getColumnName(PmToolEntity::getIsDelete), "是否删除");
        dic.put(SheetTableName.TOOL, toolMap);

        Map<String, String> jobMap = new LinkedHashMap<>(10);
        jobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getId), "JOB行号(值为0时执行添加)");
        jobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getWorkshopCode), "车间编码");
        jobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getLineCode), "线体编码");
        jobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getWorkstationCode), "工位编码");
        jobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getJobNo), "JOB号");
        jobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getWoCode), "工艺编码");
        jobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getToolCode), "工具编码");
        jobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getFeatureCode), "使用规则");
        jobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getAttribute3), "JOB描述");
        jobMap.put(MpSqlUtils.getColumnName(PmToolJobEntity::getIsDelete), "是否删除");
        dic.put(SheetTableName.JOB, jobMap);

        Map<String, String> bomMap = new LinkedHashMap<>(30);
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getId), "BOP行号(值为0时执行添加)");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWorkshopCode), "车间编码");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getLineCode), "线体编码");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWorkstationCode), "工位编码");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessNo), "工序编号(过程项)");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessName), "工序名称(过程项)");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getFeatureCode), "使用规则");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessStep), "工步编号(过程步骤)");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessRemark), "工步名称(过程步骤)");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessObjects), "作业对象");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessCount), "作业次数");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getAttribute1), "作业规范");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getBeginTime), "时序开始时间");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getEndTime), "时序结束时间");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getMaterialNo), "物料编码");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getMaterialName), "物料名称");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getMaterialQuantity), "物料用量");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getWoCode), "工艺代码");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getProcessType), "工艺类型");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getEquipmentName), "设备名称");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getEquipmentCode), "设备编码");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getToolName), "工具名称");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getToolCode), "工具编码");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getAttribute2), "工装名称");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getFrockCode), "工装编码");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getJob), "JOB号");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getAttribute3), "JOB号描述");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getAttribute4), "扭矩");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getSpecialRequest), "特殊要求");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getActionImage), "操作图册");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getMaterialImage), "零件图册");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getControlFile), "控制计划");
        bomMap.put(MpSqlUtils.getColumnName(PmBopEntity::getIsDelete), "是否删除");
        dic.put(SheetTableName.BOP, bomMap);

        Map<String, String> contorlMap = new LinkedHashMap<>(40);
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getId), "PFMEA行号(值为0时执行添加)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getWorkshopCode), "车间编码");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getLineCode), "线体编码");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getWorkstationCode), "工位编码");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getFrockCode), "工步编号(过程步骤)");

        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getProcessWorkElements), "过程工作要素");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getProcessStepFunction), "过程步骤功能");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getProductCharacteristics), "产品特性");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getProductCharacteristicSpecifications), "产品特性规范");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getProcessWorkElementFunctions), "过程工作要素功能");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getProcessCharacteristics), "过程特性");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getProcessCharacteristicSpecifications), "过程特性规范");

        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getFailureConsequence), "失效后果(FE)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getSeverity), "严重度S(P)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getFailureMode), "失效模式(FM)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getReasonForFailure), "失效原因(FC)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getPreventionControl), "预防控制(PC)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getOccurrenceDegree), "发生度O(P)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getDetectionControl), "探测控制(DC)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getDetectionDegree), "探测度D(P)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getPriority), "优先级AP(P)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getPotentialFeatureClassification), "潜在特性分类");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getSpecialProductCharacteristic), "产品特殊特性");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getProcessSpecialCharacteristics), "工艺特殊特性");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getPtc), "PTC");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getFilterCode), "过滤代码(可选)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getPreventiveMeasure), "预防措施");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getDetectionMeasures), "探测措施");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getNameOfPersonCharge), "负责人姓名");
        //contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getOperationDescription), "操作描述");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getTargetCompletionTime), "目标完成时间");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getPfmeaState), "状态");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getMeasuresEvidenceTaken), "采取的措施及证据");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getCompletionTime), "完成时间");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getSeverityD), "严重度S(D)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getOccurrenceDegreeD), "发生度O(D)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getDetectionDegreeD), "探测度D(D)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getPriorityD), "优先级AP(D)");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getRemark), "备注");
        contorlMap.put(MpSqlUtils.getColumnName(PmPfmeaEntity::getHistoricalChangeAuthorization), "历史变更授权");

        contorlMap.put(MpSqlUtils.getColumnName(PmControlPlanEntity::getMeasurementFirst), "评价/测量技术1");
        contorlMap.put(MpSqlUtils.getColumnName(PmControlPlanEntity::getMeasurementSecond), "评价/测量技术2");
        contorlMap.put(MpSqlUtils.getColumnName(PmControlPlanEntity::getSampleSize), "样本容量");
        contorlMap.put(MpSqlUtils.getColumnName(PmControlPlanEntity::getSampleFrequency), "样本频率");
        contorlMap.put(MpSqlUtils.getColumnName(PmControlPlanEntity::getControlMethods), "控制方法");
        contorlMap.put(MpSqlUtils.getColumnName(PmControlPlanEntity::getResponsePlan), "反应计划");
        contorlMap.put(MpSqlUtils.getColumnName(PmControlPlanEntity::getIsDelete), "是否删除");
        dic.put(SheetTableName.CONTROL_PLAN, contorlMap);
    }


    public void setWoForeign(List<PmWoEntity> entities,List<PmWorkShopEntity> shops,List<PmLineEntity> lines,List<PmWorkStationEntity> stations){
        List<PmTraceComponentEntity> listOfTraceComponent = pmTraceComponentService.getAllDatas();
        Map<String,Long> mapOfTraceComponent = new HashMap<>(listOfTraceComponent.size());
        for(PmTraceComponentEntity pmTraceComponentEntity : listOfTraceComponent){
            mapOfTraceComponent.put(pmTraceComponentEntity.getTraceComponentCode(),pmTraceComponentEntity.getId());
        }
        for(PmWoEntity pmWoEntity : entities){
            if(!pmWoEntity.getIsDelete()){
                PmWorkShopEntity shop = shops.stream().filter(item -> item.getWorkshopCode().equals(pmWoEntity.getWorkshopCode())).findFirst().orElse(null);
                if(shop == null){
                    throw new InkelinkException(String.format("工艺[%s]车间编码[%s]无效",pmWoEntity.getWoCode(), pmWoEntity.getWorkshopCode()));
                }
                pmWoEntity.setPrcPmWorkshopId(shop.getId());
                PmLineEntity line = lines.stream().filter(item -> item.getLineCode().equals(pmWoEntity.getLineCode())).findFirst().orElse(null);
                if(line == null){
                    throw new InkelinkException(String.format("工艺[%s]线体编码[%s]无效",pmWoEntity.getWoCode(),pmWoEntity.getLineCode()));
                }
                pmWoEntity.setPrcPmLineId(line.getId());
                PmWorkStationEntity station = stations.stream().filter(item -> item.getWorkstationCode().equals(pmWoEntity.getWorkstationCode())).findFirst().orElse(null);
                if(station == null){
                    throw new InkelinkException(String.format("工艺[%s]工位编码[%s]无效",pmWoEntity.getWoCode(),pmWoEntity.getWorkstationCode()));
                }
                pmWoEntity.setPrcPmWorkstationId(station.getId());
                if(StringUtils.isNotBlank(pmWoEntity.getTraceComponentCode())){
                    Long componentId = mapOfTraceComponent.get(pmWoEntity.getTraceComponentCode());
                    if(componentId == null){
                        throw new InkelinkException(String.format("工艺[%s]组件编码[%s]无效",pmWoEntity.getWoCode(), pmWoEntity.getTraceComponentCode()));
                    }
                    pmWoEntity.setQmDefectComponentId(componentId);
                }
                if(!FeatureTool.verifyExpression(pmWoEntity.getFeatureCode())){
                    throw new InkelinkException(String.format("工艺[%s]特征无效",pmWoEntity.getWoCode()));
                }
            }

        }
    }

    public void setToolForeign(List<PmToolEntity> entities,List<PmWorkShopEntity> shops,List<PmLineEntity> lines,List<PmWorkStationEntity> stations){
        for(PmToolEntity pmToolEntity : entities){
            if(!pmToolEntity.getIsDelete()){
                PmWorkShopEntity shop = shops.stream().filter(item -> item.getWorkshopCode().equals(pmToolEntity.getWorkshopCode())).findFirst().orElse(null);
                if(shop == null){
                    throw new InkelinkException(String.format("工具[%s]车间编码[%s]无效",pmToolEntity.getToolCode(), pmToolEntity.getWorkshopCode()));
                }
                pmToolEntity.setPrcPmWorkshopId(shop.getId());
                PmLineEntity line = lines.stream().filter(item -> item.getLineCode().equals(pmToolEntity.getLineCode())).findFirst().orElse(null);
                if(line == null){
                    throw new InkelinkException(String.format("工具[%s]线体编码[%s]无效",pmToolEntity.getToolCode(),pmToolEntity.getLineCode()));
                }
                pmToolEntity.setPrcPmLineId(line.getId());
                PmWorkStationEntity station = stations.stream().filter(item -> item.getWorkstationCode().equals(pmToolEntity.getWorkstationCode())).findFirst().orElse(null);
                if(station == null){
                    throw new InkelinkException(String.format("工具[%s]工位编码[%s]无效",pmToolEntity.getToolCode(),pmToolEntity.getWorkstationCode()));
                }
                pmToolEntity.setPrcPmWorkstationId(station.getId());
                pmToolEntity.setIsEnable(true);
            }

        }
    }

    public void setJobForeign(List<PmToolJobEntity> entities,List<PmWorkShopEntity> shops,List<PmLineEntity> lines,
                              List<PmWorkStationEntity> stationList){
        QueryWrapper<PmWoEntity> qwWo = new QueryWrapper();
        List<PmWoEntity> wos = pmWoService.getData(qwWo,false);
        QueryWrapper<PmToolEntity> qwTool = new QueryWrapper();
        List<PmToolEntity> tools = pmToolService.getData(qwTool,false);
        for(PmToolJobEntity pmToolJobEntity : entities){
            if(!pmToolJobEntity.getIsDelete()){
                PmWorkShopEntity shop = shops.stream().filter(item -> item.getWorkshopCode().equals(pmToolJobEntity.getWorkshopCode())).findFirst().orElse(null);
                if(shop == null){
                    throw new InkelinkException(String.format("Job号[%s]车间编码[%s]无效",pmToolJobEntity.getJobNo(), pmToolJobEntity.getWorkshopCode()));
                }
                pmToolJobEntity.setPrcPmWorkshopId(shop.getId());
                PmLineEntity line = lines.stream().filter(item -> item.getLineCode().equals(pmToolJobEntity.getLineCode())).findFirst().orElse(null);
                if(line == null){
                    throw new InkelinkException(String.format("Job号[%s]线体编码[%s]无效",pmToolJobEntity.getJobNo(),pmToolJobEntity.getLineCode()));
                }
                pmToolJobEntity.setPrcPmLineId(line.getId());
                PmWorkStationEntity station = stationList.stream().filter(item -> item.getWorkstationCode().equals(pmToolJobEntity.getWorkstationCode())).findFirst().orElse(null);
                if(station == null){
                    throw new InkelinkException(String.format("Job号[%s]工位编码[%s]无效",pmToolJobEntity.getJobNo(),pmToolJobEntity.getWorkstationCode()));
                }
                PmWoEntity wo = wos.stream().filter(item ->
                        item.getWoCode().equals(pmToolJobEntity.getWoCode())
                        && item.getPrcPmWorkstationId().equals(station.getId())
                ).findFirst().orElse(null);
                if(wo == null){
                    throw new InkelinkException(String.format("Job号[%s]工艺编码[%s]无效",pmToolJobEntity.getJobNo(),pmToolJobEntity.getWoCode()));
                }
                pmToolJobEntity.setPmWoId(wo.getId());
                if(StringUtils.isNotBlank(pmToolJobEntity.getToolCode())){
                    PmToolEntity tool = tools.stream().filter(item -> item.getToolCode().equals(pmToolJobEntity.getToolCode())).findFirst().orElse(null);
                    if(tool == null){
                        throw new InkelinkException(String.format("Job号[%s]工具编码[%s]无效",pmToolJobEntity.getJobNo(),pmToolJobEntity.getToolCode()));
                    }
                    pmToolJobEntity.setPrcPmToolId(tool.getId());
                }
            }
        }
    }

    public void setBopForeign(List<PmBopEntity> entities,List<PmWorkShopEntity> shops,List<PmLineEntity> lines,List<PmWorkStationEntity> stations){
        for(PmBopEntity pmBopEntity : entities){
            if(!pmBopEntity.getIsDelete()){
                PmWorkShopEntity shop = shops.stream().filter(item -> item.getWorkshopCode().equals(pmBopEntity.getWorkshopCode())).findFirst().orElse(null);
                if(shop == null){
                    throw new InkelinkException(String.format("工步[%s]车间编码[%s]无效",pmBopEntity.getProcessStep(), pmBopEntity.getWorkshopCode()));
                }
                pmBopEntity.setPrcPmWorkshopId(shop.getId());
                pmBopEntity.setWorkshopCode(shop.getWorkshopCode());
                PmLineEntity line = lines.stream().filter(item -> item.getLineCode().equals(pmBopEntity.getLineCode())).findFirst().orElse(null);
                if(line == null){
                    throw new InkelinkException(String.format("工步[%s]线体编码[%s]无效",pmBopEntity.getProcessStep(),pmBopEntity.getLineCode()));
                }
                pmBopEntity.setPrcPmLineId(line.getId());
                pmBopEntity.setLineCode(line.getLineCode());
                pmBopEntity.setLineName(line.getLineName());
                PmWorkStationEntity station = stations.stream().filter(item -> item.getWorkstationCode().equals(pmBopEntity.getWorkstationCode())).findFirst().orElse(null);
                if(station == null){
                    throw new InkelinkException(String.format("工步[%s]工位编码[%s]无效",pmBopEntity.getProcessStep(),pmBopEntity.getWorkstationCode()));
                }
                pmBopEntity.setPrcPmWorkstationId(station.getId());
                pmBopEntity.setWorkstationCode(station.getWorkstationCode());
                pmBopEntity.setWorkstationName(station.getWorkstationName());
                pmBopEntity.setIsEnable(true);
            }

        }
    }

    public void setPfmeaForeign(List<PmPfmeaEntity> entities,List<PmWorkShopEntity> shops,
                              List<PmLineEntity> lines,
                              List<PmWorkStationEntity> stations,
                                      List<PmBopEntity> bops){
        for(PmPfmeaEntity pmPfmeaEntity : entities){
            if(!pmPfmeaEntity.getIsDelete()){
                PmWorkShopEntity shop = shops.stream().filter(item -> item.getWorkshopCode().equals(pmPfmeaEntity.getWorkshopCode())).findFirst().orElse(null);
                if(shop == null){
                    throw new InkelinkException(String.format("pfmea中工步[%s]车间编码[%s]无效",pmPfmeaEntity.getFrockCode(), pmPfmeaEntity.getWorkshopCode()));
                }
                pmPfmeaEntity.setPrcPmWorkshopId(shop.getId());
                pmPfmeaEntity.setWorkshopCode(shop.getWorkshopCode());
                PmLineEntity line = lines.stream().filter(item -> item.getLineCode().equals(pmPfmeaEntity.getLineCode())).findFirst().orElse(null);
                if(line == null){
                    throw new InkelinkException(String.format("pfmea中工步[%s]线体编码[%s]无效",pmPfmeaEntity.getFrockCode(),pmPfmeaEntity.getLineCode()));
                }
                pmPfmeaEntity.setPrcPmLineId(line.getId());
                pmPfmeaEntity.setLineCode(line.getLineCode());
                PmWorkStationEntity station = stations.stream().filter(item -> item.getWorkstationCode().equals(pmPfmeaEntity.getWorkstationCode())).findFirst().orElse(null);
                if(station == null){
                    throw new InkelinkException(String.format("pfmea中工步[%s]工位编码[%s]无效",pmPfmeaEntity.getFrockCode(),pmPfmeaEntity.getWorkstationCode()));
                }
                pmPfmeaEntity.setPrcPmWorkstationId(station.getId());
                pmPfmeaEntity.setWorkstationCode(station.getWorkstationCode());
                PmBopEntity bop = bops.stream().filter(
                        item ->
                                item.getWorkstationCode().equals(pmPfmeaEntity.getWorkstationCode())
                                        && item.getProcessStep().equals(pmPfmeaEntity.getFrockCode())
                ).findFirst().orElse(null);
                if(bop == null){
                    throw new InkelinkException(String.format("pfmea中工位编码[%s]工步[%s]无效",pmPfmeaEntity.getWorkstationCode(),pmPfmeaEntity.getFrockCode()));
                }
                pmPfmeaEntity.setPrcPmBopId(bop.getId());
            }
        }
    }


    @Override
    public void importExcel(InputStream is) throws Exception {
        Map<String, List<Map<String, String>>> datas = InkelinkExcelUtils.importExcel(is,
                dic.keySet().toArray(new String[dic.keySet().size()]));
        QueryWrapper<PmWorkShopEntity> qwShop = new QueryWrapper();
        List<PmWorkShopEntity> shopList = pmWorkShopService.getData(qwShop,false);

        QueryWrapper<PmLineEntity> qwLine = new QueryWrapper();
        List<PmLineEntity> lineList = pmLineService.getData(qwLine,false);

        QueryWrapper<PmWorkStationEntity> qwStation = new QueryWrapper();
        List<PmWorkStationEntity> stationList = pmWorkStationService.getData(qwStation,false);

        QueryWrapper<PmBopEntity> qwBop = new QueryWrapper();
        List<PmBopEntity> bopList = getData(qwBop,false);

        List<SysConfigurationEntity> sysConfigurationAllDatas = sysConfigurationService.getAllDatas();
        Map<String, Map<String, String>> configTextAndValueByCategoryMap = getTextAndValueGroupByCategory(sysConfigurationAllDatas);
        for (Map.Entry<String, List<Map<String, String>>> item : datas.entrySet()) {
            if (item.getValue().isEmpty()) {
                continue;
            }
            validImportDatas(item.getValue(), dic.get(item.getKey()));
            switch (item.getKey()) {
                case SheetTableName.WO:
                    for(Map<String, String> eachRow : item.getValue()){
                        setKey(eachRow,configTextAndValueByCategoryMap,"woType","woType");
                        setKey(eachRow,configTextAndValueByCategoryMap,"WoOperType","operType");
                    }
                    Map<String,List<Map<String,String>>> insertAndUpdateDataList = splitData(item.getValue());
                    List<PmWoEntity> insertWoList = pmWoService.convertExcelDataToEntity(insertAndUpdateDataList.get("insert"));
                    List<PmWoEntity> updateWoList = pmWoService.convertExcelDataToEntity(insertAndUpdateDataList.get("updata"));
                    //设置外键
                    setWoForeign(insertWoList,shopList,lineList,stationList);
                    setWoForeign(updateWoList,shopList,lineList,stationList);

                    saveWoData(insertWoList,updateWoList);
                    this.saveChange();
                    break;
                case SheetTableName.TOOL:
                    for(Map<String, String> eachRow : item.getValue()){
                        setKey(eachRow,configTextAndValueByCategoryMap,"toolType","toolType");
                        setKey(eachRow,configTextAndValueByCategoryMap,"toolBrand","brand");
                        setKey(eachRow,configTextAndValueByCategoryMap,"toolCommType","netType");
                    }
                    insertAndUpdateDataList = splitData(item.getValue());
                    List<PmToolEntity> insertToolList = pmToolService.convertExcelDataToEntity(insertAndUpdateDataList.get("insert"));
                    List<PmToolEntity> updateToolList = pmToolService.convertExcelDataToEntity(insertAndUpdateDataList.get("updata"));
                    //设置外键
                    setToolForeign(insertToolList,shopList,lineList,stationList);
                    setToolForeign(updateToolList,shopList,lineList,stationList);

                    saveToolData(insertToolList,updateToolList);
                    this.saveChange();
                    break;
                case SheetTableName.JOB:
                    //设置外键
                    insertAndUpdateDataList = splitData(item.getValue());
                    List<PmToolJobEntity> insertJobList = pmToolJobService.convertExcelDataToEntity(insertAndUpdateDataList.get("insert"));
                    List<PmToolJobEntity> updateToolJobList = pmToolJobService.convertExcelDataToEntity(insertAndUpdateDataList.get("updata"));
                    //设置外键
                    setJobForeign(insertJobList,shopList,lineList,stationList);
                    setJobForeign(updateToolJobList,shopList,lineList,stationList);
                    saveJobData(insertJobList,updateToolJobList);
                    this.saveChange();
                    break;
                case SheetTableName.BOP:
                    for(Map<String, String> eachRow : item.getValue()){
                        setKey(eachRow,configTextAndValueByCategoryMap,"woType","processType");
                    }
                    insertAndUpdateDataList = splitData(item.getValue());
                    List<PmBopEntity> insertBopList = convertExcelDataToEntity(insertAndUpdateDataList.get("insert"));
                    List<PmBopEntity> updateBopList = convertExcelDataToEntity(insertAndUpdateDataList.get("updata"));
                    //设置外键
                    setBopForeign(insertBopList,shopList,lineList,stationList);
                    setBopForeign(updateBopList,shopList,lineList,stationList);

                    saveBopData(insertBopList,updateBopList);
                    this.saveChange();
                    break;
                case SheetTableName.CONTROL_PLAN:
                    for(Map<String, String> eachRow : item.getValue()){
                        setKey(eachRow,configTextAndValueByCategoryMap,"measurement","measurementFirst");
                        setKey(eachRow,configTextAndValueByCategoryMap,"measurement","measurementSecond");
                        setKey(eachRow,configTextAndValueByCategoryMap,"sampleFrequency","sampleFrequency");
                    }
                    insertAndUpdateDataList = splitData(item.getValue());
                    List<PmPfmeaEntity> insertPfmeaList = pmPfmeaService.convertExcelDataToEntity(insertAndUpdateDataList.get("insert"));
                    List<PmPfmeaEntity> updatePfmeaList = pmPfmeaService.convertExcelDataToEntity(insertAndUpdateDataList.get("updata"));
                    //设置外键
                    setPfmeaForeign(insertPfmeaList,shopList,lineList,stationList,bopList);
                    setPfmeaForeign(updatePfmeaList,shopList,lineList,stationList,bopList);
                    savePfmeaData(insertPfmeaList,updatePfmeaList);
                    this.saveChange();
                    break;
                default:
                    break;
            }
        }
    }

    private Map<String,List<Map<String,String>>> splitData(List<Map<String, String>> datas){
        Map<String,List<Map<String,String>>> target = new HashMap<>(2);
        List<Map<String,String>> insertList = new ArrayList<>();
        List<Map<String,String>> updataList = new ArrayList<>();
        target.put("insert",insertList);
        target.put("updata",updataList);
        for(Map<String,String> eachItem : datas){
            if(StringUtils.isBlank(eachItem.get("id"))
                || "0".equals(eachItem.get("id"))){
                insertList.add(eachItem);
            }else{
                updataList.add(eachItem);
            }
        }
        return target;
    }

    private void saveWoData(List<PmWoEntity> insertList,List<PmWoEntity> updataList){
        if(!insertList.isEmpty()){
            pmWoService.insertBatch(insertList, 200, false, 1);
        }
        if(!updataList.isEmpty()){
            for(PmWoEntity dto : updataList){
                LambdaUpdateWrapper<PmWoEntity> woWrap = new LambdaUpdateWrapper();
                woWrap.set(PmWoEntity::getWoCode, dto.getWoCode())
                        .set(PmWoEntity::getDisplayNo, dto.getDisplayNo())
                        .set(PmWoEntity::getFeatureCode, dto.getFeatureCode())
                        .set(PmWoEntity::getIsEnable, dto.getIsEnable())
                        .set(PmWoEntity::getOperType, dto.getOperType())
                        .set(PmWoEntity::getQmDefectAnomalyCode, dto.getQmDefectAnomalyCode())
                        .set(PmWoEntity::getQmDefectAnomalyDescription, dto.getQmDefectAnomalyDescription())
                        .set(PmWoEntity::getRemark, dto.getRemark())
                        .set(PmWoEntity::getTrcByGroup, dto.getTrcByGroup())
                        .set(PmWoEntity::getWoDescription, dto.getWoDescription())
                        .set(PmWoEntity::getWoGroupName, dto.getWoGroupName())
                        .set(PmWoEntity::getWoType, dto.getWoType())
                        .set(PmWoEntity::getQmDefectComponentId, dto.getQmDefectComponentId())
                        .set(PmWoEntity::getIsDelete, dto.getIsDelete())
                        .eq(PmWoEntity::getId, dto.getId()).eq(PmWoEntity::getVersion, 0);
                pmWoService.update(woWrap,false);
            }
        }
    }






    private void saveJobData(List<PmToolJobEntity> insertList,List<PmToolJobEntity> updateList){
        if(!insertList.isEmpty()){
            pmToolJobService.insertBatch(insertList,200, false, 1);
        }
        if(!updateList.isEmpty()){
            for(PmToolJobEntity pmToolJobEntity : updateList){
                LambdaUpdateWrapper<PmToolJobEntity> luw = new LambdaUpdateWrapper<>();
                luw.set(PmToolJobEntity::getPrcPmToolId, pmToolJobEntity.getPrcPmToolId());
                luw.set(PmToolJobEntity::getJobNo, pmToolJobEntity.getJobNo());
                luw.set(PmToolJobEntity::getPmWoId, pmToolJobEntity.getPmWoId());
                luw.set(PmToolJobEntity::getFeatureCode, pmToolJobEntity.getFeatureCode());
                luw.set(PmToolJobEntity::getAttribute3, pmToolJobEntity.getAttribute3());
                luw.set(PmToolJobEntity::getIsDelete, pmToolJobEntity.getIsDelete());
                luw.eq(PmToolJobEntity::getId, pmToolJobEntity.getId());
                luw.eq(PmToolJobEntity::getVersion, 0);
                pmToolJobService.update(luw,false);
            }
        }
    }

    private void saveToolData(List<PmToolEntity> insertList,List<PmToolEntity> updateList){
        if(!insertList.isEmpty()){
            pmToolService.insertBatch(insertList,200, false, 1);
        }
        if(!updateList.isEmpty()){
            for(PmToolEntity pmToolEntity : updateList){
                LambdaUpdateWrapper<PmToolEntity> luw = new LambdaUpdateWrapper<>();
                luw.set(PmToolEntity::getToolCode, pmToolEntity.getToolCode());
                luw.set(PmToolEntity::getRemark, pmToolEntity.getRemark());
                luw.set(PmToolEntity::getToolType, pmToolEntity.getToolType());
                luw.set(PmToolEntity::getDisplayNo, pmToolEntity.getDisplayNo());
                luw.set(PmToolEntity::getBrand, pmToolEntity.getBrand());
                luw.set(PmToolEntity::getIp, pmToolEntity.getIp());
                luw.set(PmToolEntity::getNetType, pmToolEntity.getNetType());
                luw.set(PmToolEntity::getPort, pmToolEntity.getPort());
                luw.set(PmToolEntity::getToolName, pmToolEntity.getToolName());
                luw.set(PmToolEntity::getIsDelete, pmToolEntity.getIsDelete());
                luw.eq(PmToolEntity::getId, pmToolEntity.getId());
                luw.eq(PmToolEntity::getVersion, 0);
                pmToolService.update(luw,false);
            }
        }
    }

    private void saveBopData(List<PmBopEntity> insertList,List<PmBopEntity> updateList){
        if(!insertList.isEmpty()){
            insertBatch(insertList,200, false, 1);
        }
        if(!updateList.isEmpty()){
            updateBatchById(updateList,200, false);
        }
    }

    private void savePfmeaData(List<PmPfmeaEntity> insertList,List<PmPfmeaEntity> updateList){
        if(!insertList.isEmpty()){
            pmPfmeaService.insertBatch(insertList,200, false, 1);
        }
        if(!updateList.isEmpty()){
            pmPfmeaService.updateBatchById(updateList,200, false);
        }
    }

    private Map<String, Map<String, String>> getTextAndValueGroupByCategory(List<SysConfigurationEntity> configurationList) {
        if (configurationList.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Map<String, String>> targetMap = new HashMap<>();
        for (SysConfigurationEntity sysConfigurationEntity : configurationList) {
            targetMap.computeIfAbsent(sysConfigurationEntity.getCategory(), v -> new HashMap<>()).put(sysConfigurationEntity.getValue(), sysConfigurationEntity.getText());
        }
        return targetMap;
    }

    private void setValue(Map<String, Object> eachRow,Map<String,
            Map<String, String>> configTextAndValueByCategoryMap,
                          String type,
                          String fieldName){
        String value = "";
        Map<String, String> mapping = configTextAndValueByCategoryMap.get(type);
        if (mapping != null && eachRow.get(fieldName) != null) {
            value = mapping.get(String.valueOf(eachRow.get(fieldName)));
        }
        eachRow.put(fieldName, StringUtils.isNotBlank(value) ? value : "");
    }

    private void setKey(Map<String, String> eachRow,Map<String,
            Map<String, String>> configTextAndValueByCategoryMap,
                          String type,
                          String fieldName){
        String value = "";
        Map<String, String> mapping = configTextAndValueByCategoryMap.get(type);
        if (mapping != null && eachRow.get(fieldName) != null) {
            for(Map.Entry<String,String> item : mapping.entrySet()){
                if(item.getValue().equals(eachRow.get(fieldName))){
                    value = item.getKey();
                }
            }
        }
        eachRow.put(fieldName, StringUtils.isNotBlank(value) ? value : "");

    }

    @Override
    public void export(String shopCode, HttpServletResponse response) throws IOException {
        QueryWrapper<PmWorkShopEntity> qwShop = new QueryWrapper();
        LambdaQueryWrapper<PmWorkShopEntity> lqwShop = qwShop.lambda();
        lqwShop.eq(PmWorkShopEntity :: getWorkshopCode,shopCode);
        PmWorkShopEntity shop = pmWorkShopService.getData(qwShop,false).stream().findFirst().orElse(null);

        QueryWrapper<PmLineEntity> qwLine = new QueryWrapper();
        LambdaQueryWrapper<PmLineEntity> lqwLine = qwLine.lambda();
        lqwLine.eq(PmLineEntity :: getPrcPmWorkshopId,shop.getId());
        List<PmLineEntity> lineList = pmLineService.getData(qwLine,false);

        QueryWrapper<PmWorkStationEntity> qwStation = new QueryWrapper();
        LambdaQueryWrapper<PmWorkStationEntity> lqwStation = qwStation.lambda();
        lqwStation.eq(PmWorkStationEntity :: getPrcPmWorkshopId,shop.getId());
        List<PmWorkStationEntity> stationList = pmWorkStationService.getData(qwStation,false);

        QueryWrapper<PmWoEntity> qwWo = new QueryWrapper();
        LambdaQueryWrapper<PmWoEntity> lqwWo = qwWo.lambda();
        lqwWo.eq(PmWoEntity :: getPrcPmWorkshopId,shop.getId());
        lqwWo.orderByAsc(PmWoEntity :: getPrcPmLineId);
        lqwWo.orderByAsc(PmWoEntity :: getPrcPmWorkstationId);
        List<PmWoEntity> woList = pmWoService.getData(qwWo,false);

        QueryWrapper<PmToolEntity> qwTool = new QueryWrapper();
        LambdaQueryWrapper<PmToolEntity> lqwTool = qwTool.lambda();
        lqwTool.eq(PmToolEntity :: getPrcPmWorkshopId,shop.getId());
        lqwTool.orderByAsc(PmToolEntity :: getPrcPmLineId);
        lqwTool.orderByAsc(PmToolEntity :: getPrcPmWorkstationId);
        List<PmToolEntity> toolList = pmToolService.getData(qwTool,false);

        QueryWrapper<PmToolJobEntity> qwToolJob = new QueryWrapper();
        LambdaQueryWrapper<PmToolJobEntity> lqwToolJob = qwToolJob.lambda();
        lqwToolJob.eq(PmToolJobEntity :: getPrcPmWorkshopId,shop.getId());
        lqwToolJob.orderByAsc(PmToolJobEntity :: getPrcPmLineId);
        lqwToolJob.orderByAsc(PmToolJobEntity :: getPmWoId);
        List<PmToolJobEntity> jobList = pmToolJobService.getData(qwToolJob,false);

        QueryWrapper<PmBopEntity> qwBop = new QueryWrapper();
        LambdaQueryWrapper<PmBopEntity> lqwBop = qwBop.lambda();
        lqwBop.eq(PmBopEntity :: getPrcPmWorkshopId,shop.getId());
        lqwBop.orderByAsc(PmBopEntity :: getLineCode);
        lqwBop.orderByAsc(PmBopEntity :: getWorkstationCode);
        lqwBop.orderByAsc(PmBopEntity :: getProcessStep);
        List<PmBopEntity> bopList = getData(qwBop,false);

        QueryWrapper<PmPfmeaEntity> qwPlan = new QueryWrapper();
        LambdaQueryWrapper<PmPfmeaEntity> lqwPlan = qwPlan.lambda();
        lqwPlan.eq(PmPfmeaEntity :: getPrcPmWorkshopId,shop.getId());
        lqwPlan.orderByAsc(PmPfmeaEntity :: getLineCode);
        lqwPlan.orderByAsc(PmPfmeaEntity :: getWorkstationCode);
        lqwPlan.orderByAsc(PmPfmeaEntity :: getFrockCode);
        List<PmPfmeaEntity> controlList = pmPfmeaService.getData(qwPlan,false);
        List<SysConfigurationEntity> sysConfigurationAllDatas = sysConfigurationService.getAllDatas();

        Map<String, Map<String, String>> configTextAndValueByCategoryMap = getTextAndValueGroupByCategory(sysConfigurationAllDatas);
        List<List<Map<String, Object>>> mapLists = new ArrayList<>();
        Map<String, Map<String, String>> pmDic = dic;
        for (Map.Entry<String, Map<String, String>> item : pmDic.entrySet()) {
            switch (item.getKey()) {
                case SheetTableName.WO: {
                    List<PmTraceComponentEntity> componentList = pmTraceComponentService.getAllDatas();
                    mapLists.add(generateWoDataForExcel(woList, shop, lineList, stationList, componentList, configTextAndValueByCategoryMap));
                    break;
                }
                case SheetTableName.TOOL: {
                    mapLists.add(generateToolDataForExcel(toolList, shop, lineList, stationList, configTextAndValueByCategoryMap));
                    break;
                }
                case SheetTableName.JOB: {
                    mapLists.add(generateToolJobDataForExcel(jobList, shop, lineList,stationList,toolList,woList));
                    break;
                }
                case SheetTableName.BOP: {
                    mapLists.add(generateBopDataForExcel(bopList,configTextAndValueByCategoryMap));
                    break;
                }
                case SheetTableName.CONTROL_PLAN: {
                    mapLists.add(generateControlPlanDataForExcel(controlList,configTextAndValueByCategoryMap));
                    break;
                }
                default:break;
            }
        }
        List<String> sheetNames = new ArrayList<>(pmDic.keySet());
        List<Map<String, String>> fieldParam = new ArrayList<>(pmDic.values());
        InkelinkExcelUtils.exportSheets(sheetNames, fieldParam, mapLists, shop.getWorkshopName() + "_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_C), response);
    }

    private List<Map<String, Object>> generateWoDataForExcel(List<PmWoEntity> aviList, PmWorkShopEntity shop, List<PmLineEntity> lineList, List<PmWorkStationEntity> stationList,
                                                             List<PmTraceComponentEntity> componentList,
                                                             Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(aviList);
        String operTypeField = "operType";
        String woField = "woType";
        String lineIdField = "prcPmLineId";
        String stationField = "prcPmWorkstationId";
        String componentIdField = "qmDefectComponentId";
        for (Map<String, Object> eachRow : excelDatas) {
            setValue(eachRow,configTextAndValueByCategoryMap,woField,woField);
            setValue(eachRow,configTextAndValueByCategoryMap,"WoOperType",operTypeField);
            eachRow.put("workshopCode", shop.getWorkshopCode());
            String lineName = "";
            if (eachRow.get(lineIdField) != null) {
                lineName = lineList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(lineIdField)))).map(PmLineEntity::getLineCode).findFirst().orElse("");
            }
            eachRow.put("lineCode", lineName != null ? lineName : "");
            String stationName = "";
            if (eachRow.get(stationField) != null) {
                stationName = stationList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(stationField)))).map(PmWorkStationEntity::getWorkstationCode).findFirst().orElse("");
            }
            eachRow.put("workstationCode", stationName != null ? stationName : "");
            String componentName = "";
            if (eachRow.get(componentIdField) != null) {
                componentName = componentList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(componentIdField)))).map(PmTraceComponentEntity::getTraceComponentCode).findFirst().orElse("");
            }
            eachRow.put("traceComponentCode", componentName != null ? componentName : "");


        }
        return excelDatas;
    }

    private List<Map<String, Object>> generateToolDataForExcel(List<PmToolEntity> aviList, PmWorkShopEntity shop, List<PmLineEntity> lineList, List<PmWorkStationEntity> stationList,
                                                               Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(aviList);
        String netTypeField = "netType";
        String brandField = "brand";
        String toolTypeField = "toolType";
        String lineIdField = "prcPmLineId";
        String stationField = "prcPmWorkstationId";
        for (Map<String, Object> eachRow : excelDatas) {
            setValue(eachRow,configTextAndValueByCategoryMap,"toolType",toolTypeField);
            setValue(eachRow,configTextAndValueByCategoryMap,"toolBrand",brandField);
            setValue(eachRow,configTextAndValueByCategoryMap,"toolCommType",netTypeField);
            eachRow.put("workshopCode", shop.getWorkshopCode());
            String lineName = "";
            if (eachRow.get(lineIdField) != null) {
                lineName = lineList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(lineIdField)))).map(PmLineEntity::getLineCode).findFirst().orElse("");
            }
            eachRow.put("lineCode", lineName != null ? lineName : "");
            String stationName = "";
            if (eachRow.get(stationField) != null) {
                stationName = stationList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(stationField)))).map(PmWorkStationEntity::getWorkstationCode).findFirst().orElse("");
            }
            eachRow.put("workstationCode", stationName != null ? stationName : "");

        }
        return excelDatas;
    }

    private List<Map<String, Object>> generateToolJobDataForExcel(List<PmToolJobEntity> materialList, PmWorkShopEntity shop, List<PmLineEntity> lineList,  List<PmWorkStationEntity> stationList, List<PmToolEntity> toolList,List<PmWoEntity> woList) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(materialList);
        String lineIdField = "prcPmLineId";
        String toolIdField = "prcPmToolId";
        String woIdField = "pmWoId";
        for (Map<String, Object> eachRow : excelDatas) {
            eachRow.put("workshopCode", shop.getWorkshopCode());
            String lineCode = "";
            if (eachRow.get(lineIdField) != null) {
                lineCode = lineList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(lineIdField)))).map(PmLineEntity::getLineCode).findFirst().orElse("");
            }
            eachRow.put("lineCode", lineCode != null ? lineCode : "");
            String toolCode = "";
            if (eachRow.get(toolIdField) != null) {
                toolCode = toolList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(toolIdField)))).map(PmToolEntity::getToolCode).findFirst().orElse("");
            }
            eachRow.put("toolCode", toolCode != null ? toolCode : "");
            String woCode = "";
            Long prcWorkstationId = null;
            if (eachRow.get(woIdField) != null) {
                PmWoEntity wo =  woList.stream().filter(c -> String.valueOf(c.getId()).equals(String.valueOf(eachRow.get(woIdField)))).findFirst().orElse(null);
                if(wo != null){
                    woCode = wo.getWoCode();
                    prcWorkstationId = wo.getPrcPmWorkstationId();
                }
            }
            eachRow.put("woCode", woCode != null ? woCode : "");
            String stationName = "";
            if(prcWorkstationId != null){
                final Long finalWorkstationId = prcWorkstationId;
                stationName = stationList.stream().filter(c -> c.getId().equals(finalWorkstationId)).map(PmWorkStationEntity::getWorkstationCode).findFirst().orElse("");
            }
            eachRow.put("workstationCode", stationName != null ? stationName : "");
        }
        return excelDatas;
    }

    private List<Map<String, Object>> generateBopDataForExcel(List<PmBopEntity> bopList,Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(bopList);

        for (Map<String, Object> eachRow : excelDatas) {
            setValue(eachRow,configTextAndValueByCategoryMap,"woType","processType");
        }
        return excelDatas;
    }

    private List<Map<String, Object>> generateControlPlanDataForExcel(List<PmPfmeaEntity> bopList,Map<String, Map<String, String>> configTextAndValueByCategoryMap) {
        List<Map<String, Object>> excelDatas = InkelinkExcelUtils.getListMap(bopList);

        for (Map<String, Object> eachRow : excelDatas) {
            setValue(eachRow,configTextAndValueByCategoryMap,"measurement","measurementFirst");
            setValue(eachRow,configTextAndValueByCategoryMap,"measurement","measurementSecond");
            setValue(eachRow,configTextAndValueByCategoryMap,"sampleFrequency","sampleFrequency");
            formatDate(eachRow,"targetCompletionTime");
            formatDate(eachRow,"completionTime");
        }
        return excelDatas;
    }

    private void formatDate(Map<String, Object> eachRow,String fieldName){
        Object val = eachRow.get(fieldName);
        if(val != null && val.getClass().equals(Date.class)){
            eachRow.put(fieldName,DateUtils.format((Date) val,DateUtils.DATE_TIME_PATTERN));
        }
    }


}