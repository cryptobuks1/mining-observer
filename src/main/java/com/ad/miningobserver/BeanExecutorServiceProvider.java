package com.ad.miningobserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class BeanExecutorServiceProvider {

    @Value(value = "${executor.thread.core.pool.size}")
    private int corePoolSize;
    @Value(value = "${executor.thread.max.pool.size}")
    private int maxPoolSize;

    /**
     * Thread poool for running Operation tasks
     */
    @Bean(name = {NameReference.EXECUTOR_OPERATION_THREAD_POOL})
    public ThreadPoolTaskExecutor operationThreadPoolExecutor() {
        final ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(corePoolSize);
        poolTaskExecutor.setMaxPoolSize(maxPoolSize);
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

    /**
     * Thread pool for running command line tasks
     */
    @Bean(name = {NameReference.EXECUTOR_TASK_THREAD_POOL})
    public ThreadPoolTaskExecutor taskThreadPoolExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
