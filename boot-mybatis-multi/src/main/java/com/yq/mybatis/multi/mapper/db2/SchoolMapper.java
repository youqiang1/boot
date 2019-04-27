package com.yq.mybatis.multi.mapper.db2;

import com.yq.mybatis.multi.entity.School;

/**
 * <p> 学校信息Mapper</p>
 * @author youq  2019/4/27 17:56
 */
public interface SchoolMapper {

    void save(School school);

    School findOne(Integer id);

}
