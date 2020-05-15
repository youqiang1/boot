package com.yq.activiti.common.enu;

import lombok.Getter;

/**
 * <p> 审核状态</p>
 * @author youq  2020/5/15 14:27
 */
@Getter
public enum ReviewStatusEnum {

    WAIT(0, "等待审核"),

    FIRST(1, "一审完成"),

    LAST(2, "终审完成"),
    ;
    private int code;

    private String desc;

    ReviewStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
