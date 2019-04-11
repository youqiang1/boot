package com.yq.mybatis.entity;

import com.yq.kernel.enu.SexEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p> 用户信息表</p>
 * @author youq  2018/5/19 14:16
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends Base {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别
     */
    private SexEnum sex;
    /**
     * 年龄
     */
    private Integer age;
}
