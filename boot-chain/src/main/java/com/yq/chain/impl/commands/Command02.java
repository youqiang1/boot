package com.yq.chain.impl.commands;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Component;

/**
 * <p> 测试</p>
 * @author youq  2019/8/19 11:43
 */
@Slf4j
@Component
public class Command02 implements SortableCommand {

    @Override
    public int getSequence() {
        return 2;
    }

    @Override
    public int compareTo(SortableCommand o) {
        if (getSequence() >= o.getSequence()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean execute(Context context) throws Exception {
        Object test = context.get("test");
        log.info("command02 execute");
        log.info("传参打印：{}", test);
        return false;
    }

}
