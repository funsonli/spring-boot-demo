package com.funsonli.springbootdemo310mongodb.dao;

import com.funsonli.springbootdemo310mongodb.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
@Component
public interface StudentDao extends MongoRepository<Student, String> {
    public List<Student> findByName(String name);
    public List<Student> findByAge(Integer type);

}
