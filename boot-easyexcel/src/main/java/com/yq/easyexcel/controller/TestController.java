package com.yq.easyexcel.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.google.common.collect.Lists;
import com.yq.easyexcel.config.ExcelUtils;
import com.yq.easyexcel.model.ExportInfo;
import com.yq.easyexcel.model.ImportInfo;
import com.yq.easyexcel.model.MultiLineHeadExcelModel;
import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> 测试</p>
 * @author youq  2019/5/14 14:21
 */
@Slf4j
@RestController
public class TestController {

    /**
     * 读取 Excel（允许多个 sheet）
     */
    @PostMapping("/readExcelWithSheets")
    public ResultData<?> readExcelWithSheets(MultipartFile excel) {
        List<Object> objects = ExcelUtils.readExcel(excel, new ImportInfo());
        if (!CollectionUtils.isEmpty(objects)) {
            List<ImportInfo> importInfos = Lists.newArrayListWithCapacity(objects.size());
            for (Object object : objects) {
                importInfos.add((ImportInfo) object);
            }
            return ResultData.success(importInfos);
        }
        return ResultData.success();
    }

    /**
     * 读取 Excel（指定某个 sheet）
     */
    @RequestMapping(value = "readExcel", method = RequestMethod.POST)
    public ResultData<?> readExcel(MultipartFile excel, int sheetNo,
                            @RequestParam(defaultValue = "1") int headLineNum) {
        List<Object> objects = ExcelUtils.readExcel(excel, new ImportInfo(), sheetNo, headLineNum);
        return ResultData.success(objects);
    }

    /**
     * 导出 Excel（一个 sheet）
     */
    @RequestMapping(value = "writeExcel", method = RequestMethod.GET)
    public ResultData<?> writeExcel(HttpServletResponse response) {
        List<ExportInfo> list = getList();
        String fileName = "D:\\temp\\easyexcel\\easyexcel01";
        String sheetName = "第一个sheet";
        ExcelUtils.writeExcel(response, list, fileName, sheetName, new ExportInfo());
        return ResultData.success();
    }

    /**
     * 导出 Excel（多个 sheet）
     */
    @RequestMapping(value = "writeExcelWithSheets", method = RequestMethod.GET)
    public ResultData<?> writeExcelWithSheets(HttpServletResponse response) throws IOException {
        List<ExportInfo> list = getList();
        String fileName = "D:\\temp\\easyexcel\\easyexcel02";
        String sheetName1 = "第一个 sheet";
        String sheetName2 = "第二个 sheet";
        String sheetName3 = "第三个 sheet";

        ExcelUtils.writeExcelWithSheets(response, list.subList(0, 10), fileName, sheetName1, new ExportInfo())
                .write(list.subList(10, 20), sheetName2, new ExportInfo())
                .write(list.subList(20, 30), sheetName3, new ExportInfo())
                .finish();
        return ResultData.success();
    }

    private List<ExportInfo> getList() {
        List<ExportInfo> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(
                    ExportInfo.builder()
                            .name("youq" + i)
                            .age(20 + i)
                            .email("123@qq.com")
                            .address("江西德兴")
                            .build()
            );
        }
        return list;
    }

    /**
     * 导出excel，多行表头
     */
    @RequestMapping(value = "writeExcelMultiLineHead", method = RequestMethod.GET)
    public ResultData<?> writeExcelMultiLineHead(HttpServletResponse response) throws IOException {
        List<MultiLineHeadExcelModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(
                    MultiLineHeadExcelModel.builder()
                            .p1("p1" + i)
                            .p2("p2" + i)
                            .p3(3 + i)
                            .p4(4 + i)
                            .p5("p5" + i)
                            .p6("p6" + i)
                            .p7("p7" + i)
                            .p8("p8" + i)
                            .p9("p9" + i)
                            .build()
            );
        }
        String fileName = "D:\\temp\\easyexcel\\easyexcel03";
        String sheetName = "第一个 sheet";

        ExcelUtils.writeExcel(response, list, fileName, sheetName, new MultiLineHeadExcelModel());
        return ResultData.success();
    }

    /**
     * 导出excel，一个sheet多个表
     */
    @GetMapping("/writeWithoutHead")
    public ResultData<?> writeWithoutHead() throws Exception {
        OutputStream out = new FileOutputStream("d:/temp/easyexcel/easyexcel04.xlsx");
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
        Sheet sheet1 = new Sheet(1, 0);
        sheet1.setSheetName("sheet1");
        List<List<String>> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            List<String> item = new ArrayList<>();
            item.add("item0" + i);
            item.add("item1" + i);
            item.add("item2" + i);
            data.add(item);
        }
        List<List<String>> head = new ArrayList<>();
        List<String> headColumn1 = new ArrayList<>();
        List<String> headColumn2 = new ArrayList<>();
        List<String> headColumn3 = new ArrayList<>();
        headColumn1.add("第一列");
        headColumn2.add("第二列");
        headColumn3.add("第三列");
        head.add(headColumn1);
        head.add(headColumn2);
        head.add(headColumn3);
        Table table = new Table(1);
        table.setHead(head);
        writer.write0(data, sheet1, table);
        writer.finish();
        return ResultData.success();
    }

    @GetMapping("/writeWithMultiTable")
    public ResultData<?> writeWithMultiTable() throws Exception {
        OutputStream out = new FileOutputStream("d:/temp/easyexcel/easyexcel05.xlsx");
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
        Sheet sheet1 = new Sheet(1, 0);
        sheet1.setSheetName("sheet1");

        // 数据全是List<String> 无模型映射关系
        Table table1 = new Table(1);
        List<List<String>> data1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<String> item = new ArrayList<>();
            item.add("item1" + i);
            item.add("item2" + i);
            item.add("item3" + i);
            data1.add(item);
        }
        writer.write0(data1, sheet1, table1);

        // 模型上有表头的注解
        Table table2 = new Table(2);
        table2.setClazz(MultiLineHeadExcelModel.class);
        List<MultiLineHeadExcelModel> data2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data2.add(
                    MultiLineHeadExcelModel.builder()
                            .p1("p1" + i)
                            .p2("p2" + i)
                            .p3(3 + i)
                            .p4(4 + i)
                            .p5("p5" + i)
                            .p6("p6" + i)
                            .p7("p7" + i)
                            .p8("p8" + i)
                            .p9("p9" + i)
                            .build()
            );
        }
        writer.write(data2, sheet1, table2);

        // 模型上没有注解，表头数据动态传入,此情况下模型field顺序与excel现实顺序一致
        List<List<String>> head = new ArrayList<>();
        List<String> headColumn1 = new ArrayList<>();
        List<String> headColumn2 = new ArrayList<>();
        List<String> headColumn3 = new ArrayList<>();
        headColumn1.add("第一列");
        headColumn2.add("第二列");
        headColumn3.add("第三列");
        head.add(headColumn1);
        head.add(headColumn2);
        head.add(headColumn3);
        Table table = new Table(1);
        table.setHead(head);
        writer.write0(data1, sheet1, table);

        writer.finish();
        return ResultData.success();
    }

}
