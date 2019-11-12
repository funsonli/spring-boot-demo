package com.funsonli.springbootdemo100session.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Student Controller
 *
 * @author Funson
 * @date 2019/10/12
 */

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @GetMapping({"", "/", "index"})
    public String index(HttpSession session) {

        Object value = session.getAttribute("guest");

        return "guest=" + value.toString();
    }

    @GetMapping("/add/{product}/{number}")
    public String add(HttpServletRequest request, @PathVariable String product, @PathVariable Integer number, HttpSession session) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().contains("JSESSION")) {
                    log.info(cookie.getName() + "=" + cookie.getValue());
                }
            }
        }

        Object oldValue = session.getAttribute("guest");
        Object value = "{product: '" + product + "', number: " + number + "}";
        session.setAttribute("guest", "{product: '" + product + "', number: " + number + "}");

        return "add success, 请刷新或者访问 /index value: " + value + " old value: " + oldValue;
    }
}
