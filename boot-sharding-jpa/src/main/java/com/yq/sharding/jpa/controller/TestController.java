package com.yq.sharding.jpa.controller;

import com.google.common.collect.Lists;
import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.model.ResultData;
import com.yq.kernel.utils.snowflake.SnowFlake;
import com.yq.sharding.jpa.entity.User;
import com.yq.sharding.jpa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p> 测试</p>
 * @author youq  2019/5/7 17:41
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/save")
    public ResultData<?> save() {
        List<User> users = Lists.newArrayList();
        SnowFlake snowFlake = new SnowFlake(1, 1);
        for (int i = 0; i < 100; i++) {
            User user = new User();
            LocalDateTime date = LocalDateTime.now();
            user.setId(snowFlake.nextId());
            user.setCreateTime(date);
            user.setLastModified(date);
            user.setName("jpa_youq" + i);
            user.setCityId(i % 2 == 0 ? 1 : 2);
            user.setSex(i % 2 == 0 ? SexEnum.MALE : SexEnum.FEMALE);
            user.setPhone("11111" + i);
            user.setEmail("136@163.com");
            user.setPassword("123456");
            users.add(user);
        }
        userRepository.saveAll(users);
        return ResultData.success();
    }

    @GetMapping("/findPage")
    public ResultData<?> findPage() {
        Pageable pageable = PageRequest.of(0,10, new Sort(Sort.Direction.ASC, "id"));
        Page<User> page = userRepository.findAll(pageable);
        return ResultData.success(page.getContent());
    }

    @GetMapping("/findOne")
    public ResultData<?> findOne(Long id) {
        Optional<User> optional = userRepository.findById(id);
        return ResultData.success(optional.get());
    }

}
