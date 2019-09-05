package com.yq.redis.service;

import com.yq.redis.util.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p> servic</p>
 * @author youq  2019/9/5 11:02
 */
@Slf4j
@Service
public class TestService {

    @Autowired
    public RedisClient redis;

    public void hset(String key, String fieldname, String value) {
        redis.hset(key, fieldname, value);
    }

    public Object hget(String key, String fieldname) {
        return redis.hget(key, fieldname);
    }

    public void sset(String key, String value) {
        redis.sset(key, value);
    }

    public Set<Object> sget(String key) {
        long setSize = redis.sgetSize(key);
        log.info("set.size: {}", setSize);
        return redis.sget(key);
    }

    public void lpush(String key, String value) {
        redis.lpush(key, value);
    }

    public String rpop(String key) {
        long llen = redis.llen(key);
        log.info("list数据长度：{}", llen);
        return (String) redis.rpop(key);
    }

    public void del(String key) {
        redis.del(key);
    }

}
