package com.yq.redisop.dao;

import com.yq.kernel.constants.RedisKeyConstants;
import com.yq.redisop.model.AccountModel;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p> 账户 redis dao</p>
 * @author youq  2019/4/29 16:46
 */
@Slf4j
@Component
public class AccountRedisDao {

    @Autowired
    private RedissonClient client;

    /**
     * <p> 账户金额加减</p>
     * @param model 账户信息
     * @return java.lang.Long
     * @author youq  2019/4/29 18:04
     */
    public Long increment(AccountModel model) {
        RMap<String, Long> map =
                client.getMap(RedisKeyConstants.ACCOUNT_BALANCE, LongCodec.INSTANCE);
        Long aLong = map.putIfAbsent(model.getAccount(), 0L);
        log.info("along: {}", aLong);
        return map.addAndGet(model.getAccount(), model.getIncrement());
    }

}
