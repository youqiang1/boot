package com.yq.activiti.service;

import com.yq.activiti.db.ProcessTask;
import com.yq.activiti.repository.ProcessTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p> 流程任务服务</p>
 * @author youq  2020/5/15 13:35
 */
@Slf4j
@Service
public class ProcessTaskService {

    @Autowired
    private ProcessTaskRepository processTaskRepository;

    /**
     * <p> 创建流程任务记录</p>
     * @param activitiProcessRecordId 流程执行记录id
     * @param processType             流程类型
     * @param activitiTaskId          activiti执行task的id
     * @author youq  2020/5/15 13:47
     */
    public void save(Integer activitiProcessRecordId, String processType, String activitiTaskId) {
        ProcessTask task = new ProcessTask();
        task.setActivitiTaskId(activitiTaskId);
        task.setProcessId(activitiProcessRecordId);
        task.setProcessType(processType);
        processTaskRepository.save(task);
    }

    /**
     * <p> 更新任务id</p>
     * @param activitiProcessRecordId 流程执行记录id
     * @param processType             流程类型
     * @param activitiTaskId          activiti执行task的id
     * @author youq  2020/5/15 14:13
     */
    public void update(Integer activitiProcessRecordId, String processType, String activitiTaskId) {
        List<ProcessTask> processTasks = processTaskRepository.findByProcessIdAndProcessType(activitiProcessRecordId, processType);
        if (CollectionUtils.isEmpty(processTasks) || processTasks.size() > 1) {
            log.info("异常的流程执行任务信息");
            return;
        }
        ProcessTask task = processTasks.get(0);
        task.setActivitiTaskId(activitiTaskId);
        processTaskRepository.save(task);
    }

}
