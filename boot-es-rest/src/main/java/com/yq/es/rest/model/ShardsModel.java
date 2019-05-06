package com.yq.es.rest.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> shards信息</p>
 * @author youq  2019/5/6 11:55
 */
@Data
@NoArgsConstructor
public class ShardsModel {

    /**
     * 总分片数
     */
    private Integer total;

    /**
     * 失败分片数
     */
    private Integer failed;

    /**
     * 成功分片数
     */
    private Integer successful;

}
