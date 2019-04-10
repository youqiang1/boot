package com.yq.es.service;

import com.google.common.collect.Lists;
import com.yq.es.controller.qo.UserQO;
import com.yq.es.entity.User;
import com.yq.es.repository.UserRepository;
import com.yq.kernel.constants.ElasticsearchConstant;
import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p> 用户service</p>
 * @author youq  2019/4/10 16:38
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    @Qualifier(value = "userBulkProcessor")
    private BulkProcessor userBulkProcessor;

    /**
     * <p> 使用spring data elasticsearch提供的repository查询数据</p>
     * @param page 页码
     * @param size 页大小
     * @return java.util.List<com.yq.es.entity.User>
     * @author youq  2019/4/10 18:02
     */
    public List<User> findAllByData(Integer page, Integer size) {
        //Pageable
        Pageable pageable = new PageRequest(page, size,
                new Sort(Sort.Direction.DESC, "createTime"));
        //NativeSearchQueryBuilder
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        NativeSearchQuery searchQuery = builder.withPageable(pageable).build();
        Page<User> users = userRepository.search(searchQuery);
        return Lists.newArrayList(users);
    }

    /**
     * <p> 通过elasticsearchTemplate分页查询数据</p>
     * @param qo 请求参数
     * @return org.springframework.data.domain.Page<com.yq.es.entity.User>
     * @author youq  2019/4/10 17:37
     */
    public Page<User> findPageByTemplate(UserQO qo) {
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        //时间范围
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bqb.must(
                QueryBuilders.rangeQuery("createTime")
                        .from(sdf.format(qo.getBeginTime()))
                        .to(sdf.format(qo.getEndTime()))
                        //> <=
                        .includeUpper(false).includeLower(true)
        );
        //数字范围
        bqb.must(
                QueryBuilders.rangeQuery("age")
                        .from(qo.getBeginAge())
                        .to(qo.getEndAge())
                        //默认为true，此处可不写
                        .includeUpper(true).includeLower(true)
        );
        //等于
        bqb.must(QueryBuilders.matchPhraseQuery("phone", qo.getPhone()));
        bqb.must(QueryBuilders.matchPhraseQuery("sex", qo.getSex().name()));
        //prefix
        bqb.must(QueryBuilders.prefixQuery("username", qo.getUsername()));
        bqb.must(QueryBuilders.prefixQuery("email", qo.getEmail()));
        //Pageable
        Pageable pageable = new PageRequest(qo.getPage(), qo.getSize(),
                new Sort(Sort.Direction.DESC, "createTime"));
        //NativeSearchQueryBuilder
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        NativeSearchQuery searchQuery = builder.withIndices(ElasticsearchConstant.USER_INDEX)
                .withTypes(ElasticsearchConstant.USER_TYPE)
                .withPageable(pageable)
                .withQuery(bqb)
                .build();
        return elasticsearchTemplate.queryForPage(searchQuery, User.class);
    }


    /**
     * <p> 通过elasticsearchTemplate查询所有数据</p>
     * @return java.util.List<com.yq.es.entity.User>
     * @author youq  2019/4/10 16:55
     */
    public List<User> findAllByTemplate() {
        //Pageable
        Pageable pageable = new PageRequest(0, ElasticsearchConstant.QUERY_MAX_LIMIT,
                new Sort(Sort.Direction.DESC, "createTime"));
        //NativeSearchQueryBuilder
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        NativeSearchQuery searchQuery = builder.withIndices(ElasticsearchConstant.USER_INDEX)
                .withTypes(ElasticsearchConstant.USER_TYPE)
                .withPageable(pageable)
                .build();
        Page<User> page = elasticsearchTemplate.queryForPage(searchQuery, User.class);
        if (page != null && page.getTotalElements() > 0) {
            int total = (int) page.getTotalElements();
            List<User> returnList = Lists.newArrayListWithCapacity(total);
            returnList.addAll(page.getContent());
            //如果超过10000条，多次查询
            if (total > ElasticsearchConstant.QUERY_MAX_LIMIT) {
                int count = total % ElasticsearchConstant.QUERY_MAX_LIMIT == 0
                        ? total / ElasticsearchConstant.QUERY_MAX_LIMIT
                        : total / ElasticsearchConstant.QUERY_MAX_LIMIT + 1;
                for (int i = 1; i < count; i++) {
                    pageable = new PageRequest(i, ElasticsearchConstant.QUERY_MAX_LIMIT);
                    searchQuery = builder.withIndices(ElasticsearchConstant.USER_INDEX)
                            .withTypes(ElasticsearchConstant.USER_TYPE)
                            .withPageable(pageable)
                            .build();
                    List<User> list
                            = elasticsearchTemplate.queryForList(searchQuery, User.class);
                    returnList.addAll(list);
                }
            }
            return returnList;
        }
        return null;
    }

    /**
     * <p> spring data elasticsearch 提供的入库，实时处理</p>
     * @param users 对象列表
     * @author youq  2019/4/10 16:46
     */
    public void dataSave(List<User> users) {
        userRepository.save(users);
    }

    /**
     * <p> bulkProcess 提供的入库，异步处理</p>
     * @param users 对象列表
     * @author youq  2019/4/10 16:46
     */
    public void bulkSave(List<User> users) {
        for (User user : users) {
            userBulkProcessor.add(
                    new IndexRequest(ElasticsearchConstant.USER_INDEX, ElasticsearchConstant.USER_TYPE)
                            .source(ObjectUtils.toJson(user))
            );
        }
    }

    /**
     * <p> 删除</p>
     * @param sex 性别
     * @author youq  2019/4/10 19:01
     */
    public void delete(SexEnum sex) {
        List<User> users = userRepository.findBySex(sex);
        if (!CollectionUtils.isEmpty(users)) {
            userRepository.delete(users);
        }
    }

}
