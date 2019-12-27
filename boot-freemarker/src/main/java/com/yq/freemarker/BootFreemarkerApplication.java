package com.yq.freemarker;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/12/27 17:36
 */
@SpringBootApplication
public class BootFreemarkerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootFreemarkerApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
