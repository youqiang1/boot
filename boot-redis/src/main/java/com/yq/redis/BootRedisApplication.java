package com.yq.redis;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/9/4 17:00
 */
@SpringBootApplication
public class BootRedisApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootRedisApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
