package com.yq.limit.controller;

import com.yq.kernel.model.ResultData;
import com.yq.kernel.utils.DateUtils;
import com.yq.limit.anno.RateLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p> 测试</p>
 * @author youq  2019/4/28 15:38
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * <p> 同一用户10秒内只能请求10次</p>
     * lua脚本有问题，现在lua脚本的情况是：数值在redis中存储过期时间为2秒，超过两秒计数就重新开始了
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/4/28 16:28
     */
    @RateLimit(key = "limiter", time = 10, count = 10)
    @GetMapping("/limiter")
    public ResultData<?> limiter() {
        RedisAtomicInteger entityIdCounter =
                new RedisAtomicInteger("entityIdCounter", redisTemplate.getConnectionFactory());
        String date = DateUtils.localDateTimeDefaultFormat(LocalDateTime.now());
        return ResultData.success("时间【" + date + "】累计访问次数：" + entityIdCounter.getAndIncrement());
    }

}
