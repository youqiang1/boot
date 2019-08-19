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
public class TestFilter implements Filter {

    /**
     * <p>
     * 在Chain执行完Command之后(包括Filter.execute),将会从链的末端倒序依次执行所有的Filter.postprocess()方法,
     * Filter中如果抛出异常将会被忽略;此方法的设计初衷为"清理Context"中参数或者执行结果,或者重置执行结果,
     * 如果在Command中存在创建"资源消耗"的对象(比如数据源连接,比如线程资源等),可以在Filter中考虑释放这些资源.
     * 当所有的Filter执行完成之后,Context中保持的数据,应该是可靠的.
     * </p>
     * @param context   上下文对象
     * @param exception 异常对象
     * @return boolean
     * @author youq  2019/8/19 16:24
     */
    @Override
    public boolean postprocess(Context context, Exception exception) {
        if (exception == null) {
            return true;
        }
        log.error("chain exception: ", exception);
        return false;
    }

    @Override
    public boolean execute(Context context) throws Exception {
        log.info("test filter execute");
        return false;
    }

}
