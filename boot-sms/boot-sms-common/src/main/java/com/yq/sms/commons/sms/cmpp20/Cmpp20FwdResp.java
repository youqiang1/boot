package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> fwd响应</p>
 * @author youq  2019/8/26 19:32
 */
@Data
@Slf4j
public class Cmpp20FwdResp extends IHeadBodyMessage {

    private byte[] msgId;

    private int pkTotal;

    private int pkNumber;

    private int result;

    private Cmpp20HeadMessage headMessage;

    @Override
    public void byteToObject(byte[] content) {
        this.bitContent = content;
        int indexOf = 0;
        msgId = new byte[8];
        System.arraycopy(content, indexOf, msgId, 0, 8);
        indexOf += 8;
        pkTotal = content[indexOf];
        indexOf += 1;
        pkNumber = content[indexOf];
        indexOf += 1;
        result = content[indexOf];
    }

    @Override
    public void objectToByte() {
        int indexOf = 0;
        bitContent = new byte[11];
        System.arraycopy(msgId, 0, bitContent, indexOf, msgId.length);
        indexOf += 8;
        byte[] tempByte = new byte[] {(byte) pkTotal};
        System.arraycopy(tempByte, 0, bitContent, indexOf, tempByte.length);
        indexOf += 1;
        tempByte = new byte[] {(byte) pkNumber};
        System.arraycopy(tempByte, 0, bitContent, indexOf, tempByte.length);
        indexOf += 1;
        tempByte = new byte[] {(byte) result};
        System.arraycopy(tempByte, 0, bitContent, indexOf, tempByte.length);
    }

}
