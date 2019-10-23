package com.funsonli.springbootdemo301rediscache.dao;

import com.funsonli.springbootdemo301rediscache.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
@Repository
public interface StudentDao extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {

}
