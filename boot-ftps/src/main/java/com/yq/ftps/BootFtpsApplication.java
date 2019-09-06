package com.yq.ftps;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/9/6 17:37
 */
@SpringBootApplication
public class BootFtpsApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootFtpsApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
