package com.yq.activiti.service;

import com.google.common.collect.Maps;
import com.yq.activiti.entity.Activiti;
import com.yq.activiti.entity.ActivitiTask;
import com.yq.activiti.util.ActivitiUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p> 测试</p>
 * RepositoryService:  流程仓库Service，用于管理流程仓库，例如：部署，删除，读取流程资源
 * IdentityService：身份Service，可以管理，查询用户，组之间的关系
 * RuntimeService：运行时Service，可以处理所有正在运行状态的流程实例，任务等
 * TaskService：任务Service，用于管理，查询任务，例如：签收，办理，指派等
 * HistoryService：历史Service，可以查询所有历史数据，例如：流程实例，任务，活动，变量，附件等
 * FormService：表单Service，用于读取和流程，任务相关的表单数据
 * ManagementService：引擎管理Service，和具体业务无关，主要是可以查询引擎配置，数据库，作业等
 * DynamicBpmnService：一个新增的服务，用于动态修改流程中的一些参数信息等，是引擎中的一个辅助的服务
 * @author youq  2020/5/7 15:38
 */
@Slf4j
@Service
public class ActivitiService {

    private static final String PROCESS_DEFINE_KEY = "testProcess";

    private static final String NEXT_ASSIGNEE = "youq";

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

    public List<Activiti> myActiviti(String username) {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery().startedBy(username).list();
        List<Activiti> activities = new ArrayList<>();
        for (ProcessInstance instance : instances) {
            activities.add(getActiviti(instance));
        }
        return activities;
    }

    private Activiti getActiviti(ProcessInstance instance) {
        Activiti activiti = new Activiti();
        activiti.setApplyUser(instance.getStartUserId());
        activiti.setDays(runtimeService.getVariable(instance.getId(), "days", Integer.class));
        activiti.setReason(runtimeService.getVariable(instance.getId(), "reason", String.class));
        activiti.setApplyTime(instance.getStartTime());
        activiti.setApplyStatus(instance.isEnded() ? "申请结束" : "等待审批");
        return activiti;
    }

    public List<ActivitiTask> myApproval(String username) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(username)
                .orderByTaskCreateTime()
                .desc()
                .list();
        List<ActivitiTask> activitiTasks = new ArrayList<>();
        for (Task task : tasks) {
            ActivitiTask activitiTask = new ActivitiTask();
            activitiTask.setId(task.getId());
            activitiTask.setName(task.getName());
            activitiTask.setCreateTime(task.getCreateTime());
            //activiti
            String instanceId = task.getProcessInstanceId();
            ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(instanceId)
                    .singleResult();
            activitiTask.setActiviti(getActiviti(instance));
            activitiTasks.add(activitiTask);
        }
        return activitiTasks;
    }

    public List<Activiti> myActivitiRecord(String username) {
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(PROCESS_DEFINE_KEY)
                .startedBy(username)
                .finished()
                .orderByProcessInstanceEndTime()
                .desc()
                .list();
        List<Activiti> activities = new ArrayList<>();
        for (HistoricProcessInstance instance : historicProcessInstances) {
            Activiti activiti = new Activiti();
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

    public List<Activiti> myApprovalRecord(String username) {
        List<HistoricProcessInstance> hisProInstance = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(PROCESS_DEFINE_KEY).involvedUser(username).finished()
                .orderByProcessInstanceEndTime().desc().list();

        List<String> auditTaskNameList = new ArrayList<>();
        auditTaskNameList.add("经理审批");
        auditTaskNameList.add("总监审批");
        List<Activiti> activitiList = new ArrayList<>();
        for (HistoricProcessInstance hisInstance : hisProInstance) {
            /*List<HistoricTaskInstance> hisTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(hisInstance.getId()).processFinished()
                .taskAssignee(userName)
                .taskNameIn(auditTaskNameList)
                .orderByHistoricTaskInstanceEndTime().desc().list();
            boolean isMyAudit = false;
            for (HistoricTaskInstance taskInstance : hisTaskInstanceList) {
                if (taskInstance.getAssignee().equals(userName)) {
                    isMyAudit = true;
                }
            }
            if (!isMyAudit) {
                continue;
            }*/
            Activiti activiti = new Activiti();
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
     * @param vac      activiti对象
     * @param username 申请人
     * @author youq  2020/5/7 16:09
     */
    public boolean startActiviti(Activiti vac, String username) {
        log.info("start activiti execute。。。");
        try {
            //认证用户的作用是设置流程发起人：
            //在开始流程之前设置，会自动在表ACT_HI_PROCINST中的START_USER_ID中设置用户ID
            //用了设置启动流程的人员ID，引擎会自动吧用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(username);
            //开始流程
            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINE_KEY);
            String processId = processInstance.getId();
            log.info("流程id：{}", processId);
            //查询当前任务
            Task currentTask = taskService.createTaskQuery().processInstanceId(processId).singleResult();
            String taskId = currentTask.getId();
            log.info("当前任务ID：{}", taskId);
            //申明任务人
            taskService.setAssignee(taskId, username);
            Map<String, Object> vars = Maps.newHashMapWithExpectedSize(4);
            vars.put("applyUser", username);
            vars.put("days", vac.getDays());
            vars.put("reason", vac.getReason());
            //在此方法中，vars 是申请时的具体信息，在完成“申请请假”任务时，可以将这些信息设置成参数。
            //完成第一步申请
            taskService.complete(taskId, vars);
            //到了下一个任务，应该在此处指派任务由谁来处理
            //重新获取当前任务
            Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
            String newTaskId = task.getId();
            taskService.setAssignee(newTaskId, NEXT_ASSIGNEE);
            log.info("最新任务ID：{}", newTaskId);
        } catch (Exception e) {
            log.info("开始流程失败：", e);
            return false;
        }
        return true;
    }

    /**
     * <p> Activiti任务认领</p>
     * taskService.setAssignee(String taskId, String userId);
     * taskService.claim(String taskId, String userId);
     * taskService.setOwner(String taskId, String userId);
     * setAssignee和claim两个的区别是在认领任务时，claim会检查该任务是否已经被认领，如果被认领则会抛出ActivitiTaskAlreadyClaimedException ,而setAssignee不会进行这样的检查，其他方面两个方法效果一致。
     * setOwner和setAssignee的区别在于,setOwner是在代理任务时使用，代表着任务的归属者，而这时，setAssignee代表的是代理办理者，
     * 举个例子来说，公司总经理现在有个任务taskA，去核实一下本年度的财务报表，他现在又很忙没时间，于是将该任务委托给其助理进行办理，此时，就应该这么做
     * taskService.setOwner(taskA.getId(), 总经理.getId());
     * taskService.setAssignee/claim(taskA.getId(), 助理.getId());
     * @param username     用户
     * @param activitiTask activiti task
     * @author youq  2020/5/7 16:38
     */
    public boolean passApproval(String username, ActivitiTask activitiTask) {
        username = NEXT_ASSIGNEE;
        String taskId = activitiTask.getId();
        String result = activitiTask.getActiviti().getResult();
        Map<String, Object> vars = new HashMap<>();
        vars.put("result", result);
        vars.put("auditor", username);
        vars.put("auditTime", new Date());
        taskService.setAssignee(taskId, username);
        taskService.complete(taskId, vars);
        return true;
    }

}
