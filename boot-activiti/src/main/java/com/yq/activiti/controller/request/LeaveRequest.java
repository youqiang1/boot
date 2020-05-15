package com.yq.activiti.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> 请假申请</p>
 * @author youq  2020/5/15 13:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest implements Serializable {

    /**
     * 申请人
     */
    private String applyUser;

    /**
     * 请假天数
     */
    private int days;

    /**
     * 请假原因
     */
    private String reason;

}
