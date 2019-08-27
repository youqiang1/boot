package com.yq.redisop.dao;

import com.google.common.collect.Lists;
import com.yq.kernel.constants.RedisKeyConstants;
import com.yq.redisop.model.SendMessageModel;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 发送redis dao</p>
 * @author youq  2019/8/26 17:17
 */
@Component
public class SendMessageRedisDao {

    @Autowired
    private RedissonClient client;

    public void save(SendMessageModel model) {
        RMap<String, SendMessageModel> map = client.getMap(RedisKeyConstants.SEND_MESSAGE);
        map.put(model.getKey(), model);
    }

    public SendMessageModel findOne(String key) {
        RMap<String, SendMessageModel> map = client.getMap(RedisKeyConstants.SEND_MESSAGE);
        return map.get(key);
    }

    public SendMessageModel getAndDel(String key) {
        RMap<String, SendMessageModel> map = client.getMap(RedisKeyConstants.SEND_MESSAGE);
        SendMessageModel model = map.get(key);
        if (model == null) {
            return null;
        }
        map.remove(key);
        return model;
    }

    /**
     * <p> 找出所有准备保存的数据</p>
     * @return java.util.List<com.yq.redisop.model.SendMessageModel>
     * @author youq  2019/8/27 11:51
     */
    public List<SendMessageModel> findAllSaveData() {
        RMap<String, SendMessageModel> map = client.getMap(RedisKeyConstants.SEND_MESSAGE);
        if (!CollectionUtils.isEmpty(map.readAllValues())) {
            List<SendMessageModel> saveList = new ArrayList<>();
            for (SendMessageModel model : Lists.newArrayList(map.readAllValues())) {
                if (model.getSaveFlag()) {
                    saveList.add(model);
                    map.remove(model.getKey());
                }
            }
            return saveList;
        }
        return null;
    }

}
