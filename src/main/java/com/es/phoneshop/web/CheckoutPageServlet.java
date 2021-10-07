package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.DefaultCartService;
import com.es.phoneshop.service.DefaultOrderService;
import com.es.phoneshop.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;
    private OrderService orderService;
    private static final String DELIVERY_DATE_PARAM = "deliveryDate";
    private static final String CHECKOUT_PAGE_JSP = "/WEB-INF/pages/checkoutPage.jsp";


    @Override
    public void init() throws ServletException {
        super.init();
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute("order", orderService.getOrder(cart));
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher(CHECKOUT_PAGE_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();

        setOrderValues(request, order, errors);

        handleErrors(request, response, order, errors);
    }

    private void handleErrors(HttpServletRequest request, HttpServletResponse response, Order order,
                              Map<String, String> errors) throws IOException, ServletException {
        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(cartService.getCart(request));
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.setAttribute("paymentMethods", orderService.getPaymentMethods());
            request.getRequestDispatcher(CHECKOUT_PAGE_JSP).forward(request, response);
        }
    }

    private void setOrderValues(HttpServletRequest request, Order order, Map<String, String> errors) {
        setRequiredValue(request, "firstName", errors, order::setFirstName);
        setRequiredValue(request, "lastName", errors, order::setLastName);
        setRequiredValue(request, "phone", errors, order::setPhone);
        setRequiredValue(request, "address", errors, order::setAddress);
        setRequiredValue(request, "paymentMethod", errors, s -> order.setPaymentMethod(PaymentMethod.valueOf(s)));
        try {
            setRequiredValue(request, DELIVERY_DATE_PARAM, errors, s -> order.setDeliveryDate(LocalDate.parse(s)));
        } catch (DateTimeParseException e) {
            errors.put(DELIVERY_DATE_PARAM, "Not a date");
        }
    }

    private void setRequiredValue(HttpServletRequest request, String param, Map<String, String> errors,
                                  Consumer<String> consumer) {
        String value = request.getParameter(param);
        if (value == null || value.isEmpty()) {
            errors.put(param, "Value is required");
        } else {
            consumer.accept(value);
        }
    }
}
