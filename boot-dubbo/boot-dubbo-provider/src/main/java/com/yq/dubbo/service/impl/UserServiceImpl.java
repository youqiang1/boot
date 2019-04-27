package com.yq.dubbo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.yq.dubbo.entity.User;
import com.yq.dubbo.service.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> 服务实现</p>
 * @author youq  2019/4/27 14:47
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Override
    public User save(User user) {
        user.setId(1);
        log.info("user: {}", user);
        return user;
    }

}
