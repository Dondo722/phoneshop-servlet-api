package com.es.phoneshop.dao;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Price;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.web.DemoDataServletContextListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    private DemoDataServletContextListener listener;

    @Mock
    private ServletContextEvent event;
    @Mock
    private ServletContext servletContext;


    @Before
    public void setup() {
        listener = new DemoDataServletContextListener();
        when(servletContext.getInitParameter("insertDemoData")).thenReturn("true");
        when(event.getServletContext()).thenReturn(servletContext);
        listener.contextInitialized(event);
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testFindProductsNoResults() {
        List<Product> products = productDao.findProducts("", null, null);
        assertNotNull(products);
        assertEquals(products,productDao.findProducts("", null, null));
        assertFalse(products.isEmpty());
    }

    @Test
    public void testGetProduct_shouldReturnProduct_whenIdValid() {
        Product actual = productDao.getProduct(1L);
        Product actualP2 = productDao.getProduct(1L);
        assertNotNull(actual);
        assertNotNull(actualP2);
        assertEquals(actual,actualP2);
    }

    @Test
    public void testGetProductFromDao_shouldThrowPNFException_whenIdNotValid() {
        try {
            productDao.getProduct(-1L);
            fail();
        } catch (ProductNotFoundException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testDeleteProductFromDao() {
        int sizeBeforeDelete = productDao.findProducts("", null, null).size();
        productDao.delete(2L);
        int sizeAfterDelete = productDao.findProducts("", null, null).size();
        assertEquals(sizeAfterDelete + 1, sizeBeforeDelete);
    }

    @Test
    public void testSaveProductAndGetHimFromDAO_withId() {
        Currency usd = Currency.getInstance("USD");
        Product savedProduct = new Product(13L, "simsxg75", "Siemens SXG75", new Price(new BigDecimal(150),new Date(), usd), 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(savedProduct);
        Product receivedProduct = productDao.getProduct(savedProduct.getId());
        assertEquals(savedProduct,receivedProduct);
    }

    @Test
    public void testFindProductsByDescriptionFromDao() {
        String query = "samsung";
        List<Product> findProducts = productDao.findProducts(query,null,null);
        assertNotNull(findProducts);
        assertNotEquals(0, findProducts.size());
        Product anyOfFinds = findProducts.get(0);
        List<String> productDesc = Arrays.asList(anyOfFinds.getDescription().toLowerCase(Locale.ROOT).split(" "));
        query = query.toLowerCase(Locale.ROOT);
        assertTrue(productDesc.contains(query));
    }

    @Test
    public void testSortProductsByPriceWithASCOrderFromDao() {
        List<Product> findProducts = productDao.findProducts("", SortField.PRICE, SortOrder.ASC);
        assertNotNull(findProducts);
        assertTrue(findProducts.size() > 2);
        Product firstFound = findProducts.get(0);
        Product secondFound = findProducts.get(1);
        assertTrue(firstFound.getPrice().getCurrentPrice().compareTo(secondFound.getPrice().getCurrentPrice()) <= 0);
    }

    @Test
    public void testSortProductsByPriceWithDESCOrderFromDao() {
        List<Product> findProducts = productDao.findProducts("",SortField.PRICE,SortOrder.DESC);
        assertNotNull(findProducts);
        assertTrue(findProducts.size() > 2);
        Product firstFound = findProducts.get(0);
        Product secondFound = findProducts.get(1);
        assertTrue(firstFound.getPrice().getCurrentPrice().compareTo(secondFound.getPrice().getCurrentPrice()) >= 0);
    }

    @Test
    public void testSortProductsByDescriptionWithASCOrderFromDao() {
        List<Product> findProducts = productDao.findProducts("",SortField.DESCRIPTION,SortOrder.ASC);
        assertNotNull(findProducts);
        assertTrue(findProducts.size() > 2);
        Product firstFound = findProducts.get(0);
        Product secondFound = findProducts.get(1);
        assertTrue(firstFound.getDescription().compareTo(secondFound.getDescription()) <= 0);
    }

    @Test
    public void testSortProductsByDescriptionWithDESCOrderFromDao() {
        List<Product> findProducts = productDao.findProducts("",SortField.DESCRIPTION,SortOrder.DESC);
        assertNotNull(findProducts);
        assertTrue(findProducts.size() > 2);
        Product firstFound = findProducts.get(0);
        Product secondFound = findProducts.get(1);
        assertTrue(firstFound.getDescription().compareTo(secondFound.getDescription()) >= 0);
    }

}
