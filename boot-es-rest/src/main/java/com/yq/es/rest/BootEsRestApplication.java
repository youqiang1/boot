package com.yq.es.rest;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/5/5 17:35
 */
@SpringBootApplication
public class BootEsRestApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootEsRestApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
