package com.yq.kernel.utils.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * <p> pdf导出工具类</p>
 * @author youq  2019/12/26 9:51
 */
@Slf4j
public class ExportPDFUtils {

    /**
     * <p> 导出pdf数据</p>
     * @param response      HttpServletResponse
     * @param title         标题
     * @param headerMap     表头信息
     * @param tableTitles   标题
     * @param tableContents 内容
     * @param fileName      文件名
     * @author youq  2019/12/26 11:20
     */
    public static void export(HttpServletResponse response, String title, Map<String, String> headerMap,
                              List<String> tableTitles, List<List<String>> tableContents, String fileName) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        OutputStream outputStream = null;
        try {
            byteArrayOutputStream = generatePDF(title, headerMap, tableTitles, tableContents);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLength(byteArrayOutputStream.size());
            outputStream = response.getOutputStream();
            // write
            byteArrayOutputStream.writeTo(outputStream);
        } catch (Exception e) {
            log.error("PDF文件【{}】导出异常：", fileName, e);
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * <p> 生成导出PDF的outputStream</p>
     * @param title         标题
     * @param headerMap     表头信息
     * @param tableTitles   标题
     * @param tableContents 内容
     * @return java.io.ByteArrayOutputStream
     * @author youq  2019/12/26 10:02
     */
    private static ByteArrayOutputStream generatePDF(String title, Map<String, String> headerMap,
                                                     List<String> tableTitles, List<List<String>> tableContents) throws Exception {
        // itext中生成PDF文件，必须要有document对象，将所有的内容加到document中
        Document doc = new Document(PageSize.A4);
        // 创建一个byte型别数组的缓冲区，利用ByteArrayOutputStream的实例向数组中写入数据
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // 中文字体,解决中文不能显示问题
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        // 设置字体样式
        //正常
        Font textFont = new Font(bfChinese, 11, Font.NORMAL);
        //加粗
        Font boldFont = new Font(bfChinese, 11, Font.BOLD);
        //二级标题
        Font titleFont = new Font(bfChinese, 15, Font.BOLD);
        // 将写好的文件用输出流输出
        PdfWriter.getInstance(doc, stream);
        doc.open();

        //region 标题
        // 设置表格列宽
        PdfPTable head = new PdfPTable(1);
        //设置列宽
        head.setTotalWidth(new float[]{520});
        //锁定列宽
        head.setLockedWidth(true);
        //前间距
        head.setSpacingBefore(10f);
        //后间距
        head.setSpacingAfter(10f);
        //将文本标题加入到head中
        PdfPCell titleCell = buildCell(title, titleFont);
        titleCell.setBorderWidth(0);
        titleCell.setMinimumHeight(20);
        head.addCell(titleCell);
        doc.add(head);
        //endregion

        //region 表头
        if (headerMap != null) {
            //表头信息
            Paragraph p = new Paragraph();
            p.setAlignment(1);
            for (String key : headerMap.keySet()) {
                //一个段落中的内容
                Phrase ph = new Phrase();
                //文本内容中的最小单元
                Chunk c1 = new Chunk(key, boldFont);
                Chunk c2 = new Chunk(headerMap.get(key), textFont);
                ph.add(c1);
                ph.add(c2);
                ph.add("                 ");
                p.add(ph);
            }
            //行高
            p.setLeading(20);
            doc.add(p);
        }
        //endregion

        //region 表格
        int size = tableTitles.size();
        //根据tableList中第二个list的size，得到pdf中表格一共几列
        float[] cellWidth = new float[size];
        //给每列赋一个平均宽度
        for (int i = 0; i < cellWidth.length; i++) {
            cellWidth[i] = 500 / size;
        }
        //设置表格有几列
        PdfPTable table = new PdfPTable(size);
        //table前间距
        table.setSpacingBefore(20f);
        //设置列宽
        table.setTotalWidth(cellWidth);
        table.setLockedWidth(true);
        //将表格的列名添加到表格中
        for (String tableTitle : tableTitles) {
            table.addCell(buildCell(tableTitle, textFont));
        }
        //数据内容处理
        for (List<String> row : tableContents) {
            for (String c : row) {
                table.addCell(buildCell(c, textFont));
            }
        }
        doc.add(table);
        //endregion

        doc.close();
        return stream;
    }

    /**
     * <p> 生成PdfPCell</p>
     * @param data 数据
     * @param font 字体样式
     * @return com.itextpdf.text.pdf.PdfPCell
     * @author youq  2019/12/26 10:35
     */
    private static PdfPCell buildCell(String data, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(data, font));
        cell.setUseAscender(true);
        //设置水平居中
        cell.setHorizontalAlignment(cell.ALIGN_CENTER);
        //设置垂直居中
        cell.setVerticalAlignment(cell.ALIGN_MIDDLE);
        //设置单元格高度
        cell.setMinimumHeight(30);
        return cell;
    }

    /**
     * <p> 以时间点为PDF取名</p>
     * @return java.lang.String
     * @author youq  2019/12/26 10:37
     */
    public static String getPDFName() {
        return System.currentTimeMillis() + ".pdf";
    }

}
