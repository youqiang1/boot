package com.yq.activiti.repository;

import com.yq.activiti.db.ProcessTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p> 流程执行任务持久化处理接口</p>
 * @author youq  2020/5/15 12:01
 */
@Repository
public interface ProcessTaskRepository extends JpaRepository<ProcessTask, Integer> {

    List<ProcessTask> findByProcessIdAndProcessType(Integer activitiProcessId, String processType);

}
