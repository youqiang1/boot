package com.yq.mqtt.client;

import com.yq.kernel.constants.GlobalConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2020/4/21 9:50
 */
@SpringBootApplication
public class MqttClientApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MqttClientApplication.class);
        application.addListeners(new ApplicationPidFileWriter(GlobalConstants.PID_FILE_NAME));
        application.run(args);
    }

}
