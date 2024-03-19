package com.ca.mfd.prc.pm.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.comm.BusinessValidUtils;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.pm.entity.PmTraceComponentEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.mapper.IPmToolJobMapper;
import com.ca.mfd.prc.pm.mapper.IPmWoMapper;
import com.ca.mfd.prc.pm.mapper.IPmWorkStationMapper;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pm.service.IPmTraceComponentService;
import com.ca.mfd.prc.pm.service.IPmWoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
 * @Description: 岗位操作
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmWoServiceImpl extends AbstractPmCrudServiceImpl<IPmWoMapper, PmWoEntity> implements IPmWoService {

    private static final Map<String, String> BOOLEAN_FIELDS_MAPPING = new HashMap<>(2);

    private static final String TRACE_COMPONENT_CODE = "traceComponentCode";
    private static final String cacheName = "PRC_PM_WO";

    private static final Object lockObj = new Object();

    static {
        BOOLEAN_FIELDS_MAPPING.put("deleteFlag", "isDelete");
        BOOLEAN_FIELDS_MAPPING.put("enableFlag", "isEnable");
    }

    @Autowired
    private IPmWoMapper woMapper;
    @Autowired
    private IPmToolJobMapper toolJobMapper;
    @Autowired
    private IPmWoMapper pmWoDao;
    @Autowired
    private IPmWorkStationMapper pmWorkStationMapper;
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IPmTraceComponentService pmTraceComponentService;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmWoEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmWoEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmWoEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmWoEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeUpdate(PmWoEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(PmWoEntity model) {
        validData(model);
    }

    private void validData(PmWoEntity model) {
        ClassUtil.validNullByNullAnnotation(model);
        validDataUnique(model.getId(), "WO_CODE", model.getWoCode(), "已经存在编码为%s的工艺数据,同工位工艺编码须唯一","PRC_PM_WORKSTATION_ID",String.valueOf(model.getPrcPmWorkstationId()));
        setTraceComponentId(model);
        if (StringUtils.isBlank(model.getFeatureCode())) {
            throw new InkelinkException(String.format("线体[%s]>工位[%s]>工艺[%s]特征值不能为空", model.getLineCode(),
                    model.getWorkstationCode(), model.getWoCode(), model.getFeatureCode()));
        }
        if (Boolean.FALSE.equals(FeatureTool.verifyExpression(model.getFeatureCode()))) {
            throw new InkelinkException(String.format("线体[%s]>工位[%s]>工艺[%s]特征值[%s]无效", model.getLineCode(),
                    model.getWorkstationCode(), model.getWoCode(), model.getFeatureCode()));
        }
        if (model.getPrcPmWorkshopId() == null || model.getPrcPmWorkshopId() == 0
                || model.getPrcPmLineId() == null || model.getPrcPmLineId() == 0) {
            validWorkStation(model);
        }

    }

    private void validWorkStation(PmWoEntity entity) {
        //工位信息
        QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkStationEntity> lwq = qw.lambda();
        String msg = "编码";
        String res = entity.getWorkstationCode();
        if (entity.getPrcPmWorkstationId() != null && entity.getPrcPmWorkstationId() > 0) {
            lwq.eq(PmWorkStationEntity::getId, entity.getPrcPmWorkstationId());
            msg = "ID";
            res = String.valueOf(entity.getPrcPmWorkstationId());
        } else {
            lwq.eq(PmWorkStationEntity::getWorkstationCode, entity.getWorkstationCode());
        }
        lwq.eq(PmWorkStationEntity::getVersion, 0);
        PmWorkStationEntity pmWorkStationEntity = this.pmWorkStationMapper.selectList(qw).stream().findFirst().orElse(null);
        if (pmWorkStationEntity == null) {
            throw new InkelinkException(String.format("工艺[%s]对应的工位%s[%s]不存在", entity.getWoCode(), msg, res));
        }
        entity.setPrcPmLineId(pmWorkStationEntity.getPrcPmLineId());
        entity.setPrcPmWorkshopId(pmWorkStationEntity.getPrcPmWorkshopId());
    }


    private void setTraceComponentId(PmWoEntity model) {
        if (model.getWoType() == 2
                && (model.getQmDefectComponentId() == 0
                || model.getQmDefectComponentId() == null)
                && StringUtils.isNotBlank(model.getTraceComponentCode())) {
            PmTraceComponentEntity pmTraceComponentEntity = this.pmTraceComponentService.getAllDatas().stream()
                    .filter(v -> Objects.equals(model.getTraceComponentCode(), v.getTraceComponentCode()))
                    .findFirst().orElse(null);
            if (pmTraceComponentEntity == null) {
                throw new InkelinkException(String.format("线体[%s]>工位[%s]>工艺[%s]组件编码[%s]无效", model.getLineCode(),
                        model.getWorkstationCode(), model.getWoCode(), model.getTraceComponentCode()));
            }
            model.setQmDefectComponentId(pmTraceComponentEntity.getId());
        } else if (model.getWoType() == 2
                && (model.getQmDefectComponentId() == 0
                || model.getQmDefectComponentId() == null)
                && StringUtils.isBlank(model.getTraceComponentCode())) {
            throw new InkelinkException(String.format("线体[%s]>工位[%s]>工艺[%s]组件编码[%s]不能为空", model.getLineCode(),
                    model.getWorkstationCode(), model.getWoCode(), model.getTraceComponentCode()));
        } else if (model.getWoType() != 2) {
            model.setQmDefectComponentId(0L);
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
    public List<PmWoEntity> getAllDatas() {
        List<PmWoEntity> datas = localCache.getObject(cacheName);
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
    public ResultVO<List<ComboInfoDTO>> getWorkStationWo(Long stationId) {
        //获取工位下的所有操作
        LambdaQueryWrapper<PmWoEntity> wo = new LambdaQueryWrapper<>();
        wo.eq(PmWoEntity::getPrcPmWorkstationId, stationId);
        List<PmWoEntity> wos = woMapper.selectList(wo);
        List<ComboInfoDTO> list = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(wos)) {
            List<Long> ids = wos.stream().map(PmWoEntity::getId).collect(Collectors.toList());
            LambdaQueryWrapper<PmToolJobEntity> to = new LambdaQueryWrapper<>();
            to.in(PmToolJobEntity::getPmWoId, ids);
            List<PmToolJobEntity> tos = toolJobMapper.selectList(to);
            if (ObjectUtil.isNotEmpty(tos)) {
                List<PmWoEntity> woList = wos.stream().filter(o -> !tos.contains(o.getId())).collect(Collectors.toList());
                list = woList.stream().map(t -> {
                    ComboInfoDTO dto = new ComboInfoDTO();
                    dto.setValue(String.valueOf(t.getId()));
                    dto.setText(t.getWoGroupName());
                    return dto;
                }).collect(Collectors.toList());
            }
        }
        return new ResultVO<List<ComboInfoDTO>>().ok(list);
    }

    @Override
    public List<PmWoEntity> getListByParentId(Long parentId) {
        QueryWrapper<PmWoEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWoEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWoEntity::getPrcPmWorkstationId, parentId);
        lambdaQueryWrapper.eq(PmWoEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmWoEntity::getVersion, 0);
        lambdaQueryWrapper.orderByAsc(PmWoEntity::getDisplayNo);
        return this.pmWoDao.selectList(queryWrapper);
    }

    @Override
    public List<PmWoEntity> getByShopId(Long shopId) {
        return pmWoDao.selectList(Wrappers.lambdaQuery(PmWoEntity.class)
                .eq(PmWoEntity::getPrcPmWorkshopId, shopId)
                .eq(PmWoEntity::getVersion, 0)
                .eq(PmWoEntity::getIsDelete, false)
                .orderByAsc(PmWoEntity::getDisplayNo));
    }

    @Override
    public List<PmWoEntity> getPmWoEntityByVersion(Long shopId, int version, Boolean flags) {
        QueryWrapper<PmWoEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWoEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWoEntity::getPrcPmWorkshopId, shopId);
        lambdaQueryWrapper.eq(PmWoEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmWoEntity::getIsEnable, true);
        lambdaQueryWrapper.eq(PmWoEntity::getIsDelete, flags);
        return pmWoDao.selectList(queryWrapper);
    }

    @Override
    public List<PmWoEntity> getPmWoEntityByWoCode(Long stationId, String woCode, Boolean flags) {
        QueryWrapper<PmWoEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWoEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWoEntity::getPrcPmWorkstationId, stationId);
        lambdaQueryWrapper.eq(PmWoEntity::getWoCode, woCode);
        lambdaQueryWrapper.eq(PmWoEntity::getIsDelete, flags);
        return pmWoDao.selectList(queryWrapper);
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> pmWOMap = new HashMap<>(12);
        pmWOMap.put("workshopCode", "车间代码");
        pmWOMap.put("lineCode", "线体代码");
        pmWOMap.put("workstationCode", "工位代码");
        pmWOMap.put("displayNo", "顺序号");
        pmWOMap.put("woCode", "编码");
        pmWOMap.put("woType", "类型");
        pmWOMap.put("woGroupName", "操作组");
        pmWOMap.put("operType", "操作类型");

        pmWOMap.put("traceComponentCode", "组件");
        pmWOMap.put("qmDefectAnomalyCode", "缺陷代码");
        pmWOMap.put("qmDefectAnomalyDescription", "缺陷名称");
        pmWOMap.put("trcByGroup", "批量追溯");
        pmWOMap.put("featureCode", "特征");
        pmWOMap.put("woDescription", "描述");
        pmWOMap.put("remark", "备注");
        pmWOMap.put("deleteFlag", "是否删除");
        pmWOMap.put("enableFlag", "是否启用");
        return pmWOMap;
    }

    @Override
    public PmWoEntity get(Serializable id) {
        QueryWrapper<PmWoEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWoEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWoEntity::getId, id);
        lambdaQueryWrapper.eq(PmWoEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmWoEntity::getVersion, 0);
        List<PmWoEntity> pmWoEntityList = pmWoDao.selectList(queryWrapper);
        return pmWoEntityList.stream().findFirst().orElse(null);
    }

    @Override
    public void update(PmWoEntity dto) {
        beforeUpdate(dto);
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
                .set(PmWoEntity::getPqsCoderuleId, dto.getPqsCoderuleId())
                .set(PmWoEntity::getQmDefectAnomalyId, dto.getQmDefectAnomalyId())
                .set(PmWoEntity::getQmDefectComponentId, dto.getQmDefectComponentId())
                .eq(PmWoEntity::getId, dto.getId()).eq(PmWoEntity::getVersion, dto.getVersion());
        super.update(woWrap);
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData,
                            Map<String, Long> mapOfComponentByNo,
                            List<String> vehicleMasterFeatures,
                            List<SysConfigurationEntity> vehicleModles) throws Exception {
        List<PmWoEntity> listOfPmWo = this.convertExcelDataToEntity(listFromExcelSheet,
                mapSysConfigByCategory, sheetName);
        //设置外键
        setForeignKey(listOfPmWo, currentUnDeployData);
        //验证并保存
        verifyAndSaveEntity(listOfPmWo, currentUnDeployData, mapOfComponentByNo, vehicleMasterFeatures, vehicleModles);
    }

    @Override
    public ResultVO<List<ComboInfoDTO>> getWoComboInfo(Long workplaceId) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        QueryWrapper<PmWoEntity> woQuery = new QueryWrapper<>();
        woQuery.lambda().eq(PmWoEntity::getVersion, 0).eq(PmWoEntity::getIsDelete, 0).eq(Objects.nonNull(workplaceId),PmWoEntity::getPrcPmWorkstationId, workplaceId);
        List<PmWoEntity> pmWoStream = woMapper.selectList(woQuery);
        if(CollectionUtils.isNotEmpty(pmWoStream)){
            List<ComboInfoDTO> list = new ArrayList<>();
            pmWoStream.forEach(t -> {
                ComboInfoDTO comboInfoDTO = new ComboInfoDTO();
                comboInfoDTO.setValue(String.valueOf(t.getId()));
                comboInfoDTO.setText(t.getWoDescription());
                list.add(comboInfoDTO);
            });
            result.ok(list);
            return result;
        }
        return new ResultVO<List<ComboInfoDTO>>().ok(new ArrayList<>());
    }

    @Override
    protected boolean canBeNullOrEmpty(Map.Entry<String, String> eachColumnData) {
        //班长配置非必填配置可以为空
        return !TRACE_COMPONENT_CODE.equalsIgnoreCase(eachColumnData.getKey())
                || !StringUtils.isBlank(eachColumnData.getValue());
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

    private void setForeignKey(List<PmWoEntity> listOfPmWo, PmAllDTO pmAllDTO) {
        if (listOfPmWo.isEmpty()) {
            return;
        }
        for (PmWoEntity pmWoEntity : listOfPmWo) {
            setForeignKey(pmWoEntity, pmAllDTO);
        }
    }

    private void setForeignKey(PmWoEntity pmWoEntity, PmAllDTO pmAllDTO) {
        List<PmWorkShopEntity> shops = pmAllDTO.getShops();
        //设置车间id
        PmWorkShopEntity shop = shops.stream().filter(item -> Objects.equals(pmWoEntity.getWorkshopCode(), item.getWorkshopCode()))
                .findFirst().orElse(null);
        if (shop == null) {
            throw new InkelinkException("Wo编码[" + pmWoEntity.getWoCode() + "]对应的车间编码(内部编码)[" + pmWoEntity.getWorkshopCode() + "]没有对应任何车间，请检查是否有配置对应编码车间");
        }
        pmWoEntity.setPrcPmWorkshopId(shop.getId());
        List<PmLineEntity> lines = pmAllDTO.getLines();
        //设置线体id
        PmLineEntity line = lines.stream().filter(item -> Objects.equals(pmWoEntity.getLineCode(), item.getLineCode())
                        && Objects.equals(pmWoEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId()))
                .findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("Wo编码[" + pmWoEntity.getWoCode() + "]对应的线体编码[" + pmWoEntity.getLineCode() + "]没有对应任何线体，请检查是否有配置对应编码线体");
        }
        pmWoEntity.setPrcPmLineId(line.getId());
        List<PmWorkStationEntity> stations = pmAllDTO.getStations();
        //设置工位id
        PmWorkStationEntity workStation = stations.stream().filter(item -> Objects.equals(pmWoEntity.getWorkstationCode(), item.getWorkstationCode())
                        && Objects.equals(pmWoEntity.getPrcPmWorkshopId(), item.getPrcPmWorkshopId())
                        && Objects.equals(pmWoEntity.getPrcPmLineId(), item.getPrcPmLineId()))
                .findFirst().orElse(null);
        if (workStation == null) {
            throw new InkelinkException("Wo编码[" + pmWoEntity.getWoCode() + "]对应的线体编码[" + pmWoEntity.getLineCode() + "]对应的工位编码[" + pmWoEntity.getWorkstationCode() + "]没有对应任何工位，请检查是否有配置对应编码工位");
        }
        pmWoEntity.setPrcPmWorkstationId(workStation.getId());
    }

    private void verifyAndSaveEntity(List<PmWoEntity> listEntity,
                                     PmAllDTO currentUnDeployData,
                                     Map<String, Long> mapOfComponentByNo,
                                     List<String> vehicleMasterFeatures,
                                     List<SysConfigurationEntity> vehicleModles) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        Map<Long, Set<String>> mapOfWoNameByStationId = new HashMap(16);
        Map<Long, Set<Integer>> mapOfWoDisplayNoByStationId = new HashMap(16);
        for (PmWoEntity wo : listEntity) {
            //验证顺序
            verifyWoDisplayNo(wo, mapOfWoDisplayNoByStationId);
            //验证名称
            verifyWoCode(wo, mapOfWoNameByStationId);
            if (mapOfComponentByNo.containsKey(wo.getTraceComponentCode())) {
                wo.setQmDefectComponentId(mapOfComponentByNo.get(wo.getTraceComponentCode()));
            }
            if (!BusinessValidUtils.checkVehicleOption(wo.getFeatureCode(), vehicleMasterFeatures, vehicleModles)) {
                throw new InkelinkException("车间[" + wo.getWorkshopCode() + "]>线体[" + wo.getLineCode() + "]>工位[" + wo.getWorkstationCode() + "]>wo编码[" + wo.getWoCode() + "]特征值[" + wo.getFeatureCode() + "]错误");
            }

            PmWoEntity existWo = currentUnDeployData.getWos().stream().filter(
                            item -> Objects.equals(item.getPrcPmWorkshopId(), wo.getPrcPmWorkshopId())
                                    && Objects.equals(item.getPrcPmLineId(), wo.getPrcPmLineId())
                                    && Objects.equals(item.getPrcPmWorkstationId(), wo.getPrcPmWorkstationId())
                                    && Objects.equals(item.getWoCode(), wo.getWoCode()))
                    .findFirst().orElse(null);
            if (existWo != null) {
                PmWoEntity existSameCodeWo = currentUnDeployData.getWos().stream().filter(
                                item -> Objects.equals(item.getPrcPmWorkshopId(), wo.getPrcPmWorkshopId())
                                        && Objects.equals(item.getPrcPmLineId(), wo.getPrcPmLineId())
                                        && Objects.equals(item.getPrcPmWorkstationId(), wo.getPrcPmWorkstationId())
                                        && Objects.equals(item.getWoCode(), wo.getWoCode())
                                        && !Objects.equals(item.getId(), existWo.getId()))
                        .findFirst().orElse(null);
                if (existSameCodeWo != null) {
                    throw new InkelinkException("车间[" + wo.getWorkshopCode() + "]>线体[" + wo.getLineCode() + "]>工位[" + wo.getWorkstationCode() + "]>wo编码[" + wo.getWoCode() + "]已经存在");
                }
                PmWoEntity existSameDisplayNoWo = currentUnDeployData.getWos().stream().filter(
                                item -> Objects.equals(item.getPrcPmWorkshopId(), wo.getPrcPmWorkshopId())
                                        && Objects.equals(item.getPrcPmLineId(), wo.getPrcPmLineId())
                                        && Objects.equals(item.getPrcPmWorkstationId(), wo.getPrcPmWorkstationId())
                                        && Objects.equals(item.getDisplayNo(), wo.getDisplayNo())
                                        && !Objects.equals(item.getId(), existWo.getId()))
                        .findFirst().orElse(null);
                if (existSameDisplayNoWo != null) {
                    throw new InkelinkException("车间[" + wo.getWorkshopCode() + "]>线体[" + wo.getLineCode() + "]>工位[" + wo.getWorkstationCode() + "]>wo顺序号[" + wo.getDisplayNo() + "]已经存在");
                }
                LambdaUpdateWrapper<PmWoEntity> luw = new LambdaUpdateWrapper();

                luw.set(PmWoEntity::getDisplayNo, wo.getDisplayNo());
                luw.set(PmWoEntity::getWoCode, wo.getWoCode());
                luw.set(PmWoEntity::getWoType, wo.getWoType());
                luw.set(PmWoEntity::getWoGroupName, wo.getWoGroupName());
                luw.set(PmWoEntity::getQmDefectComponentId, wo.getQmDefectComponentId());
                luw.set(PmWoEntity::getTrcByGroup, wo.getTrcByGroup());
                luw.set(PmWoEntity::getFeatureCode, wo.getFeatureCode());
                luw.set(PmWoEntity::getWoDescription, wo.getWoDescription());
                luw.set(PmWoEntity::getRemark, wo.getRemark());
                luw.set(PmWoEntity::getIsEnable, wo.getIsEnable());
                luw.set(PmWoEntity::getIsDelete, wo.getIsDelete());

                luw.eq(PmWoEntity::getId, existWo.getId());
                luw.eq(PmWoEntity::getVersion, 0);
                this.update(luw);
            } else if (!wo.getIsDelete()) {
                PmWoEntity existSameDisplayNoWo = currentUnDeployData.getWos().stream().filter(
                                item -> Objects.equals(item.getPrcPmWorkshopId(), wo.getPrcPmWorkshopId())
                                        && Objects.equals(item.getPrcPmLineId(), wo.getPrcPmLineId())
                                        && Objects.equals(item.getPrcPmWorkstationId(), wo.getPrcPmWorkstationId())
                                        && Objects.equals(item.getDisplayNo(), wo.getDisplayNo()))
                        .findFirst().orElse(null);
                if (existSameDisplayNoWo != null) {
                    throw new InkelinkException("车间[" + wo.getWorkshopCode() + "]>线体[" + wo.getLineCode() + "]>工位[" + wo.getWorkstationCode() + "]>wo顺序号[" + wo.getDisplayNo() + "]已经存在");
                }
                wo.setVersion(0);
                this.insert(wo);
            }
        }

    }

    private void verifyWoDisplayNo(PmWoEntity wo, Map<Long, Set<Integer>> mapOfWoDisplayNoByStationId) {
        Set<Integer> setOfWoDisplayNo = mapOfWoDisplayNoByStationId.computeIfAbsent(wo.getPrcPmWorkstationId(), v -> new HashSet<>());
        if (setOfWoDisplayNo.contains(wo.getDisplayNo())) {
            throw new InkelinkException("车间[" + wo.getWorkshopCode() + "]>线体[" + wo.getLineCode() + "]>工位[" + wo.getWorkstationCode() + "]>wo顺序号[" + wo.getDisplayNo() + "]重复");
        }
        setOfWoDisplayNo.add(wo.getDisplayNo());
    }

    private void verifyWoCode(PmWoEntity wo, Map<Long, Set<String>> mapOfWoNameByStationId) {
        Set<String> setOfWoName = mapOfWoNameByStationId.computeIfAbsent(wo.getPrcPmWorkshopId(), v -> new HashSet<>());
        if (setOfWoName.contains(wo.getWoCode())) {
            throw new InkelinkException("车间[" + wo.getWorkshopCode() + "]>线体[" + wo.getLineCode() + "]>工位[" + wo.getWorkstationCode() + "]>wo编码[" + wo.getWoCode() + "]重复");
        }
        setOfWoName.add(wo.getWoCode());
    }


}