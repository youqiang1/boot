package com.yq.kernel.utils.codec;

import com.yq.kernel.constants.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

@Slf4j
public class Aes256Tool {

    private static String Key = "9acqughp";

    private static String iv = "uggnojig$p9uoar9";

    public static void main(String[] args) throws Exception {
        String encryptStr = encrypt("123");
        System.out.println(encryptStr);
        System.out.println(new String(decrypt(encryptStr), GlobalConstants.UTF8));
        decoderFile("E:\\youq\\yx", "726171773_101_20191105143448_068856_1.zip", "E:\\youq\\yx\\111");
        encoderFile("E:\\youq\\yx\\111", "726171773_101_20191105143448_068856_1.zip", "E:\\youq\\yx");
    }

    /**
     * <p> 对文件进行加密，并删除源文件</p>
     * @param filePath    要加密的文件路径
     * @param fileName    文件
     * @param desFilePath 加密后文件路径
     * @return java.lang.String
     * @author youq  2019/4/18 15:47
     */
    public static String encoderFile(String filePath, String fileName, String desFilePath) throws IOException {
        OutputStream out = null;
        InputStream is = null;
        int bufferSize = 1024;
        try {
            out = new FileOutputStream(desFilePath + File.separator + fileName);
            is = new FileInputStream(filePath + File.separator + fileName);
            //读取文件数据
            byte[] buffer = new byte[bufferSize];
            byte[] dataBytes = new byte[is.available()];
            int t = 0;
            int r;
            while ((r = is.read(buffer)) > 0) {
                System.arraycopy(buffer, 0, dataBytes, t * bufferSize, r);
                t++;
            }
            byte[] byte_ASE = getEnCipher().doFinal(dataBytes);
            String encode = new BASE64Encoder().encodeBuffer(byte_ASE);
            encode = encode.replaceAll("\r|\n", "");
            out.write(encode.getBytes(GlobalConstants.UTF8));
            out.flush();
        } catch (Exception e) {
            log.error("zip文件加密失败：", e);
        } finally {
            if (is != null) {
                is.close();
            }
            if (out != null) {
                out.close();
            }
        }
        //删除源文件
        new File(filePath + File.separator + fileName).delete();
        return desFilePath + File.separator + fileName;
    }

    /**
     * <p> 对文件进行解密，解密后删除源文件</p>
     * @param filePath    要解密的文件路径
     * @param fileName    文件名
     * @param desFilePath 解密后文件路径
     * @return java.lang.String
     * @author youq  2019/4/18 15:48
     */
    public static String decoderFile(String filePath, String fileName, String desFilePath) throws IOException {
        OutputStream out = null;
        InputStream is = null;
        int bufferSize = 8;
        try {
            out = new FileOutputStream(desFilePath + File.separator + fileName);
            is = new FileInputStream(filePath + File.separator + fileName);
            //读取文件数据
            byte[] buffer = new byte[bufferSize];
            byte[] dataBytes = new byte[is.available()];
            int t = 0;
            int r;
            while ((r = is.read(buffer)) > 0) {
                System.arraycopy(buffer, 0, dataBytes, t * bufferSize, r);
                t++;
            }
            //解密
            String data = new String(dataBytes, GlobalConstants.UTF8);
            byte[] byte_ASE = new BASE64Decoder().decodeBuffer(data);
            out.write(getDeCipher().doFinal(byte_ASE));
            out.flush();
        } catch (Exception e) {
            log.error("zip加密文件解密失败：", e);
        } finally {
            if (is != null) {
                is.close();
            }
            if (out != null) {
                out.close();
            }
        }
        //删除源文件
        new File(filePath + File.separator + fileName).delete();
        return desFilePath + File.separator + fileName;
    }

    public static String encrypt(String data) throws Exception {
        byte[] byte_ASE = getEnCipher().doFinal(data.getBytes(GlobalConstants.UTF8));
        return new BASE64Encoder().encode(byte_ASE);
    }

    public static byte[] decrypt(String data) throws Exception {
        byte[] bytes = new BASE64Decoder().decodeBuffer(data);
        return getDeCipher().doFinal(bytes);
    }

    private static Cipher getDeCipher() throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(Key.getBytes());
        keygen.init(256, secureRandom);
        SecretKey original_Key = keygen.generateKey();
        byte[] raw = original_Key.getEncoded();
        SecretKey secretKey = new SecretKeySpec(raw, "AES");
        Cipher deCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv.getBytes());
        deCipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
        return deCipher;
    }

    private static Cipher getEnCipher() throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(Key.getBytes());
        keygen.init(256, secureRandom);
        SecretKey original_Key = keygen.generateKey();
        byte[] raw = original_Key.getEncoded();
        SecretKey secretKey = new SecretKeySpec(raw, "AES");
        Cipher enCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv.getBytes());
        enCipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
        return enCipher;
    }

}
