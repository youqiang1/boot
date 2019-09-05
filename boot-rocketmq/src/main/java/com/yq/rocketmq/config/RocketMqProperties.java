package com.yq.rocketmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p> rocketMq配置</p>
 * @author youq  2019/9/5 14:45
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rocketmq.producer")
public class RocketMqProperties {

    private String nameSrvAddr;

    private String groupName;

    private String tranGroupName;

}
