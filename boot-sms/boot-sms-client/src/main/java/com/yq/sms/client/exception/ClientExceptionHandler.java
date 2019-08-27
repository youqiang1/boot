package com.yq.sms.client.exception;

import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> 客户端异常处理</p>
 * @author youq  2019/8/23 17:29
 */
@Slf4j
@ControllerAdvice
public class ClientExceptionHandler {

    /**
     * <p> Exception异常捕捉</p>
     * @param e Exception
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/4/11 9:28
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultData<?> exceptionHandler(Exception e) {
        log.error("操作异常", e);
        return ResultData.failMsg("操作异常！");
    }

}
