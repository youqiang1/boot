package com.yq.sms.client.controller.request;

import com.yq.kernel.enu.OperatorTypeEnum;
import com.yq.sms.commons.enu.ProtocolTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> 通道编辑请求参数</p>
 * @author youq  2019/8/26 10:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelEditRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 通道号
     */
    private String channelNumber;

    /**
     * ip
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 登陆用户名
     */
    private String loginName;

    /**
     * 登陆密码
     */
    private String loginPwd;

    /**
     * 运营商
     */
    private OperatorTypeEnum operatorType;

    /**
     * 协议类型
     */
    private ProtocolTypeEnum protocol;

    /**
     * 监听ip
     */
    private String listenHost;

    /**
     * 监听端口
     */
    private Integer listenPort;

    /**
     * 版本号
     */
    private String version;

    /**
     * spId
     */
    private String spId;

    /**
     * 服务码
     */
    private String serviceCode;

    /**
     * 通道连接数
     */
    private Integer socketNumber;

}
