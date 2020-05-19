package com.yq.ftps.util;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

/**
 * <p> 配置</p>
 * @author youq  2019/9/6 18:10
 */
@Slf4j
public class FtpsUtil {

    private Session session;

    private ChannelSftp sftp;

    /**
     * <p> 连接SFTP服务器</p>
     * @param host     ip
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return com.jcraft.jsch.ChannelSftp
     * @author youq  2019/9/6 18:22
     */
    public ChannelSftp connect(String host, int port, String username, String password) throws JSchException {
        JSch jSch = new JSch();
        session = jSch.getSession(username, host, port);
        session.setPassword(password);
        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        session.setConfig(properties);
        session.connect();
        log.info("session connected, opening channel");
        Channel channel = session.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
        log.info("connected to {}", host);
        return sftp;
    }

    /**
     * <p> 连接SFTP服务器</p>
     * @param host       ip
     * @param port       端口
     * @param username   用户名
     * @param privateKey 密钥
     * @param passphrase 口令
     * @return com.jcraft.jsch.ChannelSftp
     * @author youq  2019/9/6 18:23
     */
    public ChannelSftp connect(String host, int port, String username, String privateKey, String passphrase) throws Exception {
        JSch jsch = new JSch();
        //设置密钥和密码
        if (!StringUtils.isEmpty(privateKey)) {
            if (!StringUtils.isEmpty(passphrase)) {
                //设置带口令的密钥
                jsch.addIdentity(privateKey, passphrase);
            } else {
                //设置不带口令的密钥
                jsch.addIdentity(privateKey);
            }
        }
        session = jsch.getSession(username, host, port);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.connect();
        log.info("session connected, opening channel");
        Channel channel = session.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
        log.info("connected to {}", host);
        return sftp;
    }

    public void cd(String directory) throws SftpException {
        if (!exist(directory)) {
            sftp.mkdir(directory);
        }
        sftp.cd(directory);
    }

    public void portForwardingL(int lport, String rhost, int rport) throws JSchException {
        int assignedPort = session.setPortForwardingL(lport, rhost, rport);
        log.info("localhost:{} -> {}:{}", assignedPort, rhost, rport);
    }

    /**
     * <p> 断开连接</p>
     * @author youq  2019/9/6 18:28
     */
    public void disconnect() {
        if (sftp != null) {
            sftp.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * <p> 上传文件</p>
     * @param uploadFile 上传的文件
     * @author youq  2019/9/6 18:30
     */
    public void upload(String uploadFile) throws Exception {
        File file = new File(uploadFile);
        sftp.put(new FileInputStream(file), file.getName());
    }

    /**
     * <p> 上传文件</p>
     * @param file      上传的文件
     * @author youq  2019/9/6 18:30
     */
    public void upload(File file) throws Exception {
        sftp.put(new FileInputStream(file), file.getName());
    }

    /**
     * <p> 上传文件</p>
     * @param file      上传的文件
     * @author youq  2019/9/6 18:30
     */
    public void upload(MultipartFile file, String filename) throws Exception {
        sftp.put(file.getInputStream(), filename);
    }

    /**
     * <p> 上传整个文件夹</p>
     * @param src 文件夹
     * @author youq  2019/9/6 19:43
     */
    public void uploadDir(File src) throws Exception {
        if (src.isFile()) {
            upload(src);
        } else {
            for (File file : src.listFiles()) {
                if (file.isDirectory()) {
                    uploadDir(file);
                }
                upload(file);
            }
        }
    }

    /**
     * <p> 文件夹是否存在</p>
     * @param path 文件夹路径
     * @return boolean
     * @author youq  2019/9/6 19:43
     */
    public boolean exist(String path) throws SftpException {
        String pwd = sftp.pwd();
        try {
            sftp.cd(path);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            } else {
                throw e;
            }
        } finally {
            sftp.cd(pwd);
        }
        return true;
    }

    /**
     * <p> 下载文件</p>
     * @param downloadFile 下载的文件
     * @param saveFile     保存的文件名
     * @author youq  2019/9/6 19:44
     */
    public void download(String downloadFile, String saveFile) throws Exception {
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
    }

    /**
     * <p> 下载文件</p>
     * @param downloadFile 下载的文件
     * @param saveFile     保存的文件
     * @author youq  2019/9/6 19:44
     */
    public void download(String downloadFile, File saveFile) throws Exception {
        sftp.get(downloadFile, new FileOutputStream(saveFile));
    }

    /**
     * <p> 下载整个目录的文件</p>
     * @param src FTP上的目录
     * @param dst 目标目录
     * @author youq  2019/9/6 19:52
     */
    public void downloadDir(String src, File dst) throws Exception {
        try {
            sftp.cd(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dst.mkdirs();

        Vector<ChannelSftp.LsEntry> files = sftp.ls(src);
        for (ChannelSftp.LsEntry lsEntry : files) {
            if (lsEntry.getFilename().equals(".") || lsEntry.getFilename().equals("..")) {
                continue;
            }
            if (lsEntry.getLongname().startsWith("d")) {
                downloadDir(src + "/" + lsEntry.getFilename(), new File(dst, lsEntry.getFilename()));
            } else {
                download(lsEntry.getFilename(), new File(dst, lsEntry.getFilename()));
            }
        }
    }

    /**
     * <p> 删除文件</p>
     * @param deleteFile 文件
     * @author youq  2019/9/6 19:46
     */
    public void delete(String deleteFile) throws SftpException {
        sftp.rm(deleteFile);
    }

    /**
     * <p> 列出目录下的文件</p>
     * @param directory 目录
     * @return java.util.Vector
     * @author youq  2019/9/6 19:46
     */
    public Vector listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }

}
