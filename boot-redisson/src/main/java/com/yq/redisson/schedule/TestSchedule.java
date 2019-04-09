package com.yq.redisson.schedule;

import com.yq.redisop.model.UserModel;
import com.yq.redisop.service.UserRedisService;
import com.yq.redisson.db.User;
import com.yq.redisson.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> 测试</p>
 * @author youq  2019/4/9 15:29
 */
@Slf4j
@Component
public class TestSchedule {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRedisService userRedisService;

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void executor() {
        List<User> users = userService.findAll();
        log.info("users: {}", users);
        if (CollectionUtils.isNotEmpty(users)) {
            //转成userModel
            List<UserModel> models = users.stream()
                    .filter(u -> u.getId() != null)
                    .map(
                            u -> {
                                UserModel model = new UserModel();
                                BeanUtils.copyProperties(u, model);
                                return model;
                            }
                    ).collect(Collectors.toList());
            //入redis
            if (CollectionUtils.isNotEmpty(models)) {
                log.info("{}条用户信息入redis", models.size());
                userRedisService.saveBatch(models);
            }
        }
    }

}
