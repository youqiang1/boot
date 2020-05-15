package com.yq.activiti.db;

import com.yq.activiti.common.enu.ReviewStatusEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * <p> 请假流程记录</p>
 * @author youq  2020/5/15 11:55
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "t_leave")
@Where(clause = "remove=false")
@SQLDelete(sql = "update t_leave set remove=true,last_modified=now() where id=?")
public class Leave extends Base {

    /**
     * 当前的activiti taskId
     */
    private String activitiTaskId;

    /**
     * 当前的activiti processId
     */
    private String processId;

    /**
     * 申请人
     */
    private String applyUser;

    /**
     * 请假天数
     */
    private Integer days;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 结果
     */
    private String result;

    /**
     * 审核时间
     */
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
    private LocalDateTime reviewTime;

    /**
     * 审核级别
     */
    @Enumerated(EnumType.STRING)
    private ReviewStatusEnum reviewStatus;

}
