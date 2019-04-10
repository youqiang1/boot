package com.yq.cloud.stream.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/10 14:44
 */
@SpringBootApplication
public class StreamKafkaApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(StreamKafkaApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
