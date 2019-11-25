package com.funsonli.springbootdemo151jpamultisource.service;


import com.funsonli.springbootdemo151jpamultisource.entity.user.Student;

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
