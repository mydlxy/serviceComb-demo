package com.ca.mfd.prc.pps.communication.process;

import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.communication.service.IMidAsShopPlanService;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmProductMaterialMasterProvider;
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

/**
 * AS车间计划处理
 *
 * @Author: eric
 * @Date: 2023-08-16-14:41
 * @Description:
 */
@Component
//@RefreshScope
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pps.asshopplan.enable'))}")
public class AsShopPlanProcess implements ApplicationRunner, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(AsShopPlanProcess.class);
    /**
     * 线程执行间隔
     */
    private static final int TIME_INTERVAL = 1000 * 10;

    @Value("${inkelink.pps.asshopplan.isstart:1}")
    private String processIsStart;
    @Value("${inkelink.pps.asshopplan.enable:false}")
    private Boolean enable;
    @Resource
    private TaskExecutor task;

    @Autowired
    private IMidApiLogService midApiLogService;
    @Autowired
    private IMidAsShopPlanService midAsShopPlanService;
    @Autowired
    private PmProductMaterialMasterProvider pmProductMaterialMasterProvider;


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
                    startAsShopPlan();
                } catch (InterruptedException e) {
                    logger.info("服务器启动失败,错误消息 {}", e.getMessage());
                }
            });
            logger.info("服务启动成功");
        } catch (Exception exception) {
            logger.info("服务器启动失败,错误消息 {}", exception.getMessage());
        }
    }

    public void startAsShopPlan() throws InterruptedException {
        logger.info("startAsBathPlan启动");
        while (true) {
            try {
                if (Boolean.FALSE.equals(enable)) {
                    logger.info("startAsBathPlan停止");
                    break;
                }
                if (!"1".equals(processIsStart)) {
                    logger.info("startAsBathPlan暂停");
                    Thread.sleep(TIME_INTERVAL);
                    continue;
                }
                logger.info("startAsBathPlan开始");
                midAsShopPlanService.excute("");
            } catch (InterruptedException exception) {
                logger.debug("异常：{}", exception.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception exception) {
                logger.debug("异常：{}", exception.getMessage());
            }
            Thread.sleep(TIME_INTERVAL);
        }
        logger.info("startAsBathPlan结束");
    }

    @Override
    public void destroy() throws Exception {
        logger.info("关闭执行");
        this.processIsStart = String.valueOf(0);
        this.enable = false;
    }
}
