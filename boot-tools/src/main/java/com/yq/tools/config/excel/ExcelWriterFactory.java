package com.yq.tools.config.excel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * <p> 用于导出多个 sheet 的 Excel，通过多次调用 write 方法写入多个 sheet</p>
 * @author youq  2019/5/14 11:28
 */
public class ExcelWriterFactory extends ExcelWriter {

    private OutputStream outputStream;

    private int sheetNo = 1;

    public ExcelWriterFactory(OutputStream outputStream, ExcelTypeEnum typeEnum) {
        super(outputStream, typeEnum);
        this.outputStream = outputStream;
    }

    public ExcelWriterFactory write(List<? extends BaseRowModel> list, String sheetName, BaseRowModel model) {
        this.sheetNo++;
        try {
            Sheet sheet = new Sheet(sheetNo, 0, model.getClass());
            sheet.setSheetName(sheetName);
            this.write(list, sheet);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                outputStream.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public void finish() {
        super.finish();
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
