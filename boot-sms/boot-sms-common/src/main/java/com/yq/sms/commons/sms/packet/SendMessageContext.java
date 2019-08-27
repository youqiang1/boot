package com.yq.sms.commons.sms.packet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * 发送对象,上下文类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageContext implements Serializable {

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
	private LocalDateTime submitTime;

}
