package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import com.yq.sms.commons.util.DataTypeConvert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> cmpp20协议连接响应</p>
 * @author youq  2019/8/23 20:22
 */
@Slf4j
@Data
public class Cmpp20ConnectResp extends IHeadBodyMessage {

    private int status;

    private String authenticatorISMG;

    private int version;

    @Override
    public void byteToObject(byte[] content) {
        this.bitContent = content;
        int indexOf = 0;
        status = content[0];
        indexOf += 1;
        authenticatorISMG = DataTypeConvert.bytesToHexString(content, 16);
        indexOf += 16;
        version = content[indexOf];
    }

    @Override
    public void objectToByte() {
        try {
            int indexOf = 0;
            bitContent = new byte[18];
            bitContent[indexOf] = (byte) status;
            indexOf += 1;
            byte[] TempByte = DataTypeConvert.HexStringToByteArray(authenticatorISMG);
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 16;
            bitContent[indexOf] = (byte) version;
        } catch (Exception e) {
            log.error("Exception is happened!", e);
        }
    }

    @Override
    public String toString() {
        return "Cmpp20ConnectResp{" +
                "status=" + status +
                ", authenticatorISMG='" + authenticatorISMG + '\'' +
                ", version=" + version +
                '}';
    }

}
