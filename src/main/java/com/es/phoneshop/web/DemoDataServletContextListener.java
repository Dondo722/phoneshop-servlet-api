package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.Price;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

public class DemoDataServletContextListener implements ServletContextListener {
    private ProductDao productDao;

    public DemoDataServletContextListener() {
        this.productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        boolean insertDemoData = Boolean.parseBoolean(event.getServletContext().getInitParameter("insertDemoData"));
        if (insertDemoData) {
            saveSampleProducts();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private void saveSampleProducts() {
        Currency usd = Currency.getInstance("USD");



        Product samsung = Product.newBuilder()
                .setId(1L)
                .setCode("sgs")
                .setDescription("Samsung Galaxy S")
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg")
                .setPrice(new Price(new BigDecimal(100), new Date(12), usd))
                .setStock(10000)
                .build();
        samsung.getPrice().changeCurrentPrice(new BigDecimal(100000000), new Date(13));

        Product smsungS2 = Product.newBuilder()
                .setId(2L)
                .setCode("sgs2")
                .setDescription("Samsung Galaxy S II")
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg")
                .setPrice(new Price(new BigDecimal(200), new Date(13, 2, 4), usd))
                .setStock(0)
                .build();

        smsungS2.getPrice().changeCurrentPrice(new BigDecimal(210), new Date(13, 3, 5));
        smsungS2.getPrice().changeCurrentPrice(new BigDecimal(240), new Date(14, 4, 6));


        this.productDao.save(samsung);
        this.productDao.save(smsungS2);


        this.productDao.save( Product.newBuilder()
                .setId(3L)
                .setCode("sgs3")
                .setDescription("Samsung Galaxy S III")
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg")
                .setPrice(new Price(new BigDecimal(300), new Date(1), usd))
                .setStock(5)
                .build());
        this.productDao.save( Product.newBuilder()
                .setId(4L)
                .setCode("iphone")
                .setDescription("Apple iPhone")
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg")
                .setPrice(new Price(new BigDecimal(400), new Date(1), usd))
                .setStock(10)
                .build());
        this.productDao.save( Product.newBuilder()
                .setId(5L)
                .setCode("iphone6")
                .setDescription("Apple iPhone 6")
                .setImageUrl("https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg")
                .setPrice(new Price(new BigDecimal(1000), new Date(1), usd))
                .setStock(30)
                .build());

        this.productDao.save(new Product(6L, "htces4g", "HTC EVO Shift 4G",
                new Price(new BigDecimal(320), new Date(1), usd), 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        this.productDao.save(new Product(7L, "sec901", "Sony Ericsson C901",
                new Price(new BigDecimal(420), new Date(1), usd), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        this.productDao.save(new Product(8L, "xperiaxz", "Sony Xperia XZ",
                new Price(new BigDecimal(120), new Date(1), usd), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        this.productDao.save(new Product(9L, "nokia3310", "Nokia 3310",
                new Price(new BigDecimal(70), new Date(1), usd), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        this.productDao.save(new Product(10L, "palmp", "Palm Pixi",
                new Price(new BigDecimal(170), new Date(1), usd), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        this.productDao.save(new Product(11L, "simc56", "Siemens C56",
                new Price(new BigDecimal(70), new Date(1), usd), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        this.productDao.save(new Product(12L, "simc61", "Siemens C61",
                new Price(new BigDecimal(80), new Date(1), usd), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        this.productDao.save(new Product(13L, "simsxg75", "Siemens SXG75",
                new Price(new BigDecimal(150), new Date(1), usd), 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }
}
