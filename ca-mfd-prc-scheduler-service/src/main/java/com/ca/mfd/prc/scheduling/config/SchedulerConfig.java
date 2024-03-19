package com.ca.mfd.prc.scheduling.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;

//@ConditionalOnExpression("#{'true'.equals(environment.getProperty('inkelink.core.mq.scheduling.quartz.enable'))}")
@Configuration
public class SchedulerConfig {
    @Autowired
    private QuartzJobFactory jobFactory;
    @Value("${org.quartz.dataSource.mesDS.URL}")
    private String url;
    @Value("${org.quartz.dataSource.mesDS.user}")
    private String user;
    @Value("${org.quartz.dataSource.mesDS.password}")
    private String password;
    @Value("${org.quartz.jobStore.tablePrefix}")
    private String tablePrefix;

    /**
     * 调度器
     *
     * @return
     * @throws Exception
     */
    @Bean
    public Scheduler scheduler() throws Exception {
        Scheduler scheduler = schedulerFactoryBean().getScheduler();
        return scheduler;
    }

    /**
     * Scheduler工厂类
     *
     * @return
     * @throws IOException
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
       /* PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();*/

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //  factory.setSchedulerName("prc_cluster_scheduler");
        //  factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setApplicationContextSchedulerContextKey("applicationContext");
        factory.setTaskExecutor(schedulerThreadPool());
        factory.setQuartzProperties(quartzProperties());
        // factory.setQuartzProperties(propertiesFactoryBean.getObject());
        factory.setStartupDelay(10);// 延迟10s执行

        factory.setWaitForJobsToCompleteOnShutdown(true);//这样当spring关闭时，会等待所有已经启动的quartz job结束后spring才能完全shutdown。
        factory.setOverwriteExistingJobs(false);//是否覆盖己存在的Job
        return factory;
    }

    public Properties quartzProperties() throws IOException {
        //使用Spring的PropertiesFactoryBean对属性配置文件进行管理
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        // propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();

        Properties properties = propertiesFactoryBean.getObject();
        properties.setProperty("org.quartz.scheduler.instanceName", "prc_cluster_scheduler");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.scheduler.wrapJobExecutionInUserTransaction", "false");
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quartz.threadPool.threadCount", "10");
        properties.setProperty("org.quartz.threadPool.threadPriority", "5");
        properties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
        // properties.setProperty("org.quartz.jobStore.useProperties", "true");
        properties.setProperty("org.quartz.jobStore.tablePrefix", tablePrefix);
        properties.setProperty("org.quartz.jobStore.misfireThreshold", "6000");
        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", "2000");
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");

        properties.setProperty("org.quartz.jobStore.dataSource", "mesDS");
        properties.setProperty("org.quartz.dataSource.mesDS.connectionProvider.class", "com.ca.mfd.prc.scheduling.config.DruidConnectionProvider");
        properties.setProperty("org.quartz.dataSource.mesDS.driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("org.quartz.dataSource.mesDS.URL", url);
        properties.setProperty("org.quartz.dataSource.mesDS.user", user);

        properties.setProperty("org.quartz.dataSource.mesDS.password", password);
        properties.setProperty("org.quartz.dataSource.mesDS.maxConnection", "5");

        // 账号密码解密
        /*Crypter crypter = CrypterFactory.getCrypter(CrypterFactory.AES_CBC);
        String user = properties.getProperty("org.quartz.dataSource.qzDS.user");
        if (user != null) {
            user = crypter.decrypt(user);
            properties.setProperty("org.quartz.dataSource.qzDS.user", user);
        }
        String password = properties.getProperty("org.quartz.dataSource.qzDS.password");
        if (password != null) {
            password = crypter.decrypt(password);
            properties.setProperty("org.quartz.dataSource.qzDS.password", password);
        }*/

        return properties;
    }

    /**
     * 配置Schedule线程池
     *
     * @return
     */
    @Bean
    public Executor schedulerThreadPool() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors());
        return executor;
    }


}
