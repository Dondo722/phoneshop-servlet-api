package com.es.phoneshop.service;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private static final Object instanceLock = new Object();
    private static volatile DefaultOrderService instance;

    private static final OrderDao orderDao = ArrayListOrderDao.getInstance();

    public static DefaultOrderService getInstance() {
        if (instance == null) {
            synchronized (instanceLock) {
                if (instance == null) {
                    instance = new DefaultOrderService();
                }
            }
        }
        return instance;
    }

    @Override
    public synchronized Order getOrder(Cart cart) {
        Order order = new Order();
        order.getItems().addAll(cart.getItems().stream()
                .map(i -> new CartItem(i.getProduct(), i.getQuantity()))
                .collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        order.setTotalQuantity(cart.getTotalQuantity());
        return order;
    }

    @Override
    public synchronized List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public synchronized void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(5);
    }
}
