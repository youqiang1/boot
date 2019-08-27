package com.yq.sms.commons.sms.packet;

import com.yq.sms.commons.util.DataTypeConvert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基础数据包
 *
 * @author
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeadBodyBaseMessage {

	/**
	 * 信息头内容
	 */
	private byte[] head;

	/**
	 * 信息体内容
	 */
    private byte[] content;

	/**
	 * 包体长度
	 */
    private int totalLength;

	/**
	 * 获取包的长度
	 */
	public void setTotalLength() {
		byte[] totalLength = new byte[4];
		System.arraycopy(getHead(), 0, totalLength, 0, 4);
		this.totalLength = DataTypeConvert.Bytes4ToInt(totalLength);
	}

	public String toString() {
		return String.format("TotalLength=%s|Head=%sBody=%s",
                totalLength,
                getHead() != null ? DataTypeConvert.bytesToHexString(getHead()) : "",
                getContent() != null ? DataTypeConvert.bytesToHexString(getContent()) : ""
        );
	}

}
