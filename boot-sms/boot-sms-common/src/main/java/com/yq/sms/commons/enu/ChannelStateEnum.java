package com.yq.sms.commons.enu;

/**
 * <p> 通道状态</p>
 * @author youq  2019/8/23 15:04
 */
public enum ChannelStateEnum {

    STOP(0, "停止"),

    RUNNING(1, "运行"),

    DELETE(2, "删除"),

    EXCEPTION(3, "异常");

    private int code;

    private String message;

    ChannelStateEnum(int code, String message) {
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
