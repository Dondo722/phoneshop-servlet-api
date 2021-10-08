package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.util.AdvancedSearchParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchServlet extends HttpServlet {
    private static final String JSP = "/WEB-INF/pages/advancedSearch.jsp";
    private ProductDao products;


    @Override
    public void init() throws ServletException {
        super.init();
        products = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> error = new HashMap<>();
        String description = request.getParameter("description");
        String min = request.getParameter("minPrice");
        String max = request.getParameter("maxPrice");

        if (isAllFieldsEmptyOrNull(description, min, max)) {
            request.setAttribute("products", products.findProducts(description, null, null));
        } else {
            BigDecimal minPrice = null;
            BigDecimal maxPrice = null;
            try {
                minPrice = min.isEmpty() ? null : new BigDecimal(min);
            } catch (NumberFormatException e) {
                error.put("minPrice", "Not a number");
            }
            try {
                maxPrice = max.isEmpty() ? null : new BigDecimal(max);
            } catch (NumberFormatException e) {
                error.put("maxPrice", "Not a number");
            }

            AdvancedSearchParam param = AdvancedSearchParam.valueOf(request.getParameter("searchParam"));
            request.setAttribute("errors", error);
            request.setAttribute("products", products.findProducts(description, minPrice, maxPrice, param));
        }
        request.setAttribute("searchParams", AdvancedSearchParam.values());
        request.getRequestDispatcher(JSP).forward(request, response);
    }

    private boolean isAllFieldsEmptyOrNull(String description, String min, String max) {
        return (description == null || description.isEmpty())
                && (min == null || min.isEmpty())
                && (max == null || max.isEmpty());
    }

}
