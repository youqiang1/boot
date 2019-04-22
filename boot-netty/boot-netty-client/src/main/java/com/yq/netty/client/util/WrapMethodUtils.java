package com.yq.netty.client.util;


import com.yq.netty.commons.entity.MethodInvokeMeta;

import java.lang.reflect.Method;

/**
 * <p> 封装接口调用的工具</p>
 * @author youq  2019/4/19 19:44
 */
public class WrapMethodUtils {

    /**
     * 封装 method 的元数据信息
     * @param interfaceClass 接口类
     * @param method         方法
     * @param args           参数列表
     * @return com.yq.netty.commons.entity.MethodInvokeMeta
     * @author youq  2019/4/19 19:44
     */
    public static MethodInvokeMeta readMethod(Class interfaceClass, Method method, Object[] args) {
        MethodInvokeMeta methodInvokeMeta = new MethodInvokeMeta();
        methodInvokeMeta.setInterfaceClass(interfaceClass);
        methodInvokeMeta.setArgs(args);
        methodInvokeMeta.setMethodName(method.getName());
        methodInvokeMeta.setReturnType(method.getReturnType());
        Class<?>[] parameterTypes = method.getParameterTypes();
        methodInvokeMeta.setParameterTypes(parameterTypes);
        return methodInvokeMeta;
    }

}
