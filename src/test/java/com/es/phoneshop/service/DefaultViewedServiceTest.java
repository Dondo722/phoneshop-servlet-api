package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ViewedHistory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultViewedServiceTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private Product product;

    private ViewedHistory history;
    private ViewedService viewedService = DefaultViewedService.getInstance();

    @Before
    public void setup() {
        when(request.getSession()).thenReturn(session);
        history = viewedService.getViewedHistory(request);
        history.getHistory().clear();
        verify(session).setAttribute(anyString(),any());
        assertNotNull(history);
    }

    @Test
    public void testAddProduct_IfFirstAdd_thenProductGoesToCache() {
        viewedService.addProduct(history, product);
        assertSame(history.getCache(), product);
        assertTrue(history.getHistory().isEmpty());
    }

    @Test
    public void testAddProducts_iFMoreThan0neProductAdded_thenLastProductGoesToCache() {
        viewedService.addProduct(history, product);
        viewedService.addProduct(history, new Product());
        assertNotEquals(history.getCache(), product);
        assertFalse(history.getHistory().isEmpty());
    }

    @Test
    public void testAddProducts_iFAddSameProduct_thenProductWillNotBeAdded_ifProductInCache_thenItStaysAtCache() {
        viewedService.addProduct(history, product);
        viewedService.addProduct(history, product);
        assertSame(history.getCache(), product);
        assertTrue(history.getHistory().isEmpty());
    }
}
