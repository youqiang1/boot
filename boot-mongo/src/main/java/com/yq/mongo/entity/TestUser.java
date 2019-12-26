package com.yq.mongo.entity;

import com.yq.kernel.enu.SexEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p> 用户信息表</p>
 * @author youq  2019/12/26 13:23
 */
@Data
@Document(collection = "test_user")
public class TestUser {

    @Id
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 性别
     */
    private SexEnum sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 介绍
     */
    private String introduce;

}
