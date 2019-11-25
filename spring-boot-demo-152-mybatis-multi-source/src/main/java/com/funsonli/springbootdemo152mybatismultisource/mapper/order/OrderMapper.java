package com.funsonli.springbootdemo152mybatismultisource.mapper.order;

import com.funsonli.springbootdemo152mybatismultisource.entity.Order;
import com.funsonli.springbootdemo152mybatismultisource.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/14
 */
@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM tbl_order")
    List<Order> list();

    int save(@Param("order") Order model);
}
