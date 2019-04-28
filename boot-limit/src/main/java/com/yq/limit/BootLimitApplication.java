package com.yq.limit;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/28 15:14
 */
@SpringBootApplication
public class BootLimitApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootLimitApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
