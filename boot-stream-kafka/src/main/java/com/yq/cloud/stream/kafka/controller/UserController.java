package com.yq.cloud.stream.kafka.controller;

import com.yq.cloud.stream.kafka.model.UserModel;
import com.yq.cloud.stream.kafka.service.UserService;
import com.yq.kernel.constants.GlobalConstants;
import com.yq.kernel.enu.SexEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p> 用户controller</p>
 * @author youq  2019/4/10 15:08
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/send")
    public String send() {
        UserModel user = UserModel.builder()
                .id(1)
                .createTime(LocalDateTime.now())
                .username("youq")
                .password("123456")
                .sex(SexEnum.MALE)
                .age(28)
                .build();
        userService.send(user);
        return GlobalConstants.SUCCESS;
    }

}
