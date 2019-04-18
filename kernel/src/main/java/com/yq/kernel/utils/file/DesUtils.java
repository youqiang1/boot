package com.yq.kernel.utils.file;

import com.yq.kernel.utils.ByteConvert;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;

/**
 * <p> file des cbc加解密</p>
 * @author youq  2019/4/18 15:47
 */
@Slf4j
public class DesUtils {

    private static final String DESKEY = "youqge@t";

    private static final String PASSKEY = "youqwy76";

    private static final String IDENTIFIER = "ABCD";

    /**
     * <p> 对文件进行加密，使用'\0'补全，并删除源文件</p>
     * @param filePath    要加密的文件路径
     * @param fileName    文件
     * @param desFilePath 加密后文件路径
     * @return java.lang.String
     * @author youq  2019/4/18 15:47
     */
    public static String encoder(String filePath, String fileName, String desFilePath) throws IOException {
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
            //补充
            int dataLen = dataBytes.length;
            //原始数据长度
            byte[] dataLenBytes = ByteConvert.int2Bytes(dataLen);
            //标识符
            byte[] identifierBytes = IDENTIFIER.getBytes();
            //待补充长度
            int supplementLen = 8 - dataLen % 8;
            //构建待加密的数据字节数组
            int total = dataLen + 8 + supplementLen;
            byte[] writeBytes = new byte[total];
            System.arraycopy(dataBytes, 0, writeBytes, 0, dataLen);
            System.arraycopy(dataLenBytes, 0, writeBytes, dataLen, 4);
            System.arraycopy(identifierBytes, 0, writeBytes, dataLen + 4, 4);
            //加密并写入输出流
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            byte[] k = DESKEY.getBytes();
            byte[] iv = PASSKEY.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(k)), new IvParameterSpec(iv));
            out.write(cipher.doFinal(writeBytes));
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
    public static String decoder(String filePath, String fileName, String desFilePath) throws IOException {
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
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            byte[] k = DESKEY.getBytes();
            byte[] iv = PASSKEY.getBytes();
            cipher.init(Cipher.DECRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(k)), new IvParameterSpec(iv));
            byte[] decrypt = cipher.doFinal(dataBytes);
            //获取真实数据长度
            byte[] dataLenBytes = new byte[4];
            //标识符对比字节数组
            byte[] identifierComparisonBytes = new byte[4];
            //补充字节
            byte supplementByte = 0;
            //补充字节数
            int supplementSize = 0;
            //字节数组的最后一位
            int decryptLen = decrypt.length - 1;
            for (int i = decryptLen; i > 0; i--) {
                if (decrypt[i] == supplementByte) {
                    supplementSize++;
                } else {
                    break;
                }
            }
            identifierComparisonBytes[3] = decrypt[decryptLen - supplementSize];
            identifierComparisonBytes[2] = decrypt[decryptLen - supplementSize - 1];
            identifierComparisonBytes[1] = decrypt[decryptLen - supplementSize - 2];
            identifierComparisonBytes[0] = decrypt[decryptLen - supplementSize - 3];
            String identifierComparison = new String(identifierComparisonBytes);
            log.info("identifierComparison = " + identifierComparison);
            if (!IDENTIFIER.equals(identifierComparison)) {
                return null;
            }
            dataLenBytes[3] = decrypt[decryptLen - supplementSize - 4];
            dataLenBytes[2] = decrypt[decryptLen - supplementSize - 5];
            dataLenBytes[1] = decrypt[decryptLen - supplementSize - 6];
            dataLenBytes[0] = decrypt[decryptLen - supplementSize - 7];
            int dataLen = ByteConvert.bytes2Int(dataLenBytes);
            log.info("sourceDataLen = " + dataLen);
            //构建数据字节数组，并写入输出流
            byte[] writeBytes = new byte[dataLen];
            System.arraycopy(decrypt, 0, writeBytes, 0, dataLen);
            out.write(writeBytes);
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

    public static void main(String[] args) throws Exception {
        // decoder("d:/temp/zip", "145-723005104-210200-210000-1555552800-92986.zip", "d:/temp/zip/enc");
        encoder("d:/temp/zip", "151.zip", "d:/temp/zip/enc");
    }

}
