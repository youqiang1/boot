package com.yq.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 用户年龄统计模型</p>
 * @author youq  2019/12/26 16:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAgeStatisticsModel {

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 数量
     */
    private Integer count;

}
