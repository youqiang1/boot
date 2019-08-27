package com.yq.sms.client.schedule;

import com.yq.redisop.model.SendMessageModel;
import com.yq.redisop.service.SendMessageRedisService;
import com.yq.sms.client.entity.SendMessageInfo;
import com.yq.sms.client.service.SendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * <p> 短信保存任务</p>
 * @author youq  2019/8/27 11:36
 */
@Slf4j
@Component
public class SmsSaveTask {

    @Autowired
    private SendMessageRedisService sendMessageRedisService;

    @Autowired
    private SendMessageService sendMessageService;

    @Scheduled(fixedDelay = 1000)
    public void executor() {
        try {
            //找到所有待保存的数据
            List<SendMessageModel> list = sendMessageRedisService.findAllSaveData();
            if (!CollectionUtils.isEmpty(list)) {
                //转成mysql对象入库
                List<SendMessageInfo> sendMessageInfos = list.stream().map(
                        m -> {
                            SendMessageInfo info = new SendMessageInfo();
                            BeanUtils.copyProperties(m, info);
                            return info;
                        }
                ).collect(toList());
                sendMessageService.save(sendMessageInfos);
                log.info("短信入库{}条", sendMessageInfos.size());
            }
        } catch (Exception e) {
            log.error("入库定时任务异常：", e);
        }
    }

}
