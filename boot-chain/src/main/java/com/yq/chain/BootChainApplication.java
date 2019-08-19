package com.yq.chain;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/8/16 14:24
 */
@SpringBootApplication
public class BootChainApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootChainApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
