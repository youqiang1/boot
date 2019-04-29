package com.yq.sharding.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p> user实体</p>
 * @author youq  2019/4/29 9:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private Date createTime;

    private Date lastModified;

    private String name;

    private String phone;

    private String email;

    private String password;

    private Integer cityId;

    private Integer sex;

}
