package com.yq.fastdfs;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2020/1/6 14:30
 */
@SpringBootApplication
public class BootDFSApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootDFSApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
