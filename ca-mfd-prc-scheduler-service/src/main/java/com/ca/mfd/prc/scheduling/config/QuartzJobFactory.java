package com.ca.mfd.prc.scheduling.config;


import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

//@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.scheduling.quartz.enable'))}")
@Component
public class QuartzJobFactory extends AdaptableJobFactory {

    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        //调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        //进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
