package com.yq.kernel.utils.poi.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author youq
 * @Description: Excel数据模型
 * @create 2017/9/21 16:29
 */
@Data
@Builder
public class ExcelDataModel {
    /**
     * sheet的位置，0表示第一个表格中的第一个sheet
     */
    private int sheetNum;
    /**
     * sheet的名称
     */
    private String sheetTitle;
    /**
     * 标题之上的顶部数据
     */
    private String[] top;
    /**
     * 表格的标题
     */
    private String[] headers;
    /**
     * 表格的数据
     */
    private List<List<String>> data;
}
