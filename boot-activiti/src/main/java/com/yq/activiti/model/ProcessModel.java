package com.yq.activiti.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p> 流程实体</p>
 * @author youq  2020/5/19 12:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessModel implements Serializable {

    private String id;

    private String name;

    private String imageUrl;

    private String param;

    private List<String> nodes;

}
