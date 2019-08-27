package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import com.yq.sms.commons.util.DataTypeConvert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 上行、状态报告响应</p>
 * @author youq  2019/8/26 18:42
 */
@Data
@Slf4j
public class Cmpp20DeliverResp extends IHeadBodyMessage {

    private byte[] msgId;

    private int result;

    @Override
    public void byteToObject(byte[] content) {
        this.bitContent = content;
        System.arraycopy(content, 0, msgId, 0, 8);
        result = content[8];
    }

    @Override
    public void objectToByte() {
        bitContent = new byte[9];
        System.arraycopy(msgId, 0, bitContent, 0, 8);
        bitContent[8] = (byte) result;
    }

    @Override
    public String toString() {
        String formatsString = "Msg_Id=%s\tResult=%s";
        return String.format(formatsString, DataTypeConvert.bytesToHexString(msgId), result);
    }

}
