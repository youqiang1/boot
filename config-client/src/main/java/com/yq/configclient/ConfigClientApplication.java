package com.yq.configclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p> 主类</p>
 * @EnableScheduling 定时任务开启
 * @SpringBootApplication springboot注解
 * @author youq  2019/4/9 10:41
 */
@EnableScheduling
@SpringBootApplication
public class ConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ConfigClientApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
