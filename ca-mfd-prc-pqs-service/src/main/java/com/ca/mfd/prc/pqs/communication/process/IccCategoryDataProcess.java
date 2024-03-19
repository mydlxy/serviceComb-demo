package com.ca.mfd.prc.pqs.communication.process;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.pqs.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pqs.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pqs.communication.entity.MidIccCategoryApiEntity;
import com.ca.mfd.prc.pqs.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pqs.communication.service.IMidIccCategoryApiService;
import com.ca.mfd.prc.pqs.service.IPqsLogicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 启动进程
 *
 * @Author: joel
 * @Date: 2023-08-16-14:41
 * @Description:
 */
@Component
//@RefreshScope
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pqs.icccategory.enable'))}")
public class IccCategoryDataProcess implements ApplicationRunner, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(IccCategoryDataProcess.class);
    /**
     * 线程执行间隔
     */
    private static final int TIME_INTERVAL = 1000 * 10;
    @Value("${inkelink.pqs.icccategory.isstart:1}")
    private String processIsStart;
    @Value("${inkelink.pqs.icccategory.enable:false}")
    private Boolean enable;
    @Resource
    private TaskExecutor task;

    @Autowired
    private IMidApiLogService apiLogService;

    @Autowired
    private IMidIccCategoryApiService iccCategoryApiService;

    @Autowired
    private IPqsLogicService pqsLogicService;



    /**
     * run
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {

        try {
            /**  启用线程@PostConstruct  */
            task.execute(() -> {
                try {
                    syncData();
                } catch (InterruptedException e) {
                    logger.info("服务器启动失败,错误消息 {}", e.getMessage());
                }
            });
            logger.info("服务启动成功");
        } catch (Exception exception) {
            logger.info("服务器启动失败,错误消息 {}", exception.getMessage());
        }
    }

    private void syncData() throws InterruptedException {
        logger.info("startICC_ICCCATEGORY启动");
        while (true) {
            try {
                if (Boolean.FALSE.equals(enable)) {
                    logger.info("startICC_ICCCATEGORY停止");
                    break;
                }
                if (!"1".equals(processIsStart)) {
                    logger.info("startICC_ICCCATEGORY暂停");
                    Thread.sleep(TIME_INTERVAL);
                    continue;
                }
                List<MidApiLogEntity> apilogs = apiLogService.getSyncList(ApiTypeConst.PMS_ICC_CATEGORY);
                if (apilogs == null || apilogs.isEmpty()) {
                    Thread.sleep(TIME_INTERVAL);
                    continue;
                }
                for (MidApiLogEntity apilog : apilogs) {
                    boolean success = false;
                    try {
                        UpdateWrapper<MidApiLogEntity> uplogStart = new UpdateWrapper<>();
                        uplogStart.lambda().set(MidApiLogEntity::getStatus, 4)
                                .eq(MidApiLogEntity::getId, apilog.getId());
                        apiLogService.update(uplogStart);
                        apiLogService.saveChange();

                        List<MidIccCategoryApiEntity> datas = iccCategoryApiService.getListByLog(apilog.getId());
                        pqsLogicService.receiveIccCategaryData(datas);
                        success = true;

                    } catch (Exception exception) {
                        logger.debug("数据保存异常：{}", exception.getMessage());
                        apiLogService.clearChange();
                    }
                    try {
                        UpdateWrapper<MidApiLogEntity> uplogEnd = new UpdateWrapper<>();
                        uplogEnd.lambda().set(MidApiLogEntity::getStatus, success ? 5 : 6)
                                .eq(MidApiLogEntity::getId, apilog.getId());
                        apiLogService.saveChange();
                    } catch (Exception exception) {
                        logger.debug("日志保存异常：{}", exception.getMessage());
                    }
                }
            } catch (InterruptedException exception) {
                logger.debug("异常：{}", exception.getMessage());
                Thread.currentThread().interrupt();
            }catch (Exception exception) {
                logger.debug("异常：{}", exception.getMessage());
            }
            Thread.sleep(TIME_INTERVAL);
            logger.info("startICC_ICCCATEGORY结束");

        }
    }



    @Override
    public void destroy() throws Exception {
        logger.info("关闭执行");
        this.processIsStart = String.valueOf(0);
        this.enable = false;
    }
}
