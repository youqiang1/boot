package com.yq.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <p> 主类</p>
 * @author youq  2019/4/9 13:48
 */
@EnableEurekaServer
@SpringBootApplication
public class RegisterApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RegisterApplication.class);
        app.addListeners(new ApplicationPidFileWriter("application.pid"));
        app.run(args);
    }

}
