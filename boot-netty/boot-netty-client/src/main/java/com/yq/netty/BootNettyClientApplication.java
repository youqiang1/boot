package com.yq.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/19 11:13
 */
@SpringBootApplication
public class BootNettyClientApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootNettyClientApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
