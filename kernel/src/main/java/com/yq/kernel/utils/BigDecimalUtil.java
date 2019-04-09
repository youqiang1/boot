package com.yq.kernel.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <p> bigDecimal 工具类</p>
 * @author youq  2019/1/14 12:20
 */
public class BigDecimalUtil {

    /**
     * <p> 加</p>
     * @param value1 参数1
     * @param value2 参数2
     * @return double
     * @author youq  2018/7/13 17:08
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.add(b2).doubleValue();
    }

    /**
     * <p> 加</p>
     * @param value1 参数1
     * @param value2 参数2
     * @return double
     * @author youq  2018/7/13 17:08
     */
    public static BigDecimal add(long value1, long value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2);
    }

    /**
     * <p> 减</p>
     * @param value1 参数1
     * @param value2 参数2
     * @return double
     * @author youq  2018/7/13 17:08
     */
    public static double subtract(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * <p> 乘</p>
     * @param value1 参数1
     * @param value2 参数2
     * @return double
     * @author youq  2018/7/13 17:08
     */
    public static Double multiply(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * <p> 乘</p>
     * @param multiplier1 乘数1
     * @param multiplier2 乘数2
     * @return java.math.BigDecimal
     * @author youq  2019/3/12 15:22
     */
    public static BigDecimal multiply(String multiplier1, String multiplier2) {
        BigDecimal m1 = new BigDecimal(multiplier1);
        BigDecimal m2 = new BigDecimal(multiplier2);
        return m1.multiply(m2);
    }

    /**
     * <p> BigDecimal除法</p>
     * @param divisor  除数
     * @param dividend 被除数
     * @return java.math.BigDecimal
     * @author youq  2019/1/8 10:03
     */
    public static BigDecimal divide(String divisor, String dividend) {
        String zero = "0";
        if (StringUtils.isEmpty(divisor) || StringUtils.isEmpty(dividend) || zero.equals(dividend)) {
            return new BigDecimal(zero);
        }
        BigDecimal divisorB = new BigDecimal(divisor);
        BigDecimal dividendB = new BigDecimal(dividend);
        /*
        ROUND_CEILING
        如果 BigDecimal 是正的，则做 ROUND_UP 操作；如果为负，则做 ROUND_DOWN 操作。
        ROUND_DOWN
        从不在舍弃(即截断)的小数之前增加数字。
        ROUND_FLOOR
        如果 BigDecimal 为正，则作 ROUND_UP ；如果为负，则作 ROUND_DOWN 。
        ROUND_HALF_DOWN
        若舍弃部分> .5，则作 ROUND_UP；否则，作 ROUND_DOWN 。
        ROUND_HALF_EVEN
        如果舍弃部分左边的数字为奇数，则作 ROUND_HALF_UP ；如果它为偶数，则作 ROUND_HALF_DOWN 。
        ROUND_HALF_UP
        若舍弃部分>=.5，则作 ROUND_UP ；否则，作 ROUND_DOWN 。
        ROUND_UNNECESSARY
        该“伪舍入模式”实际是指明所要求的操作必须是精确的，，因此不需要舍入操作。
        ROUND_UP
        总是在非 0 舍弃小数(即截断)之前增加数字。
         */
        return divisorB.divide(dividendB, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * <p> 除</p>
     * @param divisor       除数
     * @param dividend      被除数
     * @param places        保留小数点位数
     * @param defaultResult 默认返回值
     * @return java.math.BigDecimal
     * @author youq  2019/3/12 15:35
     */
    public static BigDecimal divide(String divisor, String dividend, int places, String defaultResult) {
        if (StringUtils.isEmpty(divisor) || StringUtils.isEmpty(dividend)) {
            return new BigDecimal(defaultResult);
        }
        BigDecimal divisorB = new BigDecimal(divisor);
        BigDecimal dividendB = new BigDecimal(dividend);
        if (dividendB.intValue() == 0) {
            return new BigDecimal(defaultResult);
        }
        return divisorB.divide(dividendB, 10, BigDecimal.ROUND_HALF_UP).setScale(places, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * <p> 比例计算</p>
     * @param divisor  除数
     * @param dividend 被除数
     * @param places   保留位数
     * @return java.lang.String 比例
     * @author youq  2019/1/8 12:29
     */
    public static String rate(Integer divisor, Integer dividend, Integer places) {
        if (divisor == 0 || dividend == 0) {
            return "0";
        }
        BigDecimal divisorB = new BigDecimal(divisor);
        BigDecimal dividendB = new BigDecimal(dividend);
        return divisorB.divide(dividendB, places + 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(places, BigDecimal.ROUND_HALF_UP)
                .stripTrailingZeros().toPlainString();
    }

    /**
     * <p> 将数字格式化为千位分隔符分隔的数字字符串</p>
     * @param data 待格式化数据
     * @return java.lang.String
     * @author youq  2019/3/8 13:41
     */
    public static String formatThousandSeparator(Object data) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(data);
    }

    /**
     * <p> 保留两位小数 格式化</p>
     * @param data 被格式化的销售
     * @return java.lang.String 格式化后的字符串
     * @author youq  2019/4/9 10:54
     */
    public static String decimalPlace2Format(Double data) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(data);
    }

}
