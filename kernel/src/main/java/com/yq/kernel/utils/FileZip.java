package com.yq.kernel.utils;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p> java压缩成zip</p>
 * @author youq  2018/11/22 14:56
 */
public class FileZip {

    /**
     * <p> 压缩文件夹</p>
     * @param inputFileName 要压缩的文件夹(整个完整路径)
     * @param zipFileName   压缩后的文件(整个完整路径)
     * @author youq  2018/11/22 14:56
     */
    public static void zip(String inputFileName, String zipFileName) throws Exception {
        zip(zipFileName, new File(inputFileName));
    }

    /**
     * <p> 压缩文件夹</p>
     * @param zipFileName 压缩后的文件(整个完整路径)
     * @param inputFile   要压缩的文件
     * @author youq  2018/11/22 15:03
     */
    public static void zip(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipFileName));
        zip(out, inputFile, "");
        out.close();
    }

    /**
     * <p> 压缩操作</p>
     * @param out  ZipOutputStream
     * @param file 待压缩文件|文件夹
     * @param base basePath
     * @author youq  2018/11/22 14:58
     */
    private static void zip(ZipOutputStream out, File file, String base) throws Exception {
        if (file.isDirectory()) {
            File[] fl = file.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            if (fl != null && fl.length > 0) {
                for (File aFl : fl) {
                    zip(out, aFl, base + aFl.getName());
                }
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(file);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }

    /**
     * <p> 文件压缩</p>
     * @param fileName    待压缩文件
     * @param zipFileName 压缩后的文件名
     * @author youq  2018/11/22 14:57
     */
    public static void fileZip(String fileName, String zipFileName) {
        try {
            //定义要压缩的文件
            File file = new File(fileName);
            //定义压缩文件的名称
            File zipFile = new File(zipFileName);
            //定义输入文件流
            InputStream input = new FileInputStream(file);
            //定义压缩输出流
            ZipOutputStream zipOut = null;
            //实例化压缩输出流,并制定压缩文件的输出路径
            zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            //设置注释
            // zipOut.setComment("www.demo.com");
            int temp = 0;
            while ((temp = input.read()) != -1) {
                zipOut.write(temp);
            }
            input.close();
            zipOut.close();
            file.delete();
        } catch (Exception e) {
            LoggerFactory.getLogger(FileZip.class).error("压缩文件失败：", e);
        }

    }

}


//=====================文件压缩=========================
/*//把文件压缩成zip
File zipFile = new File("E:/demo.zip");
//定义输入文件流
InputStream input = new FileInputStream(file);
//定义压缩输出流	
ZipOutputStream zipOut = null;
//实例化压缩输出流,并制定压缩文件的输出路径  就是E盘下,名字叫 demo.zip
zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
zipOut.putNextEntry(new ZipEntry(file.getName()));
//设置注释
zipOut.setComment("www.demo.com");
int temp = 0;
while((temp = input.read()) != -1) {
	zipOut.write(temp);	
}		
input.close();
zipOut.close();*/
//==============================================
