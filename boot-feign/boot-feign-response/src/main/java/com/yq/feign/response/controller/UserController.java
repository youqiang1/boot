package com.yq.feign.response.controller;

import com.yq.feign.response.entity.User;
import com.yq.feign.response.service.UserService;
import com.yq.feign.service.feign.UserFeignService;
import com.yq.feign.service.feign.qo.SaveUserQO;
import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p> 用户controller</p>
 * @author youq  2019/4/12 10:52
 */
@Slf4j
@RestController
public class UserController implements UserFeignService {

    @Autowired
    private UserService userService;

    /**
     * <p> 接收request请求，保存用户信息</p>
     * @param qo 请求参数
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/4/12 11:01
     */
    @Override
    public ResultData<?> saveUser(@RequestBody SaveUserQO qo) {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        BeanUtils.copyProperties(qo, user);
        user.setCreateTime(now);
        user.setLastModified(now);
        user.setRemove(false);
        userService.save(user);
        return ResultData.success();
    }

}
