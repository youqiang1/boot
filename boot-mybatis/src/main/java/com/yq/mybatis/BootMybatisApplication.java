package com.yq.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/11 13:23
 */
@SpringBootApplication
public class BootMybatisApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootMybatisApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
