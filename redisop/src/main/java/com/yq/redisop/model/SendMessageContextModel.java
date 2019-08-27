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
import java.util.HashMap;

/**
 * <p> 发送上下文model</p>
 * @author youq  2019/8/26 15:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageContextModel implements Serializable {

    /**
     * 发送标实
     */
    private String sendKey;

    /**
     * 发送内容,因为有长短信的内容，所以必须在同一连接上发送，因而有了集合的存在
     */
    private byte[] sendContent;

    /**
     * 发送时的对象
     */
    private HashMap<String, String> sendObject;

    /**
     * 客户提交时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime submitTime;

    /**
     * 计费条数
     */
    private Integer billingCount;

}
