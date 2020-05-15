package com.yq.activiti.service;

import com.yq.activiti.common.enu.ReviewLevelEnum;
import com.yq.activiti.db.User;
import com.yq.activiti.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> 用户信息服务</p>
 * @author youq  2020/5/15 13:16
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * <p> 根据审核级别查询用户列表</p>
     * @param reviewLevel 审核级别
     * @return java.util.List<com.yq.activiti.db.User>
     * @author youq  2020/5/15 13:19
     */
    public List<User> findByReviewLevel(ReviewLevelEnum reviewLevel) {
        return userRepository.findByReviewLevel(reviewLevel);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
