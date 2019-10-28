package com.funsonli.springbootdemo090blog.service;

import com.funsonli.springbootdemo090blog.entity.Post;

import java.util.List;
import java.util.Optional;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
public interface PostService {
    List<Post> index();
    Post save(Post model);
    Post findById(String id);
}
