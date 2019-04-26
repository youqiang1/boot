package com.yq.jpa.controller.qo;

import com.yq.jpa.common.SearchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 证书查询请求参数</p>
 * @author yq  2018/5/19 14:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateQO {

    /**
     * 查询类型
     */
    private SearchType searchType;

    /**
     * 查询条件
     */
    private String condition;

}
