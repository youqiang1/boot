package com.yq.es.controller.qo;

import com.yq.kernel.enu.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p> 用户查询请求参数</p>
 * @author youq  2019/4/10 17:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 年龄范围，开始
     */
    private Integer beginAge;

    /**
     * 年龄范围，结束
     */
    private Integer endAge;

    /**
     * 性别
     */
    private SexEnum sex;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 页大小
     */
    private Integer size;

}
