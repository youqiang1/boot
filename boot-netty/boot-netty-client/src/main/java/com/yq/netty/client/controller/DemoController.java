package com.yq.netty.client.controller;

import com.yq.netty.rpc.DemoService;
import com.yq.netty.commons.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p> 主要用来进行模拟测试的类.就不用写接口来进行测试了</p>
 * @author youq  2019/4/19 19:54
 */
@Slf4j
@Component
public class DemoController {

    /**
     * 测试业务
     */
    @Resource
    private DemoService demoService;

    /**
     * 真正远程调用的方法
     * @throws InterruptedException interruptedException
     */
    public void call() throws InterruptedException {
        // 用于模拟服务器正常启动后，人工调用远程服务代码
        Thread.sleep(10 * 1000);
        log.warn("[准备进行业务测试]");
        log.warn("[rpc测试] ");
        int sum = demoService.sum(5, 8);
        log.warn("[rpc测试结果] {}", sum);
        log.warn("[字符串测试] ");
        String print = demoService.print();
        log.warn("[字符串测试结果] {}", print);
        log.warn("[对象测试] ");
        User userInfo = demoService.getUserInfo();
        log.warn("[对象测试结果] {}", userInfo);
        log.warn("[异常测试]");
        try {
            double division = demoService.division(3, 0);
            log.warn("[异常测试结果] {}", division);
        } catch (Exception e) {
            log.error("[服务器异常] {}", e.getMessage());
        }
    }

}
