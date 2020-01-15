package com.yq.tools.config.excel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * <p> excel操作工具类</p>
 * @author youq  2019/5/14 12:13
 */
public class ExcelUtils {

    /**
     * <p> 读取Excel，多个sheet</p>
     * @param excel    excel文件
     * @param rowModel 实体类映射，基础BaseRowModel类
     * @return java.util.List<java.lang.Object>
     * @author youq  2019/5/14 12:16
     */
    public static List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return null;
        }
        for (Sheet sheet : reader.getSheets()) {
            if (rowModel != null) {
                sheet.setClazz(rowModel.getClass());
            }
            reader.read(sheet);
        }
        return excelListener.getData();
    }

    /**
     * <p> 读取excel文件中某个sheet的数据</p>
     * @param excel    excel文件
     * @param rowModel 实体类映射，基础BaseRowModel类
     * @param sheetNo  sheet的序号，从1开始
     * @return java.util.List<java.lang.Object>
     * @author youq  2019/5/14 12:23
     */
    public static List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel, int sheetNo) {
        return readExcel(excel, rowModel, sheetNo, 1);
    }

    /**
     * <p> 读取某个sheet的数据</p>
     * @param excel       excel文件
     * @param rowModel    实体类映射，基础BaseRowModel类
     * @param sheetNo     sheet的序号，从1开始
     * @param headLineNum 表头行数，默认为1
     * @return java.util.List<java.lang.Object>
     * @author youq  2019/5/14 12:25
     */
    public static List<Object> readExcel(MultipartFile excel, BaseRowModel rowModel, int sheetNo, int headLineNum) {
        ExcelListener excelListener = new ExcelListener();
        ExcelReader reader = getReader(excel, excelListener);
        if (reader == null) {
            return null;
        }
        reader.read(new Sheet(sheetNo, headLineNum, rowModel.getClass()));
        return excelListener.getData();
    }

    /**
     * <p> 导出excel，一个sheet，带表头</p>
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param filename  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param rowModel  映射实体类，Excel 模型
     * @author youq  2019/5/14 14:12
     */
    public static void writeExcelToFolder(List<? extends BaseRowModel> list,
                                  String filename, String sheetName, BaseRowModel rowModel) {
        ExcelWriter writer = new ExcelWriter(getOutputStream(filename), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, rowModel.getClass());
        sheet.setSheetName(sheetName);
        writer.write(list, sheet);
        writer.finish();
    }

    /**
     * <p> 导出excel，一个sheet，带表头</p>
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param filename  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param rowModel  映射实体类，Excel 模型
     * @author youq  2019/5/14 14:12
     */
    public static void writeExcel(HttpServletResponse response, List<? extends BaseRowModel> list,
                                  String filename, String sheetName, BaseRowModel rowModel) {
        ExcelWriter writer = new ExcelWriter(getOutputStream(filename, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, rowModel.getClass());
        sheet.setSheetName(sheetName);
        writer.write(list, sheet);
        writer.finish();
    }

    /**
     * <p> 导出excel，一个sheet，带表头</p>
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param filename  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param rowModel  映射实体类，Excel 模型
     * @author youq  2019/5/14 14:12
     */
    public static void writeExcel2(HttpServletResponse response, List<? extends BaseRowModel> list,
                                  String filename, String sheetName, BaseRowModel rowModel, int sheetNo) {
        ExcelWriter writer = new ExcelWriter(getOutputStream(filename, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(sheetNo, 0, rowModel.getClass());
        sheet.setSheetName(sheetName);
        writer.write(list, sheet);
        writer.finish();
    }

    /**
     * <p> 导出excel，多个sheet，带表头</p>
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param filename  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param rowModel  映射实体类，Excel 模型
     * @author youq  2019/5/14 14:12
     */
    public static ExcelWriterFactory writeExcelWithSheets(HttpServletResponse response, List<? extends BaseRowModel> list,
                                                          String filename, String sheetName, BaseRowModel rowModel) {
        ExcelWriterFactory writerFactory = new ExcelWriterFactory(getOutputStream(filename, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, rowModel.getClass());
        sheet.setSheetName(sheetName);
        writerFactory.write(list, sheet);
        return writerFactory;
    }

    /**
     * <p> 导出文件是为Writer生成outputStream</p>
     * @param filename 文件名
     * @param response HttpServletResponse
     * @return java.io.OutputStream
     * @author youq  2019/5/14 12:28
     */
    private static OutputStream getOutputStream(String filename, HttpServletResponse response) {
        String filePath = filename + ".xlsx";
        File dbfFile = new File(filePath);
        try {
            boolean flag = true;
            if (!dbfFile.exists() || dbfFile.isDirectory()) {
                flag = dbfFile.createNewFile();
            }
            if (flag) {
                filename = new String(dbfFile.getName().getBytes(), "ISO-8859-1");
                response.addHeader("Content-Disposition", "attachment;filename=" + filename);
                response.setContentType("application/octet-stream");
                response.setCharacterEncoding("utf-8");
                return response.getOutputStream();
            } else {
                throw new ExcelException("创建文件失败！");
            }
        } catch (Exception e) {
            throw new ExcelException("创建文件失败！");
        }
    }

    /**
     * <p> 导出文件是为Writer生成outputStream</p>
     * @param filename 文件名
     * @return java.io.OutputStream
     * @author youq  2019/5/14 12:28
     */
    private static OutputStream getOutputStream(String filename) {
        String filePath = filename + ".xlsx";
        File dbfFile = new File(filePath);
        try {
            boolean flag = true;
            if (!dbfFile.exists() || dbfFile.isDirectory()) {
                flag = dbfFile.createNewFile();
            }
            if (flag) {
                return new FileOutputStream(dbfFile);
            } else {
                throw new ExcelException("创建文件失败！");
            }
        } catch (Exception e) {
            throw new ExcelException("创建文件失败！");
        }
    }

    /**
     * <p> 创建ExcelReader对象</p>
     * @param excel         excel文件
     * @param excelListener 自定义的监听器
     * @return com.alibaba.excel.ExcelReader
     * @author youq  2019/5/14 12:20
     */
    private static ExcelReader getReader(MultipartFile excel, ExcelListener excelListener) {
        String filename = excel.getOriginalFilename();
        if (StringUtils.isEmpty(filename)
                || (!filename.toLowerCase().endsWith(".xls") && (!filename.toLowerCase().endsWith(".xlsx")))) {
            throw new ExcelException("文件格式错误");
        }
        try {
            InputStream inputStream = new BufferedInputStream(excel.getInputStream());
            return new ExcelReader(inputStream, null, excelListener, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
