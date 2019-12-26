package com.yq.mongo;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/12/23 19:39
 */
@SpringBootApplication
public class BootMongoApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootMongoApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
