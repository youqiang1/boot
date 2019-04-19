package com.yq.netty.commons.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * <p> 服务器可能返回空的处理</p>
 * @author youq  2019/4/19 15:22
 */
@Data
public class NullWritable implements Serializable {

    /**
     * 单例
     */
    private static NullWritable instance = new NullWritable();

    private NullWritable() {
    }

    /**
     * <p> 返回代表Null的对象</p>
     * @return com.yq.netty.commons.entity.NullWritable
     * @author youq  2019/4/19 15:23
     */
    public static NullWritable nullWritable() {
        return instance;
    }

}
