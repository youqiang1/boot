package com.yq.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p> redis操作工具类</p>
 * @author youq  2019/9/4 17:09
 */
@Slf4j
@Component
public class RedisClient {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * <p> 指定失效时间</p>
     * @param key        键
     * @param expireTime 过期时间，秒
     * @return boolean
     * @author youq  2019/9/4 17:10
     */
    public boolean expire(String key, long expireTime) {
        try {
            if (expireTime > 0) {
                redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.info("redis expire exception: ", e);
            return false;
        }
    }

    /**
     * <p> 获取过期时间</p>
     * @param key redis key
     * @return long 过期时间，秒
     * @author youq  2019/9/4 17:12
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * <p> 判断key是否存在</p>
     * @param key redis key
     * @return boolean true 存在
     * @author youq  2019/9/4 17:13
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.info("redis hasKey exception: ", e);
            return false;
        }
    }

    /**
     * <p> 删除redis数据</p>
     * @param key redis key
     * @author youq  2019/9/4 17:16
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * <p> 查询redis数据</p>
     * @param key redis key
     * @return java.lang.Object
     * @author youq  2019/9/4 17:17
     */
    public Object get(String key) {
        return StringUtils.isEmpty(key) ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * <p> redis写数据</p>
     * @param key   键
     * @param value 值
     * @return boolean
     * @author youq  2019/9/4 17:19
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.info("redis set exception: ", e);
            return false;
        }
    }

    /**
     * <p> redis写数据</p>
     * @param key        键
     * @param value      值
     * @param expireTime 超时时间，秒
     * @return boolean
     * @author youq  2019/9/4 17:19
     */
    public boolean set(String key, Object value, long expireTime) {
        try {
            if (expireTime > 0) {
                redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.info("redis expire set exception: ", e);
            return false;
        }
    }

    /**
     * <p> 加</p>
     * @param key    键
     * @param addend 加数
     * @return long
     * @author youq  2019/9/4 17:26
     */
    public long incr(String key, long addend) {
        if (addend <= 0) {
            throw new RuntimeException("加数必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, addend);
    }

    /**
     * <p> 减</p>
     * @param key        键
     * @param subtrahend 被减数
     * @return long
     * @author youq  2019/9/4 17:26
     */
    public long decr(String key, long subtrahend) {
        if (subtrahend <= 0) {
            throw new RuntimeException("被减数必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -subtrahend);
    }

    //region hash操作

    /**
     * <p> hash操作获取数据</p>
     * @param key       redis key
     * @param fieldname redis fieldname name
     * @return java.lang.Object
     * @author youq  2019/9/4 17:38
     */
    public Object hget(String key, String fieldname) {
        return redisTemplate.opsForHash().get(key, fieldname);
    }

    /**
     * <p> 获取redis hash 所有键值</p>
     * @param key redis key
     * @author youq  2019/9/4 17:44
     */
    public Map<Object, Object> hgetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * <p> redis 插入一条hash数据</p>
     * @param key       key
     * @param fieldname hash key
     * @param value     hash value
     * @return boolean
     * @author youq  2019/9/4 17:48
     */
    public boolean hset(String key, String fieldname, Object value) {
        try {
            redisTemplate.opsForHash().put(key, fieldname, value);
            return true;
        } catch (Exception e) {
            log.error("redis hset exception: ", e);
            return false;
        }
    }

    /**
     * <p> redis 插入一条hash数据，并设置过期时间</p>
     * @param key        key
     * @param fieldname  hash key
     * @param value      hash value
     * @param expireTime 过期时间，秒
     * @return boolean
     * @author youq  2019/9/4 17:48
     */
    public boolean hset(String key, String fieldname, Object value, long expireTime) {
        try {
            redisTemplate.opsForHash().put(key, fieldname, value);
            if (expireTime > 0) {
                expire(key, expireTime);
            }
            return true;
        } catch (Exception e) {
            log.error("redis expire hset exception: ", e);
            return false;
        }
    }

    /**
     * <p> redis 插入hash数据</p>
     * @param key  key
     * @param hash hash
     * @return boolean
     * @author youq  2019/9/4 17:48
     */
    public boolean hsetAll(String key, Map<String, Object> hash) {
        try {
            redisTemplate.opsForHash().putAll(key, hash);
            return true;
        } catch (Exception e) {
            log.error("redis hset all exception: ", e);
            return false;
        }
    }

    /**
     * <p> redis 插入hash数据，并设置过期时间</p>
     * @param key        key
     * @param hash       hash
     * @param expireTime 过期时间，秒
     * @return boolean
     * @author youq  2019/9/4 17:48
     */
    public boolean hsetAll(String key, Map<String, Object> hash, long expireTime) {
        try {
            redisTemplate.opsForHash().putAll(key, hash);
            if (expireTime > 0) {
                expire(key, expireTime);
            }
            return true;
        } catch (Exception e) {
            log.error("redis expire hset all exception: ", e);
            return false;
        }
    }

    /**
     * <p> 删除hash数据</p>
     * @param key       redis key
     * @param fieldname hash key
     * @author youq  2019/9/4 17:58
     */
    public void hdel(String key, Object... fieldname) {
        redisTemplate.opsForHash().delete(key, fieldname);
    }

    /**
     * <p> 查看hash是否存在该fieldname数据</p>
     * @param key       redis key
     * @param fieldname hash key
     * @return boolean
     * @author youq  2019/9/4 17:59
     */
    public boolean hHasKey(String key, String fieldname) {
        return redisTemplate.opsForHash().hasKey(key, fieldname);
    }
    //endregion


    //region set操作

    /**
     * <p> 获取set数据集</p>
     * @param key redis key
     * @return java.util.Set<java.lang.Object>
     * @author youq  2019/9/5 9:54
     */
    public Set<Object> sget(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * <p> 根据value从一个set中查询是否存在</p>
     * @param key   redis key
     * @param value set中的数据
     * @return boolean
     * @author youq  2019/9/5 9:55
     */
    public boolean sHasKey(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * <p> 将数据放入redis set集合中</p>
     * @param key    redis key
     * @param values 数据集合
     * @return long
     * @author youq  2019/9/5 9:56
     */
    public long sset(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("redis sset exception: ", e);
            return 0;
        }
    }

    /**
     * <p> 将数据放入redis set集合中，可设置过期时间</p>
     * @param key        redis key
     * @param expireTime 过期时间，秒
     * @param values     数据集合
     * @return long
     * @author youq  2019/9/5 9:56
     */
    public long sset(String key, long expireTime, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (expireTime > 0) {
                expire(key, expireTime);
            }
            return count;
        } catch (Exception e) {
            log.error("redis sset exception: ", e);
            return 0;
        }
    }

    /**
     * <p> 获取redis中set集合的长度</p>
     * @param key redis key
     * @return long
     * @author youq  2019/9/5 10:01
     */
    public long sgetSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * <p> 删除redis中set集合中的数据</p>
     * @param key    redis key
     * @param values 数据集
     * @return long
     * @author youq  2019/9/5 10:02
     */
    public long sdel(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }
    //endregion

    //region list操作

    /**
     * <p> 获取list集合中的数据</p>
     * @param key   redis key
     * @param start 开始条数
     * @param end   结束条数
     * @return java.util.List<java.lang.Object>
     * @author youq  2019/9/5 10:08
     */
    public List<Object> lget(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * <p> 获取list集合的长度</p>
     * @param key redis key
     * @return long
     * @author youq  2019/9/5 10:09
     */
    public long llen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * <p> 获取list集合中的数据</p>
     * @param key   redis key
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return java.lang.Object
     * @author youq  2019/9/5 10:11
     */
    public Object lgetIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * <p> 从队列左边放入数据</p>
     * @param key   redis key
     * @param value 数据
     * @return boolean
     * @author youq  2019/9/5 10:15
     */
    public boolean lpush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            log.info("redis lpush exception: ");
            return false;
        }
    }

    /**
     * <p> 从队列左边批量放入数据</p>
     * @param key   redis key
     * @param values 数据集合
     * @return boolean
     * @author youq  2019/9/5 10:15
     */
    public boolean lpushAll(String key, List<Object> values) {
        try {
            redisTemplate.opsForList().leftPushAll(key, values);
            return true;
        } catch (Exception e) {
            log.info("redis lpushAll exception: ");
            return false;
        }
    }

    /**
     * <p> 从队列右边放入数据</p>
     * @param key   redis key
     * @param value 数据
     * @return boolean
     * @author youq  2019/9/5 10:15
     */
    public boolean rpush(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.info("redis rpush exception: ");
            return false;
        }
    }

    /**
     * <p>
     * 从队列左边获取并删除数据，
     * 当堆使用时与rpush配合，先进先出
     * 当栈使用时与lpush配合，先进后出
     * </p>
     * @param key redis key
     * @return java.lang.Object
     * @author youq  2019/9/5 10:17
     */
    public Object lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * <p>
     * 从队列右边获取并删除数据，
     * 当堆使用时与lpush配合，先进先出
     * 当栈使用时与rpush配合，先进后出
     * </p>
     * @param key redis key
     * @return java.lang.Object
     * @author youq  2019/9/5 10:17
     */
    public Object rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    //endregion

}
