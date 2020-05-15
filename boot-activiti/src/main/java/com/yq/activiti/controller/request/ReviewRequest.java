package com.yq.activiti.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> 审核请求参数</p>
 * @author youq  2020/5/15 14:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest implements Serializable {

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 审核结果
     */
    private String result;

    /**
     * 当前的activiti taskId
     */
    private String activitiTaskId;

    /**
     * 当前的activiti processId
     */
    private String processId;

    /**
     * 请假记录id
     */
    private Integer leaveId;

}
