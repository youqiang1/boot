package com.yq.es.rest.exception;

import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * <p> 异常处理</p>
 * @author youq  2019/5/6 11:14
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResultData<?> exceptionHandle(Exception e) {
        log.error("操作异常：", e);
        return ResultData.failMsg(e.getMessage());
    }

}
