package com.yq.activiti.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> 申请审核</p>
 * @author youq  2020/5/20 15:51
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyRequest implements Serializable {

    /**
     * 流程key
     */
    private String processKey;

    /**
     * 申请用户
     */
    private String user;

    /**
     * 备注
     */
    private String desc;

    /**
     * 指派处理的用户
     */
    private String assignUser;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 操作回复结果（问题已解决、问题待解决、回复、打回）
     */
    private String result;

}
