package com.yq.easyexcel.repository;

import com.yq.easyexcel.db.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p> 用户 repository</p>
 * @author youq  2019/4/9 15:28
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByPhone(String phone);

}
