package com.yq.feign.response;

import com.yq.feign.service.config.EnableBootFeignService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * <p> main</p>
 * @author youq  2019/4/12 10:43
 */
@EnableEurekaClient
@EnableBootFeignService
@SpringBootApplication
public class BootFeignResponseApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootFeignResponseApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
