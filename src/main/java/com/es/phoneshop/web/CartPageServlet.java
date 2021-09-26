package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.DefaultCartService;
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

public class CartPageServlet  extends HttpServlet {
    private ProductDao products;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        products = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cartService.getCart(request));
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<Long,String> errors = new HashMap<>();
        Product product = new Product();
        int quantity;
        try {
            String[] productIds = request.getParameterValues("productId");
            String[] quantities = request.getParameterValues("quantity");

            Cart cart = cartService.getCart(request);
            for (int i = 0; i < productIds.length; i++) {
                product = parseProductFromString(productIds[i]);
                quantity = parseQuantityFromString(quantities[i],request,product);
                cartService.update(cart,product,quantity);
            }
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        } catch (ParseProductFromRequestException e) {
            response.sendError(500);
        } catch (ParseException e) {
            errors.put(product.getId(), "not a number");
            request.setAttribute("errors", errors);
            doGet(request,response);
        } catch (OutOfStockException e) {
            errors.put(product.getId(), "out of stock, available " + e.getStockAvailable());
            request.setAttribute("errors", errors);
            doGet(request,response);
        }
    }


    private int parseQuantityFromString(String quantityString, HttpServletRequest request, Product product) throws ParseException, OutOfStockException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        int quantity = format.parse(quantityString).intValue();
        if (quantity <= 0) {
            throw new OutOfStockException(product,quantity);
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
