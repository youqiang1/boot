package com.yq.sharding.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 地址信息实体</p>
 * @author youq  2019/4/29 9:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private Long id;

    private String code;

    private String name;

    private String pid;

    private Integer type;

    private Integer lit;

}
