package com.funsonli.springbootdemo151jpamultisource.dao.order;

import com.funsonli.springbootdemo151jpamultisource.entity.order.Order;
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
public interface OrderDao extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

}
