package com.yq.kernel.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
     * @param file    被压缩的文件
     * @param out     输出流
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
     * @param dir     被压缩的文件目录
     * @param out     输出流
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
     * @param file    文件
     * @param out     输出流
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

}
