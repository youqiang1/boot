package com.yq.activiti.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> activiti task</p>
 * @author youq  2020/5/7 15:49
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackModelTask implements Serializable {

    private String id;

    private String name;

    private FeedbackModel activiti;

    private Date createTime;

}
