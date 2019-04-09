package com.yq.kernel.utils.poi;

import com.yq.kernel.utils.poi.model.ExcelDataModel;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.util.List;

/**
 * @Description: 导出Excel工具类
 * @Date: 2017/9/21 14:06
 * @Author: youq
 */
public class ExportExcelUtils {

    /**
     * @Description: 导出Excel的方法
     * @param: workbook
     * @param: dataModel 表格数据
     * @Date: 2017/9/21 20:21
     * @Author: panliyong
     */
    public static void exportExcel(HSSFWorkbook workbook, ExcelDataModel dataModel)
            throws Exception {
        //sheet的位置，0表示第一个表格中的第一个sheet
        int sheetNum = dataModel.getSheetNum();
        //sheet的名称
        String sheetTitle = dataModel.getSheetTitle();
        //标题之上的顶部数据
        String[] top = dataModel.getTop();
        //表格的标题
        String[] headers = dataModel.getHeaders();
        //表格的数据
        List<List<String>> result = dataModel.getData();

        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);

        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);

        int rownum = 0;
        if (top != null && top.length > 0) {
            HSSFRow topRow = sheet.createRow(rownum);
            for (int i = 0; i < top.length; i++) {
                setCellValue(topRow, i, style, top[i]);
            }
            rownum = 1;
        }

        // 产生表格标题行
        HSSFRow row;
        if (headers != null && headers.length > 0) {
            row = sheet.createRow(rownum);
            for (int i = 0; i < headers.length; i++) {
                setCellValue(row, i, style, headers[i]);
            }
        }
        // 遍历集合数据，产生数据行
        if (result != null) {
            ++rownum;
            for (List<String> m : result) {
                row = sheet.createRow(rownum);
                int cellIndex = 0;
                for (String str : m) {
                    HSSFCell cell = row.createCell((short) cellIndex);
                    cell.setCellValue(str);
                    cellIndex++;
                }
                rownum++;
            }
        }
        // 自适应列宽
        sheet.autoSizeColumn(1, true);
    }

    /**
     * <p> 设置内容</p>
     * @param row     HSSFRow
     * @param i       列数
     * @param style   HSSFCellStyle
     * @param content 列内容
     * @author youq  2019/1/11 17:27
     */
    private static void setCellValue(HSSFRow row, int i, HSSFCellStyle style, String content) {
        HSSFCell cell = row.createCell((short) i);
        cell.setCellStyle(style);
        HSSFRichTextString text = new HSSFRichTextString(content);
        cell.setCellValue(text.toString());
    }

}
