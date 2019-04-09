package com.yq.redisop.common;

/**
 * <p> redis类型</p>
 * @author youq  2019/4/9 14:13
 */
public enum RedissonTypeEnum {
    /**
     * 集群
     */
    CLUSTER,
    /**
     * 哨兵
     */
    SENTINEL,
    /**
     * 单节点
     */
    SINGLE,
}
