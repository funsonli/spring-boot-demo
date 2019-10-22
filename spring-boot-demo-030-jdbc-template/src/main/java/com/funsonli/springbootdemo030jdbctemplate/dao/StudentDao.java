package com.funsonli.springbootdemo030jdbctemplate.dao;

import com.funsonli.springbootdemo030jdbctemplate.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
@Repository
public class StudentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Student> list() {
        List<Student> list = jdbcTemplate.query("select * from student", new Object[]{}, new BeanPropertyRowMapper(Student.class));
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    public int save(Student student) {
        return jdbcTemplate.update("insert into student(id, name, age) values(?, ?, ?)", student.getId(), student.getName(), student.getAge());
    }

}
