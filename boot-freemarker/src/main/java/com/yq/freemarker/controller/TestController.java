package com.yq.freemarker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p> 测试</p>
 * @author youq  2019/12/27 17:38
 */
@Controller
public class TestController {

    /**
     * <p> spring boot 2.2.0之前freemarker默认后缀为ftl</p>
     * @author youq  2019/12/27 19:00
     */
    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("welcome", "hello world");
        return "index";
    }

    /**
     * <p> spring boot 2.2.0之后freemarker默认后缀修改为ftlh</p>
     * @author youq  2019/12/27 19:00
     */
    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("welcome", "hello world");
        return "hello";
    }

}
