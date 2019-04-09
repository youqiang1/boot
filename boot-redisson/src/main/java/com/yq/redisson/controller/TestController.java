package com.yq.redisson.controller;

import com.yq.redisop.model.UserModel;
import com.yq.redisop.service.UserRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p> 测试</p>
 * @author youq  2019/4/9 16:22
 */
@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRedisService userRedisService;

    @GetMapping("/findUser")
    public String findUser(HttpServletRequest request) {
        List<UserModel> models = userRedisService.findAll();
        request.setAttribute("models", models);
        return "index";
    }

}
