package com.yq.kernel.utils.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * <p> 密码验证器</p>
 * @author youq  2019/1/14 10:34
 */
public class MyAuthenticator extends Authenticator {

    private String userName;

    private String password;

    public MyAuthenticator() {
    }

    MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}