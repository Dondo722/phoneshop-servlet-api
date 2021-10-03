package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.ViewedHistory;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.DefaultCartService;
import com.es.phoneshop.service.DefaultViewedService;
import com.es.phoneshop.service.ViewedService;
import com.es.phoneshop.web.exception.ParseProductFromRequestException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao products;
    private CartService cartService;
    private ViewedService viewedService;

    @Override
    public void init() throws ServletException {
        super.init();
        products = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        viewedService = DefaultViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Product product = parseProductFromRequest(request);
            ViewedHistory viewed = viewedService.getViewedHistory(request);
            viewedService.addProduct(viewed, product);
            request.setAttribute("cart", cartService.getCart(request));
            request.setAttribute("product", product);
            request.setAttribute("viewed", viewed);
            request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
        } catch (ParseProductFromRequestException e) {
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Product product = parseProductFromRequest(request);
            int quantity = parseQuantityFromRequest(request, product);
            cartService.add(cartService.getCart(request), product, quantity);

            response.sendRedirect(request.getContextPath() + "/products/" + product.getId() +
                    "?add_message=Item added to card successfully");
        } catch (ParseProductFromRequestException e) {
            response.sendError(500);
        } catch (ParseException e) {
            request.setAttribute("error", "not a number");
            doGet(request, response);
        } catch (OutOfStockException e) {
            request.setAttribute("error", "out of stock, available " + e.getStockAvailable());
            doGet(request, response);
        }
    }

    private int parseQuantityFromRequest(HttpServletRequest request, Product product) throws ParseException, OutOfStockException {
        String quantityString = request.getParameter("quantity");
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        int quantity = format.parse(quantityString).intValue();
        if (quantity <= 0) {
            throw new OutOfStockException(product, quantity);
        }
        return quantity;
    }

    private Product parseProductFromRequest(HttpServletRequest request) throws ParseProductFromRequestException {
        String stringId = request.getPathInfo();
        long productId;
        try {
            productId = Long.parseLong(stringId.substring(1));
        } catch (NumberFormatException e) {
            throw new ParseProductFromRequestException();
        }
        return products.getProduct(productId);
    }
}
