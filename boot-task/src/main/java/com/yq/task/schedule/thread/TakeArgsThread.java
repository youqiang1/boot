package com.yq.task.schedule.thread;

import com.yq.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * <p> 带参的处理线程</p>
 * @author youq  2018/12/10 11:56
 */
@Slf4j
public class TakeArgsThread implements Runnable {

    private String hello;

    private TaskService taskService;

    public TakeArgsThread(String hello, TaskService taskService) {
        this.hello = hello;
        this.taskService = taskService;
    }

    public void exception() {
        String result = taskService.say2(hello);
        log.info("时间：{}，resultStr: {}", LocalDateTime.now(), result);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        String result = taskService.say(hello);
        log.info("时间：{}，resultStr: {}", LocalDateTime.now(), result);
    }

}
