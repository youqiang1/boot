package com.yq.task.pool;

import com.yq.task.schedule.thread.TakeArgsThread;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p> 自定义拒绝策略</p>
 * @author youq  2019/6/6 16:05
 */
@Slf4j
public class MyRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        log.info("MyRejectedExecutionHandler start");
        if (r instanceof TakeArgsThread) {
            TakeArgsThread thread = (TakeArgsThread) r;
            thread.exception();
        } else {
            log.info(r.toString());
        }
    }

}
