package com.yq.kernel.utils.mail;

import lombok.Data;

import java.util.List;
import java.util.Properties;

/**
 * <p> 发送邮件使用的基本信息</p>
 * @author youq  2019/1/14 10:31
 */
@Data
public class MailSenderInfo {

    /**
     * 发送邮件的服务器的IP和端口
     */
    private String mailServerHost;

    private String mailServerPort = "25";

    /**
     * 邮件发送者的地址
     */
    private String fromAddress;

    /**
     * 接收邮件的昵称
     */
    private String fromNick;

    /**
     * 邮件接收者的地址
     */
    private String[] toAddress;

    /**
     * 邮件接收者的地址(抄送)
     */
    private String[] ccAddress;

    /**
     * 登陆邮件发送服务器的用户名和密码
     */
    private String userName;
    private String password;

    /**
     * 是否需要身份验证
     */
    private boolean validate = false;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件的文本内容
     */
    private String content;

    /**
     * 邮件附件的文件名
     */
    private List<String> attachFileNames;

    /**
     * 获得邮件会话属性
     */
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        return p;
    }

}