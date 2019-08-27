package com.yq.kernel.enu;

/**
 * <p> 运营商类型</p>
 * @author youq  2019/8/23 15:52
 */
public enum OperatorTypeEnum {

    CMCC(0, "移动"),

    CUCC(1, "联通"),

    CTCC(2, "电信");

    private int code;

    private String message;

    OperatorTypeEnum(int code, String message) {
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
