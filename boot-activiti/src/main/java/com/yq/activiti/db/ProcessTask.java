package com.yq.activiti.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p> 流程执行任务（没啥用）</p>
 * @author youq  2020/5/15 11:59
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "t_process_task")
@Where(clause = "remove=false")
@SQLDelete(sql = "update t_process_task set remove=true,last_modified=now() where id=?")
public class ProcessTask extends Base {

    /**
     * activiti taskId
     */
    private String activitiTaskId;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 执行流程id
     */
    private Integer processId;

    /**
     * 流程类型
     */
    private String processType;

}
