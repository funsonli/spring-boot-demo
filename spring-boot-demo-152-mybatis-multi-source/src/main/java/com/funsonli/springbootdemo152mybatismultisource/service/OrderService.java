package com.funsonli.springbootdemo152mybatismultisource.service;


import com.funsonli.springbootdemo152mybatismultisource.entity.Order;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
public interface OrderService {
    List<Order> index();
    Integer save(Order order);
}
