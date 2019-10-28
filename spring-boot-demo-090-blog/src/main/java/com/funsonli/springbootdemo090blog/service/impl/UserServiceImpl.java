package com.funsonli.springbootdemo090blog.service.impl;

import com.funsonli.springbootdemo090blog.dao.UserDao;
import com.funsonli.springbootdemo090blog.entity.User;
import com.funsonli.springbootdemo090blog.service.UserService;
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
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public List<User> index() {
        return userDao.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User save(User model) {
        return userDao.save(model);
    }
}
