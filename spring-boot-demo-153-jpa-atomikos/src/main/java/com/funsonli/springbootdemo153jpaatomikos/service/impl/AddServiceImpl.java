package com.funsonli.springbootdemo153jpaatomikos.service.impl;

import com.funsonli.springbootdemo153jpaatomikos.dao.order.OrderDao;
import com.funsonli.springbootdemo153jpaatomikos.dao.user.StudentDao;
import com.funsonli.springbootdemo153jpaatomikos.entity.order.Order;
import com.funsonli.springbootdemo153jpaatomikos.entity.user.Student;
import com.funsonli.springbootdemo153jpaatomikos.service.AddService;
import com.funsonli.springbootdemo153jpaatomikos.util.SnowFlake;
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
    StudentDao studentDao;

    @Autowired
    OrderDao orderDao;

    @Override
    @Transactional
    public void add(String name) {
        Student student = new Student();
        String studentId = String.valueOf(SnowFlake.getInstance().nextId());
        student.setId(studentId);
        student.setName(name);
        student.setAge(18);
        studentDao.save(student);

        if ("0".equals(name)) {
            int k = 10 / Integer.valueOf(name);
        }

        Order order = new Order();
        order.setStudentId(studentId);
        order.setId(String.valueOf(SnowFlake.getInstance().nextId()));
        order.setSn(UUID.randomUUID().toString().replace("-", ""));
        order.setAmount(0);
        orderDao.save(order);
    }
}
