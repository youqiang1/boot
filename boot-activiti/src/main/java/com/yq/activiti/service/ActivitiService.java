package com.yq.activiti.service;

import com.yq.activiti.model.ProcessModel;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
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
