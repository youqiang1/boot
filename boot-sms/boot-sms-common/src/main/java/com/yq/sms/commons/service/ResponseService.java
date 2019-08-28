package com.yq.sms.commons.service;

import com.yq.kernel.constants.GlobalConstants;
import com.yq.kernel.utils.DateUtils;
import com.yq.kernel.utils.ObjectUtils;
import com.yq.redisop.model.SendMessageContextModel;
import com.yq.redisop.model.SendMessageModel;
import com.yq.redisop.service.SendMessageContextRedisService;
import com.yq.redisop.service.SendMessageRedisService;
import com.yq.sms.commons.sms.cmpp20.Cmpp20FwdResp;
import com.yq.sms.commons.sms.cmpp20.Cmpp20SubmitResp;
import com.yq.sms.commons.spring.BeanFactoryUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * <p> 响应服务</p>
 * @author youq  2019/8/26 14:29
 */
@Slf4j
public class ResponseService {

    private SendMessageContextRedisService sendMessageContextRedisService = BeanFactoryUtils.getBean(SendMessageContextRedisService.class);

    private SendMessageRedisService sendMessageRedisService = BeanFactoryUtils.getBean(SendMessageRedisService.class);

    public void processResponse(Object processObject) {
        if (processObject instanceof Cmpp20SubmitResp) {
            Cmpp20SubmitResp submitResp = (Cmpp20SubmitResp) processObject;
            SendMessageContextModel model = sendMessageContextRedisService.getAndDel(submitResp.getHeadMessage().getSequenceId() + "");
            if (model != null) {
                String operatorMessageId = submitResp.getSeq1() + "," + submitResp.getSeq2() + "," + submitResp.getSeq3();
                HashMap<String, String> sendPackage = model.getSendObject();
                String messageId = sendPackage.get("smsId");
                String sendTimeLong = sendPackage.get("sendTime");
                LocalDateTime sendTime = DateUtils.millisecond2LocalDateTime(Long.parseLong(sendTimeLong));
                String mobile = sendPackage.get("mobile");
                SendMessageModel sendMessageModel = sendMessageRedisService.getAndDel(messageId + mobile);
                //已发数据处理
                if (sendMessageModel != null) {
                    sendMessageModel.setSendTime(sendTime);
                    sendMessageModel.setKey(operatorMessageId + mobile);
                    sendMessageModel.setOperatorMessageId(operatorMessageId);
                    sendMessageModel.setState(submitResp.getResult() == 0 ? GlobalConstants.SUCCESS : GlobalConstants.FAIL);
                    sendMessageModel.setExplain(submitResp.getResult() == 0 ? "" : "响应失败");
                    sendMessageModel.setSaveFlag(submitResp.getResult() != 0);
                    sendMessageModel.setBillingCount(model.getBillingCount());
                    sendMessageRedisService.save(sendMessageModel);
                } else {
                    log.info("没找到已发数据");
                }
            } else {
                log.info("响应已比对完成，不再比对");
            }
        } else if (processObject instanceof Cmpp20FwdResp) {
            Cmpp20FwdResp fwdResp = (Cmpp20FwdResp) processObject;
            SendMessageContextModel model = sendMessageContextRedisService.getAndDel(fwdResp.getHeadMessage().getSequenceId() + "");
            if (model != null) {
                String operatorMessageId = new String(fwdResp.getMsgId());
                HashMap<String, String> sendPackage = model.getSendObject();
                String messageId = sendPackage.get("smsId");
                String sendTimeLong = sendPackage.get("sendTime");
                LocalDateTime sendTime = DateUtils.millisecond2LocalDateTime(Long.parseLong(sendTimeLong));
                String mobile = sendPackage.get("mobile");
                SendMessageModel sendMessageModel = sendMessageRedisService.getAndDel(messageId + mobile);
                //已发数据处理
                if (sendMessageModel != null) {
                    String newKey = operatorMessageId + mobile;
                    sendMessageModel.setSendTime(sendTime);
                    sendMessageModel.setKey(newKey);
                    sendMessageModel.setOperatorMessageId(operatorMessageId);
                    sendMessageModel.setState(fwdResp.getResult() == 0 ? GlobalConstants.SUCCESS : GlobalConstants.FAIL);
                    sendMessageModel.setExplain(fwdResp.getResult() == 0 ? "" : "响应失败");
                    sendMessageModel.setSaveFlag(fwdResp.getResult() != 0);
                    sendMessageModel.setBillingCount(model.getBillingCount());
                    sendMessageRedisService.save(sendMessageModel);
                } else {
                    log.info("数据已处理");
                }
            } else {
                log.info("响应比对完成，不再比对");
            }
        } else {
            log.info("无法识别的响应信息：{}", ObjectUtils.toJson(processObject));
        }
    }

}
