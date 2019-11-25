package com.funsonli.springbootdemo151jpamultisource.service;


import com.funsonli.springbootdemo151jpamultisource.entity.order.Order;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
public interface OrderService {
    List<Order> index();
    Order save(Order model);
}
