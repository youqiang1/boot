package com.yq.task.schedule.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p> 不带参数的处理线程</p>
 * @author youq  2018/12/10 11:51
 */
@Slf4j
@Component
public class NoArgsThread implements Runnable{

    @Value("${test.str}")
    private String str;

    @Override
    public void run() {
        log.info("没有参数的线程，读下配置文件的test.str：【{}】", str);
    }

}
