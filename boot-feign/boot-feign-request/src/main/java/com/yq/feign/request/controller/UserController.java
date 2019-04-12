package com.yq.feign.request.controller;

import com.yq.feign.service.feign.UserFeignService;
import com.yq.feign.service.feign.qo.SaveUserQO;
import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.model.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 用户controller</p>
 * @author youq  2019/4/12 10:15
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserFeignService userFeignService;

    @GetMapping("/save")
    public ResultData<?> save(String username) {
        SaveUserQO qo = SaveUserQO.builder()
                .username(username)
                .password("123123")
                .age(28)
                .sex(SexEnum.MALE)
                .phone("13000000000")
                .email("123@feign.com")
                .build();
        userFeignService.saveUser(qo);
        return ResultData.success();
    }

}
