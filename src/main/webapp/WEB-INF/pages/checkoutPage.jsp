<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout page">
  <p>
    Cart: ${order}
  </p>
  <c:if test="${not empty param.message}">
    <p class="success">
        ${param.message}
    </p>
  </c:if>
  <c:if test="${not empty errors}">
    <p class="error">
      An error occurred while placing order
    </p>
  </c:if>
    <c:if test="${not empty order.items}">
      <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
        <table>
          <thead>
          <tr>
            <td>Image</td>
            <td>
              Description
            </td>
            <td class="quantity">
              Quantity
            </td>
            <td class="price">
              Price
            </td>
          </tr>
          </thead>
          <tr>
            <c:forEach var="item" items="${order.items}" varStatus="status">
          <tr>
            <td>
              <img class="product-tile" src="${item.product.imageUrl}">
            </td>
            <td>
              <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                  ${item.product.description}
              </a>
            </td>
          <td class="quantity">
            <fmt:formatNumber value="${item.quantity}"/>
          </td>
            <td class="price">
              <a href="${pageContext.servletContext.contextPath}/pricehistory/${item.product.id}">
                <fmt:formatNumber value="${item.product.price.currentPrice}" type="currency" currencySymbol="${item.product.price.currency.symbol}"/>
              </a>
            </td>
          </tr>
          </c:forEach>
          <tr>
            <td></td>
            <td></td>
            <td>
              <p class="quantity">Total quantity:</p>
              <div class="quantity">
                  ${order.totalQuantity}</div>
            </td>
            <td>
              <p class="price">Total cost:</p>
              <div class="price">
                  ${order.totalCost}
              </div>
            </td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>
            </td>
            <td>
              <p class="price">Subtotal:</p>
              <div class="price">
                  ${order.subtotal}
              </div>
            </td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>
            </td>
            <td>
              <p class="price">Delivery cost:</p>
              <div class="price">
                  ${order.deliveryCost}
              </div>
            </td>
          </tr>
      </table>
      <h1>Order data:</h1>
      <table>
        <tags:checkoutTableRaw name="firstName" label="First name" order="${order}" errors="${errors}"></tags:checkoutTableRaw>
        <tags:checkoutTableRaw name="lastName" label="Last name" order="${order}" errors="${errors}"></tags:checkoutTableRaw>
        <tags:checkoutTableRaw name="phone" label="Phone" order="${order}" errors="${errors}"></tags:checkoutTableRaw>
        <tags:checkoutTableRaw name="deliveryDate" label="Delivery date" order="${order}" errors="${errors}"></tags:checkoutTableRaw>
        <tags:checkoutTableRaw name="address" label="Address" order="${order}" errors="${errors}"></tags:checkoutTableRaw>
        <tr>
          <td>Payment method<span class="required">*</span></td>
          <td>
            <select name="paymentMethod">
                <option></option>
                <c:forEach var="method" items="${paymentMethods}">
                  <c:choose>
                  <c:when test="${param.paymentMethod == method}">
                    <option selected>${method}</option>
                  </c:when>
                  <c:otherwise>
                    <option>${method}</option>
                  </c:otherwise>
                  </c:choose>
                </c:forEach>
            </select>
            <c:if test="${not empty errors}">
              <div class="error">
                  ${errors["paymentMethod"]}
              </div>
            </c:if>
          </td>
        </tr>
      </table>
      <p>
      <button>Confirm the order</button>
      </p>
    </form>
    </c:if>
</tags:master>