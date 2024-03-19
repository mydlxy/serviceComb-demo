package com.ca.mfd.prc.scheduling.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 自定义静态工具类
 */
@Component
public class AppContextUtil implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(AppContextUtil.class);

    //定义静态ApplicationContext
    private static ApplicationContext applicationContext = null;

    //获取 applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 重写接口方法，该方法的参数为框架自动加载的IOC容器对象
     * 该方法在启动项目的时候会自动执行，前提是该类上有IOC相关注解，例如@Component
     *
     * @param applicationContext IOC容器
     * @throws BeansException e
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //将框架加载的IOC赋值给全局静态IOC
        AppContextUtil.applicationContext = applicationContext;
        logger.info("==================ApplicationContext加载-----------------");
    }

    //通过name获取Bean
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    //通过class获取Bean
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    //通过name、Clazz返回指定Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }


}
