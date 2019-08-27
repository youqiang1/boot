package com.yq.sms.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> 发送短信请求参数</p>
 * @author youq  2019/8/26 14:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMsgRequest implements Serializable {

    private String mobile;

    private String msg;

    private String channelId;

    private String extendedCode;

    private String userId;

}
