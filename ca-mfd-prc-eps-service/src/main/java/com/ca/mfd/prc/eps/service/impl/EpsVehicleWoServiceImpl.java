package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoExecuteEntity;
import com.ca.mfd.prc.eps.mapper.IEpsVehicleWoMapper;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmToolEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmTraceComponentEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWoEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmTraceComponentProvider;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.remote.app.pps.dto.FilterFetureExpressionPara;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsProductProcessEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.AnalysisFeatureProvider;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsEntryProvider;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsProductProcessProvider;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoExecuteService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 车辆操作信息
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsVehicleWoServiceImpl extends AbstractCrudServiceImpl<IEpsVehicleWoMapper, EpsVehicleWoEntity> implements IEpsVehicleWoService {
    private static final Logger logger = LoggerFactory.getLogger(EpsVehicleWoServiceImpl.class);
    private static final String SN_CODE = "sn";
    private static ConcurrentMap<String, List<EpsVehicleWoEntity>> cacheWos = new ConcurrentHashMap<>();
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private PmTraceComponentProvider pmTraceComponentProvider;
    @Autowired
    private AnalysisFeatureProvider analysisFeatureProvider;
    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private PpsProductProcessProvider ppsProductProcessProvider;
    @Autowired
    private PpsEntryProvider ppsEntryProvider;
    @Autowired
    private IEpsVehicleWoMapper epsVehicleWoMapper;
    @Autowired
    private IEpsVehicleWoExecuteService epsVehicleWoExecuteService;

    @Override
    public PageData<EpsVehicleWoEntity> getPageVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, int pageIndex, int pageSize) {
        PageData<EpsVehicleWoEntity> page = new PageData<>();
        page.setPageIndex(pageIndex);
        page.setPageSize(pageSize);
        //将SN字段查询，由默认的 AllLike 改为 Equal
        if (conditions != null) {
            conditions.forEach(c -> {
                if (StringUtils.equalsIgnoreCase(c.getColumnName(), SN_CODE)) {
                    c.setOperator(ConditionOper.Equal);
                }
            });
        }
        getPageVehicleDatas(conditions, sorts, page);
        return page;
    }

    /**
     * 根据sn查询工艺
     *
     * @param sn
     * @return
     */
    @Override
    public List<EpsVehicleWoEntity> getBySn(String sn) {

        QueryWrapper<EpsVehicleWoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EpsVehicleWoEntity::getSn, sn)
                .ne(EpsVehicleWoEntity::getResult, 1);
        return getData(queryWrapper, false);
    }

    private void getPageVehicleDatas(List<ConditionDto> conditions, List<SortDto> sorts, PageData<EpsVehicleWoEntity> page) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        if (sorts == null || sorts.size() == 0) {
            sorts = new ArrayList<>();
            SortDto st = new SortDto();
            st.setColumnName(MpSqlUtils.getColumnName(EpsVehicleWoEntity::getLastUpdateDate));
            st.setDirection(ConditionDirection.DESC);
            sorts.add(st);
        }
        //EpsVehicleWoEntity.class,
        List<ConditionDto> conditionCons = MpSqlUtils.filtrationCondition(EpsVehicleWoEntity.class, conditions, "", false);
        //MpSqlUtils.filtrationCondition(PpsOrderEntity.class, conditions, "", false);
        List<SortDto> sortsNew = MpSqlUtils.filtrationSort(EpsVehicleWoEntity.class, sorts, "", false);
        //MpSqlUtils.filtrationSort(PpsOrderEntity.class, sorts, "", false);
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put("where_a", conditionCons);
        map.put("order", sortsNew);

        Page<EpsVehicleWoEntity> mpage = new Page<>(page.getPageIndex(), page.getPageSize());
        Page<EpsVehicleWoEntity> pdata = epsVehicleWoMapper.getPageVehicleDatas(mpage, map);
        page.setDatas(pdata.getRecords());
        page.setTotal((int) pdata.getTotal());
    }

    /**
     * 生成工单工艺
     *
     * @param ppsEntryInfo
     * @return
     */
    public void createEntryWo(PpsEntryEntity ppsEntryInfo) {
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        PmWorkShopEntity shopInfo = pmall.getShops().stream().filter(o -> StringUtils.equals(o.getWorkshopCode(), ppsEntryInfo.getWorkshopCode())).findFirst().orElse(null);
        List<PmLineEntity> areaInfos = pmall.getLines().stream().filter(o -> Objects.equals(o.getPrcPmWorkshopId(), shopInfo.getId())).collect(Collectors.toList());
        PpsProductProcessEntity productProcessInfo = ppsProductProcessProvider.getAllDatas().stream().filter(o ->
                Objects.equals(o.getId(), ppsEntryInfo.getPrcPpsProductProcessId())).findFirst().orElse(null);
        //获取工单已经有的工艺
        QueryWrapper<EpsVehicleWoEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsVehicleWoEntity::getSn, ppsEntryInfo.getSn())
                .ne(EpsVehicleWoEntity::getResult, 0)
                .eq(EpsVehicleWoEntity::getWorkshopCode, ppsEntryInfo.getWorkshopCode());
        List<EpsVehicleWoEntity> existWos = selectList(qry);
        List<String> charValues = new ArrayList<>();
        try {
            charValues = ppsOrderProvider.getOrderCharacteristicByOrderId(ppsEntryInfo.getPrcPpsOrderId().toString())
                    .stream().map(PmProductCharacteristicsEntity::getProductCharacteristicsValue).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("", e);
        }
        QueryWrapper<EpsVehicleWoEntity> qryWoid = new QueryWrapper<>();
        qryWoid.lambda().eq(EpsVehicleWoEntity::getSn, ppsEntryInfo.getSn())
                .eq(EpsVehicleWoEntity::getResult, 0)
                .eq(EpsVehicleWoEntity::getWorkshopCode, ppsEntryInfo.getWorkshopCode());
        List<Long> woIds = selectList(qryWoid).stream().map(EpsVehicleWoEntity::getId).collect(Collectors.toList());

        //删除未作的工艺，重新生成
        if (woIds != null && woIds.size() > 0) {
            delete(woIds.toArray(new Long[0]));
        }

        UpdateWrapper<EpsVehicleWoExecuteEntity> delWoExecute = new UpdateWrapper<>();
        delWoExecute.lambda().eq(EpsVehicleWoExecuteEntity::getWorkshopCode, ppsEntryInfo.getWorkshopCode())
                .eq(EpsVehicleWoExecuteEntity::getSn, ppsEntryInfo.getSn());
        epsVehicleWoExecuteService.delete(delWoExecute);

        if (productProcessInfo != null)//有工艺路径，才能够生成工艺
        {
            //获取关联的线体
            areaInfos = areaInfos.stream().filter(o ->
                    productProcessInfo.getPpsProductProcessAviInfos().stream().anyMatch(p ->
                            StringUtils.equals(p.getLineCode(), o.getLineCode())
                                    && StringUtils.equals(p.getWorkshopCode(), shopInfo.getWorkshopCode()))
            ).collect(Collectors.toList());
            for (PmLineEntity area : areaInfos) {
                createAreaWo(ppsEntryInfo, area, existWos, charValues);
            }
        }
    }

    /**
     * 生成工单工艺
     *
     * @param barCode
     * @param shopCode
     * @return
     */
    @Override
    public void createEntryWo(String barCode, String shopCode) {
        PpsEntryEntity ppsEntryInfo = ppsEntryProvider.getFirstEntryTypeShopCodeSn(barCode, 1, shopCode);
        if (ppsEntryInfo != null) {
            createEntryWo(ppsEntryInfo);
        } else {
            logger.error("没有找到工单：" + barCode + ",车间：" + shopCode);
        }
    }

    /**
     * 创建线体工艺
     *
     * @param ppsEntryInfo
     * @param pmAreaInfo
     * @param existWos
     * @param charValues
     * @return
     */
    private void createAreaWo(PpsEntryEntity ppsEntryInfo, PmLineEntity pmAreaInfo, List<EpsVehicleWoEntity> existWos, List<String> charValues) {
        List<EpsVehicleWoEntity> epsVehicleWoInfos = new ArrayList<>();
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        //获取线体下面的工艺
        List<PmWoEntity> areaWos = pmall.getWos().stream().filter(o -> Objects.equals(o.getPrcPmLineId(), pmAreaInfo.getId())).collect(Collectors.toList());

        FilterFetureExpressionPara filterFetureExp = new FilterFetureExpressionPara();
        filterFetureExp.setBarcode(ppsEntryInfo.getSn());
        filterFetureExp.setFetureExpressions(areaWos.stream().map(c -> c.getFeatureCode()).collect(Collectors.toList()));
        List<String> fetureCodes = analysisFeatureProvider.filterFeatureExpression(filterFetureExp);
        List<String> woCodes = areaWos.stream().filter(c -> fetureCodes.contains(c.getFeatureCode())).map(c -> c.getWoCode()).distinct().collect(Collectors.toList());
        for (String woCode : woCodes) {
            //可能具备横幅工艺
            List<PmWoEntity> pmWoInfos = areaWos.stream().filter(c -> StringUtils.equals(c.getWoCode(), woCode)).collect(Collectors.toList());
            //加载操作
            EpsVehicleWoEntity epsVehicleWo = existWos.stream().filter(o -> StringUtils.equals(o.getWoCode(), pmWoInfos.get(0).getWoCode())).findFirst().orElse(null);
            if (epsVehicleWo == null) {
                epsVehicleWo = createWo(ppsEntryInfo, pmWoInfos.get(0));

                //加载对象
                epsVehicleWoInfos.add(epsVehicleWo);
            }
            //一条工艺可能对应多个执行配置
            for (PmWoEntity item : pmWoInfos) {
                loadWoExecute(epsVehicleWo, item, ppsEntryInfo.getModel(), charValues);
            }
        }
        //执行保存结果
        insertBatch(epsVehicleWoInfos);
    }

    /**
     * 创建操作
     *
     * @param ppsEntryInfo
     * @param pmWo
     * @return
     */
    EpsVehicleWoEntity createWo(PpsEntryEntity ppsEntryInfo, PmWoEntity pmWo) {
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        PmWorkStationEntity pmWorkPlace = pmall.getStations().stream().filter(w -> Objects.equals(w.getId(), pmWo.getPrcPmWorkstationId())).findFirst().orElse(null);
        PmWorkShopEntity pmShopInfo = pmall.getShops().stream().filter(w -> Objects.equals(w.getId(), pmWorkPlace.getPrcPmWorkshopId())).findFirst().orElse(null);

        //生成EPSvehicleWo
        EpsVehicleWoEntity epsVehicleWoEntity = new EpsVehicleWoEntity();

        epsVehicleWoEntity.setId(IdGenerator.getId());
        epsVehicleWoEntity.setSn(ppsEntryInfo.getSn());
        epsVehicleWoEntity.setEntryNo(ppsEntryInfo.getEntryNo());
        epsVehicleWoEntity.setWorkshopCode(pmShopInfo.getWorkshopCode());
        epsVehicleWoEntity.setWorkstationCode(pmWorkPlace.getWorkstationCode());
        epsVehicleWoEntity.setWoCode(pmWo.getWoCode());
        epsVehicleWoEntity.setWoDescription(pmWo.getWoDescription());
        epsVehicleWoEntity.setWoDisplayNo(pmWo.getDisplayNo());
        epsVehicleWoEntity.setWoGroup(pmWo.getWoGroupName());
        epsVehicleWoEntity.setWoType(pmWo.getWoType());
        epsVehicleWoEntity.setWoOperType(pmWo.getOperType());
        epsVehicleWoEntity.setDefectAnomalyCode(pmWo.getQmDefectAnomalyCode());
        epsVehicleWoEntity.setTrcByGroup(pmWo.getTrcByGroup());

        return epsVehicleWoEntity;
    }

    /**
     * 操作加载工具
     *
     * @param woInfo
     * @param pmWoInfo
     * @param model
     * @param characteristicValues
     * @return
     */
    void loadWoExecute(EpsVehicleWoEntity woInfo, PmWoEntity pmWoInfo, String model, List<String> characteristicValues) {
        //获取工艺关联的JOB
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        List<PmToolJobEntity> pmToolJobInfos = pmall.getToolJobs().stream().filter(o -> Objects.equals(o.getPmWoId(), pmWoInfo.getId())).collect(Collectors.toList());
        PmWorkStationEntity stationInfo = pmall.getStations().stream().filter(c -> Objects.equals(c.getId(), pmWoInfo.getPrcPmWorkstationId())).findFirst().orElse(null);

        EpsVehicleWoExecuteEntity woExecuteInfo = new EpsVehicleWoExecuteEntity();
        woExecuteInfo.setPrcEpsVehicleWoId(woInfo.getId());
        woExecuteInfo.setSn(woInfo.getSn());
        woExecuteInfo.setWorkstationCode(stationInfo.getWorkstationCode());
        woExecuteInfo.setWorkshopCode(woInfo.getWorkshopCode());
        woExecuteInfo.setPrcPmWoId(pmWoInfo.getId());
        woExecuteInfo.setWoCode(pmWoInfo.getWoCode());

        //装载工具和JOB
        for (PmToolJobEntity pmToolJobInfo : pmToolJobInfos) {
            if (FeatureTool.calExpression(pmToolJobInfo.getFeatureCode(), model, characteristicValues)) {
                //加载job
                woExecuteInfo.setJobNo(pmToolJobInfo.getJobNo());
                PmToolEntity pmToolInfo = pmall.getTools().stream().filter(o -> Objects.equals(o.getId(), pmToolJobInfo.getPrcPmToolId())).findFirst().orElse(null);
                if (pmToolInfo != null) {
                    woExecuteInfo.setPrcPmToolId(pmToolInfo.getId());
                    woExecuteInfo.setToolCode(pmToolInfo.getToolCode());
                    woExecuteInfo.setToolName(pmToolInfo.getToolName());
                    woExecuteInfo.setToolToolType(pmToolInfo.getToolType());
                    woExecuteInfo.setToolType(pmToolInfo.getNetType());
                    woExecuteInfo.setToolIp(pmToolInfo.getIp());
                    woExecuteInfo.setJobPort(pmToolInfo.getPort());
                    woExecuteInfo.setToolBrand(pmToolInfo.getBrand());
                }
                break;
            }
        }

        //追溯工艺组件
        if (pmWoInfo.getQmDefectComponentId() != 0) {
            PmTraceComponentEntity componentInfo = pmTraceComponentProvider.getDataCache().stream().filter(c ->
                    Objects.equals(c.getId(), pmWoInfo.getQmDefectComponentId())).findFirst().orElse(null);
            if (componentInfo != null) {
                woExecuteInfo.setTraceComponentCode(componentInfo.getTraceComponentCode());
            }
        }
        epsVehicleWoExecuteService.insert(woExecuteInfo);
    }

    /**
     * 获取工位上面的所有工艺
     *
     * @param workstationCode
     * @param sn
     * @return
     */
    public List<EpsVehicleWoEntity> getWorkstationWos(String workstationCode, String sn) {
        //根据工位和车辆唯一码获取工艺列表

        List<EpsVehicleWoExecuteEntity> woExecuteInfos = epsVehicleWoExecuteService.getListByStationSn(workstationCode, sn);

        List<Long> woIds = woExecuteInfos.stream().map(c -> c.getPrcEpsVehicleWoId()).collect(Collectors.toList());

        //查询出工艺主数据
        QueryWrapper<EpsVehicleWoEntity> qry = new QueryWrapper<>();
        qry.lambda().in(EpsVehicleWoEntity::getId, woIds);
        List<EpsVehicleWoEntity> woInfos = selectList(qry);

        for (EpsVehicleWoEntity item : woInfos) {
            //将工艺执行配置绑定在工艺主数据上面 WorkstationCode== workstationCode这个条件防止配置错误导致现场执行混乱
            EpsVehicleWoExecuteEntity woexecuteInfo = woExecuteInfos.stream().filter(c -> Objects.equals(c.getPrcEpsVehicleWoId(), item.getId())
                    && StringUtils.equals(c.getWorkstationCode(), workstationCode)).findFirst().orElse(null);
            if (woexecuteInfo != null) {
                item.setWoExecuteInfo(woexecuteInfo);
            }
        }
        return woInfos;
    }

    /**
     * 是否生成过工艺 true 是   false 没有
     *
     * @param productCode
     * @return
     */
    public Boolean isHaveCreateWo(String productCode) {
        if (cacheWos.containsKey(productCode)) {
            QueryWrapper<EpsVehicleWoEntity> qry = new QueryWrapper<>();
            qry.lambda().eq(EpsVehicleWoEntity::getSn, productCode);
            return selectCount(qry) > 0;
        } else {
            return false;
        }
    }
}