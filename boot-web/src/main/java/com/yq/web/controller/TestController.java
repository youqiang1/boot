package com.yq.web.controller;

import com.yq.kernel.model.ResultData;
import com.yq.starter.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试</p>
 * @author youq  2019/4/30 13:49
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/contextLoads")
    public ResultData<?> contextLoads() {
        return ResultData.success(testService.sayHello());
    }

}
