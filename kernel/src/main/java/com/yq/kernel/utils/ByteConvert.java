package com.yq.kernel.utils;

/**
 * <p> byte转换</p>
 * @author youq  2019/4/18 14:50
 */
public class ByteConvert {

    private static final int INT_LEN = 4;

    private static final int CHAR_LEN = 4;

    /**
     * int转换为大端byte[]（高位放在低地址中）
     * @param iValue int值
     */
    public static byte[] int2Bytes(int iValue) {
        byte[] rst = new byte[4];
        rst[0] = (byte) ((iValue & 0xFF000000) >> 24);
        rst[1] = (byte) ((iValue & 0xFF0000) >> 16);
        rst[2] = (byte) ((iValue & 0xFF00) >> 8);
        rst[3] = (byte) (iValue & 0xFF);
        return rst;
    }

    /**
     * int转换为小端byte[]（高位放在高地址中）
     * @param iValue int值
     */
    public static byte[] int2Bytes_LE(int iValue) {
        byte[] rst = new byte[4];
        // 先写int的最后一个字节
        rst[0] = (byte) (iValue & 0xFF);
        // int 倒数第二个字节
        rst[1] = (byte) ((iValue & 0xFF00) >> 8);
        // int 倒数第三个字节
        rst[2] = (byte) ((iValue & 0xFF0000) >> 16);
        // int 第一个字节
        rst[3] = (byte) ((iValue & 0xFF000000) >> 24);
        return rst;
    }

    /**
     * 转换byte数组为int（大端）
     */
    public static int bytes2Int(byte[] bytes) {
        if (bytes.length < INT_LEN) {
            return -1;
        }
        int iRst = (bytes[0] << 24) & 0xFF;
        iRst |= (bytes[1] << 16) & 0xFF;
        iRst |= (bytes[2] << 8) & 0xFF;
        iRst |= bytes[3] & 0xFF;
        return iRst;
    }

    /**
     * 转换byte数组为int（小端）
     * @note 数组长度至少为4，按小端方式转换,即传入的bytes是小端的，按这个规律组织成int
     */
    public static int bytes2Int_LE(byte[] bytes) {
        if (bytes.length < INT_LEN) {
            return -1;
        }
        int iRst = (bytes[0] & 0xFF);
        iRst |= (bytes[1] & 0xFF) << 8;
        iRst |= (bytes[2] & 0xFF) << 16;
        iRst |= (bytes[3] & 0xFF) << 24;
        return iRst;
    }

    /**
     * 转换byte数组为Char（小端）
     * @note 数组长度至少为2，按小端方式转换
     */
    public char bytes2Char_LE(byte[] bytes) {
        if (bytes.length < CHAR_LEN) {
            return (char) -1;
        }
        int iRst = (bytes[0] & 0xFF);
        iRst |= (bytes[1] & 0xFF) << 8;
        return (char) iRst;
    }

    /**
     * 转换byte数组为char（大端）
     * @note 数组长度至少为2，按小端方式转换
     */
    public char bytes2Char(byte[] bytes) {
        if (bytes.length < CHAR_LEN) {
            return (char) -1;
        }
        int iRst = (bytes[0] << 8) & 0xFF;
        iRst |= bytes[1] & 0xFF;
        return (char) iRst;
    }

    /**
     * 转换字符数组为定长byte[]
     * @param chars 字符数组
     * @return 若指定的定长不足返回null, 否则返回byte数组
     */
    public byte[] chars2Bytes_LE(char[] chars) {
        if (chars == null) {
            return null;
        }
        int iCharCount = chars.length;
        byte[] rst = new byte[iCharCount * CHAR_LEN];
        for (int i = 0; i < iCharCount; i++) {
            rst[i * 2] = (byte) (chars[i] & 0xFF);
            rst[i * 2 + 1] = (byte) ((chars[i] & 0xFF00) >> 8);
        }
        return rst;
    }

    /**
     * 转换String为byte[]
     * @param str
     */
    public byte[] string2Bytes_LE(String str) {
        if (str == null) {
            return null;
        }
        char[] chars = str.toCharArray();
        return chars2Bytes_LE(chars);
    }

}
