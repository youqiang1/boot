package com.yq.es.controller;

import com.google.common.collect.Lists;
import com.yq.es.controller.qo.UserQO;
import com.yq.es.entity.User;
import com.yq.es.service.UserService;
import com.yq.kernel.constants.GlobalConstants;
import com.yq.kernel.enu.SexEnum;
import com.yq.kernel.model.ListViewData;
import com.yq.kernel.model.ResultData;
import com.yq.kernel.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p> 用户Controller</p>
 * @author youq  2019/4/10 16:39
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/delete")
    public ResultData<?> delete(String sex) {
        userService.delete(SexEnum.valueOf(sex));
        return ResultData.success(GlobalConstants.SUCCESS);
    }

    @GetMapping("/findAllByData")
    public ResultData<?> findAllByData(Integer page, Integer size) {
        List<User> users = userService.findAllByData(page, size);
        log.info("user.size(): {}", users.size());
        return ResultData.success(ObjectUtils.toJson(users));
    }

    @GetMapping("/findAllByTemplate")
    public ResultData<?> findAllByTemplate() {
        List<User> users = userService.findAllByTemplate();
        log.info("user.size(): {}", users.size());
        return ResultData.success(users);
    }

    @GetMapping("/findPageByTemplate")
    public ResultData<?> findPageByTemplate(Integer page, Integer size) {
        //当前时间
        Date curr = new Date();
        //一天前
        Calendar cal = Calendar.getInstance();
        cal.setTime(curr);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date beginTime = cal.getTime();
        //请求参数
        UserQO qo = UserQO.builder()
                .username("youq")
                .email("123")
                .phone("13000000000")
                .beginAge(10)
                .endAge(30)
                .sex(SexEnum.MALE)
                .beginTime(beginTime)
                .endTime(curr)
                .page(page)
                .size(size)
                .build();
        //查询
        Page<User> userPage = userService.findPageByTemplate(qo);
        if (userPage != null) {
            ListViewData<User> listViewData = new ListViewData<>();
            listViewData.setTotal(userPage.getTotalElements());
            listViewData.setList(userPage.getContent());
            return ResultData.success(listViewData);
        } else {
            return ResultData.fail();
        }
    }

    /**
     * <p> spring data elasticsearch 提供的入库，实时处理</p>
     * @author youq  2019/4/10 16:46
     */
    @GetMapping("/dataSave")
    public ResultData<?> dataSave() {
        List<User> users = buildUsers();
        userService.dataSave(users);
        return ResultData.success();
    }

    /**
     * <p> bulkProcess 提供的入库，异步处理</p>
     * @author youq  2019/4/10 16:46
     */
    @GetMapping("/bulkSave")
    public ResultData<?> bulkSave() {
        List<User> users = buildUsers();
        userService.bulkSave(users);
        return ResultData.success();
    }

    private List<User> buildUsers() {
        Date date = new Date();
        int userSize = 70;
        List<User> users = Lists.newArrayListWithCapacity(userSize);
        for (int i = 0; i < userSize; i++) {
            User user = User.builder()
                    .createTime(date)
                    .username("youq" + (i + 1))
                    .password("123456")
                    .sex(SexEnum.MALE)
                    .age(i)
                    .phone("13000000000")
                    .email("123@wd.com")
                    .build();
            users.add(user);
        }
        return users;
    }

}
