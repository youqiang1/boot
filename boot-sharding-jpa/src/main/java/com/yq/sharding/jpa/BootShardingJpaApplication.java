package com.yq.sharding.jpa;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/29 9:15
 */
@SpringBootApplication
public class BootShardingJpaApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootShardingJpaApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
