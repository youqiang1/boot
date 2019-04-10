package com.yq.es.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yq.kernel.constants.ElasticsearchConstant;
import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.utils.IdGen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> user model</p>
 * @author youq  2019/4/9 15:43
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = ElasticsearchConstant.USER_INDEX, type = ElasticsearchConstant.USER_TYPE)
public class User implements Serializable {
    @Id
    @Builder.Default
    private String id = IdGen.DEFAULT.gen();
    /**
     * 创建时间
     */
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 用户名
     */
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String username;
    /**
     * 密码
     */
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String password;
    /**
     * 手机号
     */
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String phone;
    /**
     * 邮箱
     */
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String email;
    /**
     * 性别
     */
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private SexEnum sex;
    /**
     * 年龄
     */
    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer age;
}
