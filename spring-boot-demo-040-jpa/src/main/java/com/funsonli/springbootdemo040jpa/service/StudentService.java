package com.funsonli.springbootdemo040jpa.service;

import com.funsonli.springbootdemo040jpa.entity.Student;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
public interface StudentService {
    List<Student> index();
    Student save(Student student);
}
