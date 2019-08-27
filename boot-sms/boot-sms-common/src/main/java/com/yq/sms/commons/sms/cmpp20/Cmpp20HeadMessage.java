package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import com.yq.sms.commons.util.DataTypeConvert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> cmpp20协议数据头</p>
 * @author youq  2019/8/23 19:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cmpp20HeadMessage extends IHeadBodyMessage {

    private int totalLength;

    private int commandId;

    private int sequenceId;

    @Override
    public void byteToObject(byte[] content) {
        this.bitContent = content;
        int indexOf = 0;
        totalLength = DataTypeConvert.Bytes4ToInt(content, indexOf);
        indexOf += 4;
        commandId = DataTypeConvert.Bytes4ToInt(content, indexOf);
        indexOf += 4;
        sequenceId = DataTypeConvert.Bytes4ToInt(content, indexOf);
    }

    @Override
    public void objectToByte() {
        bitContent = new byte[12];
        int indexOf = 0;
        byte[] tempData = null;
        tempData = DataTypeConvert.toHH(totalLength);
        System.arraycopy(tempData, 0, bitContent, indexOf, 4);
        indexOf += 4;
        tempData = DataTypeConvert.toHH(commandId);
        System.arraycopy(tempData, 0, bitContent, indexOf, 4);
        indexOf += 4;
        tempData = DataTypeConvert.LongToBytes4(sequenceId);
        System.arraycopy(tempData, 0, bitContent, indexOf, 4);
    }

    @Override
    public String toString() {
        return "Cmpp20HeadMessage{" +
                "totalLength=" + totalLength +
                ", commandId=" + commandId +
                ", sequenceId=" + sequenceId +
                '}';
    }

}
