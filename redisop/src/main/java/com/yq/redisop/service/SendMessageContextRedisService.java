package com.yq.redisop.service;

import com.yq.redisop.dao.SendMessageContextRedisDao;
import com.yq.redisop.model.SendMessageContextModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 发送redis service</p>
 * @author youq  2019/8/26 16:11
 */
@Service
public class SendMessageContextRedisService {

    @Autowired
    private SendMessageContextRedisDao sendMessageContextRedisDao;

    public void save(SendMessageContextModel model) {
        sendMessageContextRedisDao.save(model);
    }

    public SendMessageContextModel getAndDel(String sequenceId) {
        return sendMessageContextRedisDao.getAndDel(sequenceId);
    }

}
