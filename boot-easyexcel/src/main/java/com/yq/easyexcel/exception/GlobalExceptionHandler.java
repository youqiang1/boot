package com.yq.easyexcel.exception;

import com.yq.easyexcel.config.ExcelException;
import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p> 异常处理</p>
 * @author youq  2019/5/14 14:26
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(ExcelException.class)
    public ResultData<?> handler(ExcelException ee) {
        return ResultData.failMsg(ee.getMessage());
    }

}
