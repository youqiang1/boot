package com.yq.es.rest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> es hits child</p>
 * @author youq  2019/5/6 12:00
 */
@Data
@NoArgsConstructor
public class ChildHitModel<T> {

    /**
     * index name
     */
    private String _index;

    /**
     * type name
     */
    private String _type;

    /**
     * id
     */
    private String _id;

    /**
     * 具体数据
     */
    private T _source;

    /**
     * 分数
     */
    private Double _score;

}
