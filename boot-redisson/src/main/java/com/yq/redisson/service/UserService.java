package com.yq.redisson.service;

import com.yq.redisson.db.User;
import com.yq.redisson.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> 用户 service</p>
 * @author youq  2019/4/9 15:30
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * <p> 查询所有用户信息</p>
     * @return java.util.List<com.yq.redisson.db.User>
     * @author youq  2019/4/9 15:31
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
