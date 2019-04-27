package com.yq.mybatis.multi.service;

import com.yq.mybatis.multi.entity.School;
import com.yq.mybatis.multi.mapper.db2.SchoolMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p> 学校信息Service</p>
 * @author youq  2019/4/27 17:56
 */
@Service
public class SchoolService {

    @Autowired
    private SchoolMapper schoolMapper;

    @Transactional("slaveTransactionManager")
    public void save(String name, String province) {
        School school = new School();
        school.setName(name);
        school.setProvince(province);
        schoolMapper.save(school);
    }

    public School findOne(Integer id) {
        return schoolMapper.findOne(id);
    }

}
