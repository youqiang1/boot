package com.yq.zuul;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * <p> main</p>
 * @author youq  2019/4/24 17:22
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class BootZuulApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootZuulApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
