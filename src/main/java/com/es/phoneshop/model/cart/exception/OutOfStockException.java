package com.es.phoneshop.model.cart.exception;

import com.es.phoneshop.model.product.Product;

public class OutOfStockException extends Exception {
    private Product product;
    private int stockRequested;
    private int stockAvailable;

    public OutOfStockException(Product product, int stockRequested) {
        this.product = product;
        this.stockRequested = stockRequested;
        this.stockAvailable = product.getStock();
    }

    public Product getProduct() {
        return product;
    }

    public int getStockRequested() {
        return stockRequested;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }
}
