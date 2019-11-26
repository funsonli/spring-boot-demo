package com.funsonli.springbootdemo154mybatisatomikos.mapper.user;

import com.funsonli.springbootdemo154mybatisatomikos.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
@Mapper
public interface StudentMapper {
    @Select("SELECT * FROM student")
    List<Student> list();

    int save(@Param("student") Student student);
}
