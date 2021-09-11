package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Price;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

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


    private void saveSampleProducts(){
        Currency usd = Currency.getInstance("USD");
        Product samsung = new Product(1L, "sgs", "Samsung Galaxy S",
                new Price(new BigDecimal(100),new  Date(12),usd), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        samsung.getPrice().changeCurrentPrice( new BigDecimal(110),new Date());

        Product smsungS2 = new Product(2L, "sgs2", "Samsung Galaxy S II",
                new Price(new BigDecimal(200),new  Date(13,2,4),usd), 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg");
        smsungS2.getPrice().changeCurrentPrice( new BigDecimal(210),new Date(13,3,5));
        smsungS2.getPrice().changeCurrentPrice( new BigDecimal(240),new Date(14,4,6));


        this.productDao.save(samsung);
        this.productDao.save(smsungS2);
        this.productDao.save(new Product(3L, "sgs3", "Samsung Galaxy S III",
                new Price(new BigDecimal(300),new  Date(),usd), 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        this.productDao.save(new Product(4L, "iphone", "Apple iPhone",
                new Price(new BigDecimal(400),new  Date(),usd), 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        this.productDao.save(new Product(5L, "iphone6", "Apple iPhone 6",
                new Price(new BigDecimal(1000),new  Date(),usd), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        this.productDao.save(new Product(6L, "htces4g", "HTC EVO Shift 4G",
                new Price(new BigDecimal(320),new  Date(),usd), 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        this.productDao.save(new Product(7L, "sec901", "Sony Ericsson C901",
                new Price(new BigDecimal(420),new  Date(),usd), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        this.productDao.save(new Product(8L, "xperiaxz", "Sony Xperia XZ",
                new Price(new BigDecimal(120),new  Date(),usd), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        this.productDao.save(new Product(9L, "nokia3310", "Nokia 3310",
                new Price(new BigDecimal(70),new  Date(),usd), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        this.productDao.save(new Product(10L, "palmp", "Palm Pixi",
                new Price(new BigDecimal(170),new  Date(),usd), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        this.productDao.save(new Product(11L, "simc56", "Siemens C56",
                new Price(new BigDecimal(70),new  Date(),usd), 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        this.productDao.save(new Product(12L, "simc61", "Siemens C61",
                new Price(new BigDecimal(80),new  Date(),usd), 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        this.productDao.save(new Product(13L, "simsxg75", "Siemens SXG75",
                new Price(new BigDecimal(150),new  Date(),usd), 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }


}
