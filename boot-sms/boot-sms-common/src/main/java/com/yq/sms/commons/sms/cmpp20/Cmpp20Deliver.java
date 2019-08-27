package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import com.yq.sms.commons.util.DataTypeConvert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 状态报告 上行</p>
 * @author youq  2019/8/26 18:27
 */
@Data
@Slf4j
public class Cmpp20Deliver extends IHeadBodyMessage {

    private byte[] msgId;

    private String destId;

    private String serviceId;

    private int tpPid;

    private int tpUdhi;

    private int msgFmt;

    private String srcTerminalId;

    /**
     * 是否为状态报告0：非状态报告1：状态报告
     */
    private int registeredDelivery;

    private int msgLength;

    private String msgContent;

    private String reserve;

    private byte[] msgContentB;

    private Cmpp20DeliverReport report;

    @Override
    public void byteToObject(byte[] content) {
        try {
            int indexOf = 0;
            msgId = new byte[8];
            System.arraycopy(content, indexOf, msgId, 0, msgId.length);
            indexOf += 8;
            destId = new String(content, indexOf, 21).trim();
            indexOf += 21;
            serviceId = new String(content, indexOf, 10).trim();
            indexOf += 10;
            tpPid = content[indexOf];
            indexOf += 1;
            tpUdhi = content[indexOf];
            indexOf += 1;
            msgFmt = content[indexOf];
            indexOf += 1;
            srcTerminalId = new String(content, indexOf, 21).trim();
            indexOf += 21;
            registeredDelivery = content[indexOf];
            indexOf += 1;
            msgLength = (0x000000FF & ((int) content[indexOf]));
            indexOf += 1;
            msgContentB = new byte[msgLength];
            System.arraycopy(content, indexOf, msgContentB, 0, msgContentB.length);
            indexOf += msgLength;
            if (registeredDelivery == 1) {
                report = new Cmpp20DeliverReport();
                report.byteToObject(msgContentB);
                msgContent = report.toString();
            } else {
                if (tpUdhi == 0) {
                    switch (msgFmt) {
                        case 15:
                            msgContent = new String(msgContentB, 0, msgLength, "gbk").trim();
                            break;
                        case 8:
                            msgContent = new String(msgContentB, 0, msgLength, "UnicodeBigUnmarked").trim();
                            break;
                        case 0:
                            msgContent = new String(msgContentB, 0, msgLength).trim();
                            break;
                        default:
                            msgContent = DataTypeConvert.bytesToHexString(msgContentB);
                            break;
                    }
                } else {
                    msgContent = DataTypeConvert.bytesToHexString(msgContentB);
                }
            }
            reserve = new String(content, indexOf, 8).trim();
        } catch (Exception e) {
            log.error("Exception is happened!", e);
        }
    }

    @Override
    public void objectToByte() {
        try {
            int indexOf = 0;
            int msgLength = 0;
            if (tpUdhi == 0) {
                msgContentB = msgContent.getBytes("gbk");
                msgFmt = 15;
                msgLength = msgContentB.length;
            }else {
                msgLength = msgContentB.length;
            }
            // 消息体的长度不包含消息头的长度 Registered_Delivery == 1 是状态报告
            int ContentLength = (registeredDelivery == 0) ? (73 + msgLength) : (73 + 60);

            bitContent = new byte[ContentLength];
            byte[] TempByte = msgId;
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 8;
            TempByte = destId.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 21;
            TempByte = serviceId.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 10;
            TempByte = new byte[] { (byte) tpPid };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = new byte[] { (byte) tpUdhi };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = new byte[] { (byte) msgFmt };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = srcTerminalId.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 21;
            TempByte = new byte[] { (byte) registeredDelivery };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = new byte[] { (byte) msgLength };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            // -------------------
            if (this.registeredDelivery == 1) {
                report.objectToByte();
                byte[] bitContent2 = report.bitContent;
                System.arraycopy(bitContent2, 0, bitContent, indexOf, bitContent2.length);
            } else {
                System.arraycopy(msgContentB, 0, bitContent, indexOf, msgContentB.length);
            }
            indexOf += msgLength;
            TempByte = reserve.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
        } catch (Exception e) {
            log.error("Exception is happened!", e);
        }
    }

    @Override
    public String toString() {
        String formatString = "Msg_Id=%s\tDest_Id=%s\tService_Id=%s\tTP_pid=%s\tTP_udhi=%s\tMsg_Fmt=%s\tSrc_terminal_Id=%s\tRegistered_Delivery=%s\tMsg_Length=%s\tMsg_Content=%s\tReserved=";
        return String.format(formatString, DataTypeConvert.bytesToHexString(msgId), destId, serviceId, tpPid, tpUdhi, msgFmt, srcTerminalId, registeredDelivery, msgLength, msgContent, reserve);
    }

}
