package com.yq.es.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * <p> ES批量录入处理器定义</p>
 * @author youq  2019/4/2 11:18
 */
@Slf4j
@Configuration
public class EsBulkProcessor {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Bean
    public BulkProcessor userBulkProcessor() {
        return BulkProcessor.builder(
                elasticsearchTemplate.getClient(),
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                        log.info("【EsBulkProcessor】入库开始，数量：{}", request.numberOfActions());
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                        log.info("【EsBulkProcessor】入库完成，数量：{}", request.numberOfActions());
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        log.error("【EsBulkProcessor】入库异常，数量：{}", request.numberOfActions(), failure);
                    }
                })
                //一次处理最大条数
                .setBulkActions(5000)
                //一次处理最大size
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                //flush间隔时间
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                //线程数
                .setConcurrentRequests(10)
                //重试时长和次数，只针对EsRejectedExecutionException
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueSeconds(30), 3))
                .build();
    }

}
