package com.yq.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p> main</p>
 * @author youq  2019/4/11 10:45
 */
@EnableScheduling
@SpringBootApplication
public class BootTaskApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootTaskApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
