package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQConstants;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.provider.RabbitMqSysQueueNoteProvider;
import com.ca.mfd.prc.pps.communication.dto.LmsLockPlanDto;
import com.ca.mfd.prc.pps.communication.entity.MidAsBathPlanEntity;
import com.ca.mfd.prc.pps.communication.service.IMidAsBathPlanExtendService;
import com.ca.mfd.prc.pps.communication.service.IMidAsBathPlanService;
import com.ca.mfd.prc.pps.dto.LmsPartPlanDTO;
import com.ca.mfd.prc.pps.dto.ModuleOrderListInfo;
import com.ca.mfd.prc.pps.dto.ModuleSplitDataInfo;
import com.ca.mfd.prc.pps.dto.PlanPartsSplitEntryReckonInfo;
import com.ca.mfd.prc.pps.dto.PlanPartsSplitEntryReckonPara;
import com.ca.mfd.prc.pps.dto.ProcessArea;
import com.ca.mfd.prc.pps.dto.ProcessRelationInfo;
import com.ca.mfd.prc.pps.dto.RecieveCharacteristicsDataRequest;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueModuleEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsProcessRelationEntity;
import com.ca.mfd.prc.pps.mapper.IPpsPlanPartsMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductBomVersionsProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductMaterialMasterProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsEntryPartsService;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueModuleService;
import com.ca.mfd.prc.pps.service.IPpsPlanPartsService;
import com.ca.mfd.prc.pps.service.IPpsProcessRelationService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 生产计划-零部件服务实现
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PpsPlanPartsServiceImpl extends AbstractCrudServiceImpl<IPpsPlanPartsMapper, PpsPlanPartsEntity> implements IPpsPlanPartsService {
    private static final Logger logger = LoggerFactory.getLogger(PpsPlanPartsServiceImpl.class);

    @Autowired
    private PmProductBomVersionsProvider pmProductBomVersionsProvider;

    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Autowired
    private IPpsEntryPartsService ppsEntryPartsService;

    @Autowired
    private IPpsModuleIssueModuleService ppsModuleIssueModuleService;

    @Autowired
    private IPpsProcessRelationService ppsProcessRelationService;

    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private PmProductMaterialMasterProvider pmProductMaterialMasterProvider;

    @Autowired
    private RabbitMqSysQueueNoteProvider sysQueueNoteService;

    @Override
    public PpsPlanPartsEntity getFirstByPlanNo(String planNo) {
        QueryWrapper<PpsPlanPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanPartsEntity::getPlanNo, planNo);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }


    /**
     * 发送Lms批次锁定计划(预成组)
     *
     * @param ppsPlanIds
     */
    @Override
    public void sendLmsModuleLockPlan(List<Long> ppsPlanIds) {
        QueryWrapper<PpsPlanPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PpsPlanPartsEntity::getId, ppsPlanIds);
        List<PpsPlanPartsEntity> items = selectList(qry);
        for (PpsPlanPartsEntity item : items) {
            sendLmsBatchLockPlan(item);
        }
    }

    /**
     * 发送Lms批次锁定计划(批次)
     *
     * @param request
     */
    @Override
    public void sendLmsPartsLockPlan(List<PlanPartsSplitEntryReckonInfo> request) {
        if (request.isEmpty()) {
            return;
        }
        //获取订单信息
        PpsPlanPartsEntity item = getFirstByPlanNo(request.get(0).getPlanNo());
        sendLmsBatchLockPlan(item);
    }

    /**
     * 发送Lms批次锁定计划
     *
     * @param item
     */
    private void sendLmsBatchLockPlan(PpsPlanPartsEntity item) {
        SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
        sysQueueNoteEntity.setGroupName(RabbitMQConstants.GROUP_NAME_LMS_LOCKPLAN_QUEUE);
        LmsLockPlanDto info = getLmsLockPlanDto(item);
        sysQueueNoteEntity.setContent(JsonUtils.toJsonString(info));
        sysQueueNoteService.addSimpleMessage(sysQueueNoteEntity);
    }

    private LmsLockPlanDto getLmsLockPlanDto(PpsPlanPartsEntity item){
        LmsLockPlanDto info = new LmsLockPlanDto();
        String aviCode = item.getStartAvi();
        info.setAviCode(aviCode);
        info.setVin("");
        info.setProductType("2");
        info.setProductCode(item.getProductCode());
        info.setOneId("");
        info.setManager("");
        info.setPassTime(new Date());
        info.setPlanNo(item.getPlanNo());
        info.setUniqueCode(item.getId());
        info.setOrderCategory(String.valueOf(item.getOrderCategory()));
        return info;
    }

    /**
     * 根据顺序获取还未拆分完的订单（生产未完成,未冻结）
     *
     * @param orderCategory
     * @return
     */
    @Override
    public List<PpsPlanPartsEntity> getListNoCompele(Integer orderCategory) {
        QueryWrapper<PpsPlanPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanPartsEntity::getOrderCategory, orderCategory)
                .ne(PpsPlanPartsEntity::getPlanStatus, 0)
                .lt(PpsPlanPartsEntity::getPlanStatus, 20)
                .eq(PpsPlanPartsEntity::getIsFreeze, false)
                .orderByAsc(PpsPlanPartsEntity::getEstimatedStartDt);
        return selectList(qry);
    }


    /**
     * 计划冻结
     *
     * @param ppsPlanIds 主键集合
     */
    @Override
    public void freeze(List<Long> ppsPlanIds) {
        if (ppsPlanIds.isEmpty()) {
            return;
        }
        for (Long planId : ppsPlanIds) {
            PpsPlanPartsEntity ppsPlanPartsInfo = this.get(planId);
            if (ppsPlanPartsInfo == null) {
                return;
            }
            if (ppsPlanPartsInfo.getPlanStatus() == 20) {
                throw new InkelinkException("该计划状态不正确，不能冻结！");
            }
            if (ppsPlanPartsInfo.getIsFreeze()) {
                throw new InkelinkException(ppsPlanPartsInfo.getPlanNo() + "该计划编号已冻结,不需冻结！");
            }
            updatePpsPlanParts(planId, true);
        }
    }

    /**
     * 取消计划冻结
     *
     * @param ppsPlanIds 主键集合
     */
    @Override
    public void unFreeze(List<Long> ppsPlanIds) {
        if (ppsPlanIds.isEmpty()) {
            return;
        }
        for (Long planId : ppsPlanIds) {
            PpsPlanPartsEntity ppsPlanPartsInfo = this.get(planId);
            if (ppsPlanPartsInfo == null) {
                return;
            }
            if (!ppsPlanPartsInfo.getIsFreeze()) {
                throw new InkelinkException("'" + ppsPlanPartsInfo.getPlanNo() + "'该计划编号未冻结,不需解冻.");
            }
            updatePpsPlanParts(planId, false);
        }
    }

    /**
     * 获取计划的bom数据
     *
     * @param planNo 计划号
     * @return BOM数据集合
     */
    @Override
    public List<PmProductBomEntity> getPlanBom(String planNo) {
        PpsPlanPartsEntity planInfo = getFirstByPlanNo(planNo);
        if (planInfo == null) {
            throw new InkelinkException("无效的计划");
        }
        String materialNo = planInfo.getProductCode();
        return pmProductBomVersionsProvider.getBomData(materialNo, planInfo.getBomVersion());
    }

    /**
     * 获取预成组生产订单列表
     *
     * @param workstationCode 工位编码
     * @return 预成组生产订单列表
     */
    @Override
    public List<ModuleOrderListInfo> getModuleOrderList(String workstationCode) {
        List<ModuleOrderListInfo> datas = new ArrayList<>();
        //获取工位对象
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        PmWorkStationEntity workStationInfo =
                pmall.getStations().stream().filter(s -> StringUtils.equalsIgnoreCase(s.getWorkstationCode(), workstationCode)).findFirst().orElse(null);
        if (workStationInfo != null) {
            //获取线体对象
            PmLineEntity lineInfo = pmall.getLines()
                    .stream().filter(s -> Objects.equals(s.getId(), workStationInfo.getPrcPmLineId())).findFirst().orElse(null);
            //获取未完成的模组订单
            List<PpsPlanPartsEntity> orderInfos = getRecordListByOrderCategory(8, 10);
            for (PpsPlanPartsEntity item : orderInfos) {
                int finishQty = ppsEntryPartsService.getEntryPartNumByPlanNo(item.getPlanNo());
                PpsModuleIssueModuleEntity moduleEntity = ppsModuleIssueModuleService.getRecordInfoByPlanNo(item.getPlanNo(), lineInfo.getLineCode());
                if (moduleEntity != null) {
                    ModuleOrderListInfo orderListInfo = new ModuleOrderListInfo();
                    orderListInfo.setPlanNo(item.getPlanNo());
                    orderListInfo.setPackModel(item.getModel());
                    orderListInfo.setPackDes(item.getCharacteristic4());
                    orderListInfo.setPlanQty(item.getPlanQty());
                    orderListInfo.setSplitQty(item.getLockQty());
                    orderListInfo.setFinishQty(finishQty);
                    orderListInfo.setEstimatedStartDt(item.getEstimatedStartDt());
                    orderListInfo.setEstimatedEndDt(item.getEstimatedEndDt());
                    datas.add(orderListInfo);
                }
            }
        }
        return datas;
    }

    /**
     * 锁定模组计划
     *
     * @param ppsPlanIds
     * @return
     */
    @Override
    public void lockModulePlan(List<Long> ppsPlanIds) {
        UpdateWrapper<PpsPlanPartsEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(PpsPlanPartsEntity::getPlanStatus, 1)
                .in(PpsPlanPartsEntity::getId, ppsPlanIds)
                .eq(PpsPlanPartsEntity::getPlanStatus, 0);
        update(upset);
    }

    /**
     * 获取拆分数据描述
     *
     * @param planNo 锁定计划
     * @return 计划拆分结果模型
     */
    @Override
    public ModuleSplitDataInfo getModuleSplitData(String planNo) {
        PpsPlanPartsEntity planInfo = getFirstByPlanNo(planNo);
        SysConfigurationEntity numConfig = sysConfigurationProvider.getSysConfigurations("SplitModuleNum").stream().findFirst().orElse(null);
        if (numConfig == null) {
            throw new InkelinkException("未配置模组拆分数量");
        }
        ModuleSplitDataInfo et = new ModuleSplitDataInfo();
        et.setSplitNum(Integer.parseInt(numConfig.getValue()));
        et.setDifferenceNum(planInfo.getPlanQty() - planInfo.getLockQty());
        return et;
    }

    /**
     * 获取未拆分计划
     *
     * @param pageIndex
     * @param pageSize
     * @return 计划
     */
    @Override
    public IPage<PpsPlanPartsEntity> getNoLockWith(int pageIndex, int pageSize, Integer orderCategory) {
        QueryWrapper<PpsPlanPartsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPlanPartsEntity::getPlanStatus, 1)
                .isNotNull(PpsPlanPartsEntity::getCharacteristic1)
                .ne(PpsPlanPartsEntity::getCharacteristic1, "")
                .eq(PpsPlanPartsEntity::getOrderCategory, orderCategory)
                .orderByAsc(PpsPlanPartsEntity::getEstimatedStartDt);
        return getDataByPage(qry, pageIndex, pageSize);
    }

    /**
     * 计划自动拆分
     *
     * @param plan 锁定计划参数模型
     * @return 计划拆分结果模型
     */
    @Override
    public void planPartsAutoLock(PpsPlanPartsEntity plan) {
        List<PpsProcessRelationEntity> processRelations = new ArrayList<>();
        if (ConvertUtils.stringToLong(plan.getCharacteristic1()) > 0) {
            PpsProcessRelationEntity pro = ppsProcessRelationService.get(ConvertUtils.stringToLong(plan.getCharacteristic1()));
            if (pro != null) {
                processRelations.add(pro);
            }
        }
        if (ConvertUtils.stringToLong(plan.getCharacteristic2()) > 0) {
            PpsProcessRelationEntity pro = ppsProcessRelationService.get(ConvertUtils.stringToLong(plan.getCharacteristic2()));
            if (pro != null) {
                processRelations.add(pro);
            }
        }
        if (processRelations.isEmpty()) {
            plan.setErrorMes("计划自动锁定失败:没有找到工序配置");
            return;
        } else {
            plan.setErrorMes("");
        }
        List<PlanPartsSplitEntryReckonInfo> processInfo = new ArrayList<>();
        List<ProcessRelationInfo> areaConfig = ppsProcessRelationService.getRecordByOrderCategory(plan.getOrderCategory());
        if (areaConfig.isEmpty()) {
            throw new InkelinkException("未配置关联区域，请检查！");
        }
        Integer lockCount = plan.getPlanQty();
        for (PpsProcessRelationEntity process : processRelations) {
            PlanPartsSplitEntryReckonInfo processSplitInfo = new PlanPartsSplitEntryReckonInfo();
            processSplitInfo.setProcessCode(process.getProcessCode());
            processSplitInfo.setProcessName(process.getProcessName());
            processSplitInfo.setLockCount(lockCount);
            processSplitInfo.setPlanNo(plan.getPlanNo());

            //生产区域数据
            List<ProcessArea> processAreaList = new ArrayList<>();
            ProcessArea info = new ProcessArea();
            info.setStartTime(plan.getEstimatedStartDt());
            info.setEndTime(plan.getEstimatedEndDt());
            info.setLineCode(process.getLineCode());
            info.setLineName(process.getLineName());
            info.setProcessType(process.getProcessType());
            info.setCount(0);
            processAreaList.add(info);

            processSplitInfo.setProcessAreas(processAreaList);
            processSplitInfo.getProcessAreas().get(0).setCount(lockCount);
            processInfo.add(processSplitInfo);
        }
        planPartsLockEntryReckon(processInfo);
        UpdateWrapper<PpsPlanPartsEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(PpsPlanPartsEntity::getErrorMes, plan.getErrorMes())
                .eq(PpsPlanPartsEntity::getId, plan.getId());
        update(updateWrapper);

        //sendLmsModuleLockPlan();
    }

    @Autowired
    private IMidAsBathPlanExtendService midAsBathPlanExtendService;

    /**
     * 计划拆分结果模型
     *
     * @param request 锁定计划参数模型
     * @return 计划拆分结果模型
     */
    @Override
    public List<PlanPartsSplitEntryReckonInfo> planPartsSplitEntryReckon(PlanPartsSplitEntryReckonPara request) {
        List<ProcessRelationInfo> areaConfigAll = ppsProcessRelationService.getRecordByOrderCategory(request.getCategory());
        if (areaConfigAll.isEmpty()) {
            throw new InkelinkException("未配置关联区域，请检查！");
        }
        areaConfigAll = areaConfigAll.stream().filter(c -> c.getProcessType() == 1).collect(Collectors.toList());
        if (areaConfigAll.isEmpty()) {
            throw new InkelinkException("未配置关联区域，请检查！");
        }
        if (request.getLockCount() <= 0) {
            throw new InkelinkException("数量必须大于0！");
        }

        List<ProcessRelationInfo> areaConfig = areaConfigAll;
        PpsPlanPartsEntity planInfo = getFirstByPlanNo(request.getPlanNo());
        ProcessRelationInfo firstProcess = null;
        if (StringUtils.isNotBlank(planInfo.getCharacteristic1())) {
            firstProcess = areaConfigAll.stream().filter(c -> Objects.equals(c.getId(),
                            ConvertUtils.stringToLong(planInfo.getCharacteristic1())))
                    .findFirst().orElse(null);
            if (firstProcess != null) {
                areaConfig = new ArrayList<>();
                areaConfig.add(firstProcess);
                List<MidAsBathPlanEntity> bathPlans = midAsBathPlanExtendService.getByPlanNos(Arrays.asList(request.getPlanNo()));
                if (bathPlans != null && !bathPlans.isEmpty()) {
                    List<String> tpCodes = bathPlans.stream().map(MidAsBathPlanEntity::getWsCode).distinct().collect(Collectors.toList());
                    List<ProcessRelationInfo> sconfigs = areaConfigAll.stream().filter(c -> tpCodes.stream().anyMatch(s -> StringUtils.equalsIgnoreCase(s, c.getProcessCode())))
                            .collect(Collectors.toList());
                    for (ProcessRelationInfo cfg : sconfigs) {
                        if (!Objects.equals(firstProcess.getId(), cfg.getId())) {
                            areaConfig.add(cfg);
                        }
                    }
                }
               /* for (ProcessRelationInfo cfg : areaConfigAll) {
                    if (!Objects.equals(firstProcess.getId(), cfg.getId())) {
                        areaConfig.add(cfg);
                    }
                }*/
            }
        }
        if (areaConfig.isEmpty()) {
            throw new InkelinkException("未找到首工序配置！");
        }

        //验证计划状态
        verifyPlanInfo(request.getLockCount(), planInfo);
        List<PlanPartsSplitEntryReckonInfo> processInfo = new ArrayList<>();
        Map<String, List<ProcessRelationInfo>> sortedMap = new LinkedHashMap<>();
        Map<String, List<ProcessRelationInfo>> map = areaConfig.stream().
                collect(Collectors.groupingBy(ProcessRelationInfo::getProcessCode));
        map.entrySet().stream().sorted(Map.Entry.<String, List<ProcessRelationInfo>>comparingByKey())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        for (Map.Entry<String, List<ProcessRelationInfo>> process : sortedMap.entrySet()) {
            PlanPartsSplitEntryReckonInfo processSplitInfo = new PlanPartsSplitEntryReckonInfo();
            processSplitInfo.setProcessCode(process.getKey());
            processSplitInfo.setProcessName(Objects.requireNonNull(process.getValue().stream().findFirst().orElse(null)).getProcessName());
            processSplitInfo.setLockCount(request.getLockCount());
            processSplitInfo.setPlanNo(request.getPlanNo());
            List<ProcessArea> processAreaList;
            List<ProcessRelationInfo> list = process.getValue().stream().
                    //filter(c->c.getProcessType()==1).
                            sorted(Comparator.comparing(ProcessRelationInfo::getLineCode).reversed())
                    .collect(Collectors.toList());
            processAreaList = list.stream().map(s -> {
                ProcessArea info = new ProcessArea();
                info.setStartTime(planInfo.getEstimatedStartDt());
                info.setEndTime(planInfo.getEstimatedEndDt());
                info.setLineCode(s.getLineCode());
                info.setLineName(s.getLineName());
                info.setProcessType(s.getProcessType());
                info.setCount(0);
                return info;
            }).collect(Collectors.toList());
            processSplitInfo.setProcessAreas(processAreaList);
            processSplitInfo.getProcessAreas().get(0).setCount(request.getLockCount());
            processInfo.add(processSplitInfo);
        }
        return processInfo;
    }


    /**
     * 计划拆分工单
     *
     * @param request
     */
    @Override
    public void planPartsLockEntryReckon(List<PlanPartsSplitEntryReckonInfo> request) {
        if (request.isEmpty()) {
            return;
        }
        //获取订单信息
        PpsPlanPartsEntity ppsPlanPartsInfo = getFirstByPlanNo(request.get(0).getPlanNo());
        if (ppsPlanPartsInfo.getIsFreeze()) {
            throw new InkelinkException("计划已被冻结，请检查！");
        }
        List<ProcessRelationInfo> lineConfig = ppsProcessRelationService.getRecordByOrderCategory(ppsPlanPartsInfo.getOrderCategory());
        if (lineConfig.isEmpty()) {
            throw new InkelinkException("未配置关联区域，请检查！");
        }
        lineConfig = lineConfig.stream().filter(c -> c.getProcessType() == 1).collect(Collectors.toList());
        if (lineConfig.isEmpty()) {
            throw new InkelinkException("未配置关联区域，请检查！");
        }
        for (PlanPartsSplitEntryReckonInfo items : request) {
            if (items.getProcessAreas().stream().anyMatch(s -> s.getCount() < 0)) {
                throw new InkelinkException("拆分数量输入不合法，请检查！");
            }
            int sumNumber = items.getProcessAreas().stream().mapToInt(ProcessArea::getCount).sum();
            if (sumNumber != items.getLockCount()) {
                throw new InkelinkException("拆分数量不正确，请检查！");
            }
        }
        List<PpsEntryPartsEntity> ppsEntryInfos = new ArrayList<>();
        //获取唯一码KEY
        String snKey = "ppsEntrySn";
        switch (ppsPlanPartsInfo.getOrderCategory()) {
            //压铸
            case 3:
                snKey = "ppsEntryParts_Cast";
                break;
            case 4:
                snKey = "ppsEntryParts_Machining";
                break;
            case 5:
                snKey = "ppsEntryParts_Stamping";
                break;
            case 6:
                snKey = "ppsEntryParts_CoverPlate";
                break;
            default:
                break;
        }
        for (PlanPartsSplitEntryReckonInfo items : request) {
            List<ProcessArea> processAreaList = items.getProcessAreas().stream().filter(s -> s.getCount() > 0).collect(Collectors.toList());
            String finalSnKey = snKey;
            List<ProcessRelationInfo> finalLineConfig = lineConfig;
            List<PpsEntryPartsEntity> addRange = processAreaList.stream().map(s -> {
                Map<String, String> paraMap = new HashMap<>();
                paraMap.put("PlanNo", ppsPlanPartsInfo.getPlanNo());
                paraMap.put("LineCode", s.getLineCode());
                paraMap.put("ProcessNo", items.getProcessCode());
                String entryNo = sysSnConfigProvider.createSnBypara(finalSnKey, paraMap);
                PpsEntryPartsEntity info = new PpsEntryPartsEntity();
                info.setPlanNo(ppsPlanPartsInfo.getPlanNo());
                info.setEntryNo(entryNo);
                info.setProcessCode(items.getProcessCode());
                info.setProcessName(items.getProcessName());
                info.setOrderCategory(ppsPlanPartsInfo.getOrderCategory());
                info.setEstimatedStartDt(s.getStartTime());
                info.setEstimatedEndDt(s.getEndTime());
                ProcessRelationInfo lincfg = finalLineConfig.stream().filter(line -> StringUtils.equals(line.getLineCode(), s.getLineCode())).findFirst().orElse(null);
                if (lincfg == null) {
                    throw new InkelinkException("未找到线体区域[" + s.getLineCode() + "]的配置信息:");
                }
                ProcessRelationInfo codeCfg = finalLineConfig.stream().filter(line -> StringUtils.equals(line.getProcessCode(), items.getProcessCode())
                        && StringUtils.equals(line.getLineCode(), s.getLineCode())
                        && line.getProcessType() == s.getProcessType()).findFirst().orElse(null);
                if (codeCfg == null) {
                    throw new InkelinkException("未找到线体区域[" + s.getLineCode() + "],工序编码["
                            + items.getProcessCode() + "]类型[" + s.getProcessType() + "]的配置信息:");
                }
                info.setAttribute1(codeCfg.getAttribute1());
                info.setWorkshopCode(lincfg.getShopCode());
                info.setLineCode(s.getLineCode());
                info.setLineName(s.getLineName());
                info.setModel(ppsPlanPartsInfo.getModel());
                info.setPlanQuantity(s.getCount());
                info.setMaterialNo(ppsPlanPartsInfo.getMaterialNo());
                info.setMaterialCn(ppsPlanPartsInfo.getMaterialCn());
                info.setStatus(1);
                //3：压铸  4：机加   5：冲压  6：电池上盖
                info.setStatus(3);
               /* if (ppsPlanPartsInfo.getOrderCategory() == 5) {
                    info.setStatus(10);
                } else if (ppsPlanPartsInfo.getOrderCategory() == 3) {
                    info.setStatus(3);
                }*/
                //系统配置：stmold 通过零件号 获取text值
                info.setAttribute3(ppsPlanPartsInfo.getCharacteristic3());
                // 冲压 10
                // 压铸 3
                return info;
            }).collect(Collectors.toList());
            ppsEntryInfos.addAll(addRange);
        }
        ppsEntryPartsService.insertBatch(ppsEntryInfos);

        if (ppsPlanPartsInfo.getPlanStatus() == 1) {
            UpdateWrapper<PpsPlanPartsEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(PpsPlanPartsEntity::getPlanStatus, 2)
                    .setSql(" LOCK_QTY = LOCK_QTY +" + request.get(0).getLockCount())
                    .eq(PpsPlanPartsEntity::getId, ppsPlanPartsInfo.getId());
            this.update(updateWrapper);
        } else {
            UpdateWrapper<PpsPlanPartsEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().setSql(" LOCK_QTY = LOCK_QTY +" + request.get(0).getLockCount())
                    .eq(PpsPlanPartsEntity::getId, ppsPlanPartsInfo.getId());
            this.update(updateWrapper);
        }
    }

    @Override
    public void setPlanStart(Long entryId) {
        PpsEntryPartsEntity entryInfo = ppsEntryPartsService.get(entryId);
        if (entryInfo == null) {
            return;
        }
        PpsPlanPartsEntity planInfo = this.getFirstByPlanNo(entryInfo.getPlanNo());
        if (planInfo == null || planInfo.getPlanStatus() >= 10) {
            return;
        }
        UpdateWrapper<PpsPlanPartsEntity> upPlanParts = new UpdateWrapper<>();
        upPlanParts.lambda().set(PpsPlanPartsEntity::getPlanStatus, 10)
                .set(PpsPlanPartsEntity::getActualStartDt, new Date())
                .eq(PpsPlanPartsEntity::getPlanNo, planInfo.getPlanNo());
        this.update(upPlanParts);
    }

    @Override
    public void setPlanEnd(String planNo) {
        PpsPlanPartsEntity planInfo = this.getFirstByPlanNo(planNo);
        if (planInfo == null || planInfo.getPlanStatus() != 10) {
            return;
        }
        //代表计划生产完成
        if (planInfo.getLockQty() >= planInfo.getPlanQty()) {
            //判断工单是否都完成
            QueryWrapper<PpsEntryPartsEntity> qryEntryParts = new QueryWrapper<>();
            qryEntryParts.lambda().lt(PpsEntryPartsEntity::getStatus, 20)
                    .eq(PpsEntryPartsEntity::getPlanNo, planNo);
            if (ppsEntryPartsService.selectCount(qryEntryParts) == 0) {
                UpdateWrapper<PpsPlanPartsEntity> upPlanParts = new UpdateWrapper<>();
                upPlanParts.lambda().set(PpsPlanPartsEntity::getPlanStatus, 20)
                        .set(PpsPlanPartsEntity::getActualEndDt, new Date())
                        .eq(PpsPlanPartsEntity::getPlanNo, planInfo.getPlanNo());
                this.update(upPlanParts);
                //这里需要加入一个MQ广播
            }
        }
    }


    private static void verifyPlanInfo(int splitQty, PpsPlanPartsEntity planInfo) {
        if (planInfo.getIsFreeze()) {
            throw new InkelinkException("该计划已被冻结，请检查！");
        }
        if (planInfo.getPlanStatus() >= 20) {
            throw new InkelinkException("计划已经生产完成，请检查");
        }
        if (splitQty > planInfo.getPlanQty() - planInfo.getLockQty()) {
            throw new InkelinkException("请输入的分解数量不正确！");
        }
    }

    private void updatePpsPlanParts(Long id, Boolean isFreeze) {
        UpdateWrapper<PpsPlanPartsEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(PpsPlanPartsEntity::getIsFreeze, isFreeze)
                .eq(PpsPlanPartsEntity::getId, id);
        this.update(updateWrapper);
    }

    /**
     * 获取未完成的模组订单
     *
     * @param orderCategory 订单大类
     * @param planStatus    计划状态
     * @return 未完成的模组订单
     */
    private List<PpsPlanPartsEntity> getRecordListByOrderCategory(int orderCategory, int planStatus) {
        QueryWrapper<PpsPlanPartsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PpsPlanPartsEntity::getOrderCategory, orderCategory)
                .eq(PpsPlanPartsEntity::getPlanStatus, planStatus)
                .orderByAsc(PpsPlanPartsEntity::getCreationDate);
        return selectList(queryWrapper);
    }


    private final Map<String, Map<String, String>> orderDic = Maps.newHashMapWithExpectedSize(5);

    {
        Map<String, String> pressureCasting = new LinkedHashMap<>();
        pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlanNo), "计划编号");
        pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlant), "工厂");
        pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getProductCode), "产品编码");
        pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getModel), "车系");
        pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getMaterialNo), "物料编码");
        pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getMaterialCn), "物料名称");
        pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlanQty), "计划数量");
        pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getEstimatedStartDt), "预计上线时间");
        pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getEstimatedEndDt), "预计下线时间");
        //pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getBomVersion), "BOM版本号");
        pressureCasting.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getRemark), "备注");
        orderDic.put(PpsPlanPartsServiceImpl.OrderSheetTableName.PressureCasting, pressureCasting);

        Map<String, String> machining = new LinkedHashMap<>();
        machining.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlanNo), "计划编号");
        machining.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlant), "工厂");
        machining.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getModel), "车系");
        machining.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getMaterialNo), "物料编码");
        machining.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getMaterialCn), "物料名称");
        machining.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlanQty), "计划数量");
        machining.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getEstimatedStartDt), "预计上线时间");
        machining.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getEstimatedEndDt), "预计下线时间");
        orderDic.put(PpsPlanPartsServiceImpl.OrderSheetTableName.Machining, machining);

        Map<String, String> stamping = new LinkedHashMap<>();
        stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlanNo), "计划编号");
        stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlant), "工厂");
        stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getProductCode), "产品编码");
        stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getModel), "车系");
        stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getMaterialNo), "物料编码");
        stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getMaterialCn), "物料名称");
        stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlanQty), "计划数量");
        stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getEstimatedStartDt), "预计上线时间");
        stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getEstimatedEndDt), "预计下线时间");
        //stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getBomVersion), "BOM版本号");
        stamping.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getRemark), "备注");
        orderDic.put(OrderSheetTableName.Stamping, stamping);

        Map<String, String> coverPlate = new LinkedHashMap<>();
        coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlanNo), "计划编号");
        coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlant), "工厂");
        coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getProductCode), "产品编码");
        coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getModel), "车系");
        coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getMaterialNo), "物料编码");
        coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getMaterialCn), "物料名称");
        coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlanQty), "计划数量");
        coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getEstimatedStartDt), "预计上线时间");
        coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getEstimatedEndDt), "预计下线时间");
        //coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getBomVersion), "BOM版本号");
        coverPlate.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getRemark), "备注");
        orderDic.put(OrderSheetTableName.CoverPlate, coverPlate);


        Map<String, String> module = new LinkedHashMap<>();
        module.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlanNo), "计划编号");
        module.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getProductCode), "电池编码");
        module.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getModel), "电池类型");
        module.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getPlanQty), "计划数量");
        module.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getEstimatedStartDt), "预计上线时间");
        module.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getEstimatedEndDt), "预计下线时间");
        module.put(MpSqlUtils.getColumnName(PpsPlanPartsEntity::getRemark), "备注");
        orderDic.put(OrderSheetTableName.Module, module);
    }

    protected class OrderSheetTableName {
        public static final String PressureCasting = "压铸计划";
        public static final String Machining = "机加计划";
        public static final String Stamping = "冲压计划";
        public static final String CoverPlate = "盖板计划";
        public static final String Module = "预成组计划";
    }

    /**
     * 获取导入模板
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    @Override
    public void getImportTemplate(String fileName, HttpServletResponse response) throws IOException {
        super.setExcelColumnNames(orderDic.get(fileName));
        super.getImportTemplate(fileName, response);
    }

    @Override
    public void importExcel(InputStream is) throws Exception {
        Map<String, List<Map<String, String>>> datas = InkelinkExcelUtils.importExcel(is,
                orderDic.keySet().toArray(new String[orderDic.keySet().size()]));
        List<PpsPlanPartsEntity> planInfos = new ArrayList<>();
        List<RecieveCharacteristicsDataRequest> features = new ArrayList<>();
        List<PmProductMaterialMasterEntity> pmMateria = pmProductMaterialMasterProvider.getAllDatas();
        for (Map.Entry<String, List<Map<String, String>>> item : datas.entrySet()) {
            if (item.getValue().size() == 0) {
                continue;
            }
            validImportDatas(item.getValue(), orderDic.get(item.getKey()));
            switch (item.getKey()) {
                case OrderSheetTableName.PressureCasting:
                    for (Map<String, String> dic : item.getValue()) {
                        PpsPlanPartsEntity data = getExcelPpsPlanParts(dic, pmMateria, 3);
                        planInfos.add(data);
                    }
                    saveExcelData(planInfos);
                    break;

                case OrderSheetTableName.Machining:
                    for (Map<String, String> dic : item.getValue()) {
                        PpsPlanPartsEntity data = getExcelPpsPlanParts(dic, pmMateria, 4);
                        planInfos.add(data);
                    }
                    saveExcelData(planInfos);
                    break;

                case OrderSheetTableName.Stamping:
                    for (Map<String, String> dic : item.getValue()) {
                        PpsPlanPartsEntity data = getExcelPpsPlanParts(dic, pmMateria, 5);
                        planInfos.add(data);
                    }
                    saveExcelData(planInfos);
                    break;
                case OrderSheetTableName.CoverPlate:
                    for (Map<String, String> dic : item.getValue()) {
                        PpsPlanPartsEntity data = getExcelPpsPlanParts(dic, pmMateria, 6);
                        planInfos.add(data);
                    }
                    saveExcelData(planInfos);
                    break;
                case OrderSheetTableName.Module:
                    for (Map<String, String> dic : item.getValue()) {
                        PpsPlanPartsEntity data = getExcelPpsPlanParts(dic, pmMateria, 8);
                        data.setPlanStatus(0);
                        planInfos.add(data);
                    }
                    saveExcelData(planInfos);
                    break;
                default:
                    break;
            }
        }
    }

    private PpsPlanPartsEntity getExcelPpsPlanParts(Map<String, String> dic, List<PmProductMaterialMasterEntity> pmMateria, int OrderCategory)
            throws Exception {
        PpsPlanPartsEntity data = convertExcelDataToEntity(dic).get(0);
        data.setOrderCategory(OrderCategory);
        data.setPlanStatus(1);
        PmProductMaterialMasterEntity materialMasterInfo = pmMateria.stream().filter(s -> StringUtils.equals(s.getMaterialNo(), data.getMaterialNo()))
                .findFirst().orElse(null);
        String materialCn = "";
        if (materialMasterInfo == null) {
            materialCn = "";
        } else {
            materialCn = materialMasterInfo.getMaterialCn();
        }
        data.setMaterialCn(materialCn);
        data.setPlanSource(2);
        return data;
    }

    public List<PpsPlanPartsEntity> convertExcelDataToEntity(Map<String, String> datas) throws Exception {
        List<Map<String, String>> lstMp = new ArrayList<>();
        //        for (Map<String, String> map : datas) {
        //            lstMp.add(convertExcelDataBySysConfig(currentModelClass(), map));
        //        }
        lstMp.add(convertExcelDataBySysConfig(currentModelClass(), datas, this::getSysConfigurationMaps));
        return super.convertExcelDataToEntity(lstMp);
    }

    /**
     * 获取系统配置
     */
    private Map<String, String> getSysConfigurationMaps(String category) {
        return sysConfigurationProvider.getSysConfigurationMaps(category);
    }

    void validMaterNo(List<PpsPlanPartsEntity> entities) {
        for (PpsPlanPartsEntity data : entities) {
            switch (data.getOrderCategory()) {
                case 3: //压铸
                case 4://机加
                case 5://冲压
                case 6://盖板
                    //以上不关联BOM
                    return;
            }
            String bomVersion = pmProductBomVersionsProvider.getBomVersions(data.getProductCode());
            if (StringUtils.isBlank(bomVersion)) {
                throw new InkelinkException("计划" + data.getPlanNo() + "对应的产品编码" + data.getProductCode() + "未找到对应的BOM信息");
            }
            data.setBomVersion(bomVersion);
        }
    }

    /**
     * 保存excel数据
     */
    @Override
    public void saveExcelData(List<PpsPlanPartsEntity> entities) {
        StringBuilder planNos = new StringBuilder();
        for (PpsPlanPartsEntity data : entities) {
            PpsPlanPartsEntity plan = getFirstByPlanNo(data.getPlanNo());
            if (plan != null && !StringUtils.isBlank(plan.getPlanNo())) {
                planNos.append(data.getPlanNo()).append("|");
            }
        }
        if (StringUtils.isNotBlank(planNos.toString())) {
            throw new InkelinkException("计划编号【" + com.ca.mfd.prc.common.utils.StringUtils.trimEnd(planNos.toString(), "|") + "】已经存在，不允许导入！");
        }
        validMaterNo(entities);
        super.saveExcelData(entities);
    }

    /**
     * 处理即将导出的数据
     *
     * @param datas
     */
    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("estimatedStartDt") && data.get("estimatedStartDt") != null) {
                data.put("estimatedStartDt", DateUtils.format((Date) data.get("estimatedStartDt"), "yyyy-MM-dd HH:mm:ss"));
            }
            if (data.containsKey("estimatedEndDt") && data.get("estimatedEndDt") != null) {
                data.put("estimatedEndDt", DateUtils.format((Date) data.get("estimatedEndDt"), "yyyy-MM-dd HH:mm:ss"));
            }
        }
    }


    @Override
    public void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {
        ConditionDto orderCategory = conditions.stream().filter(c -> "orderCategory".equals(c.getColumnName())).findFirst().orElse(null);
        if (orderCategory == null) {
            throw new InkelinkException("需要传入orderCategory参数区分计划类型   冲压计划、整车计划");
        }
        String enumOrder = StringUtils.EMPTY;
        if (orderCategory.getValue().equals("3")) {
            enumOrder = OrderSheetTableName.CoverPlate;
        } else if (orderCategory.getValue().equals("4")) {
            enumOrder = OrderSheetTableName.Machining;
        } else if (orderCategory.getValue().equals("5")) {
            enumOrder = OrderSheetTableName.Stamping;
        } else if (orderCategory.getValue().equals("6")) {
            enumOrder = OrderSheetTableName.CoverPlate;
        } else if (orderCategory.getValue().equals("8")) {
            enumOrder = OrderSheetTableName.Module;
        }
        super.setExcelColumnNames(orderDic.get(enumOrder));
        super.export(conditions, sorts, enumOrder, response);
    }

}