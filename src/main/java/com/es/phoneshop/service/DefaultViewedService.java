package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ViewedHistory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultViewedService implements ViewedService{
    private static final String VIEWED_PRODUCTS_SESSION_ATTRIBUTE = DefaultViewedService.class.getName() + ".viewed";
    private static final Object instanceLock = new Object();
    private static volatile DefaultViewedService instance;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public static DefaultViewedService getInstance() {
        if (instance == null) {
            synchronized (instanceLock) {
                if (instance == null) {
                    instance = new DefaultViewedService();
                }
            }
        }
        return instance;
    }

    private DefaultViewedService() {
    }

    @Override
    public ViewedHistory getViewedHistory(HttpServletRequest request) {
        readLock.lock();
        try {
            ViewedHistory viewedHistory = (ViewedHistory) request.getSession().getAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE);
            if (viewedHistory == null) {
                viewedHistory = new ViewedHistory();
                request.getSession().setAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE, viewedHistory);
            }
            return viewedHistory;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * product goes to cache, product that was cached goes to viewed
     */
    @Override
    public synchronized void addProduct(ViewedHistory viewedHistory, Product product) {
        writeLock.lock();
        try {
            List<Product> viewed = viewedHistory.getHistory();
            Product cachedProduct = viewedHistory.getCache();
            if (cachedProduct != product) {
                viewedHistory.setCache(product);
                if (cachedProduct == null) {
                    return;
                }
                if (!viewed.contains(cachedProduct)) {
                    viewed.add(0, cachedProduct);
                }
                if (viewed.size() > 3) {
                    viewed.remove(3);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }
}
