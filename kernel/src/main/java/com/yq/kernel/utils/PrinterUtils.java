package com.yq.kernel.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;
import javax.swing.*;
import java.awt.print.PrinterJob;
import java.io.*;

/**
 * <p> 打印工具类 TODO 待验证</p>
 * @author youq  2019/4/10 13:11
 */
@Slf4j
public class PrinterUtils {

    public static void main(String[] args) throws Exception {
        //待打印的文件
        File file = new File("d:/temp/tx.jpg");
        //打印机名包含字串
        String printerName = "HP MFP M436 PCL6";
        jpgPrint(file, printerName);
    }

    /**
     * <p> 图片打印</p>
     * @param file        图片文件名
     * @param printerName 打印机名称
     * @author youq  2019/4/10 13:43
     */
    public static void jpgPrint(File file, String printerName) throws PrintException {
        if (file == null) {
            log.error("缺少打印文件");
            return;
        }
        if (StringUtils.isEmpty(printerName)) {
            log.error("缺少打印机名称");
            return;
        }
        InputStream fis = null;
        try {
            // 设置打印格式，如果未确定类型，可选择AUTOSENSE
            DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;
            // 设置打印参数
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            //份数
            printRequestAttributeSet.add(new Copies(1));
            //单双面
            printRequestAttributeSet.add(Sides.DUPLEX);
            //获得本台电脑连接的所有打印机
            PrintService[] printServices = PrinterJob.lookupPrintServices();
            if (printServices == null || printServices.length == 0) {
                log.info("打印失败，未找到可用打印机，请检查。");
                return;
            }
            // 定位打印服务
            PrintService printService = null;
            //匹配指定打印机
            for (PrintService existPrintService : printServices) {
                log.info("ExistingPrinter: 【{}】", existPrintService.getName());
                if (existPrintService.getName().contains(printerName)) {
                    printService = existPrintService;
                    break;
                }
            }
            if (printService == null) {
                log.error("打印失败，未找到名称为【{}】的打印机，请检查。", printerName);
                return;
            }
            // 构造待打印的文件流
            fis = new FileInputStream(file);
            Doc doc = new SimpleDoc(fis, flavor, null);
            // 创建打印作业
            DocPrintJob job = printService.createPrintJob();
            job.print(doc, printRequestAttributeSet);
        } catch (FileNotFoundException e1) {
            log.error("未找到打印的文件: ", e1);
        } finally {
            // 关闭打印的文件流
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void print(File file) {
        try {
            //创建打印作业
            JFileChooser fileChooser = new JFileChooser();
            int state = fileChooser.showOpenDialog(null);
            if (JFileChooser.APPROVE_OPTION == state) {
                //构建打印请求属性集
                HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                //设置打印格式，因为未确定类型，所以选择autosense(application/octet-stream)
                DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
                //查找所有的可用打印服务
                PrintService[] printServiceArr = PrintServiceLookup.lookupPrintServices(flavor, pras);
                //定位默认的打印服务
                PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
                //显示打印对话框
                PrintService service =
                        ServiceUI.printDialog(null, 200, 200, printServiceArr, defaultService, flavor, pras);
                if (service != null) {
                    //创建打印作业
                    DocPrintJob job = service.createPrintJob();
                    //构造待打印的文件流
                    FileInputStream fis = new FileInputStream(file);
                    DocAttributeSet docAttributeSet = new HashDocAttributeSet();
                    Doc doc = new SimpleDoc(fis, flavor, docAttributeSet);
                    job.print(doc, pras);
                }
            }
        } catch (FileNotFoundException e) {
            log.error("未找到打印的文件", e);
        } catch (PrintException e) {
            log.error("文件打印失败：", e);
        }
    }

}

