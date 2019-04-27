package com.yq.mybatis.multi;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/11 13:23
 */
@SpringBootApplication
public class BootMybatisMultiApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootMybatisMultiApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
