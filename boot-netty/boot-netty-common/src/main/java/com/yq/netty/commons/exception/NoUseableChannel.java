package com.yq.netty.commons.exception;

/**
 * <p> 没有可用的通道异常</p>
 * @author youq  2019/4/19 19:40
 */
public class NoUseableChannel extends RuntimeException {

    public NoUseableChannel(String message) {
        super(message);
    }

}
