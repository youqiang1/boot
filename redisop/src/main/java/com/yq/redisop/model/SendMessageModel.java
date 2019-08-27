package com.yq.redisop.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p> 发送内容</p>
 * @author youq  2019/8/26 17:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageModel implements Serializable {

    private String key;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 消息内容，包含签名
     */
    private String msg;

    /**
     * 通道id
     */
    private String channelId;

    /**
     * 扩展码
     */
    private String extendedCode;

    /**
     * message id
     */
    private String messageId;

    /**
     * 计费条数
     */
    private Integer billingCount;

    /**
     * 运营商message id
     */
    private String operatorMessageId;

    /**
     * 提交时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime submitTime;

    /**
     * 发送时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime sendTime;

    /**
     * 完成时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime finishTime;

    /**
     * 状态
     */
    private String state;

    /**
     * 说明
     */
    private String explain;

    /**
     * 是否入库
     */
    private Boolean saveFlag;

}
