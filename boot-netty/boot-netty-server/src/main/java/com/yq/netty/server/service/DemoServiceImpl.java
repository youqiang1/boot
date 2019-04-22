package com.yq.netty.server.service;

import com.yq.netty.commons.entity.User;
import com.yq.netty.commons.exception.ErrorParamsException;
import com.yq.netty.rpc.DemoService;
import org.springframework.stereotype.Service;

/**
 * <p> 测试Service实现类</p>
 * @author youq  2019/4/19 19:53
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Override
    public double division(int numberA, int numberB) throws ErrorParamsException {
        if (numberB == 0) {
            throw new ErrorParamsException("除数不能为0");
        }
        return numberA / numberB;
    }

    @Override
    public User getUserInfo() {
        User leader = new User();
        leader.setId(1);
        leader.setName("上级");
        leader.setSource(100);
        User user = new User();
        user.setSource(80);
        user.setId(0);
        user.setName("基层");
        user.setLeader(leader);
        return user;
    }

    @Override
    public String print() {
        return "这是来自服务器中DemoService接口的print方法打印的消息";
    }

    @Override
    public int sum(int numberA, int numberB) {
        return numberA + numberB;
    }

}
