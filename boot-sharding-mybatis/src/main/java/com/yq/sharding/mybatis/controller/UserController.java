package com.yq.sharding.mybatis.controller;

import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.model.ResultData;
import com.yq.sharding.mybatis.entity.User;
import com.yq.sharding.mybatis.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p> 用户信息Controller</p>
 * @author youq  2019/4/29 10:06
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/save")
    public ResultData<?> save() {
        long st = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            Date date = new Date();
            User user = new User();
            user.setCreateTime(date);
            user.setLastModified(date);
            user.setName("youq" + i);
            user.setCityId(i % 2 == 0 ? 1 : 2);
            user.setSex(i % 2 == 0 ? SexEnum.MALE.ordinal() : SexEnum.FEMALE.ordinal());
            user.setPhone("11111" + i);
            user.setEmail("136@163.com");
            user.setPassword("123456");
            userMapper.save(user);
        }
        log.info("耗时：{}", System.currentTimeMillis() - st);
        return ResultData.success();
    }

    @GetMapping("/findAllOrder")
    public ResultData<?> findAllOrder() {
        long st = System.currentTimeMillis();
        List<User> users = userMapper.findAllOrder();
        log.info("耗时：{}", System.currentTimeMillis() - st);
        return ResultData.success(users);
    }

    @RequestMapping("/get/{id}")
    public ResultData get(@PathVariable Long id) {
        User user = userMapper.findOne(id);
        return ResultData.success(user);
    }

}
