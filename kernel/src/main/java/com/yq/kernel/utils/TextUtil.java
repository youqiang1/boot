package com.yq.kernel.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <p> 文本导出工具类</p>
 * @author youq  2020/8/21 20:05
 */
@Slf4j
public class TextUtil {

    /**
     * <p> 导出文本文件</p>
     * @param response HttpServletResponse
     * @param data     导出的数据
     * @param fileName 文件名
     * @author youq  2020/8/21 20:09
     */
    public static void writeToTxt(HttpServletResponse response, String data, String fileName) throws Exception {
        response.setCharacterEncoding("utf-8");
        //设置响应内容的类型
        response.setContentType("text/plain");
        //设置文件的名称和格式
        response.addHeader(
                "Content-Disposition",
                "attachment; filename="
                        + URLEncoder.encode(fileName + System.currentTimeMillis(), "utf-8")
                        + ".txt");
        BufferedOutputStream buff = null;
        ServletOutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(delNull(data).getBytes(StandardCharsets.UTF_8));
            buff.flush();
            buff.close();
        } catch (Exception e) {
            log.error("导出文件文件出错：", e);
        } finally {
            try {
                if (buff != null) {
                    buff.close();
                }
                if (null != outStr) {
                    outStr.close();
                }
            } catch (Exception e) {
                log.error("关闭流对象出错：", e);
            }
        }
    }

    /**
     * <p> 如果字符串对象为 null，则返回空字符串，否则返回去掉字符串前后空格的字符串</p>
     * @param str 字符串
     * @return java.lang.String
     * @author youq  2020/8/21 20:08
     */
    public static String delNull(String str) {
        String returnStr = "";
        if (StringUtils.isNotBlank(str)) {
            returnStr = str.trim();
        }
        return returnStr;
    }

    /**
     * <p> 生成导出附件中文名。应对导出文件中文乱码</p>
     * @param cnName      导出文件名
     * @param defaultName 默认文件名
     * @return java.lang.String
     * @author youq  2020/8/21 20:08
     */
    public static String genAttachmentFileName(String cnName, String defaultName) {
        try {
            cnName = new String(cnName.getBytes("gb2312"), "ISO8859-1");
        } catch (Exception e) {
            cnName = defaultName;
        }
        return cnName;
    }

    /**
     * <p> 文本文件读取</p>
     * @param filePath 文件路径
     * @author youq  2020/8/24 13:08
     */
    public static String readTxtFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(filePath);
            //判断文件是否存在
            if (file.isFile() && file.exists()) {
                //考虑到编码格式
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt);
                }
                read.close();
            } else {
                log.info("找不到指定的文件");
            }
        } catch (Exception e) {
            log.error("读取文件内容出错：", e);
            return "";
        }
        return sb.toString();
    }

}
