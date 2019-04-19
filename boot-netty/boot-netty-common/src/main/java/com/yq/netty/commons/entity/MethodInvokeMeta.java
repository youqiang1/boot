package com.yq.netty.commons.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> 记录元方法信息的实体</p>
 * @author youq  2019/4/19 15:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MethodInvokeMeta implements Serializable {
    /**
     * 接口
     */
    private Class<?> interfaceClass;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数
     */
    private Object[] args;
    /**
     * 返回值类型
     */
    private Class<?> returnType;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
}
