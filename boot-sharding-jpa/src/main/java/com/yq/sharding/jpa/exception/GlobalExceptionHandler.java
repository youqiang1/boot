package com.yq.sharding.jpa.exception;

import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p> 全局异常处理</p>
 * @author youq  2019/4/29 10:12
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * <p> exception异常处理</p>
     * @param e Exception
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/4/29 10:14
     */
    @ExceptionHandler(Exception.class)
    public ResultData<?> exceptionHandle(Exception e) {
        log.error("操作异常：", e);
        return ResultData.fail(e.getMessage());
    }

}
