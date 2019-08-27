package com.yq.sms.client.service;

import com.yq.sms.client.entity.SendMessageInfo;
import com.yq.sms.client.repository.SendMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> 短信service</p>
 * @author youq  2019/8/27 11:46
 */
@Service
public class SendMessageService {

    @Autowired
    private SendMessageRepository sendMessageRepository;

    public void save(List<SendMessageInfo> sendMessageInfos) {
        sendMessageRepository.save(sendMessageInfos);
    }

}
