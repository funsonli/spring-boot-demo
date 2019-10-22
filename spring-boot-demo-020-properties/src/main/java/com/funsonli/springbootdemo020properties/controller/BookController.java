package com.funsonli.springbootdemo020properties.controller;

import com.funsonli.springbootdemo020properties.controller.config.properties.BookProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Book Controller
 *
 * @author Funson
 * @date 2019/10/12
 */

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookProperties bookProperties;

    @GetMapping({"/", "index"})
    public String index() {
        return "book name: " + bookProperties.getName() + " and price: " + bookProperties.getPrice() + " and authors: " + bookProperties.getAuthors().toString();
    }
}
