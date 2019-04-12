package com.yq.feign.response.repository;

import com.yq.feign.response.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> 用户Repository</p>
 * @author youq  2019/4/12 10:50
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
