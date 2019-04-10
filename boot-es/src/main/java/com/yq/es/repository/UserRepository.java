package com.yq.es.repository;

import com.yq.es.entity.User;
import com.yq.kernel.enu.SexEnum;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p> 用户repository</p>
 * @author youq  2019/4/10 16:37
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

    /**
     * <p> 根据性别查询用户信息</p>
     * @param sex 性别
     * @return java.util.List<com.yq.es.entity.User>
     * @author youq  2019/4/10 19:01
     */
    List<User> findBySex(SexEnum sex);

}
