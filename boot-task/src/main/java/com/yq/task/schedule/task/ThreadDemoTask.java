package com.yq.task.schedule.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yq.task.schedule.thread.NoArgsThread;
import com.yq.task.schedule.thread.TakeArgsThread;
import com.yq.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * <p> 多线程处理测试类</p>
 * @author youq  2018/12/10 11:44
 */
@Slf4j
@Component
public class ThreadDemoTask {

    /**
     * 线程池
     */
    private ExecutorService pool;

    @Autowired
    private NoArgsThread noArgsThread;

    @Autowired
    private TaskService taskService;

    @Value("${test.hello}")
    private String hello;

    /**
     * 初始化线程池，定时任务一般不需要关闭这个线程池，实在需要的话调用shutdown()关闭，调用后等所有线程都执行完成后，等待被回收
     */
    @PostConstruct
    public void init() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("thread-demo-%d").build();
        //corePoolSize      核心线程数，默认情况下核心线程会一直存活，即使处于闲置状态也不会受存keepAliveTime限制。除非将allowCoreThreadTimeOut设置为true。
        //maximumPoolSize   线程池所能容纳的最大线程数。超过这个数的线程将被阻塞。当任务队列为没有设置大小的LinkedBlockingQueue，这个值无效。
        //keepAliveTime     非核心线程的闲置超时时间，超过这个时间就会被回收。
        //unit              指定keepAliveTime的单位，如TimeUnit.SECONDS。当将allowCoreThreadTimeOut设置为true时对corePoolSize生效。
        //workQueue         线程池中的任务队列. 常用的有三种队列，SynchronousQueue,LinkedBlockingDeque,ArrayBlockingQueue。
        //threadFactory     线程工厂，提供创建新线程的功能。ThreadFactory是一个接口，只有一个方法
        //handler           拒绝策略，当线程池中的资源已经全部使用，添加新线程被拒绝时，会调用RejectedExecutionHandler的rejectedExecution方法，详见：https://blog.csdn.net/pozmckaoddb/article/details/51478017
        pool = new ThreadPoolExecutor(2, 10, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024), threadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * <p> 不带参的处理线程</p>
     * @author youq  2018/12/10 12:03
     */
    @Scheduled(fixedDelay = 60 * 1000)
    public void executor() {
        pool.execute(noArgsThread);
    }

    /**
     * <p> 带参的处理线程</p>
     * @author youq  2018/12/10 12:03
     */
    @Scheduled(fixedDelay = 40 * 1000)
    public void taskArgsExecutor() {
        pool.execute(new TakeArgsThread(hello, taskService));
    }

}
