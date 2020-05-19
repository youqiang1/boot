package com.yq.activiti.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> activiti</p>
 * @author youq  2020/5/7 15:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activiti implements Serializable {

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

    private Date auditTime;

}
