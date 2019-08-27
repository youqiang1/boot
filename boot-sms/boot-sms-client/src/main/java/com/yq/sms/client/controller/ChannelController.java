package com.yq.sms.client.controller;

import com.alibaba.druid.util.StringUtils;
import com.yq.kernel.model.ResultData;
import com.yq.sms.client.controller.request.ChannelEditRequest;
import com.yq.sms.commons.model.SendMsgRequest;
import com.yq.sms.client.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 通道信息controller</p>
 * @author youq  2019/8/23 17:25
 */
@Slf4j
@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @RequestMapping("/start")
    public ResultData<?> start(String channelId) {
        log.info("channel start, channelId【{}】", channelId);
        if (StringUtils.isEmpty(channelId)) {
            return ResultData.failMsg("channelId异常");
        }
        boolean startFlag = channelService.start(channelId);
        if (!startFlag) {
            return ResultData.fail();
        }
        return ResultData.success();
    }

    @PostMapping("/add")
    public ResultData<?> add(@RequestBody ChannelEditRequest request) {
        boolean flag = channelService.add(request);
        if (!flag) {
            return ResultData.fail();
        }
        return ResultData.success();
    }

    @PostMapping("/sendMsg")
    public ResultData<?> sendMsg(@RequestBody SendMsgRequest request) {
        boolean flag = channelService.sendMsg(request);
        if (!flag) {
            return ResultData.fail();
        }
        return ResultData.success();
    }

}
