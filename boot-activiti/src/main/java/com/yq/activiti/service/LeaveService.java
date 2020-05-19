package com.yq.activiti.service;

import com.google.common.collect.Maps;
import com.yq.activiti.common.constant.ActivitiConstant;
import com.yq.activiti.common.enu.ReviewLevelEnum;
import com.yq.activiti.common.enu.ReviewStatusEnum;
import com.yq.activiti.controller.request.LeaveRequest;
import com.yq.activiti.controller.request.ReviewRequest;
import com.yq.activiti.db.Leave;
import com.yq.activiti.db.User;
import com.yq.activiti.model.LeaveModel;
import com.yq.activiti.model.LeaveModelTask;
import com.yq.activiti.repository.LeaveRepository;
import com.yq.activiti.util.ActivitiUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 流程执行服务</p>
 * @author youq  2020/5/15 13:01
 */
@Slf4j
@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private ProcessTaskService processTaskService;

    @Autowired
    private UserService userService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private IdentityService identityService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    public List<LeaveModel> myActiviti(String username) {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().startedBy(username).list();
        List<LeaveModel> activities = new ArrayList<>();
        for (ProcessInstance instance : instances) {
            activities.add(getLeaveModel(instance));
        }
        return activities;
    }

    private LeaveModel getLeaveModel(ProcessInstance instance) {
        LeaveModel activiti = new LeaveModel();
        activiti.setApplyUser(instance.getStartUserId());
        activiti.setDays(runtimeService.getVariable(instance.getId(), "days", Integer.class));
        activiti.setReason(runtimeService.getVariable(instance.getId(), "reason", String.class));
        activiti.setApplyTime(instance.getStartTime());
        activiti.setApplyStatus(instance.isEnded() ? "申请结束" : "等待审批");
        return activiti;
    }

    public List<LeaveModelTask> myApproval(String username) {
        User user = userService.findByUsername(username);
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(user.getReviewLevel().name())
                .orderByTaskCreateTime()
                .desc()
                .list();
        List<LeaveModelTask> activitiTasks = new ArrayList<>();
        for (Task task : tasks) {
            LeaveModelTask activitiTask = new LeaveModelTask();
            activitiTask.setId(task.getId());
            activitiTask.setName(task.getName());
            activitiTask.setCreateTime(task.getCreateTime());
            //activiti
            String instanceId = task.getProcessInstanceId();
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(instanceId)
                    .singleResult();
            activitiTask.setActiviti(getLeaveModel(instance));
            activitiTasks.add(activitiTask);
        }
        return activitiTasks;
    }

    public List<LeaveModel> myActivitiRecord(String username) {
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(ActivitiConstant.LEAVE_PROCESS_DEFINE_KEY)
                .startedBy(username)
                .finished()
                .orderByProcessInstanceEndTime()
                .desc()
                .list();
        List<LeaveModel> activities = new ArrayList<>();
        for (HistoricProcessInstance instance : historicProcessInstances) {
            LeaveModel activiti = new LeaveModel();
            activiti.setApplyUser(instance.getStartUserId());
            activiti.setApplyTime(instance.getStartTime());
            activiti.setApplyStatus("申请结束");
            List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(instance.getId()).list();
            ActivitiUtil.setVars(activiti, variableInstances);
            activities.add(activiti);
        }
        return activities;
    }

    public List<LeaveModel> myApprovalRecord(String username) {
        User user = userService.findByUsername(username);
        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(ActivitiConstant.LEAVE_PROCESS_DEFINE_KEY)
                .involvedUser(user.getReviewLevel().name())
                .finished()
                .orderByProcessInstanceEndTime().desc().list();

        List<LeaveModel> activitiList = new ArrayList<>();
        for (HistoricProcessInstance hisInstance : hisProInstance) {
            LeaveModel activiti = new LeaveModel();
            activiti.setApplyUser(hisInstance.getStartUserId());
            activiti.setApplyStatus("申请结束");
            activiti.setApplyTime(hisInstance.getStartTime());
            List<HistoricVariableInstance> varInstanceList = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(hisInstance.getId()).list();
            ActivitiUtil.setVars(activiti, varInstanceList);
            activitiList.add(activiti);
        }
        return activitiList;
    }

    /**
     * <p> 开始请假流程</p>
     * @param request 请求参数
     * @author youq  2020/5/15 13:10
     */
    public boolean startLeave(LeaveRequest request) {
        try {
            //认证用户的作用是设置流程发起人：
            //在开始流程之前设置，会自动在表ACT_HI_PROCINST中的START_USER_ID中设置用户ID
            //用了设置启动流程的人员ID，引擎会自动吧用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(request.getApplyUser());
            //开始请假流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ActivitiConstant.LEAVE_PROCESS_DEFINE_KEY);
            String processId = processInstance.getId();
            log.info("流程id：{}", processId);
            //查询当前任务
            Task applyTask = taskService.createTaskQuery().processInstanceId(processId).singleResult();
            String taskId = applyTask.getId();
            log.info("申请任务ID：{}", taskId);
            //申明任务人
            taskService.setAssignee(taskId, request.getApplyUser());
            //是申请时的具体信息，在完成“申请请假”任务时，可以将这些信息设置成参数
            Map<String, Object> variables = Maps.newHashMap();
            variables.put("applyUser", request.getApplyUser());
            variables.put("days", request.getDays());
            variables.put("reason", request.getReason());
            variables.put("reviewStatus", ReviewStatusEnum.WAIT);
            //完成第一步申请
            taskService.complete(taskId, variables);
            //指派任务由谁来处理
            Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
            taskService.setAssignee(task.getId(), ReviewLevelEnum.FIRST.name());
            //activitiProcess add
            Integer activitiProcessId = save(request, task.getId(), processId);
            //processTask add
            processTaskService.save(activitiProcessId, ActivitiConstant.LEAVE_PROCESS_DEFINE_KEY, task.getId());
            return true;
        } catch (Exception e) {
            log.error("开始请假流程异常：", e);
            return false;
        }
    }

    /**
     * <p> 请假申请审批（一审）</p>
     * @param request 请求参数
     * @return boolean
     * @author youq  2020/5/15 13:57
     */
    public boolean firstReview(ReviewRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("result", request.getResult());
        variables.put("auditor", request.getAuditor());
        variables.put("auditTime", LocalDateTime.now());
        variables.put("reviewStatus", ReviewStatusEnum.FIRST);
        taskService.setAssignee(request.getActivitiTaskId(), request.getAuditor());
        taskService.complete(request.getActivitiTaskId(), variables);
        //指派任务由谁来处理
        Task task = taskService.createTaskQuery().processInstanceId(request.getProcessId()).singleResult();
        taskService.setAssignee(task.getId(), ReviewLevelEnum.LAST.name());
        //activitiProcess update
        firstReviewUpdate(request, task.getId());
        //processTask update
        processTaskService.update(request.getLeaveId(), ActivitiConstant.LEAVE_PROCESS_DEFINE_KEY, task.getId());
        return true;
    }

    /**
     * <p> 请假申请审批（二审）</p>
     * @param request 请求参数
     * @return boolean
     * @author youq  2020/5/15 13:57
     */
    public boolean lastReview(ReviewRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("reviewResult", request.getResult());
        variables.put("reviewers", request.getAuditor());
        variables.put("reviewTime", LocalDateTime.now());
        variables.put("reviewStatus", ReviewStatusEnum.LAST);
        taskService.setAssignee(request.getActivitiTaskId(), request.getAuditor());
        taskService.complete(request.getActivitiTaskId(), variables);
        //activitiProcess last update
        lastReviewUpdate(request);
        return true;
    }

    /**
     * <p> 流程执行记录</p>
     * @param request   请求参数
     * @param taskId    activiti taskId
     * @param processId activiti processId
     * @return java.lang.Integer
     * @author youq  2020/5/15 13:39
     */
    private Integer save(LeaveRequest request, String taskId, String processId) {
        Leave leave = new Leave();
        leave.setApplyUser(request.getApplyUser());
        leave.setDays(request.getDays());
        leave.setReason(request.getReason());
        leave.setApplyTime(LocalDateTime.now());
        leave.setActivitiTaskId(taskId);
        leave.setProcessId(processId);
        leave.setReviewStatus(ReviewStatusEnum.WAIT);
        leaveRepository.save(leave);
        return leave.getId();
    }

    /**
     * <p> 一审更新流程执行记录</p>
     * @param request        审核请求参数
     * @param activitiTaskId 新activiti任务id
     * @author youq  2020/5/15 14:16
     */
    private void firstReviewUpdate(ReviewRequest request, String activitiTaskId) {
        Leave leave = leaveRepository.findOne(request.getLeaveId());
        leave.setActivitiTaskId(activitiTaskId);
        leave.setAuditor(request.getAuditor());
        leave.setResult(request.getResult());
        leave.setAuditTime(LocalDateTime.now());
        leave.setReviewStatus(ReviewStatusEnum.FIRST);
        leaveRepository.save(leave);
    }

    /**
     * <p> 二审更新流程执行记录</p>
     * @param request        审核请求参数
     * @author youq  2020/5/15 14:16
     */
    private void lastReviewUpdate(ReviewRequest request) {
        Leave leave = leaveRepository.findOne(request.getLeaveId());
        leave.setReviewers(request.getAuditor());
        leave.setReviewResult(request.getResult());
        leave.setReviewTime(LocalDateTime.now());
        leave.setReviewStatus(ReviewStatusEnum.LAST);
        leaveRepository.save(leave);
    }

}
