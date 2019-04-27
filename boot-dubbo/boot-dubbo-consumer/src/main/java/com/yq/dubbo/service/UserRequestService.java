package com.yq.dubbo.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yq.dubbo.entity.User;
import org.springframework.stereotype.Component;

/**
 * <p> 用户service</p>
 * @author youq  2019/4/27 14:59
 */
@Component
public class UserRequestService {

    @Reference
    private UserService userRpcService;

    public User save() {
        User user = User.builder().username("youq").password("123456").build();
        return userRpcService.save(user);
    }

}
