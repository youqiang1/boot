package com.yq.chain.model;

import com.yq.kernel.enu.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.chain.impl.ContextBase;

/**
 * <p> 用户信息</p>
 * @author youq  2019/8/19 16:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContext extends ContextBase {

    private String id;

    private String name;

    private String age;

    private SexEnum sex;

}
