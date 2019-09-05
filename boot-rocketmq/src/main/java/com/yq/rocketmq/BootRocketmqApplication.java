package com.yq.rocketmq;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/9/5 14:40
 */
@SpringBootApplication
public class BootRocketmqApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootRocketmqApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
