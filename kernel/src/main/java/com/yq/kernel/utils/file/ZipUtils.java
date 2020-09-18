package com.yq.kernel.utils.file;

import com.yq.kernel.constants.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p> zip工具类</p>
 * @author youq  2019/4/18 10:35
 */
@Slf4j
public class ZipUtils {

    public static void main(String[] args) {
        try {
            String dest = "D:\\yx\\1";
            File[] unzip = unzip("D:\\yx\\123.zip", dest, "123");
            zip(dest, "D:\\yx\\2\\123.zip");
            for (File file : unzip) {
                boolean flag = file.delete();
                log.info("文件【" + file + "】删除：" + flag);
            }
        } catch (ZipException e) {
            log.error("报错了：", e);
        }
    }

    /**
     * <p> 压缩文件或文件夹</p>
     * @param srcPathName 被压缩的文件
     * @param zipFileName zip文件名
     * @author youq  2019/4/18 10:42
     */
    public static void zip(String srcPathName, String zipFileName) {
        File file = new File(srcPathName);
        File zipFile = new File(zipFileName);
        if (!file.exists()) {
            throw new RuntimeException(srcPathName + "不存在！");
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);
            compress(file, out);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p> 压缩</p>
     * @param file 被压缩的文件
     * @param out  输出流
     * @author youq  2019/4/18 10:41
     */
    private static void compress(File file, ZipOutputStream out) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            log.info("压缩文件夹：{}", file.getName());
            compressDirectory(file, out);
        } else {
            log.info("压缩文件：{}", file.getName());
            compressFile(file, out);
        }
    }

    /**
     * <p> 压缩一个目录</p>
     * @param dir 被压缩的文件目录
     * @param out 输出流
     * @author youq  2019/4/18 10:38
     */
    private static void compressDirectory(File dir, ZipOutputStream out) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length <= 0) {
            return;
        }
        for (File file : files) {
            compress(file, out);
        }
    }

    /**
     * <p> 压缩一个文件</p>
     * @param file 文件
     * @param out  输出流
     * @author youq  2019/4/18 10:37
     */
    private static void compressFile(File file, ZipOutputStream out) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(file.getName());
            out.putNextEntry(entry);
            int count;
            byte[] data = new byte[8192];
            while ((count = bis.read(data, 0, 8192)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p> 解压zip文件</p>
     * @param zip      文件
     * @param dest     解压后存放的目录
     * @param password ZIP文件的密码
     * @author youq  2020/9/18 19:26
     */
    public static File[] unzip(String zip, String dest, String password) throws ZipException {
        File zipFile = new File(zip);
        return unzip(zipFile, dest, password);
    }

    /**
     * 使用给定密码解压指定的ZIP压缩文件到指定目录
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     * @param zipFile  指定的ZIP压缩文件
     * @param dest     解压目录
     * @param password ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static File[] unzip(File zipFile, String dest, String password) throws ZipException {
        ZipFile zFile = new ZipFile(zipFile);
        zFile.setFileNameCharset(GlobalConstants.UTF8);
        if (!zFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法,可能被损坏.");
        }
        File destDir = new File(dest);
        if (destDir.isDirectory() && !destDir.exists()) {
            destDir.mkdir();
        }
        if (zFile.isEncrypted()) {
            zFile.setPassword(password.toCharArray());
        }
        zFile.extractAll(dest);

        List<FileHeader> headerList = zFile.getFileHeaders();
        List<File> extractedFileList = new ArrayList<>();
        for (FileHeader fileHeader : headerList) {
            if (!fileHeader.isDirectory()) {
                extractedFileList.add(new File(destDir, fileHeader.getFileName()));
            }
        }
        File[] extractedFiles = new File[extractedFileList.size()];
        extractedFileList.toArray(extractedFiles);
        return extractedFiles;
    }

    /**
     * <p> 解压zip文件</p>
     * @param file     文件
     * @param destDir  解压后存放的目录
     * @param encoding 编码
     * @return boolean
     * @author youq  2020/9/18 19:26
     */
    public static boolean unzip(File file, String destDir, String... encoding) throws Exception {
        if (!file.exists()) {
            throw new IllegalArgumentException("请检查文件" + file.getName() + "是否存在");
        }

        File destFile = new File(destDir);
        if (!destFile.exists()) {
            destFile.mkdir();
        }
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(file);
            String encodingStr = GlobalConstants.UTF8;
            if (encoding.length > 0) {
                encodingStr = encoding[0];
            }
            zipFile.setFileNameCharset(encodingStr);
            zipFile.extractAll(destDir);
        } catch (Exception e) {
            throw new Exception("文件解压缩失败!");
        }
        return true;
    }

}
