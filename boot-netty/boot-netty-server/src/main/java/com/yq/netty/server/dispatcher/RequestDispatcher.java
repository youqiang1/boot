package com.yq.netty.server.dispatcher;

import com.yq.netty.commons.entity.MethodInvokeMeta;
import com.yq.netty.commons.entity.NullWritable;
import com.yq.netty.commons.exception.ErrorParamsException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p> 请求分配器</p>
 * @author youq  2019/4/19 14:48
 */
@Slf4j
@Component
public class RequestDispatcher implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * <p> 发送</p>
     * @param channelHandlerContext ChannelHandlerContext
     * @param invokeMeta            用于记录远程调用的服务信息，即调用哪个接口中的哪个方法
     * @author youq  2019/4/19 15:37
     */
    public void dispatcher(ChannelHandlerContext channelHandlerContext, MethodInvokeMeta invokeMeta) {
        ChannelFuture future = null;
        //指向的接口类
        Class<?> interfaceClass = invokeMeta.getInterfaceClass();
        //所调用的方法名
        String name = invokeMeta.getMethodName();
        //方法的参数列表
        Object[] args = invokeMeta.getArgs();
        //方法的参数列表按顺序对应的类型
        Class<?>[] parameterTypes = invokeMeta.getParameterTypes();
        //通过spring获取实际对象
        Object targetObject = this.applicationContext.getBean(interfaceClass);
        //声明调用的方法对象
        Method method;
        try {
            method = targetObject.getClass().getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            log.error("获取远程方法异常：", e);
            future = channelHandlerContext.writeAndFlush(e);
            return;
        } finally {
            if (future != null) {
                future.addListeners(ChannelFutureListener.CLOSE);
            }
        }
        try {
            //执行方法
            Object result = method.invoke(targetObject, args);
            if (result == null) {
                log.info("{} -> 方法没有返回值，返回NullWritable对象", this.getClass().getName());
                future = channelHandlerContext.writeAndFlush(NullWritable.nullWritable());
            } else {
                log.info("{} -> 返回结果：{}", this.getClass().getName(), result);
                future = channelHandlerContext.writeAndFlush(result);
            }
        } catch (Exception e) {
            Throwable targetException = ((InvocationTargetException) e).getTargetException();
            log.error("{} -> 方法执行异常", this.getClass().getName());
            if (targetException instanceof ErrorParamsException) {
                log.error("{} -> 异常信息：{}", this.getClass().getName(), targetException.getMessage());
            }
            future = channelHandlerContext.writeAndFlush(targetException);
        }
        log.info("ChannelFuture: {}", future);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
