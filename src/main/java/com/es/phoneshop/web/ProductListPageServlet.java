package com.es.phoneshop.web;

import com.es.phoneshop.model.product.*;
import com.es.phoneshop.service.DefaultViewedService;
import com.es.phoneshop.service.ViewedService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao products;
    private ViewedService viewedService;

    @Override
    public void init() throws ServletException {
        super.init();
        products = ArrayListProductDao.getInstance();
        viewedService = DefaultViewedService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String parameterSortField = request.getParameter("sort");
        String parameterSortOrder = request.getParameter("order");

        SortField sortField = parameterSortField == null ? null : SortField.valueOf(parameterSortField.toUpperCase());
        SortOrder sortOrder = parameterSortOrder == null ? null : SortOrder.valueOf(parameterSortOrder.toUpperCase());

        viewedService.addProduct(viewedService.getViewedHistory(request), null);
        request.setAttribute("products", products.findProducts(query,sortField,sortOrder));
        request.setAttribute("viewed", viewedService.getViewedHistory(request));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}
