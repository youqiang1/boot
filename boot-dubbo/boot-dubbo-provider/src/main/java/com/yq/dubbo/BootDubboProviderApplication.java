package com.yq.dubbo;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/27 14:31
 */
@SpringBootApplication
public class BootDubboProviderApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootDubboProviderApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
