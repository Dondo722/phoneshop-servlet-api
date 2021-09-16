package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao products;

    @Override
    public void init() throws ServletException {
        super.init();
        products = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String stringId = request.getPathInfo();
            Long productId = Long.parseLong(stringId.substring(1));
            request.setAttribute("product",products.getProduct(productId));
            request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(500);
        }
    }
}
