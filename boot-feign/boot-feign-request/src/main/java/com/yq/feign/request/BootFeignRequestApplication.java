package com.yq.feign.request;

import com.yq.feign.service.config.EnableBootFeignService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * <p> main</p>
 * @author youq  2019/4/12 10:14
 */
@EnableEurekaClient
@EnableFeignClients
@EnableBootFeignService
@SpringBootApplication
public class BootFeignRequestApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootFeignRequestApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
