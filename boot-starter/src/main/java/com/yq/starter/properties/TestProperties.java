package com.yq.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p> 定义配置文件的属性</p>
 * @author youq  2019/4/30 13:23
 */
@ConfigurationProperties(prefix = "spring.test-starter")
public class TestProperties {

    private String name;

    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
