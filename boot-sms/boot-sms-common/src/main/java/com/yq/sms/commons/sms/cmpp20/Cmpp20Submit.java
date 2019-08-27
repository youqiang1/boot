package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * <p> 短信提交</p>
 * @author youq  2019/8/26 14:50
 */
@Slf4j
@Data
public class Cmpp20Submit extends IHeadBodyMessage {

    private long msgId;

    private int pkTotal;

    private int pkNumber;

    /**
     * 是否要求返回状态确认报告：0：不需要1：需要2：产生SMC话单（该类型短信仅供网关计费使用，不发送给目的终端) 长度 1
     */
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

    private String destTerminalId;

    private String[] destTerminalIds;

    private int msgLength;

    private String msgContent;

    private String reserve;

    private byte[] msgContentB;

    private String[] messageToString;

    private String messageId;

    private Long submitTime;

    private Long userId;

    private int protocolHeadLen;

    @Override
    public void byteToObject(byte[] content) {
        setProtocolHeadLen(6);
        try {
            int indexOf = 0;
            indexOf += 8;
            pkTotal = content[indexOf];
            indexOf += 1;
            pkNumber = content[indexOf];
            indexOf += 1;
            registeredDelivery = content[indexOf];
            indexOf += 1;
            msgLevel = content[indexOf];
            indexOf += 1;
            serviceId = new String(content, indexOf, 10).trim();
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
                    destTerminalId += ",";
                }
                destTerminalId += new String(content, indexOf, 21).trim();
            }
            indexOf += destUsrTl * 21;
            msgLength = content[indexOf];
            indexOf += 1;
            msgContentB = new byte[msgLength & 0xff];
            System.arraycopy(content, indexOf, msgContentB, 0, msgContentB.length);
            if (this.tpUdhi == 1) {
                if (msgContentB[0] == 6) {
                    setProtocolHeadLen(7);
                }
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
            log.error("Exception is happened!", e);
        }
    }

    @Override
    public void objectToByte() {
        try {
            int indexOf = 0;
            if (tpUdhi == 0) {
                msgContentB = msgContent.getBytes("gbk");
                msgFmt = 15;
            }
            msgLength = msgContentB.length;
            String[] numbers = destTerminalIds == null ? destTerminalId.split(",") : destTerminalIds;
            destUsrTl = numbers.length;
            int ContentLength = 126 + msgContentB.length + destUsrTl * 21;
            bitContent = new byte[ContentLength];
            byte[] TempByte = new byte[] { (byte) msgId };
            System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
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
            if (StringUtils.isNotEmpty(destTerminalId)) {
                destTerminalId = "";
            }
            for (String number : numbers) {
                TempByte = number.getBytes();
                System.arraycopy(TempByte, 0, bitContent, indexOf, TempByte.length);
                indexOf += 21;
                destTerminalId += number + ",";
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
            log.error("Exception is happened!", e);
        }
    }

    @Override
    public String toString() {
        try {
            return "Cmpp20Submit{" +
                    "msgId=" + msgId +
                    ", pkTotal=" + pkTotal +
                    ", pkNumber=" + pkNumber +
                    ", registeredDelivery=" + registeredDelivery +
                    ", msgLevel=" + msgLevel +
                    ", serviceId='" + serviceId + '\'' +
                    ", feeUserType=" + feeUserType +
                    ", feeTerminalId='" + feeTerminalId + '\'' +
                    ", tpPid=" + tpPid +
                    ", tpUdhi=" + tpUdhi +
                    ", msgFmt=" + msgFmt +
                    ", msgSrc='" + msgSrc + '\'' +
                    ", feeType='" + feeType + '\'' +
                    ", feeCode='" + feeCode + '\'' +
                    ", validTime='" + validTime + '\'' +
                    ", atTime='" + atTime + '\'' +
                    ", srcId='" + srcId + '\'' +
                    ", destUsrTl=" + destUsrTl +
                    ", destTerminalId='" + destTerminalId + '\'' +
                    ", destTerminalIds=" + Arrays.toString(destTerminalIds) +
                    ", msgLength=" + msgLength +
                    ", msgContent='" + (StringUtils.isEmpty(msgContent) ? new String(msgContentB, protocolHeadLen, (msgLength & 0xff) - protocolHeadLen, "UnicodeBigUnmarked") : msgContent) + '\'' +
                    ", reserve='" + reserve + '\'' +
                    ", messageId='" + messageId + '\'' +
                    ", submitTime=" + submitTime +
                    ", userId=" + userId +
                    ", protocolHeadLen=" + protocolHeadLen +
                    '}';
        } catch (UnsupportedEncodingException e) {
            log.error("exception：", e);
            return "";
        }
    }

}
