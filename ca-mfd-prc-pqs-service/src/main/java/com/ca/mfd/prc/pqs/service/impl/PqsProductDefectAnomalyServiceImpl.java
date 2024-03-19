package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.communication.dto.ProductDefectAnomalyDto;
import com.ca.mfd.prc.pqs.dto.ModifyDefectResponsibleInfo;
import com.ca.mfd.prc.pqs.dto.UpdateDutyAreaInfo;
import com.ca.mfd.prc.pqs.dto.UpdateDutyTeamNoInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.entity.PqsDeptEntity;
import com.ca.mfd.prc.pqs.entity.PqsGradeEntity;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsProductDefectAnomalyMapper;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsDeptService;
import com.ca.mfd.prc.pqs.service.IPqsGradeService;
import com.ca.mfd.prc.pqs.service.IPqsProductDefectAnomalyService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 产品缺陷记录服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class PqsProductDefectAnomalyServiceImpl extends AbstractCrudServiceImpl<IPqsProductDefectAnomalyMapper, PqsProductDefectAnomalyEntity> implements IPqsProductDefectAnomalyService {

    @Autowired
    private IPqsProductDefectAnomalyMapper pqsProductDefectAnomalyMapper;
    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private IPqsGradeService pqsGradeService;
    @Autowired
    private IPqsDeptService pqsDeptService;
    @Autowired
    private IPqsDefectAnomalyService pqsDefectAnomalyService;

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PQS_PRODUCT_DEFECT_ANOMALY";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsProductDefectAnomalyEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsProductDefectAnomalyEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsProductDefectAnomalyEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsProductDefectAnomalyEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsProductDefectAnomalyEntity> getAllDatas() {
        Function<Object, ? extends List<PqsProductDefectAnomalyEntity>> getDataFunc = (obj) -> {
            List<PqsProductDefectAnomalyEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsProductDefectAnomalyEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取已激活的缺陷ID列表
     *
     * @param tpsCode tps编码
     * @return 已激活的缺陷ID列表
     */
    @Override
    public List<String> getPqsProductDefectAnomalyList(String tpsCode) {
        QueryWrapper<PqsProductDefectAnomalyEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PqsProductDefectAnomalyEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PqsProductDefectAnomalyEntity::getSn, tpsCode);
        lambdaQueryWrapper.ne(PqsProductDefectAnomalyEntity::getStatus, 4);
        return this.selectList(queryWrapper).stream().map(PqsProductDefectAnomalyEntity::getDefectAnomalyCode).
                collect(Collectors.toList());
    }

    /**
     * 获取车辆所有缺陷列表
     *
     * @param tpsCode
     * @return
     */
    @Override
    public List<PqsProductDefectAnomalyEntity> getVehicleDefectAnomaly(String tpsCode) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("SN", tpsCode, ConditionOper.Equal));
        return getData(conditionInfos);
    }

    /**
     * 后台更新缺陷责任区域
     *
     * @param info
     */
    // TODO 字段对应需确认
    @Override
    public void updateVehicleDefectAnomalyDutyArea(UpdateDutyAreaInfo info) {
        for (Long id : info.getVehicleDefectAnomalyIds()) {
            update(new LambdaUpdateWrapper<PqsProductDefectAnomalyEntity>()
                    .set(PqsProductDefectAnomalyEntity::getResponsibleDeptCode, info.getDutyArea())
                    .set(PqsProductDefectAnomalyEntity::getResponsibleDeptName, info.getDutyAreaRemark() != null ? info.getDutyAreaRemark() : "")
                    .eq(PqsProductDefectAnomalyEntity::getId, id));
        }
    }

    /**
     * 后台更新缺陷责任班组
     *
     * @param info
     */
    // TODO 字段对应需确认
    @Override
    public void updateVehicleDefectAnomalyDutyTeamNo(UpdateDutyTeamNoInfo info) {
        for (String id : info.getVehicleDefectAnomalyIds()) {
            update(new LambdaUpdateWrapper<PqsProductDefectAnomalyEntity>()
                    .set(PqsProductDefectAnomalyEntity::getResponsibleDeptCode, info.getDutyPmAreaId())
                    .set(PqsProductDefectAnomalyEntity::getResponsibleDeptName, info.getDutyPmAreaName())
                    .set(PqsProductDefectAnomalyEntity::getResponsibleTeamNo, info.getDutyTeamNo())
                    .eq(PqsProductDefectAnomalyEntity::getId, id));
        }
    }

    /*@Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> columnNames = new HashMap<>(10);
        columnNames.put("pmWorkplaceName", "岗位");
        columnNames.put("ppsEntryCode", "工单代码");
        columnNames.put("tpsCode", "车辆标识");
        columnNames.put("pqsDefectAnomalyCode", "异常代码");
        columnNames.put("pqsDefectAnomalyDescription", "异常描述");
        columnNames.put("status", "状态");
        columnNames.put("createdBy", "创建人名");
        columnNames.put("creationDate", "创建时间");
        columnNames.put("lastUpdatedBy", "更新人名");
        columnNames.put("lastUpdatedDate", "更新时间");
        return columnNames;
    }*/

    /**
     * getPageVehicleDatas
     *
     * @param conditions
     * @param sorts
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public PageData<PqsProductDefectAnomalyEntity> getPageVehicleDatas(List<ConditionDto> conditions, List<SortDto>
            sorts, int pageIndex, int pageSize) {
        PageData<PqsProductDefectAnomalyEntity> page = new PageData<>();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);

        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        if (sorts == null || sorts.size() == 0) {
            sorts = new ArrayList<>();
            SortDto st = new SortDto();
            st.setColumnName("a.LAST_UPDATE_DATE");
            st.setDirection(ConditionDirection.DESC);
            sorts.add(st);
        }
        List<SortDto> sortsNew = MpSqlUtils.filtrationSort(currentModelClass(), sorts, "a");
        ConditionDto barcodeCondition = conditions.stream().filter(c -> StringUtils.equals(c.getColumnName(), "barcode")).findFirst().orElse(null);
        if (barcodeCondition != null) {
            barcodeCondition.setAlias("po");
        }

        Map<String, Object> map = new HashMap<>(2);
        //pps
        List<ConditionDto> conditionPo = conditions.stream().filter(o -> o.getAlias() != null && "po".equalsIgnoreCase(o.getAlias())).collect(Collectors.toList());
        List<PpsOrderEntity> orderList = null;
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setColumnName("sn");
        conditionDto.setOperator(ConditionOper.In);
        //pqs
        List<ConditionDto> conditionPvda = conditions.stream().filter(o -> StringUtils.isBlank(o.getAlias()) || "pvda".equalsIgnoreCase(o.getAlias())).collect(Collectors.toList());
        if (conditionPo.size() > 0) {
            ResultVO<List<PpsOrderEntity>> rsp = new ResultVO().ok(ppsOrderProvider.getData(conditionPo));
            if (rsp == null || !rsp.getSuccess()) {
                throw new InkelinkException("PPS模块订单获取失败。" + (rsp == null ? "" : rsp.getMessage()));
            }
            orderList = rsp.getData();
            List<String> snList = orderList.stream().map(o -> o.getSn()).distinct().collect(Collectors.toList());
            conditionDto.setValue(String.join("|", snList));
            conditionPvda.add(conditionDto);
        }
        List<ConditionDto> conditiondatas = MpSqlUtils.filtrationCondition(currentModelClass(), conditionPvda, "");
        map.put("where_a", conditiondatas);

        map.put("order", sortsNew);

        Page<PqsProductDefectAnomalyEntity> mpage = new Page<>(page.getPageIndex(), page.getPageSize());
        Page<PqsProductDefectAnomalyEntity> pdata = pqsProductDefectAnomalyMapper.getPageVehicleDatas(mpage, map);
        if (orderList == null) {
            List<String> snList = pdata.getRecords().stream().map(o -> o.getSn()).collect(Collectors.toList());
            conditionDto.setValue(String.join("|", snList));
            conditionPo.add(conditionDto);
            ResultVO<List<PpsOrderEntity>> rsp = new ResultVO<List<PpsOrderEntity>>().ok(ppsOrderProvider.getData(conditionPo));
            if (rsp == null || !rsp.getSuccess()) {
                throw new InkelinkException("PPS模块订单获取失败。" + (rsp == null ? "" : rsp.getMessage()));
            }
            orderList = rsp.getData();
        }
        Map<String, PpsOrderEntity> orderMap = orderList.stream().collect(Collectors.toMap(
                PpsOrderEntity::getSn,
                obj -> obj,
                (key1, key2) -> key1
        ));

        page.setDatas(pdata.getRecords().stream().peek(o -> {
            o.setBarcode(orderMap.get(o.getSn()) != null && StringUtils.isNotBlank(orderMap.get(o.getSn()).getBarcode()) ? orderMap.get(o.getSn()).getBarcode() : "");
            o.setSequenceNo(orderMap.get(o.getSn()) != null && StringUtils.isNotBlank(orderMap.get(o.getSn()).getDisplayNo()) ? orderMap.get(o.getSn()).getDisplayNo() : "");
        }).collect(Collectors.toList()));
        page.setTotal((int) pdata.getTotal());
        return page;
    }

    /**
     * 修改缺陷责任部门-等级
     *
     * @param info
     */
    @Override
    public void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info) {

        QueryWrapper<PqsGradeEntity> gradeEntityQueryWrapper = new QueryWrapper<>();
        gradeEntityQueryWrapper.lambda().eq(PqsGradeEntity::getGradeCode, info.getGradeCode());
        PqsGradeEntity gradeInfo = pqsGradeService.getData(gradeEntityQueryWrapper, false)
                .stream().findFirst().orElse(null);

        QueryWrapper<PqsDeptEntity> deptEntityQueryWrapper = new QueryWrapper<>();
        deptEntityQueryWrapper.lambda().eq(PqsDeptEntity::getDeptCode, info.getResponsibleDeptCode());
        PqsDeptEntity deptInfo = pqsDeptService.getData(deptEntityQueryWrapper, false)
                .stream().findFirst().orElse(null);

        List<PqsProductDefectAnomalyEntity> updateList = Lists.newArrayList();
        for (Long dataId : info.getDataIds()) {
            PqsProductDefectAnomalyEntity entity = get(dataId);
            entity.setId(dataId);
            entity.setResponsibleDeptCode(info.getResponsibleDeptCode());
            entity.setResponsibleDeptName(deptInfo == null ? StringUtils.EMPTY : deptInfo.getDeptName());
            entity.setGradeName(gradeInfo == null ? StringUtils.EMPTY : gradeInfo.getGradeName());
            entity.setGradeCode(info.getGradeCode());
            entity.setResponsibleTeamNo(info.getResponsibleTeamNo());
            updateList.add(entity);
        }
        updateBatchById(updateList, updateList.size());
    }

    /**
     * 重载获取分页数据
     *
     * @param model 分页信息
     * @return 返回分页数据
     */
    @Override
    public PageData<PqsProductDefectAnomalyEntity> page(PageDataDto model) {
        return getPageDataEx(model);
    }

    public PageData<PqsProductDefectAnomalyEntity> getPageDataEx(PageDataDto model) {
        // 获取主表数据
        PageData<PqsProductDefectAnomalyEntity> pageData = super.page(model);
        // 如果存在主数据
        if (ObjectUtils.isNotEmpty(pageData) && ObjectUtils.isNotEmpty(pageData.getDatas())) {
            List<PqsProductDefectAnomalyEntity> dataRecord = pageData.getDatas();
            // 获取车辆关联的VIN
            Set<String> vinCodes = new HashSet<>();
            // 获取管理的iccCode列表
            Set<String> iccCodes = new HashSet<>();
            for (PqsProductDefectAnomalyEntity pqsProductDefectAnomalyEntity : dataRecord) {
                if (StringUtils.isNotBlank(pqsProductDefectAnomalyEntity.getSn())) {
                    vinCodes.add(pqsProductDefectAnomalyEntity.getSn());
                }
                if (StringUtils.isNotBlank(pqsProductDefectAnomalyEntity.getDefectAnomalyCode())) {
                    iccCodes.add(pqsProductDefectAnomalyEntity.getDefectAnomalyCode());
                }
            }

            Map<String, PpsOrderEntity> orderEntityMap = new HashMap<>();
            Map<String, PqsDefectAnomalyEntity> defectAnomalyEntityMap = new HashMap<>();
            // 查询关联的车辆数据
            if (ObjectUtils.isNotEmpty(vinCodes)) {
                List<PpsOrderEntity> ppsOrderEntityList = ppsOrderProvider.getListBySnCodes(new ArrayList<>(vinCodes));
                orderEntityMap = ppsOrderEntityList.stream().collect(Collectors.toMap(PpsOrderEntity::getSn, item -> item));
            }
            // 查询缺陷信息数据
            if (ObjectUtils.isNotEmpty(iccCodes)) {
                QueryWrapper<PqsDefectAnomalyEntity> queryWrapper = Wrappers.query();
                queryWrapper.lambda().in(PqsDefectAnomalyEntity::getDefectAnomalyCode, iccCodes);
                List<PqsDefectAnomalyEntity> anomalyEntityList = pqsDefectAnomalyService.getData(queryWrapper, false);
                defectAnomalyEntityMap = anomalyEntityList.stream().collect(Collectors.toMap(PqsDefectAnomalyEntity::getDefectAnomalyCode, item -> item));
            }

            // 赋值需要关联的信息
            for (PqsProductDefectAnomalyEntity item : dataRecord) {
                if (orderEntityMap.containsKey(item.getSn())) {
                    PpsOrderEntity orderEntity = orderEntityMap.get(item.getSn());
                    if (null != orderEntity) {
                        item.setModel(orderEntity.getModel());
                    }
                }
                if (orderEntityMap.containsKey(item.getDefectAnomalyCode())) {
                    PqsDefectAnomalyEntity anomalyEntity = defectAnomalyEntityMap.get(item.getDefectAnomalyCode());
                    if (null != anomalyEntity) {
                        item.setPositionCode(anomalyEntity.getPositionCode());
                        item.setPositionDescription(anomalyEntity.getPositionDescription());
                    }
                }
            }
        }
        return pageData;
    }

    /**
     * QMS获取产品缺陷数据
     *
     * @return
     */
    @Override
    public List<ProductDefectAnomalyDto> getProductDefectAnomalyList() {
        return pqsProductDefectAnomalyMapper.getProductDefectAnomaly();
    }
}