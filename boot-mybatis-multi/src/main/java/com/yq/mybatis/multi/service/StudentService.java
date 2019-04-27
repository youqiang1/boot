package com.yq.mybatis.multi.service;

import com.yq.mybatis.multi.entity.Student;
import com.yq.mybatis.multi.mapper.db1.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p> 学生信息Service</p>
 * @author youq  2019/4/27 17:59
 */
@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Transactional("masterTransactionManager")
    public void save(String name, String studentNo) {
        Student student = new Student();
        student.setName(name);
        student.setStudentNo(studentNo);
        studentMapper.save(student);
    }

    public Student findOne(Integer id) {
        return studentMapper.findOne(id);
    }

}
