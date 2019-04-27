package com.yq.swagger;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p> main</p>
 * @author youq  2019/4/27 10:41
 */
@EnableSwagger2
@SpringBootApplication
public class BootSwaggerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootSwaggerApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
