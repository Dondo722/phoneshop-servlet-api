package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exception.ProductNotFoundException;
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
}
