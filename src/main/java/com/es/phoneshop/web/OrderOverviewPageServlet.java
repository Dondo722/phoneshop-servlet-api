package com.es.phoneshop.web;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.order.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderOverviewPageServlet extends HttpServlet {

    private OrderDao orderDao;
    private static final String ORDER_OVERVIEW_PAGE_JSP = "/WEB-INF/pages/orderCheckoutOverview.jsp";


    @Override
    public void init() throws ServletException {
        super.init();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order order = parseOrderFromRequest(request);
        request.setAttribute("page", "overview");
        request.setAttribute("order", order);
        request.getRequestDispatcher(ORDER_OVERVIEW_PAGE_JSP).forward(request, response);
    }

    private Order parseOrderFromRequest(HttpServletRequest request) {
        String secureOrderId = request.getPathInfo().substring(1);
        return orderDao.getOrderBySecureId(secureOrderId);
    }
}
