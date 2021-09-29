package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
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

public class DeleteCartItemServlet extends HttpServlet {
    private ProductDao products;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        super.init();
        products = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Product product = parseProductFromRequest(request);
            Cart cart = cartService.getCart(request);
            cartService.delete(cart,product);
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart item deleted successfully");
        } catch (ParseProductFromRequestException e) {
            response.sendError(500);
        }
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
