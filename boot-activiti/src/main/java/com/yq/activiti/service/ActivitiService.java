package com.yq.activiti.service;

import com.google.common.collect.Maps;
import com.yq.activiti.common.constant.ActivitiConstant;
import com.yq.activiti.controller.request.ApplyRequest;
import com.yq.activiti.model.FeedbackModel;
import com.yq.activiti.model.FeedbackModelTask;
import com.yq.activiti.model.ProcessModel;
import com.yq.activiti.util.ActivitiUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p> service</p>
 * @author youq  2020/5/19 12:39
 */
@Slf4j
@Service
public class ActivitiService {

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private IdentityService identityService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Resource
    private RepositoryService repositoryService;

    public List<FeedbackModel> myActiviti(String username) {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().startedBy(username).list();
        List<FeedbackModel> activities = new ArrayList<>();
        for (ProcessInstance instance : instances) {
            activities.add(getFeedbackModel(instance));
        }
        return activities;
    }

    private FeedbackModel getFeedbackModel(ProcessInstance instance) {
        FeedbackModel activiti = new FeedbackModel();
        activiti.setProcessId(instance.getId());
        activiti.setApplyUser(instance.getStartUserId());
        activiti.setFeedbackContent(runtimeService.getVariable(instance.getId(), "feedbackContent", String.class));
        activiti.setApplyTime(instance.getStartTime());
        activiti.setApplyStatus(instance.isEnded() ? "申请结束" : "等待审批");
        activiti.setAuditor(runtimeService.getVariable(instance.getId(), "auditor", String.class));
        activiti.setAnswer(runtimeService.getVariable(instance.getId(), "answer", String.class));
        activiti.setMsg(runtimeService.getVariable(instance.getId(), "msg", String.class));
        return activiti;
    }

    public List<FeedbackModelTask> myApproval(String username) {
        //查询被指派的任务信息
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(username)
                .orderByTaskCreateTime()
                .desc()
                .list();
        List<FeedbackModelTask> activitiTasks = new ArrayList<>();
        for (Task task : tasks) {
            FeedbackModelTask activitiTask = new FeedbackModelTask();
            activitiTask.setId(task.getId());
            activitiTask.setName(task.getName());
            activitiTask.setCreateTime(task.getCreateTime());
            //activiti
            String instanceId = task.getProcessInstanceId();
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(instanceId)
                    .singleResult();
            activitiTask.setActiviti(getFeedbackModel(instance));
            activitiTasks.add(activitiTask);
        }
        return activitiTasks;
    }

    public List<FeedbackModel> myActivitiRecord(String username) {
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(ActivitiConstant.FEEDBACK_PROCESS_KEY)
                .startedBy(username)
                .finished()
                .orderByProcessInstanceEndTime()
                .desc()
                .list();
        List<FeedbackModel> activities = new ArrayList<>();
        for (HistoricProcessInstance instance : historicProcessInstances) {
            FeedbackModel activiti = new FeedbackModel();
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

    public List<FeedbackModel> myApprovalRecord(String username) {
        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(ActivitiConstant.FEEDBACK_PROCESS_KEY)
                .involvedUser(username)
                .finished()
                .orderByProcessInstanceEndTime().desc().list();

        List<FeedbackModel> activitiList = new ArrayList<>();
        for (HistoricProcessInstance hisInstance : hisProInstance) {
            FeedbackModel activiti = new FeedbackModel();
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
     * <p> 开始流程</p>
     * @param request 请求参数
     * @author youq  2020/5/15 13:10
     */
    public boolean startProcess(ApplyRequest request) {
        try {
            //认证用户的作用是设置流程发起人：
            //在开始流程之前设置，会自动在表ACT_HI_PROCINST中的START_USER_ID中设置用户ID
            //用了设置启动流程的人员ID，引擎会自动吧用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(request.getUser());
            //开始请假流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(ActivitiConstant.FEEDBACK_PROCESS_KEY);
            String processId = processInstance.getId();
            log.info("流程id：{}", processId);
            //查询当前任务
            Task applyTask = taskService.createTaskQuery().processInstanceId(processId).singleResult();
            String taskId = applyTask.getId();
            log.info("申请任务ID：{}", taskId);
            //申明任务人
            taskService.setAssignee(taskId, request.getUser());
            //是申请时的具体信息，在完成“申请请假”任务时，可以将这些信息设置成参数
            Map<String, Object> variables = Maps.newHashMap();
            variables.put("applyUser", request.getUser());
            variables.put("feedbackContent", request.getDesc());
            variables.put("msg", "问题待解决");
            //完成第一步申请
            taskService.complete(taskId, variables);
            //指派任务由谁来处理
            Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
            taskService.setAssignee(task.getId(), request.getAssignUser());
            return true;
        } catch (Exception e) {
            log.error("开始流程异常：", e);
            return false;
        }
    }

    /**
     * <p> 请假申请审批（一审）</p>
     * @param request 请求参数
     * @return boolean
     * @author youq  2020/5/15 13:57
     */
    public boolean firstReview(ApplyRequest request) {
        Task applyTask = taskService.createTaskQuery().processInstanceId(request.getProcessId()).singleResult();
        Map<String, Object> variables = new HashMap<>();
        variables.put("answer", request.getDesc());
        variables.put("msg", request.getResult());
        variables.put("auditor", request.getUser());
        taskService.setAssignee(applyTask.getId(), request.getUser());
        taskService.complete(applyTask.getId(), variables);
        //指派任务由谁来处理
        Task task = taskService.createTaskQuery().processInstanceId(request.getProcessId()).singleResult();
        taskService.setAssignee(task.getId(), request.getAssignUser());
        return true;
    }

    /**
     * <p> 审核确认</p>
     * @param request 请求参数
     * @return boolean
     * @author youq  2020/5/15 13:57
     */
    public boolean reviewConfirm(ApplyRequest request) {
        Task applyTask = taskService.createTaskQuery().processInstanceId(request.getProcessId()).singleResult();
        Map<String, Object> variables = new HashMap<>();
        variables.put("msg", request.getResult());
        taskService.setAssignee(applyTask.getId(), request.getUser());
        taskService.complete(applyTask.getId(), variables);
        if (!"问题已解决".equals(request.getResult())) {
            //指派任务由谁来处理
            Task task = taskService.createTaskQuery().processInstanceId(request.getProcessId()).singleResult();
            taskService.setAssignee(task.getId(), request.getAssignUser());
        }
        return true;
    }

    /**
     * <p> 获取所有流程图</p>
     * @return java.util.List<com.yq.activiti.model.ProcessModel>
     * @author youq  2020/5/19 13:19
     */
    public List<ProcessModel> getProcessModels() {
        List<ProcessModel> models = new ArrayList<>();
        for (Map.Entry<String, ProcessDefinition> entry : processDefinitionMap().entrySet()) {
            ProcessDefinition processDefinition = entry.getValue();
            models.add(
                    ProcessModel.builder()
                            .id(processDefinition.getDeploymentId())
                            .name(processDefinition.getKey())
                            .imageUrl(processDefinition.getDiagramResourceName())
                            .param(processDefinition.getId())
                            .nodes(getProcessAuditTaskName(processDefinition.getId()))
                            .build()
            );
        }
        return models;
    }

    /**
     * <p> 获取流程审核节点的名称</p>
     * @param processDefinitionId 流程id
     * @return java.util.List<java.lang.String>
     * @author youq  2020/5/19 13:15
     */
    public List<String> getProcessAuditTaskName(String processDefinitionId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        List<String> processDefinitionTaskNameList = new ArrayList<>();
        Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
        for (FlowElement e : flowElements) {
            if (e instanceof UserTask) {
                //不要第一个申请审核节点
                if (!"task0".equals(e.getId())) {
                    processDefinitionTaskNameList.add(e.getName());
                }
            }
        }
        return processDefinitionTaskNameList;
    }

    /**
     * <p> 项目中所有流程</p>
     * @return java.util.Map<java.lang.String , org.activiti.engine.repository.ProcessDefinition>
     * @author youq  2020/5/19 11:47
     */
    public Map<String, ProcessDefinition> processDefinitionMap() {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .asc()
                .list();
        Map<String, ProcessDefinition> processDefinitionMap = new HashMap<>();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            processDefinitionMap.put(processDefinition.getKey(), processDefinition);
        }
        return processDefinitionMap;
    }

}
