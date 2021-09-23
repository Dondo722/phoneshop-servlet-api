package com.es.phoneshop.model.cart;


import com.es.phoneshop.model.cart.exception.CartItemNotFoundException;
import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public CartItem getCartItemByProduct(Product p) {
        return items.stream()
                .filter(i -> i.getProduct().equals(p))
                .findAny()
                .orElseThrow(CartItemNotFoundException::new);
    }

    @Override
    public String toString() {
        return "Cart["
                + items +
                ']';
    }
}
