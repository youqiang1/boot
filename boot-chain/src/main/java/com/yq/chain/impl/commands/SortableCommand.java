package com.yq.chain.impl.commands;

import org.apache.commons.chain.Command;

public interface SortableCommand extends Command, Comparable<SortableCommand>{

    int getSequence();

}
