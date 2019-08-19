package com.yq.chain.controller;

import com.yq.chain.impl.TestChain;
import com.yq.chain.model.UserContext;
import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试</p>
 * @author youq  2019/8/16 14:26
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestChain testChain;

    @RequestMapping("")
    public ResultData<?> test() {
        Context context = new ContextBase();
        context.put("test", "测试传参");
        try {
            if (testChain.execute(context)) {
                log.info("流程执行失败，走失败处理流程");
            } else {
                log.info("流程执行成功，走成功处理流程");
            }
        } catch (Exception e) {
            log.error("流程执行失败，走失败处理流程: ", e);
        }
        return ResultData.success();
    }

    @RequestMapping("/test2")
    public ResultData<?> test2() {
        UserContext context = new UserContext();
        context.setName("youq");
        try {
            if (testChain.execute(context)) {
                log.info("流程执行失败，走失败处理流程");
            } else {
                log.info("流程执行成功，走成功处理流程");
            }
        } catch (Exception e) {
            log.error("chain execute exception: ", e);
        }
        return ResultData.success();
    }

}
