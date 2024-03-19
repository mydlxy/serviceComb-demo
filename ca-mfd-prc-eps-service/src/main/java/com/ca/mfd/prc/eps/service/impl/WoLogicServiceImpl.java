package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.eps.dto.AnewPrintEntryReportParaDTO;
import com.ca.mfd.prc.eps.dto.SaveTrcBarcodeParaDTO;
import com.ca.mfd.prc.eps.dto.SaveTrcBarcodeResultDTO;
import com.ca.mfd.prc.eps.dto.SystemSaveTrcBarCodeDTO;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataTrcEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoLogEntity;
import com.ca.mfd.prc.eps.enums.WoDataEnum;
import com.ca.mfd.prc.eps.enums.WoStatusEnum;
import com.ca.mfd.prc.eps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmTraceComponentEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmTraceComponentProvider;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.remote.app.pps.dto.CloseStampingEntryInfo;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsEntryReportProvider;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.remote.app.pqs.dto.ActiveAnomalyInfo;
import com.ca.mfd.prc.eps.remote.app.pqs.dto.AnomalyActivity;
import com.ca.mfd.prc.eps.remote.app.pqs.dto.AnomalyDto;
import com.ca.mfd.prc.eps.remote.app.pqs.dto.RepairActivity;
import com.ca.mfd.prc.eps.remote.app.pqs.entity.PqsProductDefectAnomalyEntity;
import com.ca.mfd.prc.eps.remote.app.pqs.provider.PqsLogicProvider;
import com.ca.mfd.prc.eps.remote.app.pqs.provider.PqsProductDefectAnomalyProvider;
import com.ca.mfd.prc.eps.service.IEpsLogicService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataTrcService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoLogService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoScrBatchConfigService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoService;
import com.ca.mfd.prc.eps.service.IWoLogicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位相关
 *
 * @author inkelink Eric。zhou
 * @since 1.0.0 2023-04-12
 */
@Service
public class WoLogicServiceImpl implements IWoLogicService {

    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Autowired
    private PmTraceComponentProvider pmTraceComponentProvider;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private IEpsVehicleWoService epsVehicleWoService;

    @Autowired
    private IEpsVehicleWoLogService epsVehicleWoLogService;

    @Autowired
    private IEpsVehicleWoScrBatchConfigService epsVehicleWoScrBatchConfigService;

    @Autowired
    private IEpsLogicService epsLogicService;

    @Autowired
    private IEpsVehicleWoDataTrcService epsVehicleWoDataTrcService;

    @Autowired
    private IEpsVehicleWoDataService epsVehicleWoDataService;


    @Autowired
    private PqsProductDefectAnomalyProvider pqsProductDefectAnomalyProvider;

    @Autowired
    private PqsLogicProvider pqsLogicProvider;

    @Autowired
    private PpsOrderProvider ppsOrderProvider;

    @Autowired
    private PpsEntryReportProvider ppsEntryReportProvider;


    /**
     * 管理端修改工艺状态
     */
    @Override
    public void systemSaveWo(Serializable woId, Integer status) {
        if (status != WoStatusEnum.Ok.code() && status != WoStatusEnum.ByPass.code()) {
            throw new InkelinkException("管理端只支持状态OK和BYPASS");
        }
        EpsVehicleWoEntity woInfo = epsVehicleWoService.get(woId);
        if (woInfo == null) {
            throw new InkelinkException("未找到对应的工艺");
        }
        if (woInfo.getWoType() == WoStatusEnum.Ng.code() || woInfo.getWoType() == WoStatusEnum.ByPass.code()) {
            throw new InkelinkException("不能手动改变追溯工艺的状态");
        }
        saveWo(woId, status);
    }

    /**
     * 提交工艺
     */
    @Override
    public void saveWo(Serializable woId, int status) {
        saveWo(woId, status, "");
    }

    /**
     * 提交工艺
     */
    @Override
    public void saveWo(Serializable woId, int status, String woData) {
        EpsVehicleWoEntity woInfo = epsVehicleWoService.get(woId);
        if (woInfo == null) {
            throw new InkelinkException("未找到对应的工艺");
        }
        //修改操作数据结果并添加日志
        LambdaUpdateWrapper<EpsVehicleWoEntity> upset = new LambdaUpdateWrapper<>();
        upset.eq(EpsVehicleWoEntity::getId, woId);
        upset.set(EpsVehicleWoEntity::getResult, status);
        epsVehicleWoService.update(upset);
        EpsVehicleWoLogEntity log = new EpsVehicleWoLogEntity();
        log.setPrcEpsVehicleWoId(woInfo.getId());
        log.setResult(status);
        switch (status) {
            case 0:
                log.setWoDescription("重置工艺");
                break;
            case 1:
                log.setWoDescription("OK");
                break;
            case 2:
                log.setWoDescription("NG");
                break;
            case 3:
                log.setWoDescription("Bypass");
                break;
            default:
                break;
        }
        epsVehicleWoLogService.insert(log);
        if (status != 0) {
            //添加工艺生产数据
            for (WoDataEnum item : WoDataEnum.values()) {
                if (woInfo.getWoType() == item.code()) {
                    EpsVehicleWoEntity finalWoInfo = woInfo;
                    WoDataEnum woDataEnum = Arrays.stream(WoDataEnum.values()).filter(c -> c.code() == finalWoInfo.getWoType()).findFirst().orElse(null);
                    epsLogicService.saveVehicleData(woInfo.getId(), woInfo.getWorkstationCode(), woInfo.getSn(), woData, woInfo.getWoDescription(), woDataEnum);
                    break;
                }
            }
            //获取工艺激活的缺陷
            List<ConditionDto> conditionInfos = new ArrayList<>();
            if (Constant.EMPTY_ID2.equals(woInfo.getDefectAnomalyCode())) {
                conditionInfos.add(new ConditionDto("EPS_PRODUCT_WO_ID", woInfo.getId().toString(), ConditionOper.Equal));
            } else {
                conditionInfos.add(new ConditionDto("PQS_DEFECT_ANOMALY_ID", woInfo.getDefectAnomalyCode(), ConditionOper.Equal));
            }
            conditionInfos.add(new ConditionDto("SN", woInfo.getSn(), ConditionOper.Equal));
            conditionInfos.add(new ConditionDto("STATUS", "4", ConditionOper.Unequal));
            PqsProductDefectAnomalyEntity vehicleAnomaly = pqsProductDefectAnomalyProvider.getData(conditionInfos).stream().findFirst().orElse(null);

            //操作成功需要关闭对应的缺陷
            if (status == 1) {
                //判断是否激活如果已经激活关闭缺陷
                if (vehicleAnomaly != null) {
                    AnomalyActivity anomalyActivity = new AnomalyActivity();
                    anomalyActivity.setDataId(vehicleAnomaly.getId());
                    anomalyActivity.setStatus(4);
                    anomalyActivity.setRemark("工艺完成，缺陷自动关闭");
                    RepairActivity repairActivity = new RepairActivity();
                    repairActivity.setRepairTime(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
                    repairActivity.setRepairWay("工艺完整关闭");
                    repairActivity.setSpendTime(BigDecimal.valueOf(0));
                    anomalyActivity.setRepairActivity(repairActivity);
                    pqsLogicProvider.modifyDefectAnomalyStatus(anomalyActivity);
                }
            } else if (status == WoStatusEnum.Ng.code() || status == WoStatusEnum.ByPass.code()) {
                //需要激活对应的缺陷
                //如果缺陷未激活，将激活缺陷
                if (vehicleAnomaly == null) {
                    ActiveAnomalyInfo activeAnomalyInfo = new ActiveAnomalyInfo();
                    activeAnomalyInfo.setWorkplaceId(woInfo.getWorkstationCode());
                    //activeAnomalyInfo.setWorkplaceName(woInfo.getWorkstationName());
                    activeAnomalyInfo.setTpsCode(woInfo.getSn());

                    AnomalyDto adto = new AnomalyDto();
                    adto.setAnomalyId(ConvertUtils.stringToLong(woInfo.getDefectAnomalyCode()));
                    adto.setWoId(woInfo.getId());
                    adto.setImg("");
                    adto.setDescription(woInfo.getWoDescription() + "失败");
                    adto.setSource(0);
                    activeAnomalyInfo.getAnomalyInfos().add(adto);
                    pqsLogicProvider.activeAnomaly(activeAnomalyInfo);
                }
            }

        } else {
            //追溯工艺重置解绑
            if (woInfo.getWoType() == WoStatusEnum.Ng.code()) {
                List<ConditionDto> delEpsVehicleWoDataTrcCons = new ArrayList<>();
                delEpsVehicleWoDataTrcCons.add(new ConditionDto("EpsVehicleWoId", woInfo.getId().toString(), ConditionOper.Equal));
                epsVehicleWoDataTrcService.delete(delEpsVehicleWoDataTrcCons, false);
                epsVehicleWoDataService.delete(delEpsVehicleWoDataTrcCons, false);
            }
        }
        epsVehicleWoDataService.saveChange();

    }


    /**
     * 上传生产数据（有工艺）
     */
    @Override
    public void uploadingData(String woId, String woData) {
        EpsVehicleWoEntity woInfo = epsVehicleWoService.get(woId);
        if (woInfo == null) {
            throw new InkelinkException("无效的工艺");
        }
        EpsVehicleWoEntity finalWoInfo = woInfo;
        WoDataEnum woDataEnum = Arrays.stream(WoDataEnum.values()).filter(c -> c.code() == finalWoInfo.getWoType()).findFirst().orElse(null);
        epsLogicService.saveVehicleData(Long.valueOf(woId), woInfo.getWorkstationCode(), woInfo.getSn(), woData, woInfo.getWoDescription(), woDataEnum);
        epsVehicleWoService.saveChange();
    }

    /**
     * 修改共享工艺所属岗位
     */
    @Override
    public List<EpsVehicleWoEntity> updateCommunalWo(String wokplaceName, Serializable workplaceId, String productCode) {
        List<EpsVehicleWoEntity> epsVehicleWoInfos = new ArrayList<>();
        List<String> woCodes = sysConfigurationProvider.getComboDatas("CommunalWo").stream().filter(c -> StringUtils.contains(c.getValue(), wokplaceName)).map(ComboInfoDTO::getText).collect(Collectors.toList());
        if (woCodes.size() > 0) {
            List<ConditionDto> conditionInfos = new ArrayList<>();
            conditionInfos.add(new ConditionDto("SN", productCode, ConditionOper.Equal));
            conditionInfos.add(new ConditionDto("PM_WO_CODE", String.join("|", woCodes), ConditionOper.In));
            List<EpsVehicleWoEntity> wos = epsVehicleWoService.getData(conditionInfos);
            for (EpsVehicleWoEntity item : wos) {
                if (!StringUtils.equals(item.getWorkstationCode(), workplaceId.toString())) {
                    //item.setWorkstationName(wokplaceName);
                    item.setWorkstationCode(workplaceId.toString());
                    epsVehicleWoInfos.add(item);
                }
            }
        }
        return epsVehicleWoInfos;
    }

    @Override
    public void closeStampingEntry(CloseStampingEntryInfo para) {
//        ppsEntryService.closeStampingEntry(para);
//        ppsEntryService.saveChange();
    }

    @Override
    public void anewPrintEntryReport(AnewPrintEntryReportParaDTO para) {
        List<String> list = new ArrayList<>();
        list.add(para.getDataId());
        ppsEntryReportProvider.printEntryReport(list);
    }

    @Override
    public void newStampingEntryReport(CloseStampingEntryInfo para) {
//        ppsEntryService.newStartStampingEntry(para);
    }


    /**
     * 后台补录条码追溯工艺
     */
    @Override
    public SaveTrcBarcodeResultDTO systemSaveTrcBarCode(SystemSaveTrcBarCodeDTO info) {
        EpsVehicleWoEntity woInfo = epsVehicleWoService.get(info.getWoId());
        if (woInfo == null) {
            throw new InkelinkException("无效的工艺编号");
        }
        SaveTrcBarcodeParaDTO sdto = new SaveTrcBarcodeParaDTO();
        sdto.setProductCode(woInfo.getSn());
        sdto.setBarCode(info.getBarCode());
        sdto.setWorkplaceId(Constant.EMPTY_ID);
        sdto.setAutoState(0);
        sdto.setWoIds(Arrays.asList(woInfo.getId()));
        return saveTrcBarcode(sdto, info.getIsConstraint() == 1);
    }


    /**
     * 提交追溯工艺
     */
    @Override
    public SaveTrcBarcodeResultDTO saveTrcBarcode(SaveTrcBarcodeParaDTO info, Boolean isConstraint) {
        if (StringUtils.isBlank(info.getBarCode())) {
            throw new InkelinkException("物料条码不能为空");
        }
        SaveTrcBarcodeResultDTO result = new SaveTrcBarcodeResultDTO();
        result.setErrorMessage("未找到对应的追溯工艺");
        String materialNo = "";

        PmAllDTO pmAll = pmVersionProvider.getObjectedPm();
        //是否排除完成工艺
        boolean isExcludeSuccess = false;
        PmWorkStationEntity workplaceInfo = pmAll.getStations().stream().filter(c -> StringUtils.equals(c.getId().toString(), info.getWorkplaceId())).findFirst().orElse(null);
        //生产岗位排除完成的追溯工艺
        if (!StringUtils.isBlank(info.getWorkplaceId()) && workplaceInfo.getWorkstationType() == 1) {
            isExcludeSuccess = true;
        }
        info.setBarCode(info.getBarCode().toUpperCase().trim());
        //获取对应的车间信息
        List<PmTraceComponentEntity> components = pmTraceComponentProvider.getDataCache();
        //获取追溯工艺
        List<ConditionDto> conditionInfos = new ArrayList<>();
        List<String> woIdsStrList = info.getWoIds().stream().map(Object::toString).collect(Collectors.toList());
        conditionInfos.add(new ConditionDto("ID", String.join("|", woIdsStrList), ConditionOper.In));
        //追溯工艺
        conditionInfos.add(new ConditionDto("PM_WO_OPER_TYPE", "3", ConditionOper.Equal));
        List<EpsVehicleWoEntity> wodatas = epsVehicleWoService.getData(conditionInfos);
        if (wodatas.size() == 0) {
            throw new InkelinkException("未找到对应的追溯工艺");
        }

        //拆分工艺已追溯规则为单位
        List<EpsVehicleWoEntity> woInfos = new ArrayList<>();

        PpsOrderEntity order = ppsOrderProvider.getPpsOrderInfoByKey(info.getProductCode());
        //验证传入条码是否合法
        for (EpsVehicleWoEntity woInfo : woInfos) {
            //排除已完成的追溯工艺
            if (woInfo.getResult() == 1 && isExcludeSuccess) {
                continue;
            }
            result.setWoDescription(woInfo.getWoDescription());
            /* 判断组件是否存在  */
            //PmTraceComponentEntity componetInfo = components.stream().filter(t -> StringUtils.equals(t.getTraceComponentCode(), woInfo.getTraceComponentCode())).findFirst().orElse(null);
            //if (componetInfo == null || StringUtils.isBlank(woInfo.getTraceComponentCode())) {
            //    result.setErrorMessage("未找到相关的组件");
            //    continue;
            //}
            //强绑情况下输入条码长度可能无法截取物料号
            //验证分线总成上主线验证
            //if ("*****************".equals(componetInfo.getTraceComponentCode())) {
            //    if (StringUtils.equals(order.getSn().toUpperCase(), info.getBarCode()) || StringUtils.equals(order.getBarcode().toUpperCase(), info.getBarCode())) {
            //        isConstraint = true;
            //    } else {
            //        result.setErrorMessage("未找到可匹配条码的工艺");
            //        continue;
            //    }
            //}

            if (!isConstraint) {

                /* 判断条码是否被使用 */
                //批次件不需要判断
                if (!woInfo.getTrcByGroup() && woInfo.getWoType() == 2) {
                    List<ConditionDto> vehicleWoDataTrcCons = new ArrayList<>();
                    vehicleWoDataTrcCons.add(new ConditionDto("BARCODE", info.getBarCode(), ConditionOper.Equal));
                    vehicleWoDataTrcCons.add(new ConditionDto("RESULT", "1", ConditionOper.Equal));
                    vehicleWoDataTrcCons.add(new ConditionDto("EPS_VEHICLE_WO_ID", woInfo.getId().toString(), ConditionOper.Unequal));
                    EpsVehicleWoDataTrcEntity vehicleWoDataTrcInfo = epsVehicleWoDataTrcService.getData(vehicleWoDataTrcCons).stream().findFirst().orElse(null);
                    if (vehicleWoDataTrcInfo != null) {
                        //SaveWo(woInfo.Id, 2);
                        result.setErrorMessage("条码已被使用");
                        result.setResult(2);
                        result.setWoId(woInfo.getId());
                        break;
                    }
                }
            }
            /* 执行成功 */
            //保存追溯条码
            EpsVehicleWoDataTrcEntity pmcVehicleWoDataTrcInfo = new EpsVehicleWoDataTrcEntity();
            pmcVehicleWoDataTrcInfo.setPrcEpsVehicleWoId(Constant.DEFAULT_ID);
            pmcVehicleWoDataTrcInfo.setPrcEpsVehicleWoDataId(Constant.DEFAULT_ID);
            pmcVehicleWoDataTrcInfo.setBarcode(info.getBarCode());
            pmcVehicleWoDataTrcInfo.setPartsNumber(materialNo);
            //pmcVehicleWoDataTrcInfo.setComponentCode(componetInfo.getTraceComponentCode());
            pmcVehicleWoDataTrcInfo.setSourceType(isConstraint ? 1 : 0);


            saveWo(woInfo.getId(), 1, JsonUtils.toJsonString(pmcVehicleWoDataTrcInfo));
            result.setErrorMessage("");
            result.setResult(1);
            result.setWoId(woInfo.getId());

            /* 批次追溯自动配置 */
            //是否批次追溯工艺并非强绑操作，操作工位是否为工艺对应工位   测试不加入这个判断&& !isConstraint
            if (woInfo.getTrcByGroup() && StringUtils.equals(info.getWorkplaceId(), woInfo.getWorkstationCode()) && !isConstraint) {
                //todo lgw
            }
            break;
        }
        if (result.getResult() == 0) {
            if (StringUtils.isBlank(result.getErrorMessage())) {
                result.setErrorMessage("请重置条码对应的工艺");
            } else {
                result.setErrorMessage(result.getErrorMessage() + "，或重置条码对应的工艺");
            }
        }
        if (!StringUtils.isBlank(materialNo)) {
            result.setErrorMessage(result.getErrorMessage() + "[" + materialNo + "]");
        }
        return result;
    }

}