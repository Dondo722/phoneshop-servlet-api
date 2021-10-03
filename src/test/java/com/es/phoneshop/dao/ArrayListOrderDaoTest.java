package com.es.phoneshop.dao;

import com.es.phoneshop.dao.exception.OrderNotFoundException;
import com.es.phoneshop.model.order.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListOrderDaoTest {
    private OrderDao orderDao;

    @Mock
    private Order order;

    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Test
    public void testOrderDaoSave() {
        orderDao.save(order);
        verify(order).setId(any());
    }


    @Test
    public void testGetOrder() {
        orderDao.save(order);
        when(order.getId()).thenReturn(100L);
        Order receivedOrder = orderDao.getOrder(100L);

        assertNotNull(receivedOrder);
        assertSame(receivedOrder, order);
    }

    @Test
    public void testGetOrder_whenOrderNotExist() {
        try {
            orderDao.save(order);
            when(order.getId()).thenReturn(100L);
            Order receivedOrder = orderDao.getOrder(100000L);
            fail();
        } catch (OrderNotFoundException e) {
            assertNotNull(e);
        }
    }
}
