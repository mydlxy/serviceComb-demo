package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pps.communication.service.IPlanAutoLockService;
import com.ca.mfd.prc.pps.constant.Constant;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanLockConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pps.service.IPpsEntryPartsService;
import com.ca.mfd.prc.pps.service.IPpsPlanLockConfigService;
import com.ca.mfd.prc.pps.service.IPpsPlanPartsService;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author inkelink
 * @Description: 接口记录表服务实现
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class PlanAutoLockServiceImpl implements IPlanAutoLockService {

    private static final Logger logger = LoggerFactory.getLogger(PlanAutoLockServiceImpl.class);

    /**
     * 执行数据处理逻辑
     */
    @Override
    public String autolockPartsPlan(Integer orderCategory) {
        IPpsPlanPartsService ppsPlanPartsService = SpringContextUtils.getBean(IPpsPlanPartsService.class);
        IPpsPlanLockConfigService ppsPlanLockConfigService = SpringContextUtils.getBean(IPpsPlanLockConfigService.class);
        String errorMsg = "";
        //没有锁定的计划(排除预计上线时间为null) 每次处理100条
        int pageSize = 100;
        IPage<PpsPlanPartsEntity> planPage = ppsPlanPartsService.getNoLockWith(1, pageSize, orderCategory);
        if (planPage == null || planPage.getTotal() == 0) {
            return "";
        }
        PpsPlanLockConfigEntity planLockConfig = ppsPlanLockConfigService.getAllDatas()
                .stream().filter(c -> Objects.equals(c.getOrderCategory(), orderCategory)).findFirst().orElse(null);
        if (planLockConfig == null) {
            return "未找到分类:" + orderCategory + "对应自动拆分配置";
        }
        errorMsg = lockPartsPlan(ppsPlanPartsService, planPage.getRecords(), planLockConfig);
        //从第二页开始
        for (int index = 1; index < planPage.getPages(); index++) {
            planPage = ppsPlanPartsService.getNoLockWith(index + 1, pageSize, orderCategory);
            if (planPage == null || planPage.getTotal() == 0) {
                continue;
            }
            String errorMsg2 = lockPartsPlan(ppsPlanPartsService, planPage.getRecords(), planLockConfig);
            if (!StringUtils.isBlank(errorMsg2)) {
                errorMsg = errorMsg2;
            }
        }
        return errorMsg;
    }

    private String lockPartsPlan(IPpsPlanPartsService ppsPlanPartsService, List<PpsPlanPartsEntity> plans, PpsPlanLockConfigEntity planLockConfig) {
        if (plans == null || plans.isEmpty() ) {
            return "";
        }
        for (PpsPlanPartsEntity plan : plans) {
            if (isUpDate(plan.getEstimatedStartDt(), planLockConfig.getAdvancedTime())) {
                try {
                    logger.info("startPlanPartsLock锁定计划:{}", plan.getPlanNo());
                    synchronized (Constant.planLockEntryReckon) {
                        ppsPlanPartsService.planPartsAutoLock(plan);
                        //保存时间 ppsPlanService.updateBatchById(plans);
                        ppsPlanPartsService.saveChange();
                    }
                    List<Long> planIds = new ArrayList<>();
                    planIds.add(plan.getId());
                    ppsPlanPartsService.sendLmsModuleLockPlan(planIds);

                } catch (Exception exception) {
                    logger.error(plan.getPlanNo() + ":PlanLock异常：{}", exception.getMessage());
                    return exception.getMessage();
                }
                logger.info("endPlanParts锁定计划:{}", plan.getPlanNo());
            }
        }
        return "";
    }

    /**
     * 执行数据处理逻辑
     */
    @Override
    public String excute(Integer status) {
        IPpsEntryPartsService ppsEntryPartsService = SpringContextUtils.getBean(IPpsEntryPartsService.class);
        IPpsPlanService ppsPlanService = SpringContextUtils.getBean(IPpsPlanService.class);
        IPpsPlanLockConfigService ppsPlanLockConfigService = SpringContextUtils.getBean(IPpsPlanLockConfigService.class);
        IPpsPlanPartsService ppsPlanPartsService = SpringContextUtils.getBean(IPpsPlanPartsService.class);

        List<PpsPlanLockConfigEntity> planLockConfigs = ppsPlanLockConfigService.getAllDatas();
        if (planLockConfigs == null || planLockConfigs.isEmpty()) {
            return "";
        }
        String errorMsg = "";
        for (PpsPlanLockConfigEntity lockCfg : planLockConfigs) {
            if (Arrays.asList(1, 2, 7).contains(lockCfg.getOrderCategory())) {
                //没有锁定的计划(排除预计上线时间为null) 每次处理100条
                int pageSize = 100;
                IPage<PpsPlanEntity> planPage = ppsPlanService.getNoLockWith(1, pageSize, lockCfg.getOrderCategory().toString());
                if (planPage == null || planPage.getTotal() == 0) {
                    continue;
                }
                errorMsg = lockPlan(ppsPlanService, planPage.getRecords(), lockCfg);
                //从第二页开始
                for (int index = 1; index < planPage.getPages(); index++) {
                    planPage = ppsPlanService.getNoLockWith(index + 1, pageSize, lockCfg.getOrderCategory().toString());
                    if (planPage == null || planPage.getTotal() == 0) {
                        continue;
                    }
                    String errorMsg2 = lockPlan(ppsPlanService, planPage.getRecords(), lockCfg);
                    if (!StringUtils.isBlank(errorMsg2)) {
                        errorMsg = errorMsg2;
                    }
                }
            } else {
                int pageSize = 100;
                IPage<PpsPlanPartsEntity> planPage = ppsPlanPartsService.getNoLockWith(1, pageSize,lockCfg.getOrderCategory());
                //IPage<PpsEntryPartsEntity> planPage = ppsEntryPartsService.getNoLockWith(status,1, pageSize, lockCfg.getOrderCategory().toString());
                if (planPage == null || planPage.getTotal() == 0) {
                    continue;
                }
                errorMsg = lockPartsPlan(ppsPlanPartsService,planPage.getRecords(), lockCfg);
                //errorMsg = lockPartsEntry(status,ppsEntryPartsService, planPage.getRecords(), lockCfg);
                //从第二页开始
                for (int index = 1; index < planPage.getPages(); index++) {
                    planPage = ppsPlanPartsService.getNoLockWith(index + 1, pageSize, lockCfg.getOrderCategory());
                    //planPage = ppsEntryPartsService.getNoLockWith(status,index + 1, pageSize, lockCfg.getOrderCategory().toString());
                    if (planPage == null || planPage.getTotal() == 0) {
                        continue;
                    }
                    String errorMsg2 = lockPartsPlan(ppsPlanPartsService,planPage.getRecords(), lockCfg);
                    //String errorMsg2 = lockPartsEntry(status,ppsEntryPartsService, planPage.getRecords(), lockCfg);
                    if (!StringUtils.isBlank(errorMsg2)) {
                        errorMsg = errorMsg2;
                    }
                }

            }
        }
        return errorMsg;
    }

    private String lockPartsEntry(Integer status,IPpsEntryPartsService ppsEntryPartsService, List<PpsEntryPartsEntity> plans, PpsPlanLockConfigEntity planLockConfig) {
        if (plans == null || plans.isEmpty() || planLockConfig == null || planLockConfig.getAdvancedTime() == null) {
            return "";
        }
        for (PpsEntryPartsEntity plan : plans) {
            if (isUpDate(plan.getEstimatedStartDt(), planLockConfig.getAdvancedTime())) {
                try {
                    logger.info("startPartsEntryLock锁定零部件工单:{}", plan.getEntryNo());
                    ppsEntryPartsService.entryLock(status,plan.getId());
                    //保存时间 ppsPlanService.updateBatchById(plans);
                    ppsEntryPartsService.saveChange();

                } catch (Exception exception) {
                    logger.error(plan.getPlanNo() + ":PlanLock异常：{}", exception.getMessage());
                    return exception.getMessage();
                }
                logger.info("endPartsEntryLock锁定零部件工单:{}", plan.getEntryNo());
            }
        }
        return "";
    }

    private String lockPlan(IPpsPlanService ppsPlanService, List<PpsPlanEntity> plans, PpsPlanLockConfigEntity planLockConfig) {
        if (plans == null || plans.isEmpty() || planLockConfig == null || planLockConfig.getAdvancedTime() == null) {
            return "";
        }
        for (PpsPlanEntity plan : plans) {
            if (isUpDate(plan.getEstimatedStartDt(), planLockConfig.getAdvancedTime())) {
                try {
                    logger.info("startPlanLock锁定计划:{}", plan.getPlanNo());
                    ppsPlanService.planLock(Arrays.asList(plan.getPlanNo()), 1);
                    //保存时间 ppsPlanService.updateBatchById(plans);
                    ppsPlanService.saveChange();

                } catch (Exception exception) {
                    logger.error(plan.getPlanNo() + ":PlanLock异常：{}", exception.getMessage());
                    return exception.getMessage();
                }
                logger.info("endPlanLock锁定计划:{}", plan.getPlanNo());
            }
        }
        return "";
    }

    private boolean isUpDate(Date dt, Integer mintus) {
        if (dt == null || mintus == null || mintus == 0) {
            return false;
        }
        String now = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN);
        String endTm = DateUtils.format(dt, DateUtils.DATE_TIME_PATTERN);
        try {
            return DateUtils.getTimeTotalSeconds(now, endTm) <= mintus * 60L;
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }
    }


}