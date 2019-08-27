package com.yq.sms.commons.enu;

/**
 * <p> 协议类型</p>
 * @author youq  2019/8/23 18:15
 */
public enum ProtocolTypeEnum {

    CMPP20(0, "移动20"),

    CMPP30(1, "移动30"),

    SGIP12(2, "联通12"),

    SMGP30(3, "电信30"),

    CMPP20FWD(4, "移动20FWD");

    private int code;

    private String message;

    ProtocolTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
