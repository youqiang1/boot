package com.yq.kernel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> 统一的返回数据封装</p>
 * @author youq  2019/4/11 10:30
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

    public static final Integer SUCCESS = 200;

    /**
     * 操作结果的状态码，200为成功，其余失败
     */
    @Builder.Default
    private Integer code = 200;

    /**
     * 操作结果的描述信息，可作为页面提示信息使用
     */
    private String msg;

    /**
     * 返回的业务数据
     */
    private E data;

    public static <E> ResultData<E> success() {
        return ResultData.<E>builder().code(200).msg("调用成功").build();
    }

    public static <E> ResultData<E> successMsg(String msg) {
        return ResultData.<E>builder().code(200).msg(msg).build();
    }

    public static <E> ResultData<E> success(E data) {
        return ResultData.<E>builder().code(200).msg("调用成功").data(data).build();
    }

    public static <E> ResultData<E> success(int code, String msg) {
        return ResultData.<E>builder().code(code).msg(msg).build();
    }

    public static <E> ResultData<E> success(int code, E data, String msg) {
        return ResultData.<E>builder().code(code).msg(msg).data(data).build();
    }

    public static <E> ResultData<E> fail() {
        return ResultData.<E>builder().code(0).msg("调用失败").build();
    }

    public static <E> ResultData<E> failMsg(String msg) {
        return ResultData.<E>builder().code(0).msg(msg).build();
    }

    public static <E> ResultData<E> fail(E data) {
        return ResultData.<E>builder().code(0).msg("调用失败!").data(data).build();
    }

    public static <E> ResultData<?> fail(int code, String msg) {
        return ResultData.<E>builder().code(code).msg(msg).build();
    }

    public static <E> ResultData<?> fail(int code, String msg, E data) {
        return ResultData.<E>builder().data(data).code(code).msg(msg).build();
    }

    @Override
    public String toString() {
        return "ResultData{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }

}
