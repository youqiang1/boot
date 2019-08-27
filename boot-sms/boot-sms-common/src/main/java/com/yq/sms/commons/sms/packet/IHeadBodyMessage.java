package com.yq.sms.commons.sms.packet;

/**
 * 信息包的转换
 */
public abstract class IHeadBodyMessage {

    /**
     * 数据体
     */
    public byte[] bitContent;

	/**
     * <p> 数据包转对象</p>
     * @param content 数据包
     * @author youq  2019/8/23 16:31
     */
	public abstract void byteToObject(byte[] content);

	/**
     * <p> 对象转数据包</p>
     * @author youq  2019/8/23 16:31
     */
	public abstract void objectToByte();

}