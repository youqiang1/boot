package com.yq.mongo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p> 地址信息表</p>
 * @author youq  2019/12/26 16:24
 */
@Data
@Document(collection = "address")
public class Address {

    @Id
    private Long id;

    private Long userId;

    private String address;

}
