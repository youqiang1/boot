package com.yq.mybatis.multi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 学校信息</p>
 * @author youq  2019/4/27 17:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class School {

    private Integer id;

    private String name;

    private String province;

}
