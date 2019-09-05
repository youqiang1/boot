package com.yq.redis.controller;

import com.yq.kernel.constants.GlobalConstants;
import com.yq.redis.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * <p> test</p>
 * @author youq  2019/9/5 11:01
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    private static String REDIS_HKEY = "REDIS_HKEY";

    private static String REDIS_SKEY = "REDIS_SKEY";

    private static String REDIS_LKEY = "REDIS_LKEY";

    @Autowired
    private TestService testService;

    @RequestMapping("/hset")
    public String hset(String key, String value) {
        testService.hset(REDIS_HKEY, key, value);
        return GlobalConstants.SUCCESS;
    }

    @RequestMapping("/hget")
    public String hget(String key) {
        String value = (String) testService.hget(REDIS_HKEY, key);
        return GlobalConstants.SUCCESS + " -> " + value;
    }

    @RequestMapping("/sset")
    public String sset(String value) {
        testService.sset(REDIS_SKEY, value);
        return GlobalConstants.SUCCESS;
    }

    @RequestMapping("/sget")
    public String sget() {
        Set<Object> values = testService.sget(REDIS_SKEY);
        Set<String> result = new HashSet<>();
        for (Object o : values) {
            result.add((String) o);
        }
        return GlobalConstants.SUCCESS + " -> " + result;
    }

    @RequestMapping("/lpush")
    public String lpush(String value) {
        testService.lpush(REDIS_LKEY, value);
        return GlobalConstants.SUCCESS;
    }

    @RequestMapping("/rpop")
    public String rpop() {
        String res = testService.rpop(REDIS_LKEY);
        return GlobalConstants.SUCCESS + "  -> " + res;
    }

    @RequestMapping("/del")
    public String del(String key) {
        testService.del(key);
        return GlobalConstants.SUCCESS;
    }

}
