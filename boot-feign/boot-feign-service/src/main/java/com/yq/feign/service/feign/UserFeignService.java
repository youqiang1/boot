package com.yq.feign.service.feign;

import com.yq.feign.service.feign.qo.SaveUserQO;
import com.yq.kernel.model.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p> boot-feign-response对外提供的用户接口</p>
 * @author youq  2019/4/12 10:19
 */
@FeignClient(value = "BOOT-FEIGN-RESPONSE", fallback = UserFeignServiceFallBack.class)
public interface UserFeignService {

    /**
     * <p> 用户添加</p>
     * @param qo 请求参数
     * @return com.yq.kernel.model.ResultData<?>
     * @author youq  2019/4/12 10:54
     */
    @PostMapping(value = "/inner/user/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultData<?> saveUser(@RequestBody SaveUserQO qo);

}
