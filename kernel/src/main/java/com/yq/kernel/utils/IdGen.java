package com.yq.kernel.utils;

import java.lang.management.ManagementFactory;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID生成器 <br>
 * 20位64进制编码，依次为：0-9,A-Z,-,a-z,_
 * <ul>
 * <li>0:类别编号</li>
 * <li>1-3:服务器编号</li>
 * <li>4-5:启动编号</li>
 * <li>6-11:时间戳，秒级</li>
 * <li>12-17:序列，循环递增</li>
 * <li>18-19:校验码</li>
 * </ul>
 * @author fengshaoyun
 */
public class IdGen {

    /**
     * 编码字典
     */
    private final static char[] ENC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-abcdefghijklmnopqrstuvwxyz_".toCharArray();

    /**
     * 默认实例
     */
    public static final IdGen DEFAULT = new IdGen("D", new DecimalFormat("000").format(Math.random() * 1000));

    /**
     * 类别编号
     */
    private final char[] type;

    /**
     * 服务器编号
     */
    private final char[] server;

    /**
     * 启动编号
     */
    private final char[] startup;

    /**
     * 序列号
     */
    private final AtomicInteger seq = new AtomicInteger(0);

    /**
     * 创建ID生成器实例
     * @param type   类别编号，1位，^([0-9]|[A-Z]|-|[a-z]|_){1}$
     * @param server 服务编号，3位，^([0-9]|[A-Z]|-|[a-z]|_){3}$
     */
    public IdGen(String type, String server) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        if (server == null) {
            throw new NullPointerException("server");
        }
        if (check(type, 1) == false) {
            throw new IllegalArgumentException("type must be \"^([0-9]|[A-Z]|-|[a-z]|_){1}$\"");
        }
        if (check(server, 3) == false) {
            throw new IllegalArgumentException("server must be \"^([0-9]|[A-Z]|-|[a-z]|_){3}$\"");
        }
        this.type = type.toCharArray();
        this.server = server.toCharArray();
        int s;
        try {
            // 首选进程号
            s = Math.abs(Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]) % 4096);
        } catch (Exception e) {
            // 进程号失败,使用随机数
            s = new SecureRandom().nextInt(4096);
        }
        this.startup = new char[2];
        this.startup[0] = ENC[s >>> 6];
        this.startup[1] = ENC[s & 0x3f];
    }

    /**
     * 生成ID
     * @return ID
     */
    public String gen() {
        // 时间为秒，正最大可支持到2038年，之后变为负数，可支持到2106年
        return gen((int) (System.currentTimeMillis() / 1000L), seq.getAndIncrement());
    }

    /**
     * 生成ID
     */
    private String gen(final int time, final int seq) {
        char[] cs = new char[20];
        cs[0] = type[0];
        cs[1] = server[0];
        cs[2] = server[1];
        cs[3] = server[2];
        cs[4] = startup[0];
        cs[5] = startup[1];
        cs[6] = ENC[time >>> 30 & 0x3f];
        cs[7] = ENC[time >>> 24 & 0x3f];
        cs[8] = ENC[time >>> 18 & 0x3f];
        cs[9] = ENC[time >>> 12 & 0x3f];
        cs[10] = ENC[time >>> 6 & 0x3f];
        cs[11] = ENC[time & 0x3f];
        cs[12] = ENC[seq >>> 30 & 0x3f];
        cs[13] = ENC[seq >>> 24 & 0x3f];
        cs[14] = ENC[seq >>> 18 & 0x3f];
        cs[15] = ENC[seq >>> 12 & 0x3f];
        cs[16] = ENC[seq >>> 6 & 0x3f];
        cs[17] = ENC[seq & 0x3f];
        cs[18] = ENC[(cs[0] + cs[2] * 2 + cs[4] * 3 + cs[6] * 4 + cs[8] * 5 + cs[10] * 6 + cs[12] * 7 + cs[14] * 8
                + cs[17] * 9) & 0x3f];
        cs[19] = ENC[(cs[1] + cs[3] * 2 + cs[5] * 3 + cs[7] * 4 + cs[9] * 5 + cs[11] * 6 + cs[13] * 7 + cs[15] * 8
                + cs[16] * 9) & 0x3f];
        return new String(cs);
    }

    /**
     * 合法性检查
     */
    private boolean check(String str, int len) {
        if (str.length() != len) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '-' || c == '_')) {
                return false;
            }
        }
        return true;
    }
}
