package com.ca.mfd.prc.pm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author 阳波
 * @ClassName ThreadPoolConfig
 * @description: 线程池配置
 * @date 2023年08月24日
 * @version: 1.0
 */
@Configuration
public class ThreadPoolConfig {
    /**
     *   默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，
     *	当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
     *  当队列满了，就继续创建线程，当线程数量大于等于maxPoolSize后，开始使用拒绝策略拒绝
     */

    /**
     * 核心线程数（默认线程数）
     */
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = CORE_POOL_SIZE * 2;

    /**
     * 缓冲队列大小
     */
    private static final int QUEUE_CAPACITY = 200;
    /**
     * 线程名前缀
     */
    private static final String THREAD_NAME_PREFIX_CMC_PM = "Async-Scheduled-Prc-PM-Service-";

    @Bean("pmThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor cmcPmTaskExecutor = new ThreadPoolTaskExecutor();
        cmcPmTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        cmcPmTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        cmcPmTaskExecutor.setQueueCapacity(QUEUE_CAPACITY);
        cmcPmTaskExecutor.setThreadNamePrefix(THREAD_NAME_PREFIX_CMC_PM);
        return cmcPmTaskExecutor;
    }


}
