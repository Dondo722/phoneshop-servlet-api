package com.es.phoneshop.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {
    @Mock
    private Cart cart;
    @Mock
    private Product product;

    private OrderService orderService = DefaultOrderService.getInstance();


    @Before
    public void setup() {
        when(cart.getItems()).thenReturn(someItems());
        when(cart.getTotalCost()).thenReturn(new BigDecimal(123));
        when(cart.getTotalQuantity()).thenReturn(1);
    }


    @Test
    public void testGetOrder() {
        Order order =  orderService.getOrder(cart);

        assertNotNull(order);
        assertEquals(order.getSubtotal(),cart.getTotalCost());
        assertEquals(order.getItems().size(),someItems().size());
    }

    @Test
    public void testGetPaymentMethods() {
        List<PaymentMethod> methods = orderService.getPaymentMethods();

        assertNotNull(methods);
        assertEquals(methods.size(),PaymentMethod.values().length);
    }

    private List<CartItem> someItems(){
        List<CartItem> list = new ArrayList<>();
        list.add(new CartItem(product,1));
        list.add(new CartItem(product,1));
        list.add(new CartItem(product,1));
        list.add(new CartItem(product,1));
        list.add(new CartItem(product,1));
        return list;
    }
}
