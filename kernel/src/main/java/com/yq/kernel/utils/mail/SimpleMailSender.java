package com.yq.kernel.utils.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * <p> 不带附件的发送</p>
 *
 * @author youq  2019/1/14 10:33
 */
@Slf4j
public class SimpleMailSender {

    /**
     * 以文本格式发送邮件
     * @param mailInfo 待发送的邮件的信息
     */
    public static boolean sendTextMail(MailSenderInfo mailInfo) {
        try {
            // 判断是否需要身份认证
            MyAuthenticator authenticator = null;
            Properties pro = mailInfo.getProperties();
            if (mailInfo.isValidate()) {
                // 如果需要身份认证，则创建一个密码验证器
                authenticator = new MyAuthenticator(mailInfo.getUserName(),
                        mailInfo.getPassword());
            }
            // 根据邮件会话属性和密码验证器构造一个发送邮件的session
            Session sendMailSession = Session
                    .getDefaultInstance(pro, authenticator);
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            String[] mailTo = mailInfo.getToAddress();
            Address[] to = new InternetAddress[mailTo.length];
            for (int i = 0; i < mailTo.length; i++) {
                to[i] = new InternetAddress(mailTo[i]);
            }
            mailMessage.setRecipients(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException e) {
            log.error("邮件发送异常：", e);
            return false;
        }
    }

    /**
     * 以HTML格式发送邮件
     *
     * @param mailInfo 待发送的邮件信息
     */
    public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session
                .getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from;
            try {
                if (!StringUtils.isEmpty(mailInfo.getFromNick())) {
                    from = new InternetAddress(mailInfo.getFromAddress(), mailInfo.getFromNick());
                } else {
                    from = new InternetAddress(mailInfo.getFromAddress());
                }
                mailMessage.setFrom(from);
            } catch (UnsupportedEncodingException e) {
                log.error("创建邮件发送者地址异常：", e);
            }
            // 设置邮件消息的发送者

            // 创建邮件的接收者地址，并设置到邮件消息中
            String[] mailTo = mailInfo.getToAddress();
            Address[] to = new InternetAddress[mailTo.length];
            for (int i = 0; i < mailTo.length; i++) {
                to[i] = new InternetAddress(mailTo[i]);
            }
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipients(Message.RecipientType.TO, to);

            // 创建邮件的接收者地址(抄送)，并设置到邮件消息中
            if (mailInfo.getCcAddress() != null && mailInfo.getCcAddress().length > 0) {
                String[] mailCc = mailInfo.getCcAddress();
                Address[] cc = new InternetAddress[mailCc.length];
                for (int i = 0; i < mailCc.length; i++) {
                    cc[i] = new InternetAddress(mailCc[i]);
                }
                // Message.RecipientType.CC属性表示接收者的类型为CC
                mailMessage.setRecipients(Message.RecipientType.CC, cc);
            }
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            log.error("邮件发送异常：", ex);
        }
        return false;
    }

    public static void main(String[] args) {
        MailSenderInfo info = new MailSenderInfo();
        info.setFromAddress("techreport@vcread.com");
        info.setMailServerHost("smtp.ym.163.com");
        info.setValidate(true);
        info.setMailServerPort("25");
        info.setPassword("jsb2017");
        info.setSubject("这是一个测试");
        String[] addr = {"youqiang@unioncast.cn", "136600283@qq.com"};
        info.setToAddress(addr);
        info.setUserName("techreport@vcread.com");
        info.setContent("<a href='http://www.baidu.com'>这是百度</a>");
        try {
            sendHtmlMail(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        info.setContent(date + "[演员]需要转换。");
        sendTextMail(info);
    }

}