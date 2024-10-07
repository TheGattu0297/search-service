package com.openstock.dev.searchservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);  // Allows handling up to 10 concurrent tasks
        scheduler.setThreadNamePrefix("ScheduledTask-");
        scheduler.setRemoveOnCancelPolicy(true);  // Ensures canceled tasks are removed
        scheduler.setAwaitTerminationSeconds(30);  // Timeout for task termination
        scheduler.setWaitForTasksToCompleteOnShutdown(true);  // Wait for tasks to finish before shutting down
        scheduler.initialize();
        return scheduler;
    }
}
