package com.yq.mybatis.service;

import com.yq.mybatis.entity.User;
import com.yq.mybatis.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 用户Service</p>
 * @author youq  2019/4/11 14:03
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findOne(Integer id) {
        return userMapper.findOne(id);
    }

    public void save(User user) {
        userMapper.save(user);
    }

    public void delete(Integer id) {
        userMapper.deleteById(id);
    }

    public void softDelete(Integer id) {
        userMapper.softDelete(id);
    }

    public void update(Integer id, String email) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        userMapper.updateUserEmail(user);
    }

}
