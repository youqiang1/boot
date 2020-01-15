package com.yq.tools.service;

import com.google.common.collect.Lists;
import com.yq.kernel.constants.ElasticsearchConstant;
import com.yq.tools.entity.VoiceModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> 语音数据服务</p>
 * @author youq  2020/1/11 13:26
 */
@Slf4j
@Service
public class VoiceService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * <p> 查询话单数据</p>
     * @param beginTime     开始时间
     * @param endTime       结束时间
     * @param valid         是否只查询有效话单
     * @param callerIp        主叫IP
     * @param voice1119Node 节点
     * @return java.util.List<com.yq.tools.entity.VoiceModel>
     * @author youq  2020/1/11 13:57
     */
    public List<VoiceModel> findByTime(String beginTime, String endTime, boolean valid, String callerIp, String voice1119Node) {
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        bqb.must(
                QueryBuilders.rangeQuery("releaseTime")
                        .from(beginTime).to(endTime)
                        .includeUpper(true).includeLower(false)
        );
        //查询通话成功的信息
        if (valid) {
            bqb.must(QueryBuilders.rangeQuery("talkDuration").gt(0));
        }
        //主叫
        if (StringUtils.isNotBlank(callerIp)) {
            bqb.must(QueryBuilders.matchPhraseQuery("callerIP", callerIp));
        }
        //节点
        if (StringUtils.isNotBlank(voice1119Node)) {
            bqb.must(QueryBuilders.matchPhraseQuery("node", voice1119Node));
        }
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        builder.withQuery(bqb);
        //一次最多只能查询10000条
        Pageable pageable = new PageRequest(0, ElasticsearchConstant.QUERY_MAX_LIMIT);
        NativeSearchQuery searchQuery = builder.withIndices(ElasticsearchConstant.VOICE_INDEX)
                .withTypes(ElasticsearchConstant.VOICE_TYPE)
                .withPageable(pageable)
                .build();
        Page<VoiceModel> page = elasticsearchTemplate.queryForPage(searchQuery, VoiceModel.class);
        if (page.getTotalElements() > 0) {
            return buildResultList(page, builder);
        } else {
            return null;
        }
    }

    /**
     * <p> build 返回集合</p>
     * @param page    Page对象
     * @param builder 查询条件 NativeSearchQueryBuilder
     * @return java.util.List<com.vcread.unioncloud.tenantcharge.entity.es.Voice1119Model>
     * @author youq  2019/2/23 11:54
     */
    private List<VoiceModel> buildResultList(Page<VoiceModel> page,
                                             NativeSearchQueryBuilder builder) {
        //结果集的total
        int total = (int) page.getTotalElements();
        List<VoiceModel> returnList = Lists.newArrayListWithCapacity(total);
        returnList.addAll(page.getContent());
        //如果数据量超过10000条，多次查询
        if (total > ElasticsearchConstant.QUERY_MAX_LIMIT) {
            int count = total % ElasticsearchConstant.QUERY_MAX_LIMIT == 0
                    ? total / ElasticsearchConstant.QUERY_MAX_LIMIT
                    : total / ElasticsearchConstant.QUERY_MAX_LIMIT + 1;
            for (int i = 1; i < count; i++) {
                Pageable pageable = new PageRequest(i, ElasticsearchConstant.QUERY_MAX_LIMIT);
                NativeSearchQuery searchQuery = builder.withIndices(ElasticsearchConstant.VOICE_INDEX)
                        .withTypes(ElasticsearchConstant.VOICE_TYPE)
                        .withPageable(pageable)
                        .build();
                List<VoiceModel> list = elasticsearchTemplate.queryForList(searchQuery, VoiceModel.class);
                returnList.addAll(list);
            }
        }
        return returnList;
    }

}
