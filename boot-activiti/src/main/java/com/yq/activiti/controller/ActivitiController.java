package com.yq.activiti.controller;

import com.yq.activiti.controller.request.ApplyRequest;
import com.yq.activiti.model.FeedbackModel;
import com.yq.activiti.model.FeedbackModelTask;
import com.yq.activiti.service.ActivitiService;
import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 测试</p>
 * @author youq  2020/5/20 15:49
 */
@Slf4j
@RestController
@RequestMapping("/activiti")
public class ActivitiController {

    @Autowired
    private ActivitiService activitiService;

    /**
     * 流程操作查询
     * @param username
     * @return
     */
    @RequestMapping("/allRecord")
    public ResultData addRecord(String username) {
        Map<String, Object> model = new HashMap<>();
        //myActiviti();
        List<FeedbackModel> list = activitiService.myActiviti(username);
        model.put("applyProcesses", list);
        //myApproval();
        List<FeedbackModelTask> list3 = activitiService.myApproval(username);
        model.put("handleProcesses", list3);
        //myActivitiRecord();
        List<FeedbackModel> list2 = activitiService.myActivitiRecord(username);
        model.put("applyProcessRecords", list2);
        //myApprovalRecord();
        List<FeedbackModel> list4 = activitiService.myApprovalRecord(username);
        model.put("handleProcessRecords", list4);
        return ResultData.success(model);
    }

    @RequestMapping("/start")
    public ResultData<?> startProcess(String user, String assignUser, String feedbackContent) {
        activitiService.startProcess(
                ApplyRequest.builder()
                        .user(user)
                        .assignUser(assignUser)
                        .desc(feedbackContent)
                        .build()
        );
        return ResultData.success();
    }

    @RequestMapping("/firstReview")
    public ResultData<?> firstReview(@RequestBody ApplyRequest request) {
        activitiService.firstReview(request);
        return ResultData.success();
    }

    @RequestMapping("/reviewConfirm")
    public ResultData<?> reviewConfirm(@RequestBody ApplyRequest request) {
        activitiService.reviewConfirm(request);
        return ResultData.success();
    }

}
