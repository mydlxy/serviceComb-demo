package com.ca.mfd.prc.pm.communication.process;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsShiftEntity;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.communication.service.IMidAsShiftService;
import com.ca.mfd.prc.pm.service.IPmShcShiftService;
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
 * AS班次处理
 *
 * @Author: eric
 * @Date: 2023-08-16-14:41
 * @Description:
 */
@Component
//@RefreshScope
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pm.asshift.enable'))}")
public class AsShcShiftProcess implements ApplicationRunner, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(AsShcShiftProcess.class);
    /**
     * 线程执行间隔
     */
    private static final int TIME_INTERVAL = 1000 * 10;

    @Value("${inkelink.pm.asshift.isstart:1}")
    private String processIsStart;
    @Value("${inkelink.pm.asshift.enable:false}")
    private Boolean enable;
    @Resource
    private TaskExecutor task;

    @Autowired
    private IMidAsShiftService midAsShiftService;

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
                    startAsShift();
                } catch (InterruptedException e) {
                    logger.info("服务器启动失败,错误消息 {}", e.getMessage());
                }
            });
            logger.info("服务启动成功");
        } catch (Exception exception) {
            logger.info("服务器启动失败,错误消息 {}", exception.getMessage());
        }
    }

    public void startAsShift() throws InterruptedException {
        logger.info("startAsShift启动");
        while (true) {
            try {
                if (Boolean.FALSE.equals(enable)) {
                    logger.info("startAsShift停止");
                    break;
                }
                if (!"1".equals(processIsStart)) {
                    logger.info("startAsShift暂停");
                    Thread.sleep(TIME_INTERVAL);
                    continue;
                }
                logger.info("startAsShift开始");
                midAsShiftService.excute("");

            } catch (InterruptedException exception) {
                logger.debug("异常：{}", exception.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception exception) {
                logger.debug("异常：{}", exception.getMessage());
            }
            Thread.sleep(TIME_INTERVAL);
        }
        logger.info("startAsShift结束");
    }

    @Override
    public void destroy() throws Exception {
        logger.info("关闭执行");
        this.processIsStart = String.valueOf(0);
        this.enable = false;
    }
}
