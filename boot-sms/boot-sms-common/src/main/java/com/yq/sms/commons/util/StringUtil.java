package com.yq.sms.commons.util;

import com.alibaba.druid.util.StringUtils;

/**
 * <p> 字符串工具类</p>
 * @author youq  2019/8/23 20:06
 */
public class StringUtil {

    public static String def(String val, String def) {
        if (StringUtils.isEmpty(val)) {
            return def;
        }
        return val;
    }

}
