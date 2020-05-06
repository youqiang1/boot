package com.yq.chain.impl.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;
import org.springframework.stereotype.Component;

/**
 * <p> 测试</p>
 * @author youq  2019/8/19 15:48
 */
@Slf4j
@Component
public class TestFilter02 implements Filter {

    @Override
    public boolean postprocess(Context context, Exception exception) {
        if (exception == null) {
            return true;
        }
        log.error("chain filter02 exception: ", exception);
        return false;
    }

    @Override
    public boolean execute(Context context) throws Exception {
        log.info("test filter02 execute");
        return false;
    }

}
