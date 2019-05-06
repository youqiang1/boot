package com.yq.es.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> es查询返回model</p>
 * @author youq  2019/5/6 11:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsSearchResultModel<T> {

    /**
     * 分片信息
     */
    private ShardsModel _shards;

    /**
     * 话费的时间，单位：毫秒
     */
    private Integer took;

    /**
     * 是否超时
     */
    private boolean time_out;

    /**
     * hits
     */
    private HitsModel<T> hits;

}
