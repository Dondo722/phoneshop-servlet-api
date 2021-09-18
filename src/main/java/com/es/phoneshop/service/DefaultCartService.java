package com.es.phoneshop.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;


import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService{
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private static final Object instanceLock = new Object();
    private static volatile DefaultCartService instance;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private DefaultCartService() {
    }

    public static DefaultCartService getInstance() {
        if (instance == null) {
            synchronized (instanceLock) {
                if (instance == null) {
                    instance = new DefaultCartService();
                }
            }
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        readLock.lock();
        try {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            return cart;
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public synchronized void add(Cart cart, Product product, int quantity) throws OutOfStockException {
        writeLock.lock();
        try {
            int quantityCurrent = cart.getItems().stream()
                    .filter(i -> i.getProduct().equals(product))
                    .findAny()
                    .map(c -> c.getQuantity() + quantity)
                    .orElse(quantity);

            if (product.getStock() < quantityCurrent) {
                throw new OutOfStockException(product, quantity);
            }
            if (quantityCurrent == quantity) {
                cart.addItem(new CartItem(product, quantity));
            } else {
                CartItem item = cart.getCartItemByProduct(product);
                item.setQuantity(quantityCurrent);
            }
        } finally {
            writeLock.unlock();
        }
    }
}
