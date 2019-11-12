package com.funsonli.springbootdemo320elasticsearch.dao;

import com.funsonli.springbootdemo320elasticsearch.entity.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
public interface StudentDao extends ElasticsearchRepository<Student, String> {
    public List<Student> findByName(String name);
    public List<Student> findByAge(Integer type);
    public List<Student> findByAgeBetween(Integer min, Integer max);

}
