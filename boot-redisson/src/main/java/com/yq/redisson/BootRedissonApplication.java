package com.yq.redisson;

import com.yq.redisop.EnableRedisOperate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p> 主类</p>
 * @author youq  2019/4/9 14:56
 */
@EnableScheduling
@EnableRedisOperate
@SpringBootApplication
public class BootRedissonApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootRedissonApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
