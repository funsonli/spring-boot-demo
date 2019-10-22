package com.funsonli.springbootdemo030jdbctemplate.service.impl;

import com.funsonli.springbootdemo030jdbctemplate.dao.StudentDao;
import com.funsonli.springbootdemo030jdbctemplate.entity.Student;
import com.funsonli.springbootdemo030jdbctemplate.service.StudentService;
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
    StudentDao studentDao;

    @Override
    public List<Student> index() {
        return studentDao.list();
    }

    @Override
    public Integer save(Student student) {
        return studentDao.save(student);
    }
}
