package com.ca.mfd.prc.pm.communication.process;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidPmCountryEntity;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.communication.service.IMidPmCountryService;
import com.ca.mfd.prc.pm.service.IPmOrganizationService;
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
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.pm.country.enable'))}")
public class CountryProcess implements ApplicationRunner, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(CountryProcess.class);
    /**
     * 线程执行间隔
     */
    private static final int TIME_INTERVAL = 1000 * 10;
    @Value("${inkelink.pm.country.isstart:1}")
    private String processIsStart;
    @Value("${inkelink.pm.country.enable:false}")
    private Boolean enable;
    @Resource
    private TaskExecutor task;

    @Autowired
    private IMidPmCountryService countryService;


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
        logger.info("startcountry启动");
        while (true) {
            try {
                if (Boolean.FALSE.equals(enable)) {
                    logger.info("startcountry停止");
                    break;
                }
                if (!"1".equals(processIsStart)) {
                    logger.info("startcountry暂停");
                    Thread.sleep(TIME_INTERVAL);
                    continue;
                }
                countryService.excute("");

            } catch (InterruptedException exception) {
                logger.debug("异常：{}", exception.getMessage());
                Thread.currentThread().interrupt();
            } catch (Exception exception) {
                logger.debug("异常：{}", exception.getMessage());
            }
            Thread.sleep(TIME_INTERVAL);
            logger.info("startcountry结束");

        }
    }


    @Override
    public void destroy() throws Exception {
        logger.info("关闭执行");
        this.processIsStart = String.valueOf(0);
        this.enable = false;
    }
}
