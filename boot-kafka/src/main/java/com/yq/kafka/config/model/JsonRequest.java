package com.yq.kafka.config.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> user</p>
 *
 * @author youq  2020/5/19 下午9:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonRequest implements Serializable {

    private String type;

    private String code;

    private JsonResultRequest result;

}