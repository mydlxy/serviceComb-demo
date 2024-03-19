package com.ca.mfd.prc.pm.communication.process;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsLineCalendarEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsShopCalendarEntity;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.communication.service.IMidAsLineCalendarService;
import com.ca.mfd.prc.pm.communication.service.IMidAsShopCalendarService;
import com.ca.mfd.prc.pm.dto.CalendarFromASDTO;
import com.ca.mfd.prc.pm.service.IPmShcCalendarService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * AS日历处理
 *
 * @Author: eric
 * @Date: 2023-08-16-14:41
 * @Description:
 */
@Component
//@RefreshScope
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pm.ascalendar.enable'))}")
public class AsShcCalendarProcess implements ApplicationRunner, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(AsShcCalendarProcess.class);
    /**
     * 线程执行间隔
     */
    private static final int TIME_INTERVAL = 1000 * 10;

    @Value("${inkelink.pm.ascalendar.isstart:1}")
    private String processIsStart;
    @Value("${inkelink.pm.ascalendar.enable:false}")
    private Boolean enable;
    @Resource
    private TaskExecutor task;

    @Autowired
    private IMidAsLineCalendarService midAsLineCalendarService;

    /**
     * run
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        try {
            /**  车间日历  */
            task.execute(() -> {
                try {
                    startAsShcShopCalendar();
                } catch (InterruptedException e) {
                    logger.info("服务器启动失败,错误消息 {}", e.getMessage());
                }
            });
            /**  线体日历  */
            task.execute(() -> {
                try {
                    startAsShcLineCalendar();
                } catch (InterruptedException e) {
                    logger.info("服务器启动失败,错误消息 {}", e.getMessage());
                }
            });
            logger.info("服务启动成功");
        } catch (Exception exception) {
            logger.info("服务器启动失败,错误消息 {}", exception.getMessage());
        }
    }

    /**
     * AS车间日历处理
     */
    public void startAsShcShopCalendar() throws InterruptedException {
        logger.info("startAsShcShopCalendar启动");
        while (true) {
            try {
                if (Boolean.FALSE.equals(enable)) {
                    logger.info("startAsShcShopCalendar停止");
                    break;
                }
                if (!"1".equals(processIsStart)) {
                    logger.info("startAsShcShopCalendar暂停");
                    Thread.sleep(TIME_INTERVAL);
                    continue;
                }
                logger.info("startAsShcShopCalendar开始");
                midAsLineCalendarService.excute("");

            } catch (InterruptedException exception) {
                logger.debug("异常：{}", exception.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception exception) {
                logger.debug("异常：{}", exception.getMessage());
            }
            Thread.sleep(TIME_INTERVAL);
        }
        logger.info("startAsShcShopCalendar结束");
    }

    /**
     * AS线体日历处理
     */
    public void startAsShcLineCalendar() throws InterruptedException {
        logger.info("startAsShcLineCalendar启动");
        while (true) {
            try {
                if (Boolean.FALSE.equals(enable)) {
                    logger.info("startAsShcLineCalendar停止");
                    break;
                }
                if (!"1".equals(processIsStart)) {
                    logger.info("startAsShcLineCalendar暂停");
                    Thread.sleep(TIME_INTERVAL);
                    continue;
                }
                logger.info("startAsShcLineCalendar开始");


            } catch (InterruptedException exception) {
                logger.debug("异常：{}", exception.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception exception) {
                logger.debug("异常：{}", exception.getMessage());
            }
            Thread.sleep(TIME_INTERVAL);
        }
        logger.info("startAsShcLineCalendar结束");
    }


    @Override
    public void destroy() throws Exception {
        logger.info("关闭执行");
        this.processIsStart = String.valueOf(0);
        this.enable = false;
    }
}
