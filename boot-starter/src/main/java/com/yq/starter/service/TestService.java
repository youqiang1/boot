package com.yq.starter.service;

import com.yq.starter.properties.TestProperties;

/**
 * <p> service</p>
 * @author youq  2019/4/30 13:38
 */
public class TestService {

    private TestProperties testProperties;

    public TestService() {}

    public TestService(TestProperties testProperties) {
        this.testProperties = testProperties;
    }

    public String sayHello() {
        return "自定义starter->读取配置文件，name:" + testProperties.getName() + ", age=" + testProperties.getAge();
    }

}
