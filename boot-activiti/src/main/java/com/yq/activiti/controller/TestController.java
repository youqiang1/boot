package com.yq.activiti.controller;

import com.yq.activiti.model.Activiti;
import com.yq.activiti.model.ActivitiTask;
import com.yq.activiti.service.TestService;
import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 控制器</p>
 * @author youq  2020/5/7 15:36
 */
@Slf4j
@RestController
@RequestMapping("/activity")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/allRecord")
    public ResultData addRecord(String username) {
        Map<String, Object> model = new HashMap<>();
        //myActiviti();
        List<Activiti> list = myActiviti(username);
        model.put("list", list);
        //myApproval();
        List<ActivitiTask> list3 = myApproval(username);
        model.put("list3", list3);
        //myActivitiRecord();
        List<Activiti> list2 = myActivitiRecord(username);
        model.put("list2", list2);
        //myApprovalRecord();
        List<Activiti> list4 = myApprovalRecord(username);
        model.put("list4", list4);
        return ResultData.success(model);
    }

    /**
     * <p> 发起审批申请</p>
     * @return java.util.List<com.yq.activiti.entity.Activiti>
     * @author youq  2020/5/7 16:02
     */
    @RequestMapping(value = "/myActiviti")
    private List<Activiti> myActiviti(String username) {
        return testService.myActiviti(username);
    }

    //待我审核的请假
    @RequestMapping(value = "/myApproval", method = RequestMethod.GET)
    public List<ActivitiTask> myApproval(String username) {
        return testService.myApproval(username);
    }

    //我申请过的假
    @RequestMapping(value = "/myActivitiRecord", method = RequestMethod.GET)
    public List<Activiti> myActivitiRecord(String username) {
        return testService.myActivitiRecord(username);
    }

    //我的审核记录
    @RequestMapping(value = "/myApprovalRecord", method = RequestMethod.GET)
    public List<Activiti> myApprovalRecord(String username) {
        return testService.myApprovalRecord(username);
    }

    @RequestMapping(value = "/createActiviti", method = RequestMethod.POST)
    public ResultData<?> createActiviti(@RequestBody Activiti vac, String username) {
        return ResultData.success(testService.startActiviti(vac, username));
    }

    @RequestMapping(value = "/passApproval")
    public ResultData<?> passApproval(String id, String result, String username) {
        ActivitiTask activitiTask = new ActivitiTask();
        Activiti activiti = new Activiti();
        activitiTask.setId(id);
        activiti.setResult(result);
        activitiTask.setActiviti(activiti);
        return ResultData.success(testService.passApproval(username, activitiTask));
    }

}
