package com.yq.activiti.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yq.activiti.common.enu.ReviewStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p> activiti</p>
 * @author youq  2020/5/7 15:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveModel implements Serializable {

    /**
     * 申请人
     */
    private String applyUser;

    private int days;

    private String reason;

    private Date applyTime;

    private String applyStatus;

    /**
     * 审核人
     */
    private String auditor;

    private String result;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    /**
     * 二审审核人
     */
    private String reviewers;

    /**
     * 二审结果
     */
    private String reviewResult;

    /**
     * 二审时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewTime;

    /**
     * 审核级别
     */
    private ReviewStatusEnum reviewStatus;

}
