package com.es.phoneshop.dao;

import com.es.phoneshop.dao.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private static final Object instanceLock = new Object();
    private static volatile OrderDao instance;
    private List<Order> orderList;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private long currentId;

    private ArrayListOrderDao() {
        orderList = new ArrayList<>();
    }

    public static OrderDao getInstance() {
        if (instance == null) {
            synchronized (instanceLock) {
                if (instance == null) {
                    instance = new ArrayListOrderDao();
                }
            }
        }
        return instance;
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        readLock.lock();
        try {
            return orderList.stream()
                    .filter(p -> p.getId().equals(id))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void save(Order order) {
        writeLock.lock();
        try {
            order.setId(++currentId);
            orderList.add(order);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Order getOrderBySecureId(String id) throws OrderNotFoundException {
        readLock.lock();
        try {
            return orderList.stream()
                    .filter(p -> p.getSecureId().equals(id))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            readLock.unlock();
        }
    }
}
