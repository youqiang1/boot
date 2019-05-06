package com.yq.es.rest.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yq.es.rest.entity.User;
import com.yq.es.rest.model.ChildHitModel;
import com.yq.es.rest.model.EsSearchResultModel;
import com.yq.kernel.constants.ElasticsearchConstant;
import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.model.ResultData;
import com.yq.kernel.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

/**
 * <p> 测试</p>
 * @author youq  2019/5/5 18:01
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    private RestClient restClient;

    @RequestMapping("/test")
    public ResultData<?> test() {
        try {
            User user = User.builder()
                    .username("youq")
                    .password("123456")
                    .age(28)
                    .sex(SexEnum.MALE)
                    .id("1")
                    .build();
            HttpEntity entity = new NStringEntity(ObjectUtils.toJson(user), ContentType.APPLICATION_JSON);
            Response indexResponse = restClient.performRequest(ElasticsearchConstant.METHOD_PUT,
                    "/user_index/user_type/1", Collections.emptyMap(), entity);
            Response response = restClient.performRequest(ElasticsearchConstant.METHOD_GET,
                    "/user_index/user_type/1", ElasticsearchConstant.RESULT_JSON_FORMAT);
            log.info(EntityUtils.toString(indexResponse.getEntity()));
            log.info(EntityUtils.toString(response.getEntity()));
            return ResultData.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResultData.failMsg(e.getMessage());
        }
    }

    @RequestMapping("/api")
    public ResultData<?> api() throws Exception {
        String endpoint = "/test*";
        Response response = restClient.performRequest(
                ElasticsearchConstant.METHOD_GET, endpoint, ElasticsearchConstant.RESULT_JSON_FORMAT);
        return ResultData.success(EntityUtils.toString(response.getEntity()));
    }

    @RequestMapping("/createIndex")
    public ResultData<?> createIndex(String index) throws IOException {
        Response response = restClient.performRequest(ElasticsearchConstant.METHOD_PUT, "/" + index);
        return ResultData.success(EntityUtils.toString(response.getEntity()));
    }

    @RequestMapping("/createDocument")
    public ResultData<?> createDocument(String index, String type, String id) throws IOException {
        String endpoint = "/" + index + "/" + type + "/" + id;
        User user = User.builder()
                .username("youq" + id)
                .password("123456")
                .age(28)
                .sex(SexEnum.MALE)
                .createTime(new Date())
                .phone("130xxxxxxxx")
                .email("xxx@qq.com")
                .id(id)
                .build();
        HttpEntity entity = new NStringEntity(ObjectUtils.toJson(user), ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(ElasticsearchConstant.METHOD_PUT, endpoint, Collections.emptyMap(), entity);
        return ResultData.success(EntityUtils.toString(response.getEntity()));
    }

    @RequestMapping("/getDocument")
    public ResultData<?> getDocument(String index, String type, String id) throws IOException {
        String endpoint = "/" + index + "/" + type + "/" + id;
        Response response = restClient.performRequest(ElasticsearchConstant.METHOD_GET, endpoint);
        return ResultData.success(EntityUtils.toString(response.getEntity()));
    }

    @RequestMapping("/findAll")
    public ResultData<?> findAll(String index, String type) throws IOException {
        String endpoint = "/" + index + "/" + type + "/_search";
        HttpEntity entity = new NStringEntity("{\"query\":{\"match_all\":{}}}", ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                ElasticsearchConstant.METHOD_POST, endpoint, Collections.emptyMap(), entity);
        String resultEntity = EntityUtils.toString(response.getEntity());
        EsSearchResultModel resultModel = ObjectUtils.fromJson(resultEntity, EsSearchResultModel.class, User.class);
        List hits = resultModel.getHits().getHits();
        return ResultData.success(hits);
    }

    /**
     * <p> 根据id查询数据，其他查询修改查询语法</p>
     * @param index IndexName
     * @param type  TypeName
     * @param id    查询条件
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/5/6 11:47
     */
    @RequestMapping("/findById")
    public ResultData<?> findById(String index, String type, String id) throws IOException {
        String endpoint = "/" + index + "/" + type + "/_search";
        HttpEntity entity = new NStringEntity("{\"query\":{\"match\":{\"id\":\"" + id + "\"}}}", ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                ElasticsearchConstant.METHOD_POST, endpoint, Collections.emptyMap(), entity);
        String resultEntity = EntityUtils.toString(response.getEntity());
        EsSearchResultModel resultModel = ObjectUtils.fromJson(resultEntity, EsSearchResultModel.class, User.class);
        ChildHitModel<User> hitModel = (ChildHitModel<User>) resultModel.getHits().getHits().get(0);
        return ResultData.success(hitModel.get_source());
    }

    @RequestMapping("/update")
    public ResultData<?> update(String index, String type, String id) throws IOException {
        String endpoint = "/" + index + "/" + type + "/" + id + "/_update";
        HttpEntity entity = new NStringEntity("{\"doc\":{\"username\":\"youq02\"}}", ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                ElasticsearchConstant.METHOD_POST, endpoint, Collections.emptyMap(), entity);
        String resultEntity = EntityUtils.toString(response.getEntity());
        JSON json = JSON.parseObject(resultEntity);
        return ResultData.success(json);
    }

    @RequestMapping("/isExist")
    public ResultData<?> isExist(String index, String type) throws IOException {
        String endpoint = "/" + index + "/" + type;
        Response response = restClient.performRequest(ElasticsearchConstant.METHOD_HEAD, endpoint, Collections.emptyMap());
        log.info("statusLine: {}", response.getStatusLine());
        return response.getStatusLine().getStatusCode() == 200 ? ResultData.success("存在") : ResultData.success("不存在");
    }

    @RequestMapping("/delete")
    public ResultData<?> delete(String index, String type, String id) throws IOException {
        String endpoint = "/" + index + "/" + type + "/_query";
        Map<String, String> params = new HashMap<>();
        params.put("q", "id:" + id);
        params.put("pretty", "true");
        Response response = restClient.performRequest(ElasticsearchConstant.METHOD_DELETE, endpoint, params);
        JSONObject json = JSON.parseObject(EntityUtils.toString(response.getEntity()));
        log.info("删除返回：{}", json);
        JSONObject indices = json.getJSONObject("_indices");
        JSONObject indexResult = indices.getJSONObject(index);
        return ResultData.success(indexResult != null && indexResult.getInteger("deleted") > 0);
    }

}
