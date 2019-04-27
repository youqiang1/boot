package com.yq.mybatis.multi.mapper.db1;

import com.yq.mybatis.multi.entity.Student;

/**
 * <p> 学生信息Mapper</p>
 * @author youq  2019/4/27 17:55
 */
public interface StudentMapper {

    void save(Student student);

    Student findOne(Integer id);

}
