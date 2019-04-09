package com.yq.jpa.console.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 证书查询返回参数</p>
 * @author yq  2018/5/19 14:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateVO {

    /**
     * 证书编号
     */
    private String username;

    /**
     * 单位名称
     */
    private String name;

}
