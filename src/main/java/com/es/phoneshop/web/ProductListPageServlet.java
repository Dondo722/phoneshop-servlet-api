package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.*;
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
import java.util.HashMap;
import java.util.Map;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao products;
    private ViewedService viewedService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        products = ArrayListProductDao.getInstance();
        viewedService = DefaultViewedService.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String parameterSortField = request.getParameter("sort");
        String parameterSortOrder = request.getParameter("order");

        SortField sortField = parameterSortField == null ? null : SortField.valueOf(parameterSortField.toUpperCase());
        SortOrder sortOrder = parameterSortOrder == null ? null : SortOrder.valueOf(parameterSortOrder.toUpperCase());

        viewedService.addProduct(viewedService.getViewedHistory(request), null);
        request.setAttribute("products", products.findProducts(query, sortField, sortOrder));
        request.setAttribute("viewed", viewedService.getViewedHistory(request));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Long, String> error = new HashMap<>();
        Product product = new Product();
        int quantity;
        try {
            product = parseProductFromString(request.getParameter("productId"));
            quantity = parseQuantityFromString(request.getParameter("quantity"), request, product);
            Cart cart = cartService.getCart(request);
            cartService.add(cart, product, quantity);
            response.sendRedirect(request.getContextPath() + "/products?message= Item added to cart successfully");
        } catch (ParseProductFromRequestException e) {
            response.sendError(500);
        } catch (ParseException e) {
            error.put(product.getId(), "not a number");
            request.setAttribute("errors", error);
            doGet(request, response);
        } catch (OutOfStockException e) {
            error.put(product.getId(), "out of stock, available " + e.getStockAvailable());
            request.setAttribute("errors", error);
            doGet(request, response);
        }
    }

    private int parseQuantityFromString(String quantityString, HttpServletRequest request, Product product) throws ParseException, OutOfStockException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        int quantity = format.parse(quantityString).intValue();
        if (quantity <= 0) {
            throw new OutOfStockException(product, quantity);
        }
        return quantity;
    }

    private Product parseProductFromString(String stringId) throws ParseProductFromRequestException {
        long productId;
        try {
            productId = Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            throw new ParseProductFromRequestException();
        }
        return products.getProduct(productId);
    }
}
