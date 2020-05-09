package com.yq.activiti;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2020/5/6 17:53
 */
@SpringBootApplication
public class BootActivitiApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootActivitiApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
