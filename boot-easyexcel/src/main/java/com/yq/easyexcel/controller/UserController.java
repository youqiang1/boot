package com.yq.easyexcel.controller;

import com.yq.easyexcel.config.ExcelUtils;
import com.yq.easyexcel.db.User;
import com.yq.easyexcel.service.UserService;
import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p> 用户信息</p>
 * @author youq  2020/3/5 14:03
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 读取 Excel（允许多个 sheet）
     */
    @PostMapping("/readUser")
    public ResultData<?> readUser(MultipartFile excel) {
        userService.readUserExcel(excel);
        return ResultData.success();
    }

    /**
     * 导出 Excel（一个 sheet）
     */
    @RequestMapping(value = "exportUser", method = RequestMethod.GET)
    public ResultData<?> exportUser(HttpServletResponse response) {
        List<User> users = userService.findByPhone("13000001111");
        String fileName = "D:\\temp\\easyexcel\\easyexcel01";
        String sheetName = "用户信息";
        ExcelUtils.writeUserExcel(response, users, fileName, sheetName);
        return ResultData.success();
    }

}
