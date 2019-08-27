package com.yq.sms.commons.sms.cmpp20;

import com.yq.sms.commons.sms.packet.IHeadBodyMessage;
import lombok.Data;

/**
 * <p> 心跳响应</p>
 * @author youq  2019/8/26 19:40
 */
@Data
public class Cmpp20ActiveTestResp extends IHeadBodyMessage {

    private int reserve;

    @Override
    public void byteToObject(byte[] content) {
        reserve = content[0];
    }

    @Override
    public void objectToByte() {
        bitContent = new byte[1];
        bitContent[0] = (byte) reserve;
    }

    @Override
    public String toString() {
        return "Cmpp20ActiveTestResp{" +
                "reserve=" + reserve +
                '}';
    }

}
