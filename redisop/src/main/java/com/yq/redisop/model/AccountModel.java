package com.yq.redisop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> 账户信息</p>
 * @author youq  2019/4/29 16:44
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel implements Serializable {

    /**
     * 账户
     */
    private String account;

    /**
     * 账户增量
     */
    private Long increment;

}
