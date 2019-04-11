package com.yq.mybatis.controller;

import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.model.ResultData;
import com.yq.mybatis.entity.User;
import com.yq.mybatis.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p> 用户controller</p>
 * @author youq  2019/4/11 14:03
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    public ResultData<?> getUser(Integer id) {
        User user = userService.findOne(id);
        return  ResultData.success(user);
    }

    @GetMapping("/delete")
    public ResultData<?> delete(Integer id) {
        userService.delete(id);
        return ResultData.success();
    }

    @GetMapping("/softDelete")
    public ResultData<?> softDelete(Integer id) {
        userService.softDelete(id);
        return ResultData.success();
    }

    @GetMapping("/update")
    public ResultData<?> update(Integer id, String email) {
        userService.update(id, email);
        return ResultData.success();
    }

    @GetMapping("/save")
    public ResultData<?> save(Integer id) {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setId(id);
        user.setCreateTime(now);
        user.setLastModified(now);
        user.setRemove(false);
        user.setUsername("youq" + id);
        user.setPassword("123456");
        user.setAge(28);
        user.setSex(SexEnum.MALE);
        user.setPhone("13000000001");
        user.setEmail("1234@asd.com");
        userService.save(user);
        return ResultData.success();
    }

}
