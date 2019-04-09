package com.yq.kernel.utils.mail;

import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

@Slf4j
public class Mail {

    /**
     * 定义发件人、收件人、SMTP服务器、用户名、密码、主题、内容等
     */
    private String displayName;
    private String to;
    private String from;
    private String smtpServer;
    private String username;
    private String password;
    private String subject;
    private String content;
    /**
     * 服务器是否要身份认证
     */
    private boolean ifAuth;

    private String filename = "";

    /**
     * 用于保存发送附件的文件名的集合
     */
    @SuppressWarnings("rawtypes")
    private Vector file = new Vector();

    /**
     * 设置SMTP服务器地址
     */
    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    /**
     * 设置发件人的地址
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 设置显示的名称
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 设置服务器是否需要身份认证
     */
    public void setIfAuth(boolean ifAuth) {
        this.ifAuth = ifAuth;
    }

    /**
     * 设置E-mail用户名
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /**
     * 设置E-mail密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 设置接收者
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * 设置主题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 设置主体内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 该方法用于收集附件名
     */
    @SuppressWarnings("unchecked")
    public void addAttachfile(String fname) {
        file.addElement(fname);
    }

    public Mail() {

    }

    /**
     * 初始化SMTP服务器地址、发送者E-mail地址、用户名、密码、接收者、主题、内容
     */
    public Mail(String smtpServer, String from, String displayName,
                String username, String password, String to, String subject,
                String content) {
        this.smtpServer = smtpServer;
        this.from = from;
        this.displayName = displayName;
        this.ifAuth = true;
        this.username = username;
        this.password = password;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 初始化SMTP服务器地址、发送者E-mail地址、接收者、主题、内容
     */
    public Mail(String smtpServer, String from, String displayName, String to,
                String subject, String content) {
        this.smtpServer = smtpServer;
        this.from = from;
        this.displayName = displayName;
        this.ifAuth = false;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 发送邮件
     */
    public void send() throws Exception {
        //String message = "邮件发送成功！";
        Session session = null;
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpServer);
        // 服务器需要身份认证
        if (ifAuth) {
            props.put("mail.smtp.auth", "true");
            SmtpAuth smtpAuth = new SmtpAuth(username, password);
            session = Session.getDefaultInstance(props, smtpAuth);
        } else {
            props.put("mail.smtp.auth", "false");
            session = Session.getDefaultInstance(props, null);
        }
        session.setDebug(true);
        Transport trans;
        try {
            Message msg = new MimeMessage(session);
            try {
                Address fromAddress = new InternetAddress(from, displayName);
                msg.setFrom(fromAddress);
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(content, "text/html;charset=gb2312");
            mp.addBodyPart(mbp);
            // 有附件
            if (!file.isEmpty()) {
                @SuppressWarnings("rawtypes")
                Enumeration efile = file.elements();
                while (efile.hasMoreElements()) {
                    mbp = new MimeBodyPart();
                    // 选择出每一个附件名
                    filename = efile.nextElement().toString();
                    // 得到数据源
                    FileDataSource fds = new FileDataSource(filename);
                    // 得到附件本身并至入BodyPart
                    mbp.setDataHandler(new DataHandler(fds));
                    // 得到文件名同样至入BodyPart
                    mbp.setFileName(new String((fds.getName())
                            .getBytes("GB2312"), "iso8859-1"));
                    mp.addBodyPart(mbp);
                }
                file.removeAllElements();
            }
            // Multipart加入到信件
            msg.setContent(mp);
            // 设置信件头的发送日期
            msg.setSentDate(new Date());
            // 发送信件
            msg.saveChanges();
            trans = session.getTransport("smtp");
            trans.connect(smtpServer, username, password);
            trans.sendMessage(msg, msg.getAllRecipients());
            trans.close();
        } catch (MessagingException e) {
            log.error("邮件发送失败：", e);
        }
    }

}

class SmtpAuth extends Authenticator {

    private String username, password;

    SmtpAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}
