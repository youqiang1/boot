package com.yq.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * <p> main</p>
 * @author youq  2019/4/10 16:26
 */
@SpringBootApplication
public class BootEsApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootEsApplication.class);
        application.addListeners(new ApplicationPidFileWriter("application.pid"));
        application.run(args);
    }

}
