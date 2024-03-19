package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ArraysUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.pps.dto.BodyVehicleDTO;
import com.ca.mfd.prc.pps.dto.EntryStatusPara;
import com.ca.mfd.prc.pps.dto.OrderEntryInfo;
import com.ca.mfd.prc.pps.dto.RestEntryQueuePara;
import com.ca.mfd.prc.pps.dto.RestShopEntryQueuePara;
import com.ca.mfd.prc.pps.dto.ShopPlanMonitorInfo;
import com.ca.mfd.prc.pps.dto.SubModelInfo;
import com.ca.mfd.prc.pps.entity.PpsEntryConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pps.entity.PpsLineProductionConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsProductProcessAviEntity;
import com.ca.mfd.prc.pps.entity.PpsProductProcessEntity;
import com.ca.mfd.prc.pps.enums.EntryTypeEnum;
import com.ca.mfd.prc.pps.enums.OrderCategoryEnum;
import com.ca.mfd.prc.pps.enums.OrderStatusEnum;
import com.ca.mfd.prc.pps.enums.PlanStatusEnum;
import com.ca.mfd.prc.pps.extend.IPpsOrderExtendService;
import com.ca.mfd.prc.pps.extend.IPpsPlanExtendService;
import com.ca.mfd.prc.pps.mapper.IPpsEntryMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsEntryConfigService;
import com.ca.mfd.prc.pps.service.IPpsEntryService;
import com.ca.mfd.prc.pps.service.IPpsLineProductionConfigService;
import com.ca.mfd.prc.pps.service.IPpsPlanAviService;
import com.ca.mfd.prc.pps.service.IPpsProductProcessService;
import com.ca.mfd.prc.pps.service.IPpsVtVinRuleService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 工单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PpsEntryServiceImpl extends AbstractCrudServiceImpl<IPpsEntryMapper, PpsEntryEntity>
        implements IPpsEntryService {

    private static final int OPER_TYPE_A = 1;
    private static final int OPER_TYPE_B = 2;
    private static final Object ENTRY_LOCK_LOCK_OBJ = new Object();
    @Resource
    private IPpsEntryMapper ppsEntryDao;
    @Autowired
    private IPpsOrderExtendService ppsOrderExtendService;
    @Autowired
    private IPpsVtVinRuleService ppsVtVinRuleService;
    @Autowired
    private IPpsPlanExtendService ppsPlanExtendService;
    @Autowired
    private IPpsPlanAviService ppsPlanAviService;
    @Autowired
    private IPpsEntryConfigService ppsEntryConfigService;
    @Autowired
    private IPpsProductProcessService ppsProductProcessService;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private IPpsLineProductionConfigService ppsLineProductionConfigService;


    /**
     * 创建流水号
     */
    private String createSn(String category) {
        return sysSnConfigProvider.createSn(category);
    }

    /**
     * 查询工艺路径设置
     */
    private List<PpsProductProcessEntity> getAllProductProcess() {
        return ppsProductProcessService.getAllDatas();
    }

    /**
     * 根据tps 获取工单列表
     *
     * @param tpsCode
     * @return 工单列表
     */
    @Override
    public List<PpsEntryEntity> getPpsEntryBySn(String tpsCode) {
        QueryWrapper<PpsEntryEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryEntity::getSn, tpsCode);
        return selectList(queryWrapper);
    }

    /**
     * 根据sn 获取工单首条
     *
     * @param sn
     * @return 工单
     */
    @Override
    public PpsEntryEntity getFirstBySn(String sn) {
        QueryWrapper<PpsEntryEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsEntryEntity::getSn, sn);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 创建分线工单
     *
     * @param ppsEntryInfo
     * @param ppsEntryConfiginfo
     */
    @Override
    public void createBranchEntry(PpsEntryEntity ppsEntryInfo, PpsEntryConfigEntity ppsEntryConfiginfo) {
        if (!StringUtils.isBlank(ppsEntryConfiginfo.getModel())) {
            List<String> modelAny = ArraysUtils.splitNoEmpty(ppsEntryConfiginfo.getModel(), "[\\||\\,|\\;]");
            if (modelAny.stream().noneMatch(s -> StringUtils.equals(s, ppsEntryInfo.getModel()))) {
                return;
            }
        }

        QueryWrapper<PpsEntryEntity> wrapper = new QueryWrapper();
        wrapper.lambda().eq(PpsEntryEntity::getSubscriubeCode, ppsEntryConfiginfo.getSubCode())
                .eq(PpsEntryEntity::getSn, ppsEntryInfo.getSn());
        PpsEntryEntity oldEntity = getTopDatas(1, wrapper).stream().findFirst().orElse(null);
        if (oldEntity != null) {
            delete(oldEntity.getId());
        }

        if (!StringUtils.equals(ppsEntryInfo.getOrderCategory(), ppsEntryConfiginfo.getOrderCategory())
                && !(OrderCategoryEnum.Vehicle.codeString().equals(ppsEntryConfiginfo.getOrderCategory())
                || OrderCategoryEnum.SparePart.codeString().equals(ppsEntryInfo.getOrderCategory()))) {
            return;
        }

        //计算顺序号
        int displayno = Integer.parseInt(createSn("PpsEntry_"
                + ppsEntryInfo.getWorkshopCode()
                + "_" + ppsEntryConfiginfo.getLineCode()
                + "_" + ppsEntryConfiginfo.getSubCode()
                + "_Seq"));
        //工单号
        String entryNo = ppsEntryConfiginfo.getSubCode() + DateUtils.format(new Date(), DateUtils.DATE_PATTERN_C)
                + createSn("PpsEntry_" + ppsEntryInfo.getWorkshopCode()
                + "_" + ppsEntryConfiginfo.getLineCode()
                + "_" + ppsEntryConfiginfo.getSubCode() + "_Code");

        //创建分线工单
        PpsEntryEntity ppsAreaEntryInfo = new PpsEntryEntity();

        ppsAreaEntryInfo.setOrderNo(ppsEntryInfo.getOrderNo());
        ppsAreaEntryInfo.setParentId(ppsEntryInfo.getId());
        ppsAreaEntryInfo.setParentNo(ppsEntryInfo.getEntryNo());
        ppsAreaEntryInfo.setPlanNo(ppsEntryInfo.getPlanNo());
        ppsAreaEntryInfo.setModel(ppsEntryInfo.getModel());
        ppsAreaEntryInfo.setPrcPpsProductProcessId(ppsEntryInfo.getPrcPpsProductProcessId());
        ppsAreaEntryInfo.setLineCode(ppsEntryConfiginfo.getLineCode());
        ppsAreaEntryInfo.setSn(ppsEntryInfo.getSn());
        ppsAreaEntryInfo.setDisplayNo(displayno);
        ppsAreaEntryInfo.setEstimatedStartDt(ppsEntryInfo.getEstimatedStartDt());
        ppsAreaEntryInfo.setEstimatedEndDt(ppsEntryInfo.getEstimatedEndDt());
        ppsAreaEntryInfo.setEntryNo(entryNo);
        ppsAreaEntryInfo.setEntrySource(1);
        ppsAreaEntryInfo.setEntryType(EntryTypeEnum.AreaEntry.code());
        ppsAreaEntryInfo.setWorkshopCode(ppsEntryInfo.getWorkshopCode());
        ppsAreaEntryInfo.setPrcPpsOrderId(ppsEntryInfo.getPrcPpsOrderId());
        ppsAreaEntryInfo.setStatus(2);
        ppsAreaEntryInfo.setIsCreateWo(true);
        ppsAreaEntryInfo.setOrderSign(ppsEntryInfo.getOrderSign());
        ppsAreaEntryInfo.setOrderCategory(ppsEntryInfo.getOrderCategory());
        ppsAreaEntryInfo.setSubscriubeCode(ppsEntryConfiginfo.getSubCode());
        ppsAreaEntryInfo.setProductCode(ppsEntryInfo.getProductCode());
        ppsAreaEntryInfo.setMaterialCn(ppsEntryInfo.getMaterialCn());
        ppsAreaEntryInfo.setPlanQuantity(ppsEntryInfo.getPlanQuantity());
        //插分线
        insert(ppsAreaEntryInfo);
    }

    public List<OrderEntryInfo> getShopEntrys(String shopCode, int top) {
        return getShopEntrys(shopCode, top, "");
    }

    /**
     * 获取车间工单
     *
     * @param shopCode
     * @param top
     * @param model
     * @return
     */
    @Override
    public List<OrderEntryInfo> getShopEntrys(String shopCode, int top, String model) {
        return getShopEntrys(shopCode, top, model, "");
    }

    /**
     * 获取车间工单
     *
     * @param shopCode
     * @param top
     * @param model
     * @param lineCode
     * @return
     */
    @Override
    public List<OrderEntryInfo> getShopEntrys(String shopCode, int top, String model,String lineCode) {
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("workshopCode", shopCode, ConditionOper.Equal));
        conditions.add(new ConditionDto("EntryType", "1", ConditionOper.Equal));
        conditions.add(new ConditionDto("Status", "2", ConditionOper.Equal));
        conditions.add(new ConditionDto("areaCode", lineCode, ConditionOper.Equal));

        if (!StringUtils.isBlank(model)) {
            model = model.replace(',', '|');
            conditions.add(new ConditionDto("Model", model, ConditionOper.Equal));
        }
        return getShopEntrys(conditions, top, false);
    }

    /**
     * 获取车间工单
     *
     * @param conditions
     * @param top
     * @param desc
     * @return
     */
    private List<OrderEntryInfo> getShopEntrys(List<ConditionDto> conditions, int top, Boolean desc) {

        List<SortDto> sorts = new ArrayList<>();
        if (desc) {
            SortDto st = new SortDto();
            st.setColumnName("DisplayNo");
            st.setDirection(ConditionDirection.DESC);
            sorts.add(st);
        } else {
            SortDto st = new SortDto();
            st.setColumnName("DisplayNo");
            st.setDirection(ConditionDirection.ASC);
            sorts.add(st);
        }
        List<SortDto> sortsNew = MpSqlUtils.filtrationSort(sorts, "");
        List<ConditionDto> conditionCons = MpSqlUtils.filtrationCondition(conditions, "");
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(3);
        map.put("wheresa", conditionCons);
        map.put("order", sortsNew);
        map.put("top", top);
        return ppsEntryDao.getEntryOrder(map);
    }

    /**
     * 获取订阅码工单(分线工单)未下发的工单列表
     *
     * @param subCode
     * @param take    1
     * @param model
     * @return
     */
    @Override
    public List<OrderEntryInfo> getBranchingEntryUnissued(String subCode, String model, int take) {
        PpsEntryConfigEntity entryConfigInfo = ppsEntryConfigService.getFirstBySubCode(subCode);
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("SubScriubCode", subCode, ConditionOper.Equal));
        conditions.add(new ConditionDto("EntryType", "2", ConditionOper.Equal));
        conditions.add(new ConditionDto("Status", "2", ConditionOper.Equal));

        if (entryConfigInfo != null && !StringUtils.isBlank(entryConfigInfo.getOrderCategory())) {
            PpsLineProductionConfigEntity productionConfigInfo = ppsLineProductionConfigService.getFirstByLineCode(entryConfigInfo.getLineCode());
            if (productionConfigInfo != null) {
                conditions.add(new ConditionDto("OrderCategory", productionConfigInfo.getOrderCategory(), ConditionOper.Equal));
            }
        }
        if (!StringUtils.isBlank(model)) {
            model = model.replace(',', '|');
            conditions.add(new ConditionDto("Model", model, ConditionOper.In));
        }
        return getShopEntrys(conditions, take, false);
    }


    /**
     * 获取订阅码工单(分线工单)已下发的工单列表
     *
     * @param subCode
     * @param take
     * @param model
     * @return
     */
    @Override
    public List<OrderEntryInfo> getBranchingEntryIssued(String subCode, String model, int take) {
        PpsEntryConfigEntity entryConfigInfo = ppsEntryConfigService.getFirstBySubCode(subCode);
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("SubScriubCode", subCode, ConditionOper.Equal));
        conditions.add(new ConditionDto("EntryType", "2", ConditionOper.Equal));
        conditions.add(new ConditionDto("Status", "2", ConditionOper.GreaterThan));

        if (entryConfigInfo != null && !StringUtils.isBlank(entryConfigInfo.getOrderCategory())) {
            PpsLineProductionConfigEntity productionConfigInfo = ppsLineProductionConfigService.getFirstByLineCode(entryConfigInfo.getLineCode());
            if (productionConfigInfo != null) {
                conditions.add(new ConditionDto("OrderCategory", productionConfigInfo.getOrderCategory(), ConditionOper.Equal));
            }
        }
        if (!StringUtils.isBlank(model)) {
            model = model.replace(',', '|');
            conditions.add(new ConditionDto("Model", model, ConditionOper.In));
        }
        return getShopEntrys(conditions, take, true);
    }

    /**
     * 获取车间订单列表(整车)
     *
     * @param conditions 查询条件
     * @param sorts      排序条件
     * @param page       分页数据
     */
    @Override
    public void getShopOrders(List<ConditionDto> conditions, List<SortDto> sorts, PageData<OrderEntryInfo> page) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        if (sorts == null || sorts.size() == 0) {
            sorts = new ArrayList<>();
            SortDto st = new SortDto();
            st.setColumnName("EntryNo");
            st.setDirection(ConditionDirection.DESC);
            sorts.add(st);
        }
        List<SortDto> sortsNew = MpSqlUtils.filtrationSort(sorts, "");
        List<ConditionDto> conditionCons = MpSqlUtils.filtrationCondition(conditions, "");
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(2);
        map.put("wheresa", conditionCons);
        map.put("order", sortsNew);

        Page<OrderEntryInfo> mpage = new Page<>(page.getPageIndex(), page.getPageSize());
        Page<OrderEntryInfo> pdata = ppsEntryDao.gestShopOrders(mpage, map);
        page.setTotal((int) pdata.getTotal());
        page.setDatas(pdata.getRecords());
    }

    /**
     * 设置工单状态
     *
     * */
    public void setStatus(EntryStatusPara para) {
        if (StringUtils.isBlank(para.getEntryNo()) || para.getStatus() == null) {
            throw new InkelinkException("参数错误！");
        }
        if (para.getStatus() != 1 && para.getStatus() != 2) {
            throw new InkelinkException("参数错误！");
        }
        QueryWrapper<PpsEntryEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsEntryEntity::getEntryNo, para.getEntryNo())
                .eq(PpsEntryEntity::getEntryType, 1);
        PpsEntryEntity entry = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (entry == null || (entry.getStatus() != 1 && entry.getStatus() != 2)) {
            throw new InkelinkException("订单状态不允许此操作！");
        }
        if (entry.getStatus().equals(para.getStatus())) {
            return;
        }
        UpdateWrapper<PpsEntryEntity> upSet = new UpdateWrapper<>();
        upSet.lambda().set(PpsEntryEntity::getStatus, para.getStatus())
                .eq(PpsEntryEntity::getId, entry.getId());
        update(upSet);
    }

    /**
     * 车间工单分线监控查询
     *
     * @param shopPlanMonitorinfo 查询参数
     */
    @Override
    public void getShopPlanMonitorInfos(ShopPlanMonitorInfo shopPlanMonitorinfo) {
        HashMap<String, Object> hashMap = Maps.newHashMapWithExpectedSize(7);
        hashMap.put("pageSize", shopPlanMonitorinfo.getPageSize());
        hashMap.put("pageIndex", shopPlanMonitorinfo.getPageIndex());
        hashMap.put("shopCode", shopPlanMonitorinfo.getShopCode());
        hashMap.put("planNo", shopPlanMonitorinfo.getPlanNo());
        hashMap.put("tpsCode", shopPlanMonitorinfo.getTpsCode());
        hashMap.put("status", shopPlanMonitorinfo.getStatus());
        hashMap.put("pageCount", shopPlanMonitorinfo.getPageCount());
        List<Map> list = ppsEntryDao.getShopPlanMonitorInfos(hashMap);
        shopPlanMonitorinfo.setDatas(list);
        Integer total = (Integer) hashMap.get("pageCount");
        shopPlanMonitorinfo.setPageCount(total);
    }

    /**
     * 保存工单状态
     */
    @Override
    public void changeEntryStatus(String objectNo, String aviCode, Date passDt) {
        PpsOrderEntity orderInfo = ppsOrderExtendService.getPpsOrderInfoByKey(objectNo);
        if (orderInfo == null) {
            throw new InkelinkException(objectNo + "无效的产品标识");
        }
//        //判断计划过点履历是否设置该点位为生产开始或生产结束点位
//        List<PpsPlanAviEntity> planAviInfos = ppsPlanAviService.getListByPlanNo(orderInfo.getPlanNo());
//        planAviInfos.sort(Comparator.comparing(PpsPlanAviEntity::getPassDt));
        int aviType = 0;
        if (StringUtils.equals(orderInfo.getStartAvi(), aviCode)) {
            aviType = 23;
        } else if (StringUtils.equals(orderInfo.getEndAvi(), aviCode)) {
            aviType = 24;
        }
        if (aviType == 0 && !getAllProductProcess()
                .stream().anyMatch(o -> o.getPpsProductProcessAviInfos().stream()
                        .anyMatch(p -> StringUtils.equals(p.getAviCode(), aviCode) && p.getAviType() != 0))) {
            return;
        }

//        if (planAviInfos.size() > 0 && StringUtils.equals(planAviInfos.get(0).getAviCode(), aviCode)) {
//            //生产开始
//            aviType = StringUtils.equals(planAviInfos.get(0).getAviCode(), aviCode) ? 4 : 0;
//        } else if (planAviInfos.size() > 0 && StringUtils.equals(planAviInfos.get(planAviInfos.size() - 1).getAviCode(), aviCode)) {
//            //生产结束
//            aviType = StringUtils.equals(planAviInfos.get(planAviInfos.size() - 1).getAviCode(), aviCode) ? 5 : 0;
//        } else {
//            //判断是否是关键点，只有关键点才需要更新状态
//            if (!getAllProductProcess()
//                    .stream().anyMatch(o -> o.getPpsProductProcessAviInfos().stream()
//                            .anyMatch(p -> StringUtils.equals(p.getAviCode(), aviCode) && p.getAviType() != 0))) {
//                return;
//            }
//        }
        PmAllDTO allPm = pmVersionProvider.getObjectedPm();
        PmAviEntity aviInfo = allPm.getAvis().stream().filter(c -> StringUtils.equals(c.getAviCode(), aviCode))
                .findFirst().orElse(null);
        if (aviInfo == null) {
            throw new InkelinkException(aviCode + "无效的AVI站点");
        }
        PmLineEntity lineInfo = allPm.getLines().stream().filter(c -> Objects.equals(c.getId(), aviInfo.getPrcPmLineId()))
                .findFirst().orElse(null);
        PmWorkShopEntity shopInfo = allPm.getShops().stream().filter(c -> Objects.equals(c.getId(), lineInfo.getPrcPmWorkshopId()))
                .findFirst().orElse(null);

        //获取工单
        //备件
        PpsEntryEntity entryInfo = null;
        if (StringUtils.equals(orderInfo.getOrderCategory(), OrderCategoryEnum.SparePart.codeString())) {
            QueryWrapper<PpsEntryEntity> qryEntry = new QueryWrapper<>();
            qryEntry.lambda().eq(PpsEntryEntity::getSn, orderInfo.getSn())
                    .eq(PpsEntryEntity::getEntryType, 2);
            entryInfo = getTopDatas(1, qryEntry).stream().findFirst().orElse(null);
        } else {
            QueryWrapper<PpsEntryEntity> qryEntry = new QueryWrapper<>();
            qryEntry.lambda().eq(PpsEntryEntity::getSn, orderInfo.getSn())
                    .eq(PpsEntryEntity::getEntryType, 1)
                    .eq(PpsEntryEntity::getWorkshopCode, shopInfo.getWorkshopCode());
            entryInfo = getTopDatas(1, qryEntry).stream().findFirst().orElse(null);
        }

//        if (entryInfo == null) {
//            return;
//        }
        //获取工单的工艺路径
        Long processId = orderInfo.getPrcPpsProductProcessId();
        PpsProductProcessEntity process = getAllProductProcess().stream().filter(o -> Objects.equals(o.getId(), processId)).findFirst().orElse(null);
        PpsProductProcessAviEntity aviConfig = process.getPpsProductProcessAviInfos().stream().filter(o -> StringUtils.equals(o.getAviCode(), aviCode))
                .findFirst().orElse(null);
        if (aviConfig != null) {
            aviType = aviConfig.getAviType();
        }
        if (aviType == 0) {
            return;
        }
        PpsOrderEntity order = orderInfo;

        aviConfig.setAviType(aviConfig.getAviType() == 0 ? aviType : aviConfig.getAviType());
        //订单已报废
        if (order.getOrderStatus() == OrderStatusEnum.Scrap.code()) {
            return;
        }
        if (StringUtils.isBlank(order.getBarcode())) {
            throw new InkelinkException("订单:“" + order.getOrderNo() + "”还没有生成VIN号,无法报工");
        }
        List<ConditionDto> conPlan = new ArrayList<>();
        conPlan.add(new ConditionDto("PLAN_NO", orderInfo.getPlanNo(), ConditionOper.Equal));
        PpsPlanEntity plan = ppsPlanExtendService.getTopDatas(1, conPlan, null).stream().findFirst().orElse(null);

        switch (aviType) {
            case 11:
                //入焊装
                order.setOrderStatus(10);
                entryInfo.setActualStartDt(passDt);
                entryInfo.setStatus(20);
                break;
            case 12:
                //出焊装
                order.setOrderStatus(19);
                entryInfo.setActualEndDt(passDt);
                entryInfo.setStatus(30);
                break;
            case 13://入立库（焊）
                order.setOrderStatus(191);
                break;
            case 14://出立库（焊）
                order.setOrderStatus(192);
                break;
            case 15://入涂装
                order.setOrderStatus(20);
                entryInfo.setActualStartDt(passDt);
                entryInfo.setStatus(20);
                break;
            case 16://出涂装
                order.setOrderStatus(29);
                entryInfo.setActualEndDt(passDt);
                entryInfo.setStatus(30);
                break;
            case 17://入立库（涂）
                order.setOrderStatus(291);
                break;
            case 18://出立库（涂）
                order.setOrderStatus(292);
                break;
            case 19://入总装
                order.setOrderStatus(30);
                order.setActualStartDt(passDt);
                entryInfo.setActualStartDt(passDt);
                entryInfo.setStatus(20);
                break;
            case 20://出总装
                order.setOrderStatus(39);
                entryInfo.setActualEndDt(passDt);
                entryInfo.setStatus(30);
                break;
            case 21://入VI
                order.setOrderStatus(40);
                entryInfo.setActualStartDt(passDt);
                entryInfo.setStatus(20);
                break;
            case 22://出VI
                order.setOrderStatus(49);
                entryInfo.setActualEndDt(passDt);
                entryInfo.setStatus(30);
                break;
            case 23://生产开始--电池
                order.setOrderStatus(4);
                entryInfo.setActualStartDt(passDt);
                entryInfo.setStatus(20);
                break;
            case 24://生产完成--电池
                order.setOrderStatus(5);
                order.setActualEndDt(passDt);
                entryInfo.setStatus(30);
                break;
            case 99:
                //入库
                order.setOrderStatus(99);
                break;
            case 990:
                //出库
                order.setOrderStatus(990);
                break;
            default:
                return;
        }
        if (entryInfo != null) {
            UpdateWrapper<PpsEntryEntity> upEntry = new UpdateWrapper<>();
            upEntry.lambda().set(PpsEntryEntity::getStatus, entryInfo.getStatus())
                    .set(PpsEntryEntity::getActualStartDt, entryInfo.getActualStartDt())
                    .set(PpsEntryEntity::getActualEndDt, entryInfo.getActualEndDt())
                    .eq(PpsEntryEntity::getId, entryInfo.getId());
            update(upEntry);
        }

        if(order.getActualStartDt()==null) {
            UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
            upOrder.lambda().set(PpsOrderEntity::getActualStartDt, passDt)
                    .eq(PpsOrderEntity::getId, order.getId());
            ppsOrderExtendService.update(upOrder);
        }
        UpdateWrapper<PpsOrderEntity> upOrderSt = new UpdateWrapper<>();
        upOrderSt.lambda().set(PpsOrderEntity::getOrderStatus, order.getOrderStatus())
               .eq(PpsOrderEntity::getId, order.getId());
        ppsOrderExtendService.update(upOrderSt);

        //更新生产计划的状态(开始）
        if ((order.getOrderStatus() == OrderStatusEnum.InBodyShop.code() || order.getOrderStatus() == OrderStatusEnum.ProduceStart.code()) && plan.getPlanStatus() != PlanStatusEnum.ProduceStart.code()) {
            UpdateWrapper<PpsPlanEntity> upPlan = new UpdateWrapper<>();
            upPlan.lambda().set(PpsPlanEntity::getPlanStatus, PlanStatusEnum.ProduceStart.code())
                    .eq(PpsPlanEntity::getId, plan.getId());
            ppsPlanExtendService.update(upPlan);
        }
        //更新生产计划的状态(完成）---整车
        if (order.getOrderStatus() == OrderStatusEnum.InWarehouse.code()) {
            //获取该计划下面所有没有完成的订单
            QueryWrapper<PpsOrderEntity> qryOrder = new QueryWrapper<>();
            qryOrder.lambda().eq(PpsOrderEntity::getPlanNo, plan.getPlanNo())
                    .ne(PpsOrderEntity::getOrderStatus, OrderStatusEnum.InWarehouse.code())
                    .ne(PpsOrderEntity::getOrderNo, order.getOrderNo())
                    .ne(PpsOrderEntity::getOrderStatus, OrderStatusEnum.Scrap.code());
            if (ppsOrderExtendService.selectCount(qryOrder) == 0) {
                UpdateWrapper<PpsPlanEntity> upPlan = new UpdateWrapper<>();
                upPlan.lambda().set(PpsPlanEntity::getPlanStatus, PlanStatusEnum.ProduceEnd.code())
                        .eq(PpsPlanEntity::getId, plan.getId());
                ppsPlanExtendService.update(upPlan);
                if(order.getActualEndDt()==null) {
                    UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
                    upOrder.lambda().set(PpsOrderEntity::getActualEndDt, passDt)
                            .eq(PpsOrderEntity::getId, order.getId());
                    ppsOrderExtendService.update(upOrder);
                }
            }
        }
        //更新生产计划的状态(完成）---电池
        if (order.getOrderStatus() == OrderStatusEnum.ProduceOk.code()) {
            //获取该计划下面所有没有完成的订单
            QueryWrapper<PpsOrderEntity> qryOrder = new QueryWrapper<>();
            qryOrder.lambda().eq(PpsOrderEntity::getPlanNo, plan.getPlanNo())
                    .ne(PpsOrderEntity::getOrderStatus, OrderStatusEnum.ProduceOk.code())
                    .ne(PpsOrderEntity::getOrderStatus, OrderStatusEnum.Scrap.code())
                    .ne(PpsOrderEntity::getOrderNo, order.getOrderNo());
            if (ppsOrderExtendService.selectCount(qryOrder) == 0) {
                UpdateWrapper<PpsPlanEntity> upPlan = new UpdateWrapper<>();
                upPlan.lambda().set(PpsPlanEntity::getPlanStatus, PlanStatusEnum.ProduceEnd.code())
                        .eq(PpsPlanEntity::getId, plan.getId());
                ppsPlanExtendService.update(upPlan);
                if(order.getActualEndDt()==null) {
                    UpdateWrapper<PpsOrderEntity> upOrder = new UpdateWrapper<>();
                    upOrder.lambda().set(PpsOrderEntity::getActualEndDt, passDt)
                            .eq(PpsOrderEntity::getId, order.getId());
                    ppsOrderExtendService.update(upOrder);
                }
            }
        }
    }


    /**
     * 重置工单队列
     *
     * @param para 参数实体
     */
    @Override
    public void restEntryQueue(RestEntryQueuePara para) {
        PpsOrderEntity order = ppsOrderExtendService.getPpsOrderBySnOrBarcode(para.getSn());
        if (order != null) {
            para.setSn(order.getSn());
        }
        PpsEntryEntity entry = getEntityBySubScriubCodeAndSn(para.getSubScriubCode(), para.getSn());
        if (entry != null) {
            List<String> models = new ArrayList<>();
            if (!StringUtils.isBlank(para.getModel())) {
                models = ArraysUtils.splitNoEmpty(para.getModel(), "[\\||\\,|\\;]");
            }
            //var lessQuery = this.Table.Where(s => s.SubScriubCode == para.SubScriubCode && s.EntryType == 2 && s.Status >= 2
            // && s.DisplayNo < entry.DisplayNo && s.OrderCategory== entry.OrderCategory);
            QueryWrapper<PpsEntryEntity> qryLess = new QueryWrapper<>();
            LambdaQueryWrapper<PpsEntryEntity> lessQuery = qryLess.lambda();
            lessQuery.eq(PpsEntryEntity::getSubscriubeCode, para.getSubScriubCode())
                    .eq(PpsEntryEntity::getEntryType, 2)
                    .ge(PpsEntryEntity::getStatus, 2)
                    .lt(PpsEntryEntity::getDisplayNo, entry.getDisplayNo())
                    .eq(PpsEntryEntity::getOrderCategory, entry.getOrderCategory());
            if (models.size() > 0) {
                lessQuery.in(PpsEntryEntity::getModel, models);
            }
            lessQuery.select(PpsEntryEntity::getId);
            List<Long> lessThanIds = selectList(qryLess).stream().map(PpsEntryEntity::getId).collect(Collectors.toList());
            if (lessThanIds.size() > 0) {
                UpdateWrapper<PpsEntryEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<PpsEntryEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.in(PpsEntryEntity::getId, lessThanIds);
                lambdaUpdateWrapper.set(PpsEntryEntity::getStatus, 30);
                this.update(updateWrapper);
            }

            //this.Table.Where(s => s.SubScriubCode == para.SubScriubCode && s.EntryType == 2
            // && s.Status >= 2 && s.Status <= 30 && s.DisplayNo >= entry.DisplayNo && s.OrderCategory == entry.OrderCategory);
            QueryWrapper<PpsEntryEntity> qryGreater = new QueryWrapper<>();
            LambdaQueryWrapper<PpsEntryEntity> greaterQuery = qryGreater.lambda();
            greaterQuery.eq(PpsEntryEntity::getSubscriubeCode, para.getSubScriubCode())
                    .eq(PpsEntryEntity::getEntryType, 2)
                    .ge(PpsEntryEntity::getStatus, 2)
                    .le(PpsEntryEntity::getStatus, 30)
                    .ge(PpsEntryEntity::getDisplayNo, entry.getDisplayNo())
                    .eq(PpsEntryEntity::getOrderCategory, entry.getOrderCategory());
            if (models.size() > 0) {
                greaterQuery.in(PpsEntryEntity::getModel, models);
            }
            greaterQuery.select(PpsEntryEntity::getId);
            List<Long> greaterThanIds = selectList(qryGreater).stream().map(PpsEntryEntity::getId).collect(Collectors.toList());
            if (greaterThanIds.size() > 0) {
                UpdateWrapper<PpsEntryEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<PpsEntryEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.in(PpsEntryEntity::getId, greaterThanIds);
                lambdaUpdateWrapper.set(PpsEntryEntity::getStatus, 2);
                this.update(updateWrapper);
            }
        }
    }


    /**
     * 获取已下发未打印焊装上线列表
     *
     * @return 获取一个列表
     */
    @Override
    public List<BodyVehicleDTO> getNoPrintTpscode() {
        return ppsEntryDao.getNoPrintTpscode("WE", 0, 1, 3);
    }

    /**
     * 获取已下发已打印焊装上线列表
     *
     * @return 获取一个列表
     */
    @Override
    public List<BodyVehicleDTO> getPrintTpscode() {
        return ppsEntryDao.getNoPrintTpscode("WE", 1, 1, 20);
    }

    /**
     * 读取已经下发队列
     *
     * @return 获取一个列表
     */
    @Override
    public List<BodyVehicleDTO> getDownTpsCode() {
        return ppsEntryDao.getNoPrintTpscode("WE", 0,1, 3);
    }

    /**
     * 设置TPS码为已打印
     *
     * @param tpsCode  参数tps编码
     * @param shopCode 参数车间编码
     */
    @Override
    public void setPrintTpsCode(String tpsCode, String shopCode) {
        LambdaUpdateWrapper<PpsEntryEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PpsEntryEntity::getIsDispose, true)
                .eq(PpsEntryEntity::getWorkshopCode, shopCode)
                .eq(PpsEntryEntity::getEntryType, 1)
                .eq(PpsEntryEntity::getSn, tpsCode);
        update(updateWrapper);
    }


    /**
     * 设置车辆为上线
     *
     * @param tpsCode  参数tps编码
     * @param shopCode 车间code
     */
    @Override
    public void setBodyEntryOnline(String tpsCode, String shopCode) {
        LambdaUpdateWrapper<PpsEntryEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PpsEntryEntity::getStatus, 3)
                .eq(PpsEntryEntity::getWorkshopCode, shopCode)
                .eq(PpsEntryEntity::getEntryType, 1)
                .eq(PpsEntryEntity::getSn, tpsCode);
        update(updateWrapper);
    }


    /**
     * 根据订单号跟新状态
     *
     * @param orderNo 订单编号
     * @param status  状态
     */
    @Override
    public void updateStatusByOrderNo(String orderNo, Integer status) {
        UpdateWrapper<PpsEntryEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsEntryEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.eq(PpsEntryEntity::getOrderNo, orderNo);
        lambdaUpdateWrapper.set(PpsEntryEntity::getStatus, status);
        this.update(updateWrapper);
    }

    private void checkOrderBeforeLock(List<Long> entryIds, String shopCode) {
        QueryWrapper<PpsEntryEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(PpsEntryEntity::getId, entryIds);
        lambdaQueryWrapper.eq(PpsEntryEntity::getWorkshopCode, shopCode);
        lambdaQueryWrapper.eq(PpsEntryEntity::getEntryType, 1);
        List<PpsEntryEntity> list = this.selectList(queryWrapper);
        for (PpsEntryEntity item : list) {
            if (item.getStatus() != 1) {
                throw new InkelinkException("生产订单号：" + item.getOrderNo() + ",状态不正确，不能锁定");
            }
        }
    }



    /**
     * 根据订单编号&工单类型 获取实体
     *
     * @param orderNo   订单编号
     * @param entryType 工单类型
     * @return 实体
     */
    @Override
    public List<PpsEntryEntity> getPpsEntrysByOrderNo(String orderNo, Integer entryType) {
        QueryWrapper<PpsEntryEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryEntity::getOrderNo, orderNo);
        lambdaQueryWrapper.eq(PpsEntryEntity::getEntryType, entryType);
        return this.selectList(queryWrapper);
    }

    /**
     * 根据订单编号&工单类型 获取实体
     *
     * @param orderNo   订单编号
     * @param entryType 工单类型
     * @return 实体
     */
    @Override
    public PpsEntryEntity getPpsEntryEntityByOrderNo(String orderNo, Integer entryType) {
        QueryWrapper<PpsEntryEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryEntity::getOrderNo, orderNo);
        lambdaQueryWrapper.eq(PpsEntryEntity::getEntryType, entryType);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 根据订单编号获取实体
     *
     * @param orderId   订单编号
     * @return 实体
     */
    @Override
    public PpsEntryEntity getFirstByOrderId(Long orderId) {
        QueryWrapper<PpsEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PpsEntryEntity::getPrcPpsOrderId, orderId);
        return this.selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PpsEntryEntity getFirstByOrderShopCode(Long orderId,String shopCode) {
        QueryWrapper<PpsEntryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PpsEntryEntity::getPrcPpsOrderId, orderId)
                .eq(PpsEntryEntity::getWorkshopCode,shopCode);
        return this.selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 根据订阅码&sn查询
     *
     * @param subScriubCode
     * @param sn
     * @return 实体
     */
    public PpsEntryEntity getEntityBySubScriubCodeAndSn(String subScriubCode, String sn) {
        QueryWrapper<PpsEntryEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsEntryEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsEntryEntity::getSubscriubeCode, subScriubCode);
        lambdaQueryWrapper.eq(PpsEntryEntity::getSn, sn);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PpsEntryEntity getFirstEntryTypeShopCodeSn(String sn, Integer entryType, String shopCode) {
        QueryWrapper<PpsEntryEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsEntryEntity::getSn, sn)
                .eq(PpsEntryEntity::getEntryType, entryType)
                .eq(PpsEntryEntity::getWorkshopCode, shopCode);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }


    /**
     * 重新生成工艺
     *
     * @param entryId entryId
     */
    @Override
    public void resetWo(Long entryId) {
        if (entryId == null || entryId <= 0) {
            return;
        }
        UpdateWrapper<PpsEntryEntity> wrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsEntryEntity> lambdaUpdateWrapper = wrapper.lambda();
        lambdaUpdateWrapper.eq(PpsEntryEntity::getId, entryId);
        lambdaUpdateWrapper.set(PpsEntryEntity::getIsCreateWo, false);
        this.update(wrapper);
    }

    /**
     * 更改预计上线时间
     *
     * @param estimatedStartDt 时间
     * @param ids              更新的ID集合
     */
    @Override
    public void changeEstimatedStartDt(Date estimatedStartDt, List<Long> ids) {
        UpdateWrapper<PpsEntryEntity> wrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsEntryEntity> lambdaUpdateWrapper = wrapper.lambda();
        lambdaUpdateWrapper.in(PpsEntryEntity::getId, ids);
        lambdaUpdateWrapper.set(PpsEntryEntity::getEstimatedStartDt, estimatedStartDt);
        this.update(wrapper);
    }

    /**
     * 更改预计下线时间
     *
     * @param estimatedEndDt 时间
     * @param ids            更新的ID集合
     */
    @Override
    public void changeEstimatedEndDt(Date estimatedEndDt, List<Long> ids) {
        UpdateWrapper<PpsEntryEntity> wrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PpsEntryEntity> lambdaUpdateWrapper = wrapper.lambda();
        lambdaUpdateWrapper.in(PpsEntryEntity::getId, ids);
        lambdaUpdateWrapper.set(PpsEntryEntity::getEstimatedEndDt, estimatedEndDt);
        this.update(wrapper);
    }

}