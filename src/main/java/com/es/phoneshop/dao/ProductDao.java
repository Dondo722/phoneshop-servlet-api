package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.util.AdvancedSearchParam;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    List<Product> findProducts(String query, BigDecimal minPrice, BigDecimal maxPrice, AdvancedSearchParam param);

    void save(Product product);

    void delete(Long id);
}
