package com.yq.web.controller;

import com.google.common.collect.Lists;
import com.yq.kernel.model.ResultData;
import com.yq.kernel.utils.pdf.ExportPDFUtils;
import com.yq.starter.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p> 测试</p>
 * @author youq  2019/4/30 13:49
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/contextLoads")
    public ResultData<?> contextLoads() {
        return ResultData.success(testService.sayHello());
    }

    @RequestMapping("/exportPDFTest")
    public void exportPDFTest(HttpServletResponse response) {
        Map<String, String> headerMap = new LinkedHashMap<>();
        // 表格前的属性
        headerMap.put("开始时间：", "未选");
        headerMap.put("结束时间：", "未选");
        headerMap.put("说明：", "测试PDF导出");
        // 表格的各个字段名
        List<String> tabTitles = new LinkedList<>();
        tabTitles.add("列1");
        tabTitles.add("列2");
        tabTitles.add("列3");
        tabTitles.add("列N");
        // 表格数据
        List<List<String>> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> list = Lists.newArrayListWithCapacity(4);
            list.add(i + "");
            list.add(i * 11 + "");
            list.add(i * 111 + "");
            list.add(i * 1111 + "");
            data.add(list);
        }
        //这里可以加个list格式的检查

        //导出
        ExportPDFUtils.export(response, "测试PDF导出", headerMap, tabTitles, data, ExportPDFUtils.getPDFName());
    }

}
