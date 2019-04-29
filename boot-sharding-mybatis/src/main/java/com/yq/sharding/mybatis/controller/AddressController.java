package com.yq.sharding.mybatis.controller;

import com.yq.kernel.model.ResultData;
import com.yq.sharding.mybatis.entity.Address;
import com.yq.sharding.mybatis.mapper.AddressMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 地址信息controller</p>
 * @author youq  2019/4/29 10:15
 */
@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressMapper addressMapper;

    @RequestMapping("/save")
    public ResultData<?> save() {
        for (int i = 0; i < 10; i++) {
            Address address = new Address();
            address.setCode("code_" + i);
            address.setName("name_" + i);
            address.setPid(i + "");
            address.setType(0);
            address.setLit(i % 2 == 0 ? 1 : 2);
            addressMapper.save(address);
        }
        return ResultData.success();
    }

    @RequestMapping("/get/{id}")
    public ResultData<?> get(@PathVariable Long id) {
        Address address = addressMapper.findOne(id);
        return ResultData.success(address);
    }

}
