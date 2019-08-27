package com.yq.sms.commons.service;

import com.yq.kernel.constants.GlobalConstants;
import com.yq.kernel.utils.ObjectUtils;
import com.yq.redisop.model.SendMessageModel;
import com.yq.redisop.service.SendMessageRedisService;
import com.yq.sms.commons.sms.cmpp20.Cmpp20Deliver;
import com.yq.sms.commons.spring.BeanFactoryUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * <p> 上行、状态报告服务</p>
 * @author youq  2019/8/23 18:06
 */
@Slf4j
public class MoReportService {

    private SendMessageRedisService sendMessageRedisService = BeanFactoryUtils.getBean(SendMessageRedisService.class);

    public void processReport(Object processObject) {
        if (processObject instanceof Cmpp20Deliver) {
            Cmpp20Deliver deliver = (Cmpp20Deliver) processObject;
            if (deliver.getRegisteredDelivery() == 0) {
                log.info("上行【{}】，待处理", deliver.toString());
            } else {
                String operatorMessageId = deliver.getReport().getSeq1() + "," + deliver.getReport().getSeq2() + "," + deliver.getReport().getSeq3();
                String mobile = deliver.getSrcTerminalId();
                String status = deliver.getReport().getStat();
                SendMessageModel sendMessageModel = sendMessageRedisService.findOne(operatorMessageId + mobile);
                if (sendMessageModel == null) {
                    //数据需要二次处理，逻辑待梳理
                    log.info("找不到已发的短信，key: {}", operatorMessageId + mobile);
                    return;
                }
                sendMessageModel.setFinishTime(LocalDateTime.now());
                sendMessageModel.setSaveFlag(true);
                sendMessageModel.setState((status.equals("0") || "DELIVRD".equals(status)) ? GlobalConstants.SUCCESS : GlobalConstants.FAIL);
                sendMessageModel.setExplain((status.equals("0") || "DELIVRD".equals(status)) ? "状态成功" : "状态失败");
                sendMessageRedisService.save(sendMessageModel);
            }
        } else {
            log.info("无法识别的DELIVER信息：{}", ObjectUtils.toJson(processObject));
        }
    }

}
