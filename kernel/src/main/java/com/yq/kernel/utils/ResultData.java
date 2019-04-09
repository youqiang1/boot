package com.yq.kernel.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 统一的返回数据封装
 * @Date: 2017/11/2 14:14
 * @Author: youq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultData<E> implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 801303944859566772L;

    /**
     * 操作结果的状态码，200为成功，其余失败
     */
    protected int code = 200;

    /**
     * 操作结果的描述信息，可作为页面提示信息使用
     */
    protected String msg;

    /**
     * 返回的业务数据
     */
    protected E data;

    public static ResultData<?> success() {
        return ResultData.builder().code(200).msg("调用成功").build();
    }

    public static ResultData<?> successMsg(String msg) {
        return ResultData.builder().code(200).msg(msg).build();
    }

    public static <E> ResultData<?> success(E data) {
        return ResultData.builder().code(200).msg("调用成功").data(data).build();
    }

    public static <E> ResultData<?> success(int code, E data, String msg) {
        return ResultData.builder().code(code).msg(msg).data(data).build();
    }

    public static <E> ResultData<?> success(int code, String msg) {
        return ResultData.builder().code(code).msg(msg).build();
    }

    public static <E> ResultData<?> fail() {
        return ResultData.builder().code(0).msg("调用失败").build();
    }

    public static <E> ResultData<?> failMsg(String msg) {
        return ResultData.builder().code(0).msg(msg).build();
    }

    public static <E> ResultData<?> fail(E data) {
        return ResultData.builder().code(0).msg("调用成功!").data(data).build();
    }

    public static <E> ResultData<?> fail(int code, String msg) {
        return ResultData.builder().code(code).msg(msg).build();
    }

    public static <E> ResultData<?> fail(int code, String msg, E data) {
        return ResultData.builder().data(data).code(code).msg(msg).build();
    }

    @Override
    public String toString() {
        return "ResultData{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }
}