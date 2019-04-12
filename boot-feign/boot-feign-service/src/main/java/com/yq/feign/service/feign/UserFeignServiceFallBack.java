package com.yq.feign.service.feign;

import com.yq.feign.service.feign.qo.SaveUserQO;
import com.yq.kernel.model.ResultData;
import org.springframework.stereotype.Component;

/**
 * <p> 失败回调</p>
 * @author youq  2019/4/12 10:20
 */
@Component
public class UserFeignServiceFallBack implements UserFeignService{

    @Override
    public ResultData<?> saveUser(SaveUserQO qo) {
        return ResultData.fail();
    }

}
