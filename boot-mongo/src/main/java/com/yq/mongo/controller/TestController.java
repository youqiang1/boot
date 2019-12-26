package com.yq.mongo.controller;

import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.model.ResultData;
import com.yq.kernel.utils.snowflake.SnowFlake;
import com.yq.mongo.dao.UserDao;
import com.yq.mongo.entity.Address;
import com.yq.mongo.entity.TestUser;
import com.yq.mongo.request.TestUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p> 测试</p>
 * @author youq  2019/12/26 13:34
 */
@RestController
public class TestController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("/countAge")
    public ResultData<?> countAge() {
        return ResultData.success(userDao.countAge());
    }

    @RequestMapping("/findUserAndAddress")
    public ResultData<?> findUserAndAddress() {
        return ResultData.success(userDao.findUserAndAddress());
    }

    @RequestMapping("/findPage")
    public ResultData<?> findPage(Integer page, Long id, String name, SexEnum sex, Integer minAge, Integer maxAge, String introduce) {
        Pageable pageable = PageRequest.of(page, 5, new Sort(Sort.Direction.DESC, "id"));
        TestUserRequest request = TestUserRequest.builder()
                .id(id)
                .name(name)
                .introduce(introduce)
                .sex(sex)
                .minAge(minAge)
                .maxAge(maxAge)
                .build();
        return ResultData.success(userDao.findPage(pageable, request));
    }

    @RequestMapping("/findByName")
    public ResultData<?> findByName(String name) {
        return ResultData.success(userDao.findByName(name));
    }

    @RequestMapping("/findAll")
    public ResultData<?> findAll() {
        List<TestUser> list = userDao.findAll();
        return ResultData.success(list);
    }

    @RequestMapping("/saveAddress")
    public ResultData<?> saveAddress(Long userId, String address) {
        Address ad = new Address();
        ad.setId(new SnowFlake(1, 1).nextId());
        ad.setUserId(userId);
        ad.setAddress(address);
        userDao.saveAddress(ad);
        return ResultData.success();
    }

    @RequestMapping("/save")
    public ResultData<?> save(String name, SexEnum sex, Integer age, String introduce) {
        TestUser user = new TestUser();
        user.setId(new SnowFlake(1, 1).nextId());
        user.setName(name);
        user.setSex(sex);
        user.setAge(age);
        user.setIntroduce(introduce);

        userDao.save(user);
        return ResultData.success();
    }

    @RequestMapping("/update")
    public ResultData<?> update(Long id, String name, Integer age, String introduce) {
        TestUser user = new TestUser();
        user.setId(id);
        user.setName(name);
        user.setAge(age);
        user.setIntroduce(introduce);
        userDao.update(user);
        return ResultData.success();
    }

    @RequestMapping("/delete")
    public ResultData<?> delete(Long id) {
        userDao.delete(id);
        return ResultData.success();
    }

}
