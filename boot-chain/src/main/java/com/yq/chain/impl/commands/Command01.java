package com.yq.chain.impl.commands;

import com.yq.chain.model.UserContext;
import com.yq.kernel.enu.SexEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Component;

/**
 * <p> 测试</p>
 * @author youq  2019/8/19 11:43
 */
@Slf4j
@Component
public class Command01 implements SortableCommand {

    @Override
    public int getSequence() {
        return 1;
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
        log.info("command01 execute");
        if (context instanceof UserContext) {
            UserContext uctx = (UserContext) context;
            uctx.setSex(SexEnum.MALE);
            log.info("name: {}, {}", uctx.getName(), context.get("name"));
        }
        context.put("step", getSequence());
        return false;
    }

}
