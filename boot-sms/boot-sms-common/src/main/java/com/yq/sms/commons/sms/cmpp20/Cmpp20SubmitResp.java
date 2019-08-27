package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import com.yq.sms.commons.util.DataTypeConvert;
import lombok.Data;

/**
 * <p> 提交响应</p>
 * @author youq  2019/8/26 14:22
 */
@Data
public class Cmpp20SubmitResp extends IHeadBodyMessage {

    private byte[] msgId;

    private int result;

    private String seq1;

    private String seq2;

    private String seq3;

    private Cmpp20HeadMessage headMessage;

    @Override
    public void byteToObject(byte[] content) {
        this.bitContent = content;
        int indexOf = 0;
        msgId = new byte[8];
        System.arraycopy(content, indexOf, msgId, 0, 8);
        long[] SMS_SEQ = DataTypeConvert.getSMSSEQ(msgId);
        seq1 = SMS_SEQ[0] + "";
        seq2 = SMS_SEQ[1] + "";
        seq3 = SMS_SEQ[2] + "";
        indexOf += 8;
        result = content[indexOf];
    }

    @Override
    public void objectToByte() {
        int indexOf = 0;
        bitContent = new byte[9];
        if (msgId == null) {
            msgId = new byte[8];
        }
        System.arraycopy(msgId, indexOf, bitContent, 0, msgId.length);
        indexOf += 8;
        bitContent[indexOf] = (byte) result;
    }

    @Override
    public String toString() {
        return "CmppSubmitResp{" +
                "msgId=" + seq1 + "," + seq2 + "," + seq3 +
                ", result=" + result +
                '}';
    }

}
