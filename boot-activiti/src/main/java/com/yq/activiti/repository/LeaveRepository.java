package com.yq.activiti.repository;

import com.yq.activiti.db.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> 流程执行记录持久化处理接口</p>
 * @author youq  2020/5/15 12:01
 */
@Repository
public interface LeaveRepository extends JpaRepository<Leave, Integer> {
}
