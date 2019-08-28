package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.constants.SmsConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> cmpp20长短信处理</p>
 * @author youq  2019/8/26 16:38
 */
@Slf4j
public class CMPP20LongMessage {

    /**
     * <p> 长短信处理</p>
     * @param submit 提交的消息对象
     * @return java.util.List<byte[]>
     * @author youq  2019/8/27 11:27
     */
    public static List<byte[]> longMsgProcess(Cmpp20Submit submit) {
        List<byte[]> sendContents = new ArrayList<>();
        try {
            // 短信最大拆分长度,以字节为单位
            int cmppContentMaxSize = 140;
            byte[] msgContent = submit.getMsgContent().getBytes("UnicodeBigUnmarked");
            // 长短信处理
            if (msgContent.length > cmppContentMaxSize) {
                submit.setMsgFmt(8);
                submit.setTpUdhi(1);
                int messageUCS2Count = msgContent.length / (cmppContentMaxSize - 6);// 长短信分为多少条发送
                if (msgContent.length % (cmppContentMaxSize - 6) != 0) {
                    messageUCS2Count = messageUCS2Count + 1;
                }
                List<String> list = new ArrayList<>();
                int readIndexOf = 0;// 原内容读取位置
                for (int i = 0; i < messageUCS2Count; i++) {
                    if (i == messageUCS2Count - 1) {
                        list.add(new String(msgContent, readIndexOf, msgContent.length - readIndexOf, "UnicodeBigUnmarked"));
                    } else {
                        list.add(new String(msgContent, readIndexOf, cmppContentMaxSize - 6, "UnicodeBigUnmarked"));
                        readIndexOf += cmppContentMaxSize - 6;
                    }
                }

                submit.setMessageToString(list.toArray(new String[list.size()]));
                //消息内容协议头，长短信使用
                byte[] tpUdhiHead = new byte[6];
                tpUdhiHead[0] = 0x05;
                tpUdhiHead[1] = 0x00;
                tpUdhiHead[2] = 0x03;
                // 随机生成一个1-255的整数，用于转换成一个byte
                tpUdhiHead[3] = (byte) ((Math.random() * 100) * 2.54 + 1);
                tpUdhiHead[4] = (byte) submit.getMessageToString().length;
                tpUdhiHead[5] = 0x00;// 默认为第一条

                for (int i = 0; i < submit.getMessageToString().length; i++) {
                    int indexOf = 0;
                    tpUdhiHead[5] = (byte) (tpUdhiHead[5] + 1);
                    byte[] tempByte = submit.getMessageToString()[i].getBytes("UnicodeBigUnmarked");
                    // 拷贝头
                    byte[] b = new byte[tempByte.length + 6];
                    System.arraycopy(tpUdhiHead, 0, b, indexOf, tpUdhiHead.length);
                    // 拷贝体
                    indexOf += 6;
                    System.arraycopy(tempByte, 0, b, indexOf, tempByte.length);
                    submit.setMsgContentB(b);
                    submit.setPkNumber(tpUdhiHead[5]);
                    submit.setPkTotal(messageUCS2Count);
                    submit.objectToByte();
                    sendContents.add(submit.bitContent);
                }
            } else {
                submit.setMsgFmt(15);
                submit.setMsgContentB(submit.getMsgContent().getBytes("GBK"));
                submit.setMessageToString(new String[] {submit.getMsgContent()});
                submit.objectToByte();
                sendContents.add(submit.bitContent);
            }
        } catch (Exception e) {
            log.error("cmpp20 longMsgProcess exception: ", e);
        }
        return sendContents;
    }

    /**
     * <p> fwd长短信处理</p>
     * @param cmpp20Fwd CMPP20 FWD消息
     * @param messageId 消息id
     * @return java.util.List<byte[]>
     * @author youq  2019/8/27 18:22
     */
    public static List<byte[]> fwdLongMsgProcess(Cmpp20Fwd cmpp20Fwd, String messageId) {
        List<byte[]> sendContents = new ArrayList<>();
        try {
            // 短信最大拆分长度,以字节为单位
            int cmppContentMaxSize = 140;
            byte[] msgContent = cmpp20Fwd.getMsgContent().getBytes("UnicodeBigUnmarked");
            // 长短信处理
            if (msgContent.length > cmppContentMaxSize) {
                cmpp20Fwd.setMsgFmt(8);
                cmpp20Fwd.setTpUdhi(1);
                // 长短信分为多少条发送
                int messageUCS2Count = msgContent.length % (cmppContentMaxSize - 6) == 0 ? msgContent.length / (cmppContentMaxSize - 6) : msgContent.length / (cmppContentMaxSize - 6) + 1;
                List<String> list = new ArrayList<>();
                int readIndexOf = 0;// 原内容读取位置
                for (int i = 0; i < messageUCS2Count; i++) {
                    if (i == messageUCS2Count - 1) {
                        list.add(new String(msgContent, readIndexOf, msgContent.length - readIndexOf, SmsConstants.CHARACTER_UNICODE));
                    } else {
                        list.add(new String(msgContent, readIndexOf, cmppContentMaxSize - 6, SmsConstants.CHARACTER_UNICODE));
                        readIndexOf += cmppContentMaxSize - 6;
                    }
                }

                cmpp20Fwd.setMessageToString(list.toArray(new String[list.size()]));
                //消息内容协议头，长短信使用
                byte[] tpUdhiHead = new byte[6];
                tpUdhiHead[0] = 0x05;
                tpUdhiHead[1] = 0x00;
                tpUdhiHead[2] = 0x03;
                // 随机生成一个1-255的整数，用于转换成一个byte
                tpUdhiHead[3] = (byte) ((Math.random() * 100) * 2.54 + 1);
                tpUdhiHead[4] = (byte) cmpp20Fwd.getMessageToString().length;
                tpUdhiHead[5] = 0x00;// 默认为第一条

                for (int i = 0; i < cmpp20Fwd.getMessageToString().length; i++) {
                    int indexOf = 0;
                    tpUdhiHead[5] = (byte) (tpUdhiHead[5] + 1);
                    byte[] tempByte = cmpp20Fwd.getMessageToString()[i].getBytes(SmsConstants.CHARACTER_UNICODE);
                    // 拷贝头
                    byte[] b = new byte[tempByte.length + 6];
                    System.arraycopy(tpUdhiHead, 0, b, indexOf, tpUdhiHead.length);
                    // 拷贝体
                    indexOf += 6;
                    System.arraycopy(tempByte, 0, b, indexOf, tempByte.length);
                    cmpp20Fwd.setMsgContentB(b);
                    cmpp20Fwd.setPkNumber(tpUdhiHead[5]);
                    cmpp20Fwd.setPkTotal(messageUCS2Count);
                    cmpp20Fwd.setMsgId(messageId.getBytes());
                    cmpp20Fwd.objectToByte();
                    sendContents.add(cmpp20Fwd.bitContent);
                }
            } else {
                cmpp20Fwd.setMsgFmt(15);
                cmpp20Fwd.setMsgContentB(cmpp20Fwd.getMsgContent().getBytes(SmsConstants.CHARACTER_GBK));
                cmpp20Fwd.setMessageToString(new String[] {cmpp20Fwd.getMsgContent()});
                cmpp20Fwd.setMsgId(messageId.getBytes());
                cmpp20Fwd.objectToByte();
                sendContents.add(cmpp20Fwd.bitContent);
            }
        } catch (Exception e) {
            log.error("cmpp20 fwd longMsgProcess exception: ", e);
        }
        return sendContents;
    }

}
