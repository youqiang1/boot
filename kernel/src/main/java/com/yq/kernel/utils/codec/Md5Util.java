package com.yq.kernel.utils.codec;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p> md5加解密</p>
 * @author youq  2018/4/3 9:59
 */
@Slf4j
public class Md5Util {

    private static final String ALGORITHM = "MD5";

    private static MessageDigest messageDigest = getMessageDigest();

    /**
     * 密码加密
     * @param rawPass 字符串
     * @return 加密后串
     */
    public static String encode(String rawPass) {
        if(StringUtils.isEmpty(rawPass)) {
            return null;
        }
        try {
            //加密后的字符串
            return Base64.encodeBase64String(messageDigest.digest(rawPass.trim().getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 确定计算方法
     * @return
     */
    private static final MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm [" + ALGORITHM + "]");
        }
    }

    /**
     * <p> MD5加密并转化为十六进制字符</p>
     * @param buf 待加密字符串
     * @return java.lang.String
     * @author youq  2018/11/8 15:57
     */
    public static String encoderByMd5(String buf) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] rs = messageDigest.digest(buf.getBytes("UTF-8"));
            StringBuilder digestHexStr = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                digestHexStr.append(byteHEX(rs[i]));
            }
            return digestHexStr.toString();
        } catch (Exception e) {
            log.error("MD5加密失败:", e);
        }
        return "";
    }

    /**
     * <p> 将字节转化为十六进制字符</p>
     * @param ib 待装好字节
     * @return java.lang.String
     * @author youq  2018/11/8 15:58
     */
    private static String byteHEX(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    /**
     * <p> MD5加密</p>
     * @param source 待加密数据
     * @return java.lang.String
     * @author youq  2020/4/24 11:52
     */
    public static String md5(String source) {
        if (source == null)
            return null;
        String s = null;
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte tmp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            s = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void main(String[] args) {
        String s = "123456";
        System.out.println(md5(s));
        System.out.println(encoderByMd5(s).toLowerCase());
    }

}
