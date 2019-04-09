package com.yq.redisson.schedule;

import com.yq.redisop.model.UserModel;
import com.yq.redisop.service.UserRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p> 查询测试</p>
 * @author youq  2019/4/9 16:04
 */
@Slf4j
@Component
public class QuerySchedule {

    @Autowired
    private UserRedisService userRedisService;

    @Scheduled(fixedDelay = 60 * 1000)
    public void executor() {
        List<UserModel> models = userRedisService.findAll();
        log.info("redis中的所有用户信息：{}", models);
    }

}
