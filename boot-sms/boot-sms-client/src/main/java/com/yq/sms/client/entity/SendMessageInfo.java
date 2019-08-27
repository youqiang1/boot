package com.yq.sms.client.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

/**
 * <p> 发送内容</p>
 * @author youq  2019/8/26 17:14
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Where(clause = "remove = false")
@SQLDelete(sql = "update sms_message_info set remove = true, last_modified = now() where id = ?")
public class SendMessageInfo extends Base {

    /**
     * 手机号
     */
    @Column(name = "mobile", columnDefinition = "varchar(11)")
    private String mobile;

    /**
     * 用户id
     */
    @Column(name = "user_id", columnDefinition = "varchar(16)")
    private String userId;

    /**
     * 消息内容，包含签名
     */
    @Column(name = "msg", columnDefinition = "varchar(2000)")
    private String msg;

    /**
     * 通道id
     */
    @Column(name = "channel_id", columnDefinition = "varchar(11)")
    private String channelId;

    /**
     * 扩展码
     */
    @Column(name = "extended_code", columnDefinition = "varchar(16)")
    private String extendedCode;

    /**
     * message id
     */
    @Column(name = "message_id", columnDefinition = "varchar(400)")
    private String messageId;

    /**
     * 计费条数
     */
    @Column(name = "billing_count", columnDefinition = "int(9)")
    private Integer billingCount;

    /**
     * 运营商message id
     */
    @Column(name = "operator_message_id", columnDefinition = "varchar(32)")
    private String operatorMessageId;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 状态
     */
    @Column(name = "state", columnDefinition = "varchar(32)")
    private String state;

    /**
     * 说明
     */
    @Column(name = "explain_content", columnDefinition = "varchar(64)")
    private String explain;

    /**
     * 是否入库
     */
    private Boolean saveFlag;

}
