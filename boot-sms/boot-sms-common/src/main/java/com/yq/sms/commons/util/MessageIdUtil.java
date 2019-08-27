package com.yq.sms.commons.util;

import com.yq.kernel.utils.snowflake.SnowFlake;

/**
 * <p> messageId生成工具类</p>
 * @author youq  2019/8/23 20:11
 */
public class MessageIdUtil {

    private final Object lock = new Object();

    private int i = 0;

    private static MessageIdUtil messageIdUtil = new MessageIdUtil();

    public static  MessageIdUtil getInstance() {
        return messageIdUtil;
    }

    public int getId() {
        synchronized (lock) {
            if (i == 0x7fffffff) {
                i = 1;
            } else {
                i += 1;
            }
            return i;
        }
    }

    public String getMessageId() {
        return new SnowFlake(1, 1).nextId() + "";
    }

}
