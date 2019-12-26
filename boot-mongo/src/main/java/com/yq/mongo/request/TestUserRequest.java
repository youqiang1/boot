package com.yq.mongo.request;

import com.yq.kernel.enu.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 用户信息表</p>
 * @author youq  2019/12/26 13:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestUserRequest {

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
    private Integer minAge;

    /**
     * 年龄
     */
    private Integer maxAge;

    /**
     * 介绍
     */
    private String introduce;

}
