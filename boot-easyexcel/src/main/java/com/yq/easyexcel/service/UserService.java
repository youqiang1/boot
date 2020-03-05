package com.yq.easyexcel.service;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.yq.easyexcel.config.ExcelListener;
import com.yq.easyexcel.db.User;
import com.yq.easyexcel.model.user.UserExcelModel;
import com.yq.easyexcel.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

/**
 * <p> 用户信息</p>
 * @author youq  2020/3/5 14:03
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void readUserExcel(MultipartFile excel) {
        try {
            ExcelListener listener = new ExcelListener("user-defined", this);
            ExcelReader reader = new ExcelReader(new BufferedInputStream(excel.getInputStream()), null, listener);
            reader.read(new Sheet(1, 0, UserExcelModel.class));
        } catch (IOException e) {
            log.error("解析自定义用户信息导入模板异常：", e);
        }
    }

    public void save(List<User> users) {
        userRepository.save(users);
    }

    public List<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

}
