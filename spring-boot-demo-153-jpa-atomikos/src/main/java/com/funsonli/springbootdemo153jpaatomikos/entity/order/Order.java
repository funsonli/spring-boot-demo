package com.funsonli.springbootdemo153jpaatomikos.entity.order;

import com.funsonli.springbootdemo153jpaatomikos.util.SnowFlake;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class for
 *
 * @author Funson
 * @date 2019/10/12
 */
@Data
@Entity
@Table(name = "tbl_order")
public class Order {
    private static final long serialVersionUID = 1L;

    @Id
    private String id = String.valueOf(SnowFlake.getInstance().nextId());

    @Column(name="student_id")
    private String studentId;

    private String sn;

    private Integer amount;
}
