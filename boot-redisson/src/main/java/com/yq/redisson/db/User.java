package com.yq.redisson.db;

import com.yq.kernel.enu.SexEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * <p> 用户信息表</p>
 * @author youq  2018/5/19 14:16
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Where(clause = "remove=false")
@SQLDelete(sql = "update user set remove=true,last_modified=now() where id=?")
public class User extends Base{
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
    @Enumerated(EnumType.STRING)
    private SexEnum sex;
    /**
     * 年龄
     */
    private Integer age;
}
