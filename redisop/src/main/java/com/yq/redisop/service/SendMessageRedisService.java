package com.yq.redisop.service;

import com.yq.redisop.dao.SendMessageRedisDao;
import com.yq.redisop.model.SendMessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> 发送 redis  service</p>
 * @author youq  2019/8/26 17:13
 */
@Service
public class SendMessageRedisService {

    @Autowired
    private SendMessageRedisDao sendMessageRedisDao;

    public void save(SendMessageModel model) {
        sendMessageRedisDao.save(model);
    }

    public SendMessageModel findOne(String key) {
        return sendMessageRedisDao.findOne(key);
    }

    public SendMessageModel getAndDel(String key) {
        return sendMessageRedisDao.getAndDel(key);
    }

    /**
     * <p> 找出所有准备保存的数据</p>
     * @return java.util.List<com.yq.redisop.model.SendMessageModel>
     * @author youq  2019/8/27 11:51
     */
    public List<SendMessageModel> findAllSaveData() {
        return sendMessageRedisDao.findAllSaveData();
    }

}
