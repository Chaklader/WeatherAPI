package com.weather.api.weatherapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfiguration {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Set the number of threads in the pool
        executor.setMaxPoolSize(20); // Set the maximum number of threads
        executor.setQueueCapacity(100); // Set the capacity of the task queue
        executor.initialize();
        return executor;
    }
}
