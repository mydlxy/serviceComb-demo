package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.MidAsVehicleDto;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.entity.MidAsVehicleEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidAsVehicleMapper;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.communication.service.IMidAsVehicleService;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareBomConfigService;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareBomListService;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsProductProcessEntity;
import com.ca.mfd.prc.pps.enums.OrderCategoryEnum;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductCharacteristicsVersionsProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductMaterialMasterProvider;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AS整车信息服务实现
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@Service
public class MidAsVehicleServiceImpl extends AbstractCrudServiceImpl<IMidAsVehicleMapper, MidAsVehicleEntity> implements IMidAsVehicleService {

    private static final Logger logger = LoggerFactory.getLogger(MidAsBathPlanServiceImpl.class);
    @Autowired
    private IPpsPlanService ppsPlanService;
    @Autowired
    private PmProductCharacteristicsVersionsProvider pmProductCharacteristicsVersionsService;

    /**
     * 获取计划
     *
     * @param logid
     * @return
     */
    @Override
    public List<MidAsVehicleEntity> getListByLog(Long logid) {
        QueryWrapper<MidAsVehicleEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidAsVehicleEntity::getPrcMidApiLogId, logid);
        return selectList(qry);
    }

    @Override
    public MidAsVehicleDto getVehicleByPlanNo(String planNo) {
        QueryWrapper<MidAsVehicleEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidAsVehicleEntity::getVrn, planNo);
        MidAsVehicleEntity entity = selectList(qry).stream().findFirst().orElse(null);
        if (entity != null) {
            MidAsVehicleDto dto = new MidAsVehicleDto();
            BeanUtils.copyProperties(entity, entity);
            return dto;
        }
        return null;
    }

    /**
     * 获取整车物料号
     *
     * @param logid
     * @return
     */
    @Override
    public List<String> getvehicleMaterial(Long logid) {
        QueryWrapper<MidAsVehicleEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidAsVehicleEntity::getPrcMidApiLogId, logid)
                .select(MidAsVehicleEntity::getMaterialCode);
        return selectList(qry).stream().map(MidAsVehicleEntity::getMaterialCode)
                .distinct().collect(Collectors.toList());
    }

    /**
     * 执行数据处理逻辑
     */
    @Override
    public String excute(String logid) {
        IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        PmProductMaterialMasterProvider pmProductMaterialMasterProvider = SpringContextUtils.getBean(PmProductMaterialMasterProvider.class);
        PmVersionProvider pmVersionProvider = SpringContextUtils.getBean(PmVersionProvider.class);
        List<MidApiLogEntity> apilogs = midApiLogService.getDoList(ApiTypeConst.AS_VEHICLE_PLAN, ConvertUtils.stringToLong(logid));
        if (apilogs == null || apilogs.isEmpty()) {
            return "";
        }
        //List<PmProductMaterialMasterEntity> pmMateria = pmProductMaterialMasterProvider.getAllDatas();
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        String errorMsg = "";
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
                List<MidAsVehicleEntity> datas = this.getListByLog(apilog.getId());
                Integer stats = this.save(bomMode,pmall, pmProductMaterialMasterProvider, datas);
                this.saveChange();
                MidAsVehicleEntity errData = datas.stream().filter(c->c.getExeStatus()==2 && StringUtils.isNotBlank(c.getExeMsg()))
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
                logger.error("数据保存异常：{}", exception.getMessage());
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
                logger.error("日志保存异常：{}", exception.getMessage());
            }
        }
        return errorMsg;
    }

    private Integer save(Integer bomMode,PmAllDTO pmall, PmProductMaterialMasterProvider pmProductMaterialMasterProvider , List<MidAsVehicleEntity> list) {
        if (list == null || list.isEmpty()) {
            return 1;
        }
        Integer stats = 1;
        IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        List<MidAsVehicleEntity> upBaths = new ArrayList<>();
        List<PpsPlanEntity> upPlans = new ArrayList<>();
        List<PpsPlanEntity> addPlans = new ArrayList<>();
        String orgCode = list.get(0).getOrgCode();
        Map<String, List<MidAsVehicleEntity>> gps = list.stream().collect(Collectors.groupingBy(MidAsVehicleEntity::getVrn));
        for (Map.Entry<String, List<MidAsVehicleEntity>> gp : gps.entrySet()) {
            String planNo = gp.getKey();
            List<MidAsVehicleEntity> planBaths = gp.getValue();
            if (planBaths == null || planBaths.isEmpty() || StringUtils.isBlank(planNo)) {
                logger.info("保存AS整车计划错误，计划号为空{}，或对应数据为空", planNo);
                continue;
            }
            try {
                //验证计划是否存在
                PpsPlanEntity ppsPlanEntity = ppsPlanService.getFirstByPlanNo(planNo);
                if (ppsPlanEntity == null) {
                    //新增
                    //计划编号	整车编码	车型	预计上线时间	预计下线时间	开始站点	结束站点
                    PpsPlanEntity et = new PpsPlanEntity();
                    et.setPlanNo(planNo);
                    setPlan(midApiLogService, pmall, et, planBaths, pmProductMaterialMasterProvider);
                    addPlans.add(et);

                    updateData(planBaths, 1, "");
                    upBaths.addAll(planBaths);
                    stats = 1;
                } else if (ppsPlanEntity.getPlanStatus() > 1) {
                    String exeMsg = String.format("保存AS整车计划错误，计划号%s，状态错误", planNo);
                    logger.info(exeMsg);
                    updateData(planBaths, 3, exeMsg);
                    upBaths.addAll(planBaths);
                    stats = 3;
                } else {
                    //修改
                    PpsPlanEntity et = ppsPlanEntity;
                    setPlan(midApiLogService, pmall, et, planBaths, pmProductMaterialMasterProvider);
                    upPlans.add(et);

                    updateData(planBaths, 1, "");
                    upBaths.addAll(planBaths);
                    stats = 1;
                }
            } catch (Exception e) {
                String exeMsg = String.format("保存AS整车计划错误，计划号%s，处理错误。%s", planNo, e.getMessage());
                logger.info(exeMsg);
                logger.error("",e);
                updateData(planBaths, 2, exeMsg);
                upBaths.addAll(planBaths);
                stats = 2;
            }
        }

        //物料号分组处理
        List<PpsPlanEntity> allPlans = new ArrayList<>();
        allPlans.addAll(addPlans);
        allPlans.addAll(upPlans);
        setPlanByProductCode(bomMode,pmProductMaterialMasterProvider,midApiLogService, orgCode,allPlans);

        ppsPlanService.insertBatch(addPlans);
        ppsPlanService.updateBatchById(upPlans);
        this.updateBatchById(upBaths);
        return stats;
    }

    private void setPlanByProductCode(Integer bomMode,PmProductMaterialMasterProvider pmProductMaterialMasterProvider,
            IMidApiLogService midApiLogService ,
                                      String orgCode,
                                      List<PpsPlanEntity> upPlans) {
        Integer tmpModel = bomMode;
        List<String> startProCodes = midApiLogService.getProductCodeStartsWith();
        Map<String, List<PpsPlanEntity>> gps = upPlans.stream()
                .filter(c -> !StringUtils.isBlank(c.getProductCode()))
                .collect(Collectors.groupingBy(PpsPlanEntity::getProductCode));
        for (Map.Entry<String, List<PpsPlanEntity>> gp : gps.entrySet()) {
            String productCode = gp.getKey();
            List<PpsPlanEntity> plans = gp.getValue();
            PpsPlanEntity first = plans.get(0);
            //3 匹配BOM和本地，  2 只匹配BOM ，1 假四段码使用本地  0 本地
            if(tmpModel == 3 || tmpModel == 2) {
                if (startProCodes.stream().anyMatch(productCode::startsWith)) {
                    bomMode = 0; //本地模式 SC6481
                }
            }
            else {
                if (productCode.contains("+00000.") || productCode.endsWith(".000")
                        || startProCodes.stream().anyMatch(productCode::startsWith)
                ) {
                    bomMode = 0; //本地模式 SC6481
                }
            }
            String bomVersion = midApiLogService.getBomVersion(bomMode, orgCode, productCode
                    , DateUtils.format(new Date(), DateUtils.DATE_PATTERN));
            String chartsVersion = midApiLogService.getCharacteristicsVersion(bomMode, productCode);
            String materName = getMaterialName(productCode, pmProductMaterialMasterProvider);
            for (PpsPlanEntity et : plans) {
                //bom
                et.setBomVersion(bomVersion);
                //特征
                et.setCharacteristicVersion(chartsVersion);
                et.setCharacteristic4(materName);
                //设置 选装包、车身色，内饰色
                midApiLogService.setVeExtendInfo(et);

            }
            //同步软件BOM
            getSoftBom(bomMode, midApiLogService, productCode,
                    DateUtils.format(first.getEstimatedStartDt(), DateUtils.DATE_PATTERN));
        }
    }

    private void checkShopCode( PmAllDTO pmall,String shopCode) {
        PmWorkShopEntity shopEntry = pmall.getShops().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getWorkshopCode(), shopCode))
                .findFirst().orElse(null);
        if (shopEntry == null) {
            throw new InkelinkException("车间[" + shopCode + "]不存在");
        }
//        List<String> shops = new ArrayList<>();
//        shops.add(BodyShopCode.WE);
//        shops.add(BodyShopCode.PA);
//        shops.add(BodyShopCode.GA);
//        //BodyShopCode
//        if (!shops.contains(shopCode)) {
//            throw new InkelinkException("车间[" + shopCode + "]编码必须是焊装、涂装、总装中的一种");
//        }
    }

    @Autowired
    private IMidSoftwareBomListService midSoftwareBomListService;
    @Autowired
    private IMidSoftwareBomConfigService midSoftwareBomConfigService;

    /**
     *
     * @param productNo
     * @param effectiveDate
     *
     * */
    private void getSoftBom(Integer bomMode,IMidApiLogService midApiLogService,String productNo,String effectiveDate) {
        if (bomMode != 0) {
            try {
                midSoftwareBomConfigService.getBomConfig(productNo, effectiveDate);
                midSoftwareBomListService.getSoftBom(productNo, effectiveDate);
            } catch (Exception e) {
                logger.error("同步softbom失败", e);
            }
        }
    }

    private void setPlan(
            IMidApiLogService midApiLogService
            , PmAllDTO pmall
            , PpsPlanEntity et
            , List<MidAsVehicleEntity> planBaths
            , PmProductMaterialMasterProvider pmProductMaterialMasterProvider ) {
        //整车多条只取一条（其余是重复数据）
        MidAsVehicleEntity plan = planBaths.get(0);

        checkShopCode(pmall, plan.getVcEntryShopCode());
        checkShopCode(pmall, plan.getVcExitShopCode());

        String productCode = plan.getMaterialCode();
        //白车身逻辑
        boolean isWhiteBody = false;
        if (productCode.startsWith("5000000")) {
            et.setCharacteristic5(plan.getMaterialCode());
            //productCode = plan.getMaterialCode(); //TODO 从备注字段获取整车物料号
            productCode = plan.getAttribute2();
            if(StringUtils.isBlank(productCode)) {
                throw new InkelinkException("白车身【" + plan.getMaterialCode() + "】没有对应的整车物料号");
            }
            //productCode = midApiLogService.getWhitProductCode();
            et.setAttribute2("1"); //白车身
            isWhiteBody = true;
        } else {
            et.setAttribute2("0");
            isWhiteBody = false;
        }
        et.setProductCode(productCode);
        String startAvi = getAviCode(midApiLogService,pmall, "2", plan.getVcEntryWsCode());
        String endAvi = getAviCode(midApiLogService,pmall, "3", plan.getVcExitWsCode());
        //通过BOM系统获取   //SC6501AAABEV.CNH1001-L.+0000001.WW1
        //et.setModel(getModel(productCode));
        //通过接口获取 车辆信息
       /* //bom
        et.setBomVersion(midApiLogService.getBomVersion(plan.getOrgCode(), et.getProductCode(), DateUtils.format(plan.getVcOnlineTime(), DateUtils.DATE_PATTERN)));
        //特征
        et.setCharacteristicVersion(midApiLogService.getCharacteristicsVersion(et.getProductCode()));

        //设置 选装包、车身色，内饰色
        midApiLogService.setVeExtendInfo(et);*/

        et.setPlanQty(1);
        et.setOrderCategory(OrderCategoryEnum.Vehicle.codeString());
     /*   et.setCharacteristic4(getMaterialName(et.getProductCode(), pmProductMaterialMasterProvider));
*/
        PpsProductProcessEntity vprocessInfo = null;
        if (isWhiteBody) {
            //9 白车身
            vprocessInfo = ppsPlanService.getProcess("9");
        } else {
            vprocessInfo = ppsPlanService.getProcess(et.getOrderCategory());
        }

        if (vprocessInfo != null) {
            et.setPrcPpsProductProcessId(vprocessInfo.getId());
        }
        et.setOrderSign(plan.getCustomCode()==null?"":plan.getCustomCode());
        et.setPlanStatus(1);
        et.setPlanSource(1);
        et.setEstimatedStartDt(plan.getVcOnlineTime());
        et.setEstimatedEndDt(plan.getVcOfflineDate());

        et.setStartAvi(startAvi);
        et.setEndAvi(endAvi);
        et.setRemark("从AS下发批次计划");
/*        //同步软件BOM
        getSoftBom(midApiLogService, productCode, DateUtils.format(plan.getVcOnlineTime(), DateUtils.DATE_PATTERN));*/
    }

    private String getMaterialName(String materialCode, PmProductMaterialMasterProvider pmProductMaterialMasterProvider) {
        PmProductMaterialMasterEntity material = pmProductMaterialMasterProvider.getByMaterialNo(materialCode);
        String chara4 = material == null ? StringUtils.EMPTY : material.getMaterialCn();
        return chara4 == null ? StringUtils.EMPTY : chara4;
    }

    private String getModel(String productCode) {
        if (StringUtils.isBlank(productCode)) {
            return "";
        }
        String[] productCodes = productCode.split("\\.");
        return com.ca.mfd.prc.common.utils.StringUtils.trimEnd(productCodes[0], ".");
    }


    /**
     * 根据工位代码获取线体上的开工点和下线点(1.上线点;2.下线点)
     */
    private String getAviCode(IMidApiLogService midApiLogService,PmAllDTO pmall, String aviType, String wsCode) {
        PmAviEntity avi = midApiLogService.getAviByAsTpCode(pmall, wsCode);
                /*pmall.getAvis().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getAviCode(), wsCode))
                .findFirst().orElse(null);*/
        if (avi == null) {
            throw new InkelinkException("AVI代码[" + wsCode + "]不存在");
        }
//        PmAviEntity avi = pmall.getAvis().stream().filter(c -> Objects.equals(c.getPrcPmLineId(), line.getId())
//                && StringUtils.contains(c.getAviType(), aviType)).findFirst().orElse(null);
//        if (avi == null) {
//            throw new InkelinkException("线体[" + lineCode + "]的" + ("2".equals(aviType) ? "上线" : "下线") + "点不存在");
//        }
        return avi.getAviCode();
    }

    private void updateData(List<MidAsVehicleEntity> ups, Integer exeStatus, String exeMsg) {
        if (ups == null || ups.isEmpty()) {
            return;
        }
        for (MidAsVehicleEntity et : ups) {
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