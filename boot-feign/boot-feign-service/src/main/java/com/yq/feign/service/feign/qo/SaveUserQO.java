package com.yq.feign.service.feign.qo;

import com.yq.kernel.enu.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> 添加用户信息请求参数</p>
 * @author youq  2019/4/12 10:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserQO implements Serializable {
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
