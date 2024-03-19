package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.entity.MidAsShopPlanEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidAsShopPlanMapper;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.communication.service.IMidAsShopPlanService;
import com.ca.mfd.prc.pps.entity.PpsPlanAviEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsPlanAviService;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AS车间计划服务实现
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@Service
public class MidAsShopPlanServiceImpl extends AbstractCrudServiceImpl<IMidAsShopPlanMapper, MidAsShopPlanEntity> implements IMidAsShopPlanService {

    private static final Logger logger = LoggerFactory.getLogger(MidAsBathPlanServiceImpl.class);
    @Autowired
    private IPpsPlanAviService ppsPlanAviService;
    @Autowired
    private IPpsPlanService ppsPlanService;


    /**
     * 获取计划
     *
     * @param logid
     * @return
     */
    @Override
    public List<MidAsShopPlanEntity> getListByLog(Long logid) {
        QueryWrapper<MidAsShopPlanEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidAsShopPlanEntity::getPrcMidApiLogId, logid);
        return selectList(qry);
    }

    /**
     * 获取计划号对应车间计划LOGid
     *
     * @param vrn
     * @param releaseVer
     * @return
     */
    @Override
    public Long getLogId(String vrn, String releaseVer) {
        QueryWrapper<MidAsShopPlanEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidAsShopPlanEntity::getVrn, vrn)
                .eq(MidAsShopPlanEntity::getReleaseVer, releaseVer);
        MidAsShopPlanEntity firstData = getTopDatas(1, qry).stream().findFirst().orElse(null);
        return firstData == null ? Constant.DEFAULT_ID : firstData.getPrcMidApiLogId();
    }


    /**
     * 执行数据处理逻辑
     */
    @Override
    public String excute(String logid) {
        IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        PmVersionProvider pmVersionProvider = SpringContextUtils.getBean(PmVersionProvider.class);
        List<MidApiLogEntity> apilogs = midApiLogService.getDoList(ApiTypeConst.AS_SHOP_PLAN, ConvertUtils.stringToLong(logid));
        if (apilogs == null || apilogs.isEmpty()) {
            return "";
        }
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

                List<MidAsShopPlanEntity> datas = this.getListByLog(apilog.getId());
                Integer stats = this.save(midApiLogService,pmall,datas);
                this.saveChange();
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

    private Integer save(IMidApiLogService midApiLogService,PmAllDTO pmall, List<MidAsShopPlanEntity> list) {
        if (list == null || list.isEmpty()) {
            return 1;
        }
        Integer stats = 1;
        List<MidAsShopPlanEntity> upBaths = new ArrayList<>();
        List<PpsPlanAviEntity> addPlans = new ArrayList<>();
        Map<String, List<MidAsShopPlanEntity>> gps = list.stream().collect(Collectors.groupingBy(MidAsShopPlanEntity::getVrn));
        for (Map.Entry<String, List<MidAsShopPlanEntity>> gp : gps.entrySet()) {
            String planNo = gp.getKey();
            List<MidAsShopPlanEntity> planBaths = gp.getValue();
            if (planBaths == null || planBaths.isEmpty() || StringUtils.isBlank(planNo)) {
                logger.info("保存AS车间计划错误，计划号为空{}，或对应数据为空", planNo);
                continue;
            }
            PpsPlanEntity ppsPlanEntity = ppsPlanService.getFirstByPlanNo(planNo);
            List<PpsPlanAviEntity> ets = new ArrayList<>();
            String exeMsgSet = "";
            try {
                ets = setPlan(midApiLogService,pmall, planBaths);
            }catch (Exception e) {
                exeMsgSet = e.getMessage();
                logger.error("", e);
            }
            if(ets == null || ets.isEmpty()) {
                String exeMsg = String.format("保存AS车间计划错误，计划号%s，数据无效。", planNo) + exeMsgSet;
                logger.info(exeMsg);
                updateData(planBaths, 2, exeMsg);
                upBaths.addAll(planBaths);
                stats = 3;
                continue;
            }
            if (ppsPlanEntity == null) {
                String exeMsg = String.format("保存AS车间计划错误，计划号%s，对应计划数据为空", planNo);
                logger.info(exeMsg);
                updateData(planBaths, 2, exeMsg);
                upBaths.addAll(planBaths);
                stats = 2;
                continue;
            }
            if (ppsPlanEntity.getPlanStatus() > 1) {
                String exeMsg = String.format("保存AS车间计划错误，计划号%s，状态错误", planNo);
                logger.info(exeMsg);
                updateData(planBaths, 3, exeMsg);
                upBaths.addAll(planBaths);
                stats = 3;
                continue;
            }

            UpdateWrapper<PpsPlanAviEntity> upplanavi = new UpdateWrapper<>();
            upplanavi.lambda().eq(PpsPlanAviEntity::getPlanNo, planNo);
            ppsPlanAviService.delete(upplanavi);

            ets = ets.stream().distinct().collect(Collectors.toList());
            if (!ets.isEmpty()) {
                addPlans.addAll(ets);
            }
            updateData(planBaths, 1, "");
            upBaths.addAll(planBaths);
            stats = 1;
        }
        ppsPlanAviService.insertBatch(addPlans);
        this.updateBatchById(upBaths);
        return stats;
    }

    private List<PpsPlanAviEntity> setPlan(IMidApiLogService midApiLogService,PmAllDTO pmall, List<MidAsShopPlanEntity> planBaths) {
        List<PpsPlanAviEntity> ets = new ArrayList<>();
        if (planBaths == null || planBaths.isEmpty()) {
            return ets;
        }
        planBaths.sort(Comparator.comparing(MidAsShopPlanEntity::getPlanTime));
        int seqno = 1;
        for (MidAsShopPlanEntity shopplan : planBaths) {
            //计划编号    车间编码   线体编码     avi编码    avi类型    班次  工作日      过点时间    顺序号    计划上线时间
            PpsPlanAviEntity et = new PpsPlanAviEntity();
            et.setPlanNo(shopplan.getVrn());
            PmWorkShopEntity shop = pmall.getShops().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getWorkshopCode(), shopplan.getSchedShopCode()))
                    .findFirst().orElse(null);
            if (shop == null) {
                throw new InkelinkException("车间[" + shopplan.getSchedShopCode() + "]不存在");
            }
            et.setWorkshopCode(shop.getWorkshopCode());

//            PmLineEntity line = pmall.getLines().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getLineCode(), shopplan.getSchedLineCode()))
//                    .findFirst().orElse(null);
//            if (line == null) {
//                throw new InkelinkException("线体[" + shopplan.getSchedLineCode() + "]不存在");
//            }
//            et.setLineCode(shopplan.getSchedLineCode());

            PmAviEntity avi = midApiLogService.getAviByAsTpCode(pmall, shopplan.getSchedWsCode());
             /*       pmall.getAvis().stream().filter(c -> StringUtils.equalsIgnoreCase(c.getAviCode(), shopplan.getSchedWsCode()))
                    .findFirst().orElse(null);*/
            if (avi == null) {
                throw new InkelinkException("AVI点[" + shopplan.getSchedWsCode() + "]不存在");
            }
            et.setAttribute1(shopplan.getSchedWsCode());
            PmLineEntity line = pmall.getLines().stream().filter(c -> Objects.equals(c.getId(), avi.getPrcPmLineId()))
                    .findFirst().orElse(null);
            if (line == null) {
                throw new InkelinkException("线体[" + avi.getPrcPmLineId() + "]不存在");
            }
            et.setLineCode(line.getLineCode());
            et.setAviCode(avi.getAviCode());
            //1.正常;2.上线;3. 下线;4.开工；5.完工
            if (Arrays.asList("2", "4").stream().anyMatch(c -> avi.getAviType().contains(c))) {
                et.setAviType("2");
            } else if (Arrays.asList("3", "5").stream().anyMatch(c -> avi.getAviType().contains(c))) {
                et.setAviType("3");
            } else {
                et.setAviType("1");
            }
            et.setShiftCode(shopplan.getPlanShift());
            et.setWorkDay(shopplan.getPlanDate());
            et.setPassDt(shopplan.getPlanTime());
//            if (StringUtils.isBlank(shopplan.getSequenceNo())) {
//                et.setSequenceNo(seqno);
//            } else {
//                et.setSequenceNo(Integer.parseInt(shopplan.getSequenceNo()));
//            }
            //et.setSequenceNo(seqno); //排重后赋值
            et.setSequenceNo(Integer.parseInt(shopplan.getSequenceNo()));
            et.setBeginDt(shopplan.getPlanTime());
            et.setEndDt(shopplan.getPlanTime());

            //et.setAttribute1("AS下发计划");
            et.setAttribute2("AS");
            ets.add(et);

        }
        //排重
        ets = ets.stream().distinct().sorted(Comparator.comparing(PpsPlanAviEntity::getBeginDt)).collect(Collectors.toList());
//        for (PpsPlanAviEntity et : ets) {
//            et.setSequenceNo(seqno);
//            seqno++;
//        }
        return ets;
    }

    private void updateData(List<MidAsShopPlanEntity> ups, Integer exeStatus, String exeMsg) {
        if (ups == null || ups.isEmpty()) {
            return;
        }
        for (MidAsShopPlanEntity et : ups) {
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