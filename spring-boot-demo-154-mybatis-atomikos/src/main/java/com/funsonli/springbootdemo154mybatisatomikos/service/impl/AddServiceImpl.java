package com.funsonli.springbootdemo154mybatisatomikos.service.impl;

import com.funsonli.springbootdemo154mybatisatomikos.entity.Order;
import com.funsonli.springbootdemo154mybatisatomikos.entity.Student;
import com.funsonli.springbootdemo154mybatisatomikos.mapper.order.OrderMapper;
import com.funsonli.springbootdemo154mybatisatomikos.mapper.user.StudentMapper;
import com.funsonli.springbootdemo154mybatisatomikos.service.AddService;
import com.funsonli.springbootdemo154mybatisatomikos.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
@Service
public class AddServiceImpl implements AddService {
    @Autowired
    StudentMapper studentMapper;

    @Autowired
    OrderMapper orderMapper;

    @Override
    @Transactional
    public void add(String name) {
        Student student = new Student();
        String studentId = String.valueOf(SnowFlake.getInstance().nextId());
        student.setId(studentId);
        student.setName(name);
        student.setAge(18);
        studentMapper.save(student);

        if ("0".equals(name)) {
            int k = 10 / Integer.valueOf(name);
        }

        Order order = new Order();
        order.setStudentId(studentId);
        order.setId(String.valueOf(SnowFlake.getInstance().nextId()));
        order.setSn(UUID.randomUUID().toString().replace("-", ""));
        order.setAmount(0);
        orderMapper.save(order);
    }
}
