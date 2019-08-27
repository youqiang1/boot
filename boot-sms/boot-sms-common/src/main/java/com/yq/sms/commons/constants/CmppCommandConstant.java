package com.yq.sms.commons.constants;

/**
 * <p> CMPP协议 命令类型</p>
 * @author youq  2019/8/23 14:46
 */
public class CmppCommandConstant {

    /**
     * 请求连接
     */
	public final static int CMPP_CONNECT = 0x00000001;

    /**
     * 请求连接应答
     */
	public final static int CMPP_CONNECT_RESP = 0x80000001;

    /**
     * 终止连接
     */
	public final static int CMPP_TERMINATE = 0x00000002;

    /**
     * 终止连接应答
     */
	public final static int CMPP_TERMINATE_RESP = 0x80000002;

    /**
     * 提交短信 （下行）
     */
	public final static int CMPP_SUBMIT = 0x00000004;

    /**
     * 提交短信应答
     */
	public final static int CMPP_SUBMIT_RESP = 0x80000004;

    /**
     * 短信下发（状态报告、上行）
     */
	public final static int CMPP_DELIVER = 0x00000005;

    /**
     * 下发短信应答
     */
	public final static int CMPP_DELIVER_RESP = 0x80000005;

    /**
     * 激活测试，心跳
     */
	public final static int CMPP_ACTIVE_TEST = 0x00000008;

    /**
     * 激活测试应答
     */
	public final static int CMPP_ACTIVE_TEST_RESP = 0x80000008;

    /**
     * 跳转
     */
	public final static int CMPP_FWD = 0x00000009;

    /**
     * 跳转响应
     */
	public final static int CMPP_FWD_RESP = 0x80000009;

}
