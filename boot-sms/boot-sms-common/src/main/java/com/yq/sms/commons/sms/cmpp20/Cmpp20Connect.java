package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import com.yq.sms.commons.util.DataTypeConvert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> cmpp20协议连接命令数据体</p>
 * @author youq  2019/8/23 19:54
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cmpp20Connect extends IHeadBodyMessage {

    /**
     * 用户名 源地址，此处为SP_Id，即SP的企业代码。 长度 6
     */
    private String sourceAddr;

    /**
     * 密码
     */
    private String password;

    /**
     * 用于鉴别源地址。其值通过单向MD5 hash计算得出，表示如下：AuthenticatorSource =MD5（Source_Addr+9
     * 字节的0 +shared secret+timestamp）Shared secret
     * 由中国移动与源地址实体事先商定，timestamp格式为：MMDDHHMMSS，即月日时分秒，10位。 长度 16
     */
    private String authenticatorSource;

    /**
     * 双方协商的版本号(高位4bit表示主版本号,低位4bit表示次版本号)，对于3.0的版本，高4bit为3，低4位为0 长度 1
     */
    private int version;

    /**
     * 时间戳的明文,由客户端产生,格式为MMDDHHMMSS，即月日时分秒，10位数字的整型，右对齐 。 长度 4
     */
    private int timestamp;

    private static SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");

    @Override
    public void byteToObject(byte[] content) {
        try {
            int indexOf = 0;
            sourceAddr = new String(content, indexOf, 6);
            indexOf += 6;
            authenticatorSource = DataTypeConvert.bytesToHexString(content, indexOf, 16);
            indexOf += 16;
            version = content[indexOf];
            indexOf += 1;
            timestamp = DataTypeConvert.Bytes4ToInt(content, indexOf);
        } catch (Exception e) {
            log.error("Exception is happened!", e);
        }
    }

    @Override
    public void objectToByte() {
        try {
            int indexOf = 0;
            bitContent = new byte[27];
            byte[] TempByte = null;
            TempByte = sourceAddr.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 6;
            String dateStr = sdf.format(new Date());
            timestamp = Integer.parseInt(dateStr);
            TempByte = getAuthenticatorSource(sourceAddr, password, dateStr); // 协议用户名、密码
            authenticatorSource = DataTypeConvert.bytesToHexString(TempByte);
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 16;
            TempByte = new byte[] { (byte) version };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = DataTypeConvert.IntToBytes4(timestamp);
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
        } catch (Exception e) {
            log.error("Exception is happened!", e);
        }
    }

    public static byte[] getAuthenticatorSource(String sourceAddr, String sharedSecret, String timestamp) throws Exception {
        byte[] b09 = { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };
        MessageDigest mdTemp = MessageDigest.getInstance("MD5");
        mdTemp.update(sourceAddr.getBytes());
        mdTemp.update(b09);
        mdTemp.update(sharedSecret.getBytes());
        mdTemp.update(timestamp.getBytes());
        return mdTemp.digest();
    }

    /**
     * <p> 不足10位，前面补0</p>
     * @return java.lang.String
     * @author youq  2019/8/23 19:58
     */
    public String getTimestamp() {
        StringBuilder timestamp = new StringBuilder(this.timestamp);
        for (int i = timestamp.length(); i < 10; i++) {
            timestamp.insert(0, "0");
        }
        return timestamp.toString();
    }

    @Override
    public String toString() {
        return "Cmpp20Connect{" +
                "sourceAddr='" + sourceAddr + '\'' +
                ", password='" + password + '\'' +
                ", authenticatorSource='" + authenticatorSource + '\'' +
                ", version=" + version +
                ", timestamp=" + timestamp +
                '}';
    }

}
