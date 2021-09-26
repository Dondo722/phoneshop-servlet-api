package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ViewedHistory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class DefaultViewedService implements ViewedService{
    private static final String VIEWED_PRODUCTS_SESSION_ATTRIBUTE = DefaultViewedService.class.getName() + ".viewed";
    private static final Object instanceLock = new Object();
    private static final int VIEWED_MAX_SIZE = 3;
    private static volatile DefaultViewedService instance;

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
    public synchronized ViewedHistory getViewedHistory(HttpServletRequest request) {
        ViewedHistory viewedHistory = (ViewedHistory) request.getSession().getAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE);
        if (viewedHistory == null) {
            viewedHistory = new ViewedHistory();
            request.getSession().setAttribute(VIEWED_PRODUCTS_SESSION_ATTRIBUTE, viewedHistory);
        }
        return viewedHistory;
    }

    /**
     * product goes to cache, product that was cached goes to viewed
     */
    @Override
    public synchronized void addProduct(ViewedHistory viewedHistory, Product product) {
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
            if (viewed.size() > VIEWED_MAX_SIZE) {
                viewed.remove(VIEWED_MAX_SIZE);
            }
        }
    }
}
