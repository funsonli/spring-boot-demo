package com.funsonli.springbootdemo090blog.dao;

import com.funsonli.springbootdemo090blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
@Repository
public interface PostDao extends JpaRepository<Post, String>, JpaSpecificationExecutor<Post> {
}
