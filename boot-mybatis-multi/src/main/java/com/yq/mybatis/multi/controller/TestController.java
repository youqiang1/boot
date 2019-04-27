package com.yq.mybatis.multi.controller;

import com.yq.kernel.model.ResultData;
import com.yq.mybatis.multi.entity.School;
import com.yq.mybatis.multi.entity.Student;
import com.yq.mybatis.multi.service.SchoolService;
import com.yq.mybatis.multi.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试</p>
 * @author youq  2019/4/27 18:02
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/test")
    public ResultData<?> test() {
        schoolService.save("南昌大学", "江西");
        studentService.save("youq", "56435156435");
        return ResultData.success();
    }

    @GetMapping("/test2")
    public ResultData<?> test(Integer schoolId, Integer studentId) {
        School school = schoolService.findOne(schoolId);
        Student student = studentService.findOne(studentId);
        log.info("school: {}", school);
        log.info("student: {}", student);
        return ResultData.success();
    }

}
