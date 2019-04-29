package com.yq.sharding.mybatis.mapper;

import com.yq.sharding.mybatis.entity.Address;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p> address mapper</p>
 * @author youq  2019/4/29 9:59
 */
@Mapper
public interface AddressMapper {

    /**
     * <p> 保存</p>
     * @param address 地址信息
     * @author youq  2019/4/29 10:04
     */
    void save(Address address);

    /**
     * <p> findOne</p>
     * @param id 地址信息id
     * @return com.yq.sharding.mybatis.entity.Address
     * @author youq  2019/4/29 10:04
     */
    Address findOne(Long id);

}
