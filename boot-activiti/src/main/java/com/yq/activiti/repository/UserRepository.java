package com.yq.activiti.repository;

import com.yq.activiti.common.enu.ReviewLevelEnum;
import com.yq.activiti.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p> 用户持久化处理接口</p>
 * @author youq  2020/5/15 12:01
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByReviewLevel(ReviewLevelEnum reviewLevel);

    User findByUsername(String username);

}
