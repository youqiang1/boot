package com.yq.ftps.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 日志打印测试</p>
 * @author youq  2019/12/27 19:08
 */
@RestController
@RequestMapping("/log")
public class LogController {

    private Logger log = LoggerFactory.getLogger("balance");

    @RequestMapping("/log")
    public void log() {
        log.info("单独日志文件打印");
    }

}
