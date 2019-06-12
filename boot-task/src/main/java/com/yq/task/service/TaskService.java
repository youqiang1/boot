package com.yq.task.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p> service</p>
 * @author youq  2019/4/11 10:51
 */
@Slf4j
@Service
public class TaskService {

    public String say(String hello) {
        hello = String.format("带参线程处理：%s", hello);
        return hello;
    }

    public String say2(String hello) {
        hello = String.format("拒绝策略处理：%s", hello);
        return hello;
    }

}
