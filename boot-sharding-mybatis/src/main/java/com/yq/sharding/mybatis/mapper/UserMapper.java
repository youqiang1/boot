package com.yq.sharding.mybatis.mapper;

import com.yq.sharding.mybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p> 用户信息Mapper</p>
 * @author youq  2019/4/29 10:05
 */
@Mapper
public interface UserMapper {

    /**
     * <p> 保存</p>
     * @param user 用户信息
     * @author youq  2019/4/29 10:06
     */
    void save(User user);

    /**
     * <p> findOne</p>
     * @param id 用户信息id
     * @return com.yq.sharding.mybatis.entity.User
     * @author youq  2019/4/29 10:06
     */
    User findOne(Long id);

    /**
     * <p> findAll and order by</p>
     * @return java.util.List<com.yq.sharding.mybatis.entity.User>
     * @author youq  2019/4/29 13:47
     */
    List<User> findAllOrder();

}
