package com.funsonli.springbootdemo151jpamultisource.service.impl;

import com.funsonli.springbootdemo151jpamultisource.dao.user.StudentDao;
import com.funsonli.springbootdemo151jpamultisource.entity.user.Student;
import com.funsonli.springbootdemo151jpamultisource.service.StudentService;
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
        return studentDao.findAll();
    }

    @Override
    public Student save(Student student) {
        return studentDao.save(student);
    }
}
