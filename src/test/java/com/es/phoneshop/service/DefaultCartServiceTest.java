package com.es.phoneshop.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.Price;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private Product product;
    @Mock
    private Price price;

    private Cart cart;
    private CartService cartService = DefaultCartService.getInstance();

    @Before
    public void setup() {
        when(request.getSession()).thenReturn(session);
        cart = cartService.getCart(request);
        cart.getItems().clear();

        when(product.getPrice()).thenReturn(price);
        when(price.getCurrentPrice()).thenReturn(new BigDecimal(1));
    }

    @Test
    public void testAddToCart() {
        when(product.getStock()).thenReturn(1);

        try {
            cartService.add(cart, product, 1);
            CartItem cartItem = cart.getItems().get(0);


            assertSame(product, cartItem.getProduct());
        } catch (OutOfStockException e) {
            fail();
        }
    }

    @Test
    public void testAddToCartOutOfStock() {
        int cartSize = cart.getItems().size();

        when(product.getStock()).thenReturn(0);
        try {
            cartService.add(cart, product, 1);
            fail();
        } catch (OutOfStockException e) {
            assertEquals(cartSize, cart.getItems().size());
        }
    }

    @Test
    public void testUpdateCartHigherQuantity() {
        int itemStock = 2;

        when(product.getStock()).thenReturn(itemStock);
        try {
            cartService.add(cart, product, 1);
            CartItem itemToChange = cart.getItems().get(0);
            cartService.update(cart, itemToChange.getProduct(), itemStock);

            assertNotEquals(itemToChange.getQuantity(), 1);
            assertEquals(itemToChange.getQuantity(), itemStock);
        } catch (OutOfStockException e) {
            fail();
        }
    }

    @Test
    public void testUpdateCartLowerQuantity() {
        int itemStock = 2;

        when(product.getStock()).thenReturn(itemStock);
        try {
            cartService.add(cart, product, itemStock);
            CartItem itemToChange = cart.getItems().get(0);
            cartService.update(cart, itemToChange.getProduct(), 1);

            assertNotEquals(itemToChange.getQuantity(), itemStock);
            assertEquals(itemToChange.getQuantity(), 1);
        } catch (OutOfStockException e) {
            fail();
        }
    }

    @Test
    public void testUpdateCartOutOfStock() {
        int itemStock = 1;

        when(product.getStock()).thenReturn(itemStock);
        try {
            cartService.add(cart, product, itemStock);
        } catch (OutOfStockException e) {
            fail();
        }

        CartItem itemToChange = cart.getItems().get(0);
        try {
            cartService.update(cart, itemToChange.getProduct(), 2);
            fail();
        } catch (OutOfStockException e) {
            assertNotEquals(itemToChange.getQuantity(), 2);
            assertEquals(itemToChange.getQuantity(), itemStock);
        }
    }

    @Test
    public void testDeleteProductFromCart() {
        when(product.getStock()).thenReturn(100);
        try {
            cartService.add(cart, product, 100);
        } catch (OutOfStockException e) {
            fail();
        }
        cartService.delete(cart,product);
        assertTrue(cart.getItems().isEmpty());
    }
}
