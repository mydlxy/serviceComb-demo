package com.ca.mfd.prc.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程配置
 *
 * @author inkelink eric.zhou
 * @date 2023-08-20
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsycTaskConfig {

    @Value("${asyc-task.corePoolSize:10}")
    private int corePoolSize;

    @Value("${asyc-task.maxPoolSize:3000}")
    private int maxPoolSize;

    @Value("${asyc-task.queueCapacity:3000}")
    private int queueCapacity;

    @Value("${asyc-task.threadNamePrefix:inkelinks-asyctask-}")
    private String threadNamePrefix;

    @Value("${asyc-task.keepAliveSeconds:60}")
    private int keepAliveSeconds;

    @Value("${asyc-task.allowCoreThreadTimeOut:false}")
    private boolean allowCoreThreadTimeOut;

    @Value("${asyc-task.waitForTasksToComplete:false}")
    private boolean waitForTasksToComplete;

    @Value("${asyc-task.waitTerminationSeconds:10}")
    private int waitTerminationSeconds;

    @Bean
    public TaskExecutor taskExecutor() {

        //Java虚拟机可用的处理器数
        int processors = Runtime.getRuntime().availableProcessors();
        //定义线程池
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        taskExecutor.setCorePoolSize(corePoolSize <= 0 ? processors : corePoolSize);
        //线程池最大线程数,默认：3000
        taskExecutor.setMaxPoolSize(maxPoolSize);
        //线程队列最大线程数,默认：3000
        taskExecutor.setQueueCapacity(queueCapacity);
        //线程名称前缀
        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        //线程池中线程最大空闲时间，默认：60，单位：秒
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        //核心线程是否允许超时，默认:false
        taskExecutor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
        //IOC容器关闭时是否阻塞等待剩余的任务执行完成，默认:false（必须设置setAwaitTerminationSeconds）
        taskExecutor.setWaitForTasksToCompleteOnShutdown(waitForTasksToComplete);
        //阻塞IOC容器关闭的时间，默认：10秒（必须设置setWaitForTasksToCompleteOnShutdown）
        taskExecutor.setAwaitTerminationSeconds(waitTerminationSeconds);
        /**
         * 拒绝策略，默认是AbortPolicy
         * AbortPolicy：丢弃任务并抛出RejectedExecutionException异常
         * DiscardPolicy：丢弃任务但不抛出异常
         * DiscardOldestPolicy：丢弃最旧的处理程序，然后重试，如果执行器关闭，这时丢弃任务
         * CallerRunsPolicy：执行器执行任务失败，则在策略回调方法中执行任务，如果执行器关闭，这时丢弃任务
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        taskExecutor.initialize();
        return taskExecutor;
    }
}
