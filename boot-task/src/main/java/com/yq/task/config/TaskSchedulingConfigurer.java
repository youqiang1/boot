package com.yq.task.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * <p> 自定义的spring task配置文件</p>
 * @author youq  2019/4/11 10:47
 */
@Configuration
public class TaskSchedulingConfigurer implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler poolTaskScheduler = new ThreadPoolTaskScheduler();
        poolTaskScheduler.setPoolSize(8);
        poolTaskScheduler.initialize();
        taskRegistrar.setTaskScheduler(poolTaskScheduler);
    }

}
