package com.yq.redisop.dao;

import com.yq.kernel.constants.RedisKeyConstants;
import com.yq.redisop.model.SendMessageContextModel;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p> 发送上下文redis dao</p>
 * @author youq  2019/8/26 16:13
 */
@Component
public class SendMessageContextRedisDao {

    @Autowired
    private RedissonClient client;

    public void save(SendMessageContextModel model) {
        RMap<String, SendMessageContextModel> map = client.getMap(RedisKeyConstants.SEND_MESSAGE_CONTEXT);
        map.put(model.getSendKey(), model);
    }

    public SendMessageContextModel getAndDel(String sequenceId) {
        RMap<String, SendMessageContextModel> map = client.getMap(RedisKeyConstants.SEND_MESSAGE_CONTEXT);
        SendMessageContextModel model = map.get(sequenceId);
        if (model == null) {
            return null;
        }
        map.remove(sequenceId);
        return model;
    }

}
