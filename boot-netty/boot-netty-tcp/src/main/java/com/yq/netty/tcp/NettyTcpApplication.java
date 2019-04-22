package com.yq.netty.tcp;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/22 13:14
 */
@SpringBootApplication
public class NettyTcpApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(NettyTcpApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
