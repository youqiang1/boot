package com.yq.sms.commons.sms.cmpp20;

import com.alibaba.druid.util.StringUtils;
import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> fwd消息</p>
 * @author youq  2019/8/27 17:40
 */
@Data
@Slf4j
public class Cmpp20Fwd extends IHeadBodyMessage {

    private String sourceId;

    private String destinationId;

    private int nodesCount;

    private int msgFwdType;

    private byte[] msgId;

    private int pkTotal;

    private int pkNumber;

    private int registeredDelivery;

    private int msgLevel;

    private String serviceId;

    private int feeUserType;

    private String feeTerminalId;

    private int tpPid;

    private int tpUdhi;

    private int msgFmt;

    private String msgSrc;

    private String feeType;

    private String feeCode;

    private String validTime;

    private String atTime;

    private String srcId;

    private int destUsrTl;

    private String destId;

    private int msgLength;

    private String msgContent;

    private String reserve;

    private byte[] msgContentB;

    private String[] messageToString;

    private int protocolHeadLen;

    @Override
    public void byteToObject(byte[] content) {
        try {
            int indexOf = 0;
            sourceId = new String(content, indexOf, 6);
            indexOf += 6;
            destinationId = new String(content, indexOf, 6);
            indexOf += 6;
            nodesCount = content[indexOf];
            indexOf += 1;
            msgFwdType = content[indexOf];
            indexOf += 1;
            msgId = new byte[8];
            System.arraycopy(content, indexOf, msgId, 0, 8);
            indexOf += 8;
            pkTotal = content[indexOf];
            indexOf += 1;
            pkNumber = content[indexOf];
            indexOf += 1;
            registeredDelivery = content[indexOf];
            indexOf += 1;
            msgLevel = content[indexOf];
            indexOf += 1;
            serviceId = new String(content,indexOf,10);
            indexOf += 10;
            feeUserType = content[indexOf];
            indexOf += 1;
            feeTerminalId = new String(content, indexOf, 21).trim();
            indexOf += 21;
            tpPid = content[indexOf];
            indexOf += 1;
            tpUdhi = content[indexOf];
            indexOf += 1;
            msgFmt = content[indexOf];
            indexOf += 1;
            msgSrc = new String(content, indexOf, 6).trim();
            indexOf += 6;
            feeType = new String(content, indexOf, 2).trim();
            indexOf += 2;
            feeCode = new String(content, indexOf, 6).trim();
            indexOf += 6;
            validTime = new String(content, indexOf, 17).trim();
            indexOf += 17;
            atTime = new String(content, indexOf, 17).trim();
            indexOf += 17;
            srcId = new String(content, indexOf, 21).trim();
            indexOf += 21;
            destUsrTl = content[indexOf];
            indexOf += 1;
            for (int i = 0; i < destUsrTl; i++) {
                if (i > 0) {
                    destId += ",";
                }
                destId += new String(content, indexOf, 21).trim();
            }
            indexOf += destUsrTl * 21;
            msgLength = content[indexOf];
            indexOf += 1;
            msgContentB = new byte[msgLength & 0xff];
            System.arraycopy(content, indexOf, msgContentB, 0, msgContentB.length);
            if (this.tpUdhi== 1) {
                protocolHeadLen = msgContentB[0] + 1;
                //代表长短信
                byte tpUdhiHead[] = new byte[protocolHeadLen];
                System.arraycopy(this.msgContentB, 0, tpUdhiHead, 0, protocolHeadLen);
                pkTotal = tpUdhiHead[protocolHeadLen - 2];
                pkNumber = tpUdhiHead[protocolHeadLen - 1];
                byte tmpContent[] = new byte[(this.msgLength & 0xff) - protocolHeadLen];
                System.arraycopy(this.msgContentB, protocolHeadLen, tmpContent, 0, (this.msgLength & 0xff) - protocolHeadLen);
                msgContent = new String(tmpContent, "UTF-16BE").trim();
            } else {
                if (this.msgFmt == 15) {
                    msgContent = new String(msgContentB, "GBK").trim();
                } else if (this.msgFmt == 8) {
                    msgContent = new String(msgContentB, "UTF-16BE").trim();
                }
            }
            indexOf += msgContentB.length;
            reserve = new String(content, indexOf, 8).trim();
        } catch (Exception e) {
            log.error("fwd消息解析异常：", e);
        }
    }

    @Override
    public void objectToByte() {
        try {
            int indexOf = 0;
            if (tpUdhi == 0) {
                if(msgContentB == null){
                    msgContentB = msgContent.getBytes("gbk");
                }
                msgFmt = 15;
            }
            msgLength = msgContentB.length;
            String[] numbers = destId != null ? destId.split(",") : new String[]{};
            destUsrTl = numbers.length;
            int ContentLength = 140 + msgContentB.length + destUsrTl * 21;

            bitContent = new byte[ContentLength];
            byte[] TempByte = sourceId.getBytes() ;
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 6;
            TempByte = destinationId.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 6;
            TempByte = new byte[] { (byte) nodesCount };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf+=1;
            TempByte = new byte[] { (byte) msgFwdType };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf+=1;
            System.arraycopy(msgId, 0, bitContent, indexOf, 8);
            indexOf += 8;
            TempByte = new byte[] { (byte) pkTotal };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = new byte[] { (byte) pkNumber };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = new byte[] { (byte) registeredDelivery };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = new byte[] { (byte) msgLevel };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = serviceId.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 10;
            TempByte = new byte[] { (byte) feeUserType };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = feeTerminalId.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 21;
            TempByte = new byte[] { (byte) tpPid };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = new byte[] { (byte) tpUdhi };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = new byte[] { (byte) msgFmt };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = msgSrc.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 6;
            TempByte = feeType.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 2;
            TempByte = feeCode.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 6;
            TempByte = validTime.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 17;
            TempByte = atTime.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 17;
            TempByte = srcId.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 21;
            destUsrTl = numbers.length;
            TempByte = new byte[] { (byte) destUsrTl };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            if (!StringUtils.isEmpty(destId)) {
                destId = "";
            }
            for (int i = 0; i < numbers.length; i++) {
                TempByte = numbers[i].getBytes();
                System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
                indexOf += 21;
                destId += numbers[i] + ",";
            }
            TempByte = new byte[] { (byte) msgLength };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += 1;
            TempByte = msgContentB;
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
            indexOf += msgLength;
            TempByte = reserve.getBytes();
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
        } catch (Exception e) {
            log.error("cmpp fwd object to byte array exception: ", e);
        }
    }

}
