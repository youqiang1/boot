package com.yq.sms.client.entity;

import com.yq.kernel.enu.OperatorTypeEnum;
import com.yq.sms.commons.enu.ChannelStateEnum;
import com.yq.sms.commons.enu.ProtocolTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * <p> 通道信息</p>
 * @author youq  2019/8/23 17:38
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Where(clause = "remove = false")
@SQLDelete(sql = "update channel_info set remove = true, last_modified = now() where id = ?")
public class ChannelInfo extends Base {

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
     * 通道状态
     */
    @Enumerated(EnumType.STRING)
    private ChannelStateEnum state;

    /**
     * 运营商
     */
    @Enumerated(EnumType.STRING)
    private OperatorTypeEnum operatorType;

    /**
     * 协议类型
     */
    @Enumerated(EnumType.STRING)
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
