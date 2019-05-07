package com.yq.sharding.jpa.repository;

import com.yq.sharding.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> user repository</p>
 * @author youq  2019/5/7 17:41
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
