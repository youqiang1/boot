package com.yq.mongo.dao;

import com.yq.mongo.entity.Address;
import com.yq.mongo.entity.TestUser;
import com.yq.mongo.model.UserAgeStatisticsModel;
import com.yq.mongo.request.TestUserRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p> dao</p>
 * @author youq  2019/12/26 13:26
 */
@Component
public class UserDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(TestUser testUser) {
        mongoTemplate.save(testUser);
    }

    public void saveAddress(Address ad) {
        mongoTemplate.save(ad);
    }

    public void update(TestUser user) {
        Query query = new Query(Criteria.where("id").is(user.getId()));
        Update update = new Update();
        update.set("name", user.getName());
        update.set("age", user.getAge());
        update.set("introduce", user.getIntroduce());
        mongoTemplate.updateFirst(query, update, TestUser.class);
    }

    public void delete(Long id) {
        TestUser user = mongoTemplate.findById(id, TestUser.class);
        mongoTemplate.remove(user);
    }

    /**
     * <p> 查询所有用户信息</p>
     * @return java.util.List<com.yq.mongo.entity.TestUser>
     * @author youq  2019/12/26 17:42
     */
    public List<TestUser> findAll() {
        return mongoTemplate.findAll(TestUser.class);
    }

    /**
     * <p> 根据名称模糊查询用户信息</p>
     * @param name 名称
     * @return java.util.List<com.yq.mongo.entity.TestUser>
     * @author youq  2019/12/26 17:42
     */
    public List<TestUser> findByName(String name) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("name").regex(name);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, TestUser.class);
    }

    /**
     * <p> 分页</p>
     * @param pageable Pageable
     * @param user     查询请求
     * @return org.springframework.data.domain.Page<com.yq.mongo.entity.TestUser>
     * @author youq  2019/12/26 17:32
     */
    public Page<TestUser> findPage(Pageable pageable, TestUserRequest user) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (StringUtils.isNotBlank(user.getName())) {
            criteria.and("name").regex(user.getName());
        }
        if (StringUtils.isNotBlank(user.getIntroduce())) {
            criteria.and("introduce").regex(user.getIntroduce());
        }
        if (user.getId() != null) {
            criteria.and("id").is(user.getId());
        }
        if (user.getMinAge() != null && user.getMinAge() > 0) {
            criteria.and("age").gte(user.getMinAge());
        }
        if (user.getMaxAge() != null && user.getMaxAge() > 0) {
            criteria.and("age").lte(user.getMaxAge());
        }
        if (user.getSex() != null) {
            criteria.and("sex").is(user.getSex());
        }
        query.addCriteria(criteria);

        List<TestUser> testUsers = mongoTemplate.find(query.with(pageable), TestUser.class);
        return PageableExecutionUtils.getPage(testUsers, pageable, () -> mongoTemplate.count(query, TestUser.class));
    }

    /**
     * <p> 多表查询</p>
     * @return java.util.List<java.util.Map>
     * @author youq  2019/12/26 17:27
     */
    public List<Map> findUserAndAddress() {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                //从表名
                .from("address")
                //主表关联字段，不知道为什么用id不管用
                .localField("_id")
                //从表中关联主表的字段
                .foreignField("userId")
                .as("userAddress");
        Aggregation aggregation = Aggregation.newAggregation(lookupOperation);
        return mongoTemplate.aggregate(aggregation, "test_user", Map.class).getMappedResults();
    }

    /**
     * <p> count</p>
     * @return java.util.List<com.yq.mongo.model.UserAgeStatisticsModel>
     * @author youq  2019/12/26 17:26
     */
    public List<UserAgeStatisticsModel> countAge() {
        //select
        ProjectionOperation projectionOperation = Aggregation.project("age", "count");
        //where
        MatchOperation matchOperation = Aggregation.match(new Criteria());
        //group
        GroupOperation groupOperation = Aggregation.group("age")
                .count().as("count")
                .last("age").as("age");
        Aggregation aggregation = Aggregation.newAggregation(projectionOperation, matchOperation, groupOperation);

        AggregationResults<UserAgeStatisticsModel> userAgeStatisticsModels =
                mongoTemplate.aggregate(aggregation, "test_user", UserAgeStatisticsModel.class);
        return userAgeStatisticsModels.getMappedResults();
    }

}
