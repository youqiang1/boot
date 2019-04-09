package com.yq.kernel.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author youqiang
 * @Description: 下载文件工具类
 * @create 2017/9/21 14:08
 */
@Slf4j
public class DownloadFileUtils {

    /**
     * <p> 下载工具类，下载并删除下载后的文件</p>
     * @param response HttpServletResponse
     * @param fileName 文件名称|不是路径
     * @param file     下载的文件
     * @author youq  2018/11/21 15:47
     */
    public static void downloadFile(HttpServletResponse response, String fileName, File file) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        String encodeFileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        response.addHeader("Content-Disposition", "attachment;filename=" + encodeFileName);

        Files.copy(file.toPath(), response.getOutputStream());

        //删掉临时文件
        if (file.exists()) {
            boolean deleteResult = file.delete();
            if (!deleteResult) {
                log.warn("文件" + file.getAbsolutePath() + "删除失败！");
            }
        }
    }

    /**
     * <p> 下载工具类，下载不除下载后的文件</p>
     * @param response HttpServletResponse
     * @param fileName 文件名称|不是路径
     * @param file     下载的文件
     * @author youq  2018/11/21 15:47
     */
    public static void downloadFileNotDelete(HttpServletResponse response, String fileName, File file) throws IOException {
        // 读到流中
        InputStream inStream = new FileInputStream(file);
        // 设置输出的格式
        response.reset();
        response.setContentType("bin");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
