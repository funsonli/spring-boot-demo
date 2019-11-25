package com.funsonli.springbootdemo152mybatismultisource.service;


import com.funsonli.springbootdemo152mybatismultisource.entity.Student;

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
