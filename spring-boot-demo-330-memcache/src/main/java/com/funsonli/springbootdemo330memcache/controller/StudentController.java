package com.funsonli.springbootdemo330memcache.controller;

import net.rubyeye.xmemcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Student Controller
 *
 * @author Funson
 * @date 2019/10/12
 */

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private MemcachedClient memcachedClient;

    @GetMapping({"", "/", "index"})
    public String index() {
        try {
            String id = memcachedClient.get("id");
            if (id != null) {
                return "view id: " + id;
            } else {
                return "please view id first. link: /student/view/1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "please view id first. link: /student/view/1";
    }

    @GetMapping("/view/{id}")
    public String view(HttpServletRequest request, @PathVariable String id) {
        try {
            memcachedClient.set("id", 0, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
