package com.yq.activiti.controller;

import com.yq.activiti.controller.request.LeaveRequest;
import com.yq.activiti.controller.request.ReviewRequest;
import com.yq.activiti.entity.LeaveModel;
import com.yq.activiti.entity.LeaveModelTask;
import com.yq.activiti.service.LeaveService;
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
 * <p> 请假</p>
 * @author youq  2020/5/15 13:01
 */
@Slf4j
@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    /**
     * 流程操作查询 TODO 查询activiti的表，暂时没找到流程中的taskId、processId存储的地方，查询Leave表可以解决
     * @param username
     * @return
     */
    @RequestMapping("/allRecord")
    public ResultData addRecord(String username) {
        Map<String, Object> model = new HashMap<>();
        //myActiviti();
        List<LeaveModel> list = leaveService.myActiviti(username);
        model.put("list", list);
        //myApproval();
        List<LeaveModelTask> list3 = leaveService.myApproval(username);
        model.put("list3", list3);
        //myActivitiRecord();
        List<LeaveModel> list2 = leaveService.myActivitiRecord(username);
        model.put("list2", list2);
        //myApprovalRecord();
        List<LeaveModel> list4 = leaveService.myApprovalRecord(username);
        model.put("list4", list4);
        return ResultData.success(model);
    }

    /**
     * <p> 请假申请</p>
     * @param request 请求参数
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2020/5/15 13:06
     */
    @RequestMapping("/startLeave")
    private ResultData<?> startLeave(@RequestBody LeaveRequest request) {
        boolean flag = leaveService.startLeave(request);
        log.info("请假申请：{}", flag);
        return ResultData.success(flag);
    }

    /**
     * <p> 一审</p>
     * @param request 请求参数
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2020/5/15 13:06
     */
    @RequestMapping("/firstReview")
    private ResultData<?> firstReview(@RequestBody ReviewRequest request) {
        boolean flag = leaveService.firstReview(request);
        log.info("请假申请一审：{}", flag);
        return ResultData.success(flag);
    }

    /**
     * <p> 终审</p>
     * @param request 请求参数
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2020/5/15 13:06
     */
    @RequestMapping("/lastReview")
    private ResultData<?> lastReview(@RequestBody ReviewRequest request) {
        boolean flag = leaveService.lastReview(request);
        log.info("请假申请终审：{}", flag);
        return ResultData.success(flag);
    }

}
