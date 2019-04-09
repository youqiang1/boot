package com.yq.redisop.dao;

import com.google.common.collect.Lists;
import com.yq.kernel.constants.RedisKeyConstants;
import com.yq.redisop.model.UserModel;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p> user redis dao</p>
 * @author youq  2019/4/9 15:43
 */
@Component
public class UserRedisDao {

    @Autowired
    private RedissonClient client;

    /**
     * <p> 添加</p>
     * @param model 用户信息
     * @author youq  2019/4/9 15:51
     */
    public void save(UserModel model) {
        RMap<Integer, UserModel> map = client.getMap(RedisKeyConstants.USER_INFO);
        map.put(model.getId(), model);
    }

    /**
     * <p> 批量添加</p>
     * @param models 用户信息集合
     * @author youq  2019/4/9 15:51
     */
    public void saveBatch(List<UserModel> models) {
        RMap<Integer, UserModel> map = client.getMap(RedisKeyConstants.USER_INFO);
        for (UserModel model : models) {
            map.put(model.getId(), model);
        }
    }

    /**
     * <p> 查询所有用户信息</p>
     * @return java.util.List<com.yq.redisop.model.UserModel>
     * @author youq  2019/4/9 15:52
     */
    public List<UserModel> findAll() {
        RMap<Integer, UserModel> map = client.getMap(RedisKeyConstants.USER_INFO);
        return Lists.newArrayList(map.readAllValues());
    }

    /**
     * <p> 查询所有用户信息，map</p>
     * @return java.util.Map<java.lang.Integer , com.yq.redisop.model.UserModel>
     * @author youq  2019/4/9 15:53
     */
    public Map<Integer, UserModel> findAllForMap() {
        return client.getMap(RedisKeyConstants.USER_INFO);
    }

    /**
     * <p> 查询用户信息</p>
     * @param userId 用户id
     * @return com.yq.redisop.model.UserModel
     * @author youq  2019/4/9 15:54
     */
    public UserModel findOne(Integer userId) {
        RMap<Integer, UserModel> map = client.getMap(RedisKeyConstants.USER_INFO);
        return map.get(userId);
    }

    /**
     * <p> 根据用户名查询用户信息</p>
     * @param username 用户名
     * @return com.yq.redisop.model.UserModel
     * @author youq  2019/4/9 15:56
     */
    public UserModel findByUsername(String username) {
        RMap<Integer, UserModel> map = client.getMap(RedisKeyConstants.USER_INFO);
        Map<String, UserModel> nameMap =
                map.readAllValues().stream().collect(Collectors.toMap(UserModel::getUsername, Function.identity()));
        return nameMap.get(username);
    }

}
