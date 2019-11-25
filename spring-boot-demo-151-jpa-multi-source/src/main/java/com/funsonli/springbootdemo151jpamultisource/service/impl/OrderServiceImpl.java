package com.funsonli.springbootdemo151jpamultisource.service.impl;

import com.funsonli.springbootdemo151jpamultisource.dao.order.OrderDao;
import com.funsonli.springbootdemo151jpamultisource.entity.order.Order;
import com.funsonli.springbootdemo151jpamultisource.service.OrderService;
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
    OrderDao modelDao;

    @Override
    public List<Order> index() {
        return modelDao.findAll();
    }

    @Override
    public Order save(Order model) {
        return modelDao.save(model);
    }
}
