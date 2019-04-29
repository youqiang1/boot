package com.yq.redisop.service;

import com.yq.redisop.dao.AccountRedisDao;
import com.yq.redisop.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p> 账户redis service</p>
 * @author youq  2019/4/29 16:44
 */
@Service
public class AccountRedisService {

    @Autowired
    private AccountRedisDao accountRedisDao;

    public Long increment(AccountModel model) throws RuntimeException {
        if (model == null || StringUtils.isEmpty(model.getAccount()) || model.getIncrement() == null) {
            throw new RuntimeException("请求参数异常");
        }
        return accountRedisDao.increment(model);
    }

}
