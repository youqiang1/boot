package com.yq.chain.impl;

import com.yq.chain.impl.commands.Command01;
import com.yq.chain.impl.commands.Command02;
import com.yq.chain.impl.commands.SortableCommand;
import com.yq.chain.impl.filter.TestFilter;
import com.yq.chain.impl.filter.TestFilter02;
import org.apache.commons.chain.impl.ChainBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p> 测试</p>
 * @author youq  2019/8/19 11:41
 */
@Component
public class TestChain extends ChainBase {

    @Autowired
    private Command01 command01;

    @Autowired
    private Command02 command02;

    @Autowired
    private TestFilter filter;

    @Autowired
    private TestFilter02 filter2;

    private List<SortableCommand> commandList;

    @PostConstruct
    private void init() {
        commandList = new ArrayList<>();
        commandList.add(command01);
        commandList.add(command02);
        Collections.sort(commandList);
        //过滤器
        addCommand(filter);
        addCommand(filter2);
        //command执行
        commandList.forEach(this::addCommand);
    }

}
