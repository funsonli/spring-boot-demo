package com.funsonli.springbootdemo090blog.controller;

import com.funsonli.springbootdemo090blog.entity.Post;
import com.funsonli.springbootdemo090blog.entity.User;
import com.funsonli.springbootdemo090blog.service.PostService;
import com.funsonli.springbootdemo090blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Site Controller
 *
 * @author Funson
 * @date 2019/10/12
 */

@Controller
public class SiteController {
    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @GetMapping({"", "/", "index"})
    public String index(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            request.setAttribute("user", user);
        }

        List<Post> posts = postService.index();
        request.setAttribute("posts", posts);

        return "site/index";
    }

    @GetMapping("/login")
    public String login() {
        return "site/login";
    }

    @PostMapping("/login")
    public String save(@ModelAttribute User modelAttribute, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "binding error";
        }

        User model = userService.findByUsername(modelAttribute.getUsername());
        if (model != null && model.getPassword().equals(modelAttribute.getPassword())) {
            request.getSession().setAttribute("user", modelAttribute);
        }

        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String view(HttpServletRequest request, @PathVariable String id) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            request.setAttribute("user", user);
        }

        Post post = postService.findById(id);
        request.setAttribute("post", post);

        return "site/post";
    }

    @GetMapping("/post/add")
    public String postAdd(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        } else {
            request.setAttribute("user", user);
        }

        return "site/post-add";
    }

    @PostMapping("/post/add")
    public String postSave(@ModelAttribute Post modelAttribute, BindingResult result, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        postService.save(modelAttribute);

        return "redirect:/";
    }

}
