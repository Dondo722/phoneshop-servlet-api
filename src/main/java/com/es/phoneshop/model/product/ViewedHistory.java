package com.es.phoneshop.model.product;

import java.util.ArrayList;
import java.util.List;


public class ViewedHistory {
    private List<Product> history;

    private Product cache;

    public ViewedHistory() {
        this.history = new ArrayList<>();
    }

    public List<Product> getHistory() {
        return history;
    }

    public Product getCache() {
        return cache;
    }

    public void setCache(Product cache) {
        this.cache = cache;
    }
}
