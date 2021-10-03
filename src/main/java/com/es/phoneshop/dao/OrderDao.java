package com.es.phoneshop.dao;

import com.es.phoneshop.dao.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;

    void save(Order order);

    Order getOrderBySecureId(String id) throws OrderNotFoundException;
}
