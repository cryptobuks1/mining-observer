package com.ad.miningobserver.gpu.control;

import com.ad.miningobserver.NameReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class GpuTaskSchedular implements SchedulingConfigurer {
    
    private static final int QUERY_TIME = 10 * 1000; // 10sec
    private static final int DELAY_TIME = 5 * 1000; // 5sec

    private final TaskExecutorFakeImpl taskExecutor;
    private final GpuQueryTask gpuQueryTask;

    @Autowired
    public GpuTaskSchedular(TaskExecutorFakeImpl taskExecutor) {
        this.taskExecutor = taskExecutor;
        this.gpuQueryTask = new GpuQueryTask();
    }

    /**
     * Scheduled thread pool for running GPU lookup
     */
    @Bean(name = {NameReference.EXECUTOR_GPU_SCHEDULAR})
    public Executor gpuThreadPoolSchedular() {
        return Executors.newScheduledThreadPool(1);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        IntervalTask intervalTask = 
                new IntervalTask(this.gpuQueryTask, QUERY_TIME, DELAY_TIME);
        registrar.addFixedDelayTask(intervalTask);
        registrar.setScheduler(this.gpuThreadPoolSchedular());
    }

    /**
     * Class that will execute a query to the underlying graphics card provider and
     * retrieve the statistics information.
     */
    private class GpuQueryTask implements Runnable {

        @Override
        public void run() {
            final String[] queryArgs = new SMICommand().queryMendatoryInfoGPUs();
            taskExecutor.executeCommand(queryArgs);
        }
    }
}
