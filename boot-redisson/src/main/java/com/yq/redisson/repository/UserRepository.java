package com.yq.redisson.repository;

import com.yq.redisson.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> 用户 repository</p>
 * @author youq  2019/4/9 15:28
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
