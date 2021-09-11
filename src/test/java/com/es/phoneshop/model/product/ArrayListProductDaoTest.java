package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNoResults() {
        List<Product> products = productDao.findProducts();
        assertNotNull(products);
        assertEquals(products,productDao.findProducts());
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
        int sizeBeforeDelete = productDao.findProducts().size();
        productDao.delete(2L);
        int sizeAfterDelete = productDao.findProducts().size();
        assertEquals(sizeAfterDelete + 1, sizeBeforeDelete);
    }

    @Test
    public void testSaveProductAndGetHimFromDAO_withId() {
        Currency usd = Currency.getInstance("USD");
        Product savedProduct = new Product(13L, "simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg");
        productDao.save(savedProduct);
        Product receivedProduct = productDao.getProduct(savedProduct.getId());
        assertEquals(savedProduct,receivedProduct);
    }
}
