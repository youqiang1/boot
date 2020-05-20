package com.yq.activiti.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> 反馈model</p>
 * @author youq  2020/5/20 16:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackModel implements Serializable {

    private String processId;

    /**
     * 申请人
     */
    private String applyUser;

    private String feedbackContent;

    private Date applyTime;

    private String applyStatus;

    /**
     * 审核人
     */
    private String auditor;

    private String answer;

    private String msg;

}
