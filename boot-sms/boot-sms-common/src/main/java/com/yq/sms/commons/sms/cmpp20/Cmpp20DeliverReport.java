package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import com.yq.sms.commons.util.DataTypeConvert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 状态报告</p>
 * @author youq  2019/8/26 18:30
 */
@Slf4j
@Data
public class Cmpp20DeliverReport extends IHeadBodyMessage {

    private String seq1;

    private String seq2;

    private String seq3;

    private byte[] msgId;

    private String stat;

    private String submitTime;

    private String doneTime;

    private String destTerminalId;

    private int smscSequence;

    @Override
    public void byteToObject(byte[] content) {
        int indexOf = 0;
        msgId = new byte[8];
        System.arraycopy(content, indexOf, msgId, 0, msgId.length);
        long[] SMS_SEQ = DataTypeConvert.getSMSSEQ(msgId);
        seq1 = SMS_SEQ[0] + "";
        seq2 = SMS_SEQ[1] + "";
        seq3 = SMS_SEQ[2] + "";
        indexOf += 8;
        stat = new String(content, indexOf, 7).trim();
        indexOf += 7;
        submitTime = new String(content, indexOf, 10).trim();
        indexOf += 10;
        doneTime = new String(content, indexOf, 10).trim();
        indexOf += 10;
        destTerminalId = new String(content, indexOf, 21).trim();
        indexOf += 21;
        smscSequence = DataTypeConvert.Bytes4ToInt(content, indexOf);
    }

    @Override
    public void objectToByte() {
        try {
            int ContentLength = 60;
            bitContent = new byte[ContentLength];
            int indexOf = 0;
            byte[] TempByte = msgId;
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 8;
            TempByte = stat.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 7;
            TempByte = submitTime.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 10;
            TempByte = doneTime.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 10;
            TempByte = destTerminalId.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 21;
            TempByte = new byte[] { (byte) smscSequence };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
        } catch (Exception e) {
            log.error("Exception is happened!", e);
        }
    }

    @Override
    public String toString() {
        String formatString = "Msg_Id=%s\tStat=%s\tSubmit_time=%s\tDone_time=%s\tDest_terminal_Id=%s\tSMSC_sequence=%s\t";
        return String.format(formatString, seq1 + "," + seq2 + "," + seq3, stat, submitTime, doneTime, destTerminalId, smscSequence);
    }

}
