package com.yq.sms.client;

import com.yq.kernel.constants.GlobalConstants;
import com.yq.redisop.EnableRedisOperate;
import com.yq.sms.commons.EnableSmsCommon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p> main</p>
 * @author youq  2019/8/23 14:51
 */
@EnableSmsCommon
@EnableRedisOperate
@EnableScheduling
@SpringBootApplication
public class BootSmsClientApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootSmsClientApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
