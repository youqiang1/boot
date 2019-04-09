package com.yq.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p> 主类</p>
 * @author youq  2019/4/9 17:22
 */
@EnableScheduling
@SpringBootApplication
public class BootRabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootRabbitmqApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
