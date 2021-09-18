package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ViewedHistory;

import javax.servlet.http.HttpServletRequest;

public interface ViewedService {
    ViewedHistory getViewedHistory(HttpServletRequest request);
    void addProduct(ViewedHistory viewedHistory,Product product);
}
