package com.funsonli.springbootdemo152mybatismultisource.service.impl;

import com.funsonli.springbootdemo152mybatismultisource.entity.Order;
import com.funsonli.springbootdemo152mybatismultisource.mapper.order.OrderMapper;
import com.funsonli.springbootdemo152mybatismultisource.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Override
    public List<Order> index() {
        return orderMapper.list();
    }

    @Override
    public Integer save(Order order) {
        return orderMapper.save(order);
    }
}
