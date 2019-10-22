package com.yq.easyexcel.controller;

import com.yq.easyexcel.util.ExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p> 测试</p>
 * @author youq  2019/10/22 10:47
 */
@Slf4j
@RestController
@RequestMapping("test2")
public class Test2Controller {

    @GetMapping("csv")
    public void csv(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileName = this.getFileName(request, "测试数据.csv");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");

        LinkedHashMap<String, Object> header = new LinkedHashMap<>();
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        header.put("1", "姓名");
        header.put("2", "年龄");
        List<LinkedHashMap<String, Object>> data = new ArrayList<>();
        body.put("1", "小明");
        body.put("2", "小王");
        data.add(header);
        data.add(body);
        data.add(body);
        data.add(body);
        FileCopyUtils.copy(ExportUtil.exportCSV(data), response.getOutputStream());
    }

    private String getFileName(HttpServletRequest request, String name) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("USER-AGENT");
        return userAgent.contains("Mozilla") ? new String(name.getBytes(), "ISO8859-1") : name;
    }

}
