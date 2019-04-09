package com.yq.kernel.utils;

import java.time.LocalDate;
import java.util.concurrent.atomic.LongAdder;

/**
 * <p> 流水号生成</p>
 * @author youq  2019/1/7 10:48
 */
public class SerialNumberGen {

    private static LongAdder longAdder = new LongAdder();

    /**
     * <p> 租户系统交易流水号生成</p>
     * 由账户系统生成，规则：
     1. 16位数字,YYMMDD+10位流水号;
     2. 前6位数字：
     YY：对应业务日期格式中年份的后2位
     MM：对应业务月份的2位，如12，07
     DD：对应业务日期在当月的天数，如31，08
     3 10位流水号：
     例如：0000000001
     * @return java.lang.String
     * @author youq  2019/1/7 10:49
     */
    public static String gen() {
        LocalDate now = LocalDate.now();
        String prefix = now.toString().replace("-", "").substring(2, 8);
        return prefix + genPostfix();
    }
    
    /**
     * <p> 获取10为后缀</p>
     * @return java.lang.String
     * @author youq  2019/1/15 13:46
     */
    private static String genPostfix() {
        longAdder.increment();
        Long l = longAdder.longValue();
        if (l < 10) {
            return "000000000" + l;
        } else if (l < 100) {
            return "00000000" + l;
        } else if (l < 1000) {
            return "0000000" + l;
        } else if (l < 10000) {
            return "000000" + l;
        } else if (l < 100000) {
            return "00000" + l;
        } else if (l < 1000000) {
            return "0000" + l;
        } else if (l < 10000000) {
            return "000" + l;
        } else if (l < 100000000) {
            return "00" + l;
        } else if (l < 1000000000) {
            return "0" + l;
        } else if (l < 10000000000L) {
            return l.toString();
        } else {
            longAdder = new LongAdder();
            longAdder.increment();
            return "0000000001";
        }
    }

}
