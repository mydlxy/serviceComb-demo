package com.ca.mfd.prc.pps.communication.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.constant.BodyShopCode;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.VehicleModelDto;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.entity.MidAsBathPlanEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidAsBathPlanMapper;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.communication.service.IMidAsBathPlanService;
import com.ca.mfd.prc.pps.dto.ProcessRelationInfo;
import com.ca.mfd.prc.pps.entity.PpsLineProductionConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsProcessRelationEntity;
import com.ca.mfd.prc.pps.entity.PpsProductProcessEntity;
import com.ca.mfd.prc.pps.enums.OrderCategoryEnum;
import com.ca.mfd.prc.pps.extend.IPpsPlanExtendService;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmOrgProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductCharacteristicsVersionsProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductMaterialMasterProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsLineProductionConfigService;
import com.ca.mfd.prc.pps.service.IPpsPlanPartsService;
import com.ca.mfd.prc.pps.service.IPpsProcessRelationService;
import com.ca.mfd.prc.pps.service.IPpsProductProcessService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AS批次计划服务实现
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@Service
public class MidAsBathPlanServiceImpl extends AbstractCrudServiceImpl<IMidAsBathPlanMapper, MidAsBathPlanEntity> implements IMidAsBathPlanService {
    private static final Logger logger = LoggerFactory.getLogger(MidAsBathPlanServiceImpl.class);
    @Autowired
    private IPpsPlanExtendService ppsPlanExtendService;
    @Autowired
    private IPpsPlanPartsService ppsPlanPartsService;
    @Autowired
    private IPpsProcessRelationService ppsProcessRelationService;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private IPpsProductProcessService ppsProductProcessService;
    @Autowired
    private PmProductCharacteristicsVersionsProvider pmProductCharacteristicsVersionsService;
    @Autowired
    private PmOrgProvider pmOrgProvider;
    @Autowired
    private IPpsLineProductionConfigService ppsLineProductionConfigService;


    @Override
    public List<MidAsBathPlanEntity> getByPlanNos(List<String> plannos) {
        if (plannos == null || plannos.isEmpty()) {
            return new ArrayList<>();
        }
        QueryWrapper<MidAsBathPlanEntity> qry = new QueryWrapper<>();
        qry.lambda().in(MidAsBathPlanEntity::getAttribute3, plannos)
                .orderByDesc(MidAsBathPlanEntity::getCreationDate);
        return selectList(qry);
    }

    /**
     * 获取计划
     *
     * @param logid
     * @return
     */
    @Override
    public List<MidAsBathPlanEntity> getListByLog(Long logid) {
        QueryWrapper<MidAsBathPlanEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidAsBathPlanEntity::getPrcMidApiLogId, logid);
        return selectList(qry);
    }

    /**
     * 执行数据处理逻辑
     */
    @Override
    public String excute(String logid) {
        IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        PmProductMaterialMasterProvider pmProductMaterialMasterProvider = SpringContextUtils.getBean(PmProductMaterialMasterProvider.class);
        PmVersionProvider pmVersionProvider = SpringContextUtils.getBean(PmVersionProvider.class);
        List<MidApiLogEntity> apilogs = midApiLogService.getDoList(ApiTypeConst.AS_BATH_PLAN, ConvertUtils.stringToLong(logid));
        if (apilogs == null || apilogs.isEmpty()) {
            return "";
        }
        String errorMsg = "";
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
       // List<PmProductMaterialMasterEntity> pmMateria = pmProductMaterialMasterProvider.getAllDatas();
        for (MidApiLogEntity apilog : apilogs) {
            boolean success = false;
            try {
                UpdateWrapper<MidApiLogEntity> uplogStart = new UpdateWrapper<>();
                uplogStart.lambda().set(MidApiLogEntity::getStatus, 4)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogStart);
                midApiLogService.saveChange();

                Integer bomMode = StringUtils.isBlank(apilog.getAttribute2())?null:
                        Integer.parseInt(apilog.getAttribute2());
                bomMode = midApiLogService.getBomMode(bomMode);

                List<MidAsBathPlanEntity> datas = this.getListByLog(apilog.getId());
                Integer stats = this.save(bomMode,pmall,pmProductMaterialMasterProvider, datas);
                this.saveChange();
                MidAsBathPlanEntity errData = datas.stream().filter(c->c.getExeStatus()==2 && StringUtils.isNotBlank(c.getExeMsg()))
                        .findFirst().orElse(null);
                if(errData!=null) {
                    errorMsg = errData.getExeMsg();
                    if (stats == 1) {
                        stats = 2;
                    }
                }
                success = stats == 1;

            } catch (Exception exception) {
                errorMsg = "数据保存异常："+ exception.getMessage();
                logger.debug("数据保存异常：{}", exception.getMessage());
            }
            try {
                midApiLogService.clearChange();
                UpdateWrapper<MidApiLogEntity> uplogEnd = new UpdateWrapper<>();
                LambdaUpdateWrapper<MidApiLogEntity> upwaper = uplogEnd.lambda();
                upwaper.set(MidApiLogEntity::getStatus, success ? 5 : 6);
                if (!StringUtils.isBlank(errorMsg)) {
                    upwaper.set(MidApiLogEntity::getRemark, com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errorMsg,300));
                }
                upwaper.eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogEnd);
                midApiLogService.saveChange();
            } catch (Exception exception) {
                errorMsg = "日志保存异常："+ exception.getMessage();
                logger.debug("日志保存异常：{}", exception.getMessage());
            }
        }
        return errorMsg;
    }

    private String setProcessTaskCode(String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        code = code.trim();
        //PCGD-20231104-0108-01
        String[] arrs = code.split("-");
        StringBuilder sb = new StringBuilder();
        if (arrs.length >= 3) {
            sb.append(arrs[0]).append("-").append(arrs[1]).append("-").append(arrs[2]);
            return sb.toString();
        } else {
            return code;
        }
    }

    private PmAviEntity getAviInfo(IMidApiLogService midApiLogService,PmAllDTO pmall,String aviCode) {
        /*PmAviEntity aviInfo = pmall.getAvis().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getAviCode(), aviCode)).findFirst().orElse(null);
        if (aviInfo != null) {
            return aviInfo;
        }
        return pmall.getAvis().stream().filter(c -> StringUtils.startsWithIgnoreCase(c.getAviCode(), aviCode + "-"))
                .sorted(Comparator.comparing(PmAviEntity::getAviCode)).findFirst().orElse(null);*/
        PmAviEntity avi = midApiLogService.getAviByAsTpCode(pmall, aviCode);
        if (avi == null) {
            throw new InkelinkException("AVI代码[" + aviCode + "]不存在");
        }
        return avi;
    }

    private Integer save(Integer bomMode,PmAllDTO pmall,PmProductMaterialMasterProvider pmProductMaterialMasterProvider, List<MidAsBathPlanEntity> list) {
        if (list == null || list.isEmpty()) {
            return 1;
        }
        //重写批次号
        for (MidAsBathPlanEntity et : list) {
            //et.setProcessTaskCode();
            et.setAttribute3(setProcessTaskCode(et.getProcessTaskCode()));
        }
        IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        List<SysConfigurationEntity> categoryArea = sysConfigurationProvider.getSysConfigurations("ORDER_CATEGORY_AREA");
        List<MidAsBathPlanEntity> upBaths = new ArrayList<>();
        Map<String, List<MidAsBathPlanEntity>> gps = list.stream().collect(Collectors.groupingBy(MidAsBathPlanEntity::getAttribute3));
        Integer stats = 1;
        for (Map.Entry<String, List<MidAsBathPlanEntity>> gp : gps.entrySet()) {
            String planNo = gp.getKey();
            List<MidAsBathPlanEntity> planBathsAll = gp.getValue();
            if (planBathsAll == null || planBathsAll.isEmpty() || StringUtils.isBlank(planNo)) {
                logger.info("保存AS批次计划错误，计划号为空{}，或对应数据为空", planNo);
                continue;
            }
            try {
                String aviCodeTmp = "";
                String materialCodeTmp = "";
                //校验数据
                String checkMsg = "";
                Map<String, List<MidAsBathPlanEntity>> gpPlans = new LinkedHashMap<>();
                for (MidAsBathPlanEntity plan : planBathsAll) {
                    String shopCode = plan.getShopCode();
                    String aviCode = plan.getWsCode();
                    PmAviEntity aviInfo = getAviInfo(midApiLogService,pmall,aviCode);
                    if (aviInfo == null) {
                        checkMsg = String.format("保存AS批次计划错误，未找到AVI点,编码：%s的信息", aviCode);
                        break;
                    }
                    plan.setAttribute2(aviInfo.getAviCode());
                    aviCode = aviInfo.getAviCode();

                    PmLineEntity lineInfo = pmall.getLines().stream().filter(c -> Objects.equals(c.getId(), aviInfo.getPrcPmLineId())).findFirst().orElse(null);
                    if (lineInfo == null) {
                        checkMsg = String.format("保存AS批次计划错误，未找到线体,ID：%s，AVI编码：%s的信息", aviInfo.getPrcPmLineId(), aviCode);
                        break;
                    }
                    String lineCode = lineInfo.getLineCode();
                    //获取线体对应的订单大类
                    String orderCategoryTmp = getOrderCategory(categoryArea, shopCode, lineCode);
                    if (StringUtils.isBlank(orderCategoryTmp)) {
                        checkMsg = String.format("保存AS批次计划错误，未找到线体：%s，AVI编码：%s对应的订单类型配置", lineCode, aviCode);
                        break;
                    }
//                    //计划大类不一致（不应该在一个批次里）
//                    if (!StringUtils.isBlank(orderCategory) && !StringUtils.equals(orderCategory, orderCategoryTmp)) {
//                        checkMsg = String.format("保存AS批次计划错误，同一个工单下，出现多个订单大类【%s】AVI【%s】、【%s】AVI【%s】对应的订单类型配置"
//                                , orderCategory, aviCodeTmp, orderCategoryTmp, aviCode);
//                        break;
//                    }
                    //校验零件号是否一致
                    if (!StringUtils.isBlank(materialCodeTmp) && !StringUtils.equals(materialCodeTmp, plan.getMaterialCode())) {
                        checkMsg = String.format("保存AS批次计划错误，同一个工单下，出现多个零件号【%s】AVI【%s】、【%s】AVI【%s】对应的订单类型配置"
                                , materialCodeTmp, aviCodeTmp, plan.getMaterialCode(), aviCode);
                        break;
                    }
                    materialCodeTmp = plan.getMaterialCode();
                    aviCodeTmp = aviCode;
                    plan.setLineCode(lineCode);

                    if (!gpPlans.containsKey(orderCategoryTmp)) {
                        gpPlans.put(orderCategoryTmp, new ArrayList<>());
                    }
                    gpPlans.get(orderCategoryTmp).add(plan);
                }
                if (!StringUtils.isBlank(checkMsg)) {
                    logger.info(checkMsg);
                    updateData(planBathsAll, 2, checkMsg);
                    upBaths.addAll(planBathsAll);
                    continue;
                }
                //一个批次包含多个计划大类
                if (gpPlans.keySet().size() > 1) {
                    //如果是电池计划，包含预成组，是允许的,其它情况不允许
                    /*if (!(gpPlans.keySet().size() == 2 && gpPlans.containsKey("2") && gpPlans.containsKey("8"))) {
                        List<String> orderCategorys =  gpPlans.keySet().stream().collect(Collectors.toList());//.toArray();
                        String startAvi = gpPlans.get(orderCategorys.get(0)).get(0).getAttribute2();
                        String endAvi = gpPlans.get(orderCategorys.get(1)).get(0).getAttribute2();
                        checkMsg = String.format("保存AS批次计划错误，同一个工单下，出现多个订单大类【%s】AVI【%s】、【%s】AVI【%s】对应的订单类型配置"
                                , orderCategorys.get(0), startAvi, orderCategorys.get(1), endAvi);
                    }*/
                }

                if (!StringUtils.isBlank(checkMsg)) {
                    logger.info(checkMsg);
                    updateData(planBathsAll, 2, checkMsg);
                    upBaths.addAll(planBathsAll);
                    continue;
                }

                //分组处理
                for (Map.Entry<String, List<MidAsBathPlanEntity>> entry : gpPlans.entrySet()) {
                    String orderCategory = entry.getKey();
                    List<MidAsBathPlanEntity> planBaths = entry.getValue();
                    switch (orderCategory) {
                        case "3": //压铸
                        case "4": //机加
                        case "5": //冲压
                        case "6": //电池上盖
                        case "8": //预成组
                        {
                            String orgCode = pmOrgProvider.getCurrentOrgCode();
                            stats = doPlanPartsData(bomMode,pmall,midApiLogService, orgCode, planNo, orderCategory, planBaths, pmProductMaterialMasterProvider);
                        }
                        break;
                        case "2": //电池包
                        case "7": //备件
                        {
                            stats = doPlanData(bomMode,pmall,midApiLogService, planNo, orderCategory, planBaths, pmProductMaterialMasterProvider);
                        }
                        break;
                        default: {
                            String exeMsg = String.format("保存AS批次计划错误，计划类型：%s对应的订单类型配置错误", orderCategory);
                            logger.info(exeMsg);
                            updateData(planBaths, 2, exeMsg);
                            stats = 2;
                        }
                        break;
                    }
                }

            } catch (Exception e) {
                String exeMsg = String.format("保存AS批次计划错误，计划号%s，处理错误。%s", planNo, e.getMessage());
                logger.info(exeMsg);
                logger.error("", e);
                updateData(planBathsAll, 2, exeMsg);
                stats = 2;
            }
            upBaths.addAll(planBathsAll);
        }
        this.updateBatchById(upBaths);
        return stats;
    }

    private String getOrderCategory(List<SysConfigurationEntity> categoryArea,String shopCode,String lineCode) {
        //判断备件
        PpsLineProductionConfigEntity partsConfig = ppsLineProductionConfigService.getAllDatas().stream()
                .filter(c -> StringUtils.equalsIgnoreCase(c.getLineCode(), lineCode)).findFirst().orElse(null);
        if (partsConfig != null) {
            return OrderCategoryEnum.SparePart.codeString();
        }
        //判断电池、盖板、预成组（通过配置）
        SysConfigurationEntity configuration = categoryArea.stream().filter(c -> StringUtils.equals(c.getValue(), lineCode)).findFirst().orElse(null);
        if (configuration != null && !StringUtils.isBlank(configuration.getText())) {
            return configuration.getText();
        } else if (BodyShopCode.FD.equalsIgnoreCase(shopCode)) {
            //铸造
            return OrderCategoryEnum.PressureCasting.codeString();
        } else if (BodyShopCode.MA.equalsIgnoreCase(shopCode)) {
            //机加
            return OrderCategoryEnum.Machining.codeString();
        } else if (BodyShopCode.ST.equalsIgnoreCase(shopCode)) {
            //冲压
            return OrderCategoryEnum.Stamping.codeString();
        } else {
            return "";
        }
    }

    private Integer doPlanData(
            Integer bomMode,
            PmAllDTO pmall,
            IMidApiLogService midApiLogService
            , String planNo
            , String orderCategory
            , List<MidAsBathPlanEntity> planBaths
            , PmProductMaterialMasterProvider pmProductMaterialMasterProvider
    ) {
        MidAsBathPlanEntity startAvi = planBaths.stream().filter(c ->StringUtils.equals(c.getWsFlag(), "0"))
                .sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanStartTime))
                .findFirst().orElse(null);
        MidAsBathPlanEntity endAvi = planBaths.stream().filter(c ->StringUtils.equals(c.getWsFlag(), "1"))
                .sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanEndTime).reversed())
                .findFirst().orElse(null);
        if (startAvi == null || endAvi == null) {
            String exeMsg = String.format("保存AS批次计划错误，计划号%s，上线和下线数据不完整", planNo);
            logger.info(exeMsg);
            updateData(planBaths, 2, exeMsg);
            return 2;
        }
        //验证计划是否存在
        PpsPlanEntity ppsPlanEntity = ppsPlanExtendService.getFirstByPlanNo(planNo);
        if (ppsPlanEntity == null) {
            PpsPlanEntity et = new PpsPlanEntity();
            et.setPlanNo(planNo);
            et.setOrderCategory(orderCategory);
            Integer stats = setPlan(bomMode,midApiLogService, et, planBaths, pmProductMaterialMasterProvider, startAvi, endAvi);
            ppsPlanExtendService.insert(et);
            updateData(planBaths, stats, "");
            return stats;
        } else if (ppsPlanEntity.getPlanStatus() > 1) {
            String exeMsg = String.format("保存AS批次计划错误，计划号%s，状态错误", planNo);
            logger.info(exeMsg);
            updateData(planBaths, 3, exeMsg);
            return 3;
        } else {
            //修改
            ppsPlanEntity.setOrderCategory(orderCategory);
            Integer stats = setPlan(bomMode,midApiLogService, ppsPlanEntity, planBaths, pmProductMaterialMasterProvider, startAvi, endAvi);
            ppsPlanExtendService.update(ppsPlanEntity);
            updateData(planBaths, stats, "");
            return stats;
        }
    }

    private Integer doPlanPartsData(
            Integer bomMode,
            PmAllDTO pmall,
            IMidApiLogService midApiLogService
            , String plant
            , String planNo
            , String orderCategory
            , List<MidAsBathPlanEntity> planBaths
            , PmProductMaterialMasterProvider pmProductMaterialMasterProvider
    ) {
        MidAsBathPlanEntity startAvi = planBaths.stream().filter(c ->StringUtils.equals(c.getWsFlag(), "0"))
                .sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanStartTime))
                .findFirst().orElse(null);
        MidAsBathPlanEntity endAvi = planBaths.stream().filter(c ->StringUtils.equals(c.getWsFlag(), "1"))
                .sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanEndTime).reversed())
                .findFirst().orElse(null);
        if (startAvi == null || endAvi == null) {
            String exeMsg = String.format("保存AS批次计划错误，计划号%s，上线和下线数据不完整", planNo);
            logger.info(exeMsg);
            updateData(planBaths, 3, exeMsg);
            return 3;
        }
        //验证计划是否存在
        PpsPlanPartsEntity ppsPlanEntity = ppsPlanPartsService.getFirstByPlanNo(planNo);
        if (ppsPlanEntity == null) {
            PpsPlanPartsEntity et = new PpsPlanPartsEntity();
            et.setPlanNo(planNo);
            et.setPlant(plant);
            et.setOrderCategory(Integer.parseInt(orderCategory));
            Integer stats = setPartsPlan(bomMode,pmall,midApiLogService, et, planBaths, pmProductMaterialMasterProvider, startAvi, endAvi);
            setProcessRelease(pmall,et, planBaths);
            ppsPlanPartsService.insert(et);
            updateData(planBaths, stats, "");
            return stats;
        } else if (ppsPlanEntity.getPlanStatus() > 1) {
            String exeMsg = String.format("保存AS批次计划错误，计划号%s，状态错误", planNo);
            logger.info(exeMsg);
            updateData(planBaths, 3, exeMsg);
            return 3;
        } else {
            //修改
            ppsPlanEntity.setPlant(plant);
            ppsPlanEntity.setOrderCategory(Integer.parseInt(orderCategory));
            Integer stats = setPartsPlan(bomMode,pmall,midApiLogService, ppsPlanEntity, planBaths, pmProductMaterialMasterProvider, startAvi, endAvi);
            setProcessRelease(pmall,ppsPlanEntity, planBaths);
            ppsPlanPartsService.update(ppsPlanEntity);
            updateData(planBaths, stats, "");
            return stats;
        }
    }

    /**
     * 写入批次件的工序数据
     *
     * */
    private void setProcessRelease(PmAllDTO pmall,PpsPlanPartsEntity ppsPlanEntity, List<MidAsBathPlanEntity> planBaths) {
        //as工作区域
        List<String> linceCodes = planBaths.stream().map(MidAsBathPlanEntity::getLineCode).distinct().collect(Collectors.toList());
        //更具工区获取线体(根据tp点获取的线体)
        //List<String> linceCodes = pmall.getLines().stream().filter(c -> c.getMergeLine() != null
        //        && asLinceCodes.contains(c.getMergeLine())).map(PmLineEntity::getLineCode).distinct().collect(Collectors.toList());
        //获取两个工序

        List<PpsProcessRelationEntity> processRelations = new ArrayList<>();
        for (String line : linceCodes) {
            if (processRelations.size() < 1) {
                PpsProcessRelationEntity first = ppsProcessRelationService.getFirstByLineCode(line,1);
                if (first != null) {
                    processRelations.add(first);
                }
            }
        }
        //没有匹配工艺，取第一个。
//        if(processRelations.isEmpty()) {
//            List<PpsProcessRelationEntity> process = ppsProcessRelationService.getListByOrderCategory(ppsPlanEntity.getOrderCategory());
//            if (process != null && process.size() > 0) {
//                ppsPlanEntity.setCharacteristic1(process.get(0).getId().toString());
//            }
//        }

        if (processRelations.size() > 0) {
            ppsPlanEntity.setCharacteristic1(processRelations.get(0).getId().toString());
        }
        if (processRelations.size() > 1) {
            ppsPlanEntity.setCharacteristic2(processRelations.get(1).getId().toString());
        }
    }

    private Integer setPlan(
            Integer bomMode,
            IMidApiLogService midApiLogService
            , PpsPlanEntity et
            , List<MidAsBathPlanEntity> planBaths
            , PmProductMaterialMasterProvider pmProductMaterialMasterProvider
            , MidAsBathPlanEntity startAvi
            , MidAsBathPlanEntity endAvi) {
        et.setStartAvi(startAvi.getAttribute2()); //getWsCode
        et.setEndAvi(endAvi.getAttribute2());  //getWsCode
        String materialCode = startAvi.getMaterialCode(); //物料编码
        //1,"整车"), 2, "电池包"), 3, "压铸"), 4, "机加"), 5, "冲压"), 6, "电池上盖"), 7, "备件"), 8, "预成组");
        switch (et.getOrderCategory()) {
            case "2": //电池包
            {
                et.setProductCode(materialCode);
                et.setModel(materialCode);
                et.setCharacteristic1(materialCode);

                et.setPlanQty(startAvi.getLotQuantity());
                //生产顺序号 TODO
                et.setDisplayNo(0);
                et.setBomVersion(midApiLogService.getBomPartVersion(bomMode,materialCode));
                et.setCharacteristic4(getMaterialName(materialCode, pmProductMaterialMasterProvider));
                et.setCharacteristic5(materialCode);

                Date planStartDt = planBaths.stream().sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanStartTime))
                        .findFirst().orElse(null).getPlanStartTime();
                et.setEstimatedStartDt(planStartDt);
                Date planEndDt = planBaths.stream().sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanEndTime).reversed())
                        .findFirst().orElse(null).getPlanEndTime();
                et.setEstimatedEndDt(planEndDt);

                et.setRemark("从AS下发批次计划");
                et.setOrderSign(StringUtils.EMPTY);
                et.setPlanStatus(1);
                et.setPlanSource(1);
                et.setAttribute2("0");//1白车身
                //生产路线
                PpsProductProcessEntity vprocessInfo = getProcess(et.getOrderCategory());
                if (vprocessInfo != null) {
                    et.setPrcPpsProductProcessId(vprocessInfo.getId());
                }
            }
            break;
            case "7": //备件
            {

                Date planStartDt = planBaths.stream().sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanStartTime))
                        .findFirst().orElse(null).getPlanStartTime();
                et.setEstimatedStartDt(planStartDt);
                Date planEndDt = planBaths.stream().sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanEndTime).reversed())
                        .findFirst().orElse(null).getPlanEndTime();
                et.setEstimatedEndDt(planEndDt);

                String productCode = midApiLogService.getProduceCodeByMaterialNo(bomMode,materialCode,planBaths.stream().findFirst().orElse(null).getOrgCode(),DateUtils.format(planEndDt,DateUtils.DATE_PATTERN), "1");
                et.setProductCode(productCode);
                if(StringUtils.isBlank(productCode)) {
                    throw new InkelinkException("未找到零件[" + materialCode + "]对应的整车物料号");
                }
                //通过零件编码解析
                //et.setModel(getModel(productCode));
                midApiLogService.setVeExtendInfo(et);

                et.setCharacteristic5(materialCode);
                et.setCharacteristic4(getMaterialName(materialCode, pmProductMaterialMasterProvider));
                //生产顺序号 TODO
                et.setDisplayNo(0);
                //生产区域
                et.setCharacteristic10(startAvi.getLineCode());
                et.setPlanQty(startAvi.getLotQuantity());

                //获取整车
                et.setBomVersion(midApiLogService.getBomVersion(bomMode,planBaths.stream().findFirst().orElse(null).getOrgCode(),productCode
                        ,DateUtils.format(new Date(),DateUtils.DATE_PATTERN)));
                //获取整车
                et.setCharacteristicVersion(midApiLogService.getCharacteristicsVersion(bomMode,productCode));

                et.setRemark("从AS下发批次计划");
                et.setOrderSign(StringUtils.EMPTY);
                et.setPlanStatus(1);
                et.setPlanSource(1);
                et.setAttribute2("0");//1白车身
            }
            break;
            default:
                break;
        }
        return 1;

    }

    private PpsProductProcessEntity getProcess(String orderCategory) {
        if (StringUtils.isBlank(orderCategory)) {
            return null;
        }
        return ppsProductProcessService.getProcess(orderCategory);
    }

    private String getStmold(String materialCode) {
        List<SysConfigurationEntity> cfgs = sysConfigurationProvider.getSysConfigurations("stmold");
        if (cfgs == null || cfgs.isEmpty()) {
            return "";
        }
        SysConfigurationEntity cData = cfgs.stream().filter(c -> StringUtils.equalsIgnoreCase(c.getValue(), materialCode)).findFirst().orElse(null);
        return cData == null ? "" : cData.getText();
    }

    private Integer setPartsPlan(
            Integer bomMode,
            PmAllDTO pmall,
            IMidApiLogService midApiLogService
            , PpsPlanPartsEntity et
            , List<MidAsBathPlanEntity> planBaths
            , PmProductMaterialMasterProvider pmProductMaterialMasterProvider
            , MidAsBathPlanEntity startAvi
            , MidAsBathPlanEntity endAvi) {
        et.setStartAvi(startAvi.getAttribute2()); //getWsCode
        et.setEndAvi(endAvi.getAttribute2()); //getWsCode
        et.setAttribute1(startAvi.getWsCode());
        et.setAttribute2(endAvi.getWsCode());
        String materialCode = startAvi.getMaterialCode(); //物料编码
        //1,"整车"), 2, "电池包"), 3, "压铸"), 4, "机加"), 5, "冲压"), 6, "电池上盖"), 7, "备件"), 8, "预成组");
        switch (et.getOrderCategory()) {
            case 3: //压铸
            case 4: //机加
            case 5: //冲压
            case 6: //电池上盖
            {
                Date planStartDt = planBaths.stream().sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanStartTime))
                        .findFirst().orElse(null).getPlanStartTime();
                et.setEstimatedStartDt(planStartDt);
                Date planEndDt = planBaths.stream().sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanEndTime).reversed())
                        .findFirst().orElse(null).getPlanEndTime();
                et.setEstimatedEndDt(planEndDt);
                if (et.getOrderCategory() == 6) {
                    String productCode = midApiLogService.getProduceCodeByMaterialNo(bomMode,materialCode,planBaths.stream().findFirst().orElse(null).getOrgCode(),DateUtils.format(planEndDt,DateUtils.DATE_PATTERN), "2");
                    et.setProductCode(productCode);
                    et.setCharacteristic6(materialCode);
                    et.setModel(materialCode);
                } else {
                    String productCode = midApiLogService.getProduceCodeByMaterialNo(bomMode,materialCode,planBaths.stream().findFirst().orElse(null).getOrgCode(),DateUtils.format(planEndDt,DateUtils.DATE_PATTERN), "1");
                    et.setProductCode(productCode);
                    //通过零件编码解析
                    //et.setModel(getModel(productCode));
                    VehicleModelDto vehicleModel = null;
                    try {
                        vehicleModel = midApiLogService.getBomVehicleModel(productCode);
                    } catch (Exception e) {
                        logger.error("从bom获取整车车辆信息失败", e);
                    }
                    //车系
                    if (vehicleModel != null) {
                        et.setCharacteristic6(vehicleModel.getBomRoom());
                        et.setModel(vehicleModel.getBomRoom());
                    }
                    et.setCharacteristic3(getStmold(materialCode));
                    //3：压铸  4：机加   5：冲压  6：电池上盖
                    if (et.getOrderCategory() == 5 && StringUtils.isBlank(et.getCharacteristic3())) {

                        //TODO 抛错
                    }
                }

                et.setMaterialNo(materialCode);
                et.setMaterialCn(getMaterialName(materialCode, pmProductMaterialMasterProvider));
                //汇总？
                et.setPlanQty(startAvi.getLotQuantity());

                et.setRemark("从AS下发批次计划");

                et.setPlanStatus(1);
                et.setPlanSource(2);
            }
            break;
            case 8: //预成组
            {
                et.setProductCode(materialCode);
                et.setModel(materialCode);
                et.setCharacteristic6(materialCode);

                //确认产品编码是整车物料号还是零件物料号
//                String productCode = midApiLogService.getProduceCodeByMaterialNo(materialCode, "2");
//                et.setProductCode(materialCode);
//                //通过零件编码解析
//                et.setModel(getModel(productCode));
//                VehicleModelDto vehicleModel = null;
//                try {
//                    vehicleModel = midApiLogService.getBomVehicleModel(productCode);
//                } catch (Exception e) {
//                    logger.error("从bom获取整车车辆信息失败", e);
//                }
//                //车系
//                if(vehicleModel!=null) {
//                    et.setCharacteristic6(vehicleModel.getBaseVehicleCode());
//                }


                et.setMaterialNo(materialCode);
                et.setMaterialCn(getMaterialName(materialCode, pmProductMaterialMasterProvider));
                //汇总?
                et.setPlanQty(startAvi.getLotQuantity());
                Date planStartDt = planBaths.stream().sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanStartTime))
                        .findFirst().orElse(null).getPlanStartTime();
                et.setEstimatedStartDt(planStartDt);
                Date planEndDt = planBaths.stream().sorted(Comparator.comparing(MidAsBathPlanEntity::getPlanEndTime).reversed())
                        .findFirst().orElse(null).getPlanEndTime();
                et.setEstimatedEndDt(planEndDt);

                et.setRemark("从AS下发批次计划");

                et.setPlanStatus(0);
                et.setPlanSource(2);
                et.setBomVersion(midApiLogService.getBomPartVersion(bomMode,materialCode));

            }
            break;
            default:
                break;
        }
        return 1;
    }

    private String getModel(String productCode) {
        if (StringUtils.isBlank(productCode)) {
            return "";
        }
        String[] productCodes = productCode.split("\\.");
        return com.ca.mfd.prc.common.utils.StringUtils.trimEnd(productCodes[0], ".");
    }

    private String getMaterialName(String materialCode, PmProductMaterialMasterProvider pmProductMaterialMasterProvider) {
        PmProductMaterialMasterEntity material = pmProductMaterialMasterProvider.getByMaterialNo(materialCode);
        String chara4 = material == null ? StringUtils.EMPTY : material.getMaterialCn();
        return chara4 == null ? StringUtils.EMPTY : chara4;
    }

    private void updateData(List<MidAsBathPlanEntity> ups, Integer exeStatus, String exeMsg) {
        if (ups == null || ups.isEmpty()) {
            return;
        }
        for (MidAsBathPlanEntity et : ups) {
            if (exeStatus != null) {
                //处理状态0：未处理，1：成功， 2：失败，3：不处理
                et.setExeStatus(exeStatus);
            }
            if (exeMsg != null) {
                et.setExeMsg(exeMsg);
            }
            et.setExeTime(new Date());
        }
    }

}