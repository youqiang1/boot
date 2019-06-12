package com.yq.task.schedule.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yq.task.pool.MyRejectedExecutionHandler;
import com.yq.task.schedule.thread.TakeArgsThread;
import com.yq.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * <p> 测试，不建议一个类里面写多个定时，更倾向于一个定时写一个类，也看实际情况</p>
 * @author youq  2018/12/10 11:11
 */
@Slf4j
@Component
public class Demo2Task {

    @Autowired
    private TaskService taskService;

    @Value("${test.hello}")
    private String hello;

    private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("push-redis-report-%d").build();

    private static ExecutorService executorService = new ThreadPoolExecutor(1, 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(2), threadFactory, new MyRejectedExecutionHandler());

    /**
     * <p> 带参的处理线程</p>
     * @author youq  2018/12/10 12:03
     */
    @Scheduled(fixedDelay = 60 * 1000)
    public void taskArgsExecutor() {
        for (int i = 0; i < 5; i++) {
            executorService.execute(new TakeArgsThread(hello, taskService));
        }
        log.info("class: {}", executorService.hashCode());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
        int queueThreadCount = threadPoolExecutor.getQueue().size();
        int activeThreadCount = threadPoolExecutor.getActiveCount();
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        long taskCount = threadPoolExecutor.getTaskCount();
        int corePoolSize = threadPoolExecutor.getCorePoolSize();
        boolean shutdownFlag = threadPoolExecutor.isShutdown();
        boolean terminated = threadPoolExecutor.isTerminated();
        boolean terminating = threadPoolExecutor.isTerminating();
        log.info("总线程数：{}，执行完成线程数：{}，当前活动线程数：{}，当前排队线程数：{}, corePoolSize:{}",
                taskCount, completedTaskCount, activeThreadCount, queueThreadCount, corePoolSize);
        log.info("shutdown: {}, terminated: {}, terminating: {}", shutdownFlag, terminated, terminating);
    }

}
