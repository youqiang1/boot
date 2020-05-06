package com.yq.swagger.controller.vo;

import com.yq.kernel.enu.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 用户信息</p>
 * @author youq  2019/4/27 11:15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "user", description = "请求入参 user")
public class UserVO {

    @ApiModelProperty(value = "用户名", notes = "用户名", name = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "性别")
    private SexEnum sex;

}
