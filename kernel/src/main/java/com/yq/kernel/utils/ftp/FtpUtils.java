package com.yq.kernel.utils.ftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p> ftp工具类</p>
 * @author youq  2019/12/2 18:09
 */
@Slf4j
public class FtpUtils {

    private static FTPClient ftp;

    private static final String TEMP_SUFFIX = ".tmp";

    public static void main(String[] args) throws Exception {
        connect("A", "test,HBLOC,20191202", "", 21, "", "");
        File file = new File("E:\\youq\\yx\\111\\1.txt");
        boolean b = uploadFile(file);
        log.info("上传文件【{}】，结果：{}", file.getName(), b);
    }

    private static boolean connect(String ftpUploadFolder, String folderName,
                                   String addr, int port, String username, String password) throws Exception {
        ftp = new FTPClient();
        int reply;
        ftp.connect(addr, port);
        ftp.login(username, password);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftp.setDataTimeout(60000);
        ftp.setConnectTimeout(60000);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return false;
        }
        ftp.changeWorkingDirectory(ftpUploadFolder);
        String[] pathArr = folderName.split(",");
        for (String aPathArr : pathArr) {
            boolean flag = ftp.makeDirectory(aPathArr);
            log.info("在目标服务器建立文件夹【{}】，结果：{} ", aPathArr, flag);
            ftp.changeWorkingDirectory(aPathArr);
        }
        return true;
    }

    public static boolean uploadFile(File file) {
        boolean flag = true;
        try {
            //文件名
            String fileName = file.getName();
            //上传流
            FileInputStream input = new FileInputStream(file);
            //上传
            flag = ftp.storeFile(fileName + TEMP_SUFFIX, input);
            if (flag) {
                ftp.rename(fileName + TEMP_SUFFIX, fileName);
                log.info("文件【{}】上传完成", file.getAbsolutePath());
            } else {
                // 改为被动模式
                ftp.enterLocalPassiveMode();
                flag = ftp.storeFile(fileName + TEMP_SUFFIX, input);
                if (flag) {
                    ftp.rename(fileName + TEMP_SUFFIX, fileName);
                    log.info("文件【{}】上传完成<被动>", file.getAbsolutePath());
                } else {
                    log.error("上传失败,filename={}", fileName);
                }
            }
            //关闭连接
            ftp.logout();
        } catch (Exception e) {
            log.info("ftp上传失败：", e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    log.info("ftp连接关闭失败：", e);
                }
            }
        }
        return flag;
    }

}
