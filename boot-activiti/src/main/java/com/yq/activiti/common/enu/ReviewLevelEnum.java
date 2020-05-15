package com.yq.activiti.common.enu;

import lombok.Getter;

/**
 * <p> 审核级别</p>
 * @author youq  2020/5/15 13:13
 */
@Getter
public enum ReviewLevelEnum {

    COMMON(0, "普通人员"),

    FIRST(1, "一审人员"),

    LAST(2, "终审人员"),
    ;
    private int code;

    private String desc;

    ReviewLevelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
