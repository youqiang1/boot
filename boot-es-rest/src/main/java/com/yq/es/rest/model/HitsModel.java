package com.yq.es.rest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p> es hits</p>
 * @author youq  2019/5/6 11:59
 */
@Data
@NoArgsConstructor
public class HitsModel<T> {

    /**
     * 数据总数
     */
    private Integer total;

    /**
     * 最大分数（score的含义，就是document对于一个search的相关度的匹配数据，越相关，就越匹配，分数也越高）
     */
    private Double max_score;

    /**
     * 具体数据
     */
    private List<ChildHitModel<T>> hits;

}
