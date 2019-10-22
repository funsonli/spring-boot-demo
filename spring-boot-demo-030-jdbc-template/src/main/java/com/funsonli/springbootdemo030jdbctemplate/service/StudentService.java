package com.funsonli.springbootdemo030jdbctemplate.service;

import com.funsonli.springbootdemo030jdbctemplate.entity.Student;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
public interface StudentService {
    List<Student> index();
    Integer save(Student student);
}
