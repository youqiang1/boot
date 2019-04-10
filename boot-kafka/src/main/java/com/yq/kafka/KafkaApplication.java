package com.yq.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/10 9:16
 */
@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(KafkaApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
