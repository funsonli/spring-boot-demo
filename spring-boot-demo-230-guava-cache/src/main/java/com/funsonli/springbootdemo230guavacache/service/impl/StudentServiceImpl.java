package com.funsonli.springbootdemo230guavacache.service.impl;

import com.funsonli.springbootdemo230guavacache.dao.StudentDao;
import com.funsonli.springbootdemo230guavacache.entity.Student;
import com.funsonli.springbootdemo230guavacache.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentDao studentDao;

    @Override
    public List<Student> index() {
        return studentDao.findAll();
    }

    @Override
    @CachePut(value = "student", key = "#student.id")
    public Student save(Student student) {
        log.info("inser to mysql: " + student.getId());
        return studentDao.save(student);
    }

    @Override
    @Cacheable(value = "student", key = "#id")
    public Optional<Student> findById(String id) {
        log.info("find from mysql: " + id);
        return studentDao.findById(id);
    }

    @Override
    @CacheEvict(value = "student", key = "#id")
    public void deleteById(String id) {
        log.info("delete from mysql: " + id);
        studentDao.deleteById(id);
    }
}
