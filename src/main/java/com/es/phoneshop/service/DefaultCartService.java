package com.es.phoneshop.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.exception.CartItemNotFoundException;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private static final Object instanceLock = new Object();
    private static volatile DefaultCartService instance;

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
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Product product, int quantity) throws OutOfStockException {
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
            CartItem item = getCartItemByProduct(cart, product);
            item.setQuantity(quantityCurrent);
        }
        recalculateCart(cart);
    }

    @Override
    public void update(Cart cart, Product product, int quantity) throws OutOfStockException {
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity);
        }
        CartItem item = getCartItemByProduct(cart, product);
        item.setQuantity(quantity);
        recalculateCart(cart);
    }

    @Override
    public void delete(Cart cart, Product product) {
        cart.getItems().removeIf(i ->
                product.getId().equals(i.getProduct().getId()));
        recalculateCart(cart);
    }

    private CartItem getCartItemByProduct(Cart cart, Product product) {
        return cart.getItems().stream()
                .filter(i -> i.getProduct().equals(product))
                .findAny()
                .orElseThrow(CartItemNotFoundException::new);
    }

    private void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .mapToInt(Integer::intValue)
                .sum());
        cart.setTotalCost(cart.getItems().stream()
                .map(i -> i.getProduct().getPrice().getCurrentPrice().multiply(cart.getItems().stream()
                        .filter(cartItem -> cartItem.getProduct().equals(i.getProduct()))
                        .map(item -> BigDecimal.valueOf(item.getQuantity()))
                        .findAny().orElse(BigDecimal.ONE)))
                .reduce(BigDecimal.ZERO,BigDecimal::add));
    }
}
