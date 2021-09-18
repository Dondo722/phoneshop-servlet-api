package com.es.phoneshop.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private Product product;

    private Cart cart;
    private CartService cartService = DefaultCartService.getInstance();

    @Before
    public void setup() {
        when(request.getSession()).thenReturn(session);
        cart = cartService.getCart(request);
        cart.getItems().clear();
        verify(session).setAttribute(anyString(),any());
        assertNotNull(cart);
    }

    @Test
    public void testAddToCart()  {
        try {
            when(product.getStock()).thenReturn(1);
            cartService.add(cart,product,1);
            CartItem cartItem = cart.getItems().get(0);


            assertSame(product,cartItem.getProduct());
        } catch (OutOfStockException e) {
            fail();
        }
    }

    @Test
    public void testAddToCartOutOfStock()  {
        int cartSize = cart.getItems().size();
        try {
            when(product.getStock()).thenReturn(0);
            cartService.add(cart,product,1);
            fail();
        } catch (OutOfStockException e) {
            assertEquals(cartSize,cart.getItems().size());
        }
    }
}
