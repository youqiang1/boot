package com.yq.limit.exception;

import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> 异常处理</p>
 * @author youq  2019/4/28 16:21
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * <p> RuntimeException异常捕捉</p>
     * @param e RuntimeException
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/4/11 9:28
     */
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public ResultData<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("运行时异常", e);
        return ResultData.failMsg(e.getMessage());
    }

}
