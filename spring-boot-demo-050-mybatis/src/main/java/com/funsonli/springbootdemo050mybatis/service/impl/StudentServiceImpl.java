package com.funsonli.springbootdemo050mybatis.service.impl;

import com.funsonli.springbootdemo050mybatis.entity.Student;
import com.funsonli.springbootdemo050mybatis.mapper.StudentMapper;
import com.funsonli.springbootdemo050mybatis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentMapper studentMapper;

    @Override
    public List<Student> index() {
        return studentMapper.list();
    }

    @Override
    public Integer save(Student student) {
        return studentMapper.save(student);
    }
}
