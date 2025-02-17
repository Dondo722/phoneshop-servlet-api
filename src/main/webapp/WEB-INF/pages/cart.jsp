<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
  <p>
    Cart: ${cart}
  </p>
  <c:if test="${not empty param.message}">
    <p class="success">
        ${param.message}
    </p>
  </c:if>
  <c:if test="${not empty errors}">
    <p class="error">
      An error occurred while updating items data
    </p>
  </c:if>
    <c:if test="${not empty cart.items}">
      <form method="post" action="${pageContext.servletContext.contextPath}/cart">
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
            <td></td>
          </tr>
          </thead>
          <tr>
            <c:forEach var="item" items="${cart.items}" varStatus="status">
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
            <fmt:formatNumber value="${item.quantity}" var="quantity_format"/>
            <c:set var="error" value="${errors[item.product.id]}"/>
            <input name="quantity" value="${not empty error ? paramValues["quantity"][status.index] : quantity_format}" class="quantity">
            <c:if test="${not empty error}">
              <div class="error">
                ${errors[item.product.id]}
              </div>
            </c:if>
            <input type="hidden" name="productId" value="${item.product.id}">
          </td>
            <td class="price">
              <a href="${pageContext.servletContext.contextPath}/pricehistory/${item.product.id}">
                <fmt:formatNumber value="${item.product.price.currentPrice}" type="currency" currencySymbol="${item.product.price.currency.symbol}"/>
              </a>
            </td>
          <td>
            <button form="deleteCartItem" formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">Delete</button>
          </td>
          </tr>
          </c:forEach>
          <tr>
            <td></td>
            <td></td>
            <td>
              <p class="quantity">Total quantity:</p>
              <div class="quantity">
                  ${cart.totalQuantity}</div>
            </td>
            <td>
              <p class="price">Total cost:</p>
              <div class="price">
                  ${cart.totalCost}
              </div>
            </td>
          </tr>
      </table>
      <div>
      <button> Update </button>
      </div>
      </form>
      <form action="${pageContext.servletContext.contextPath}/checkout">
        <button > Order </button>
      </form>
      <form id="deleteCartItem" method="post"></form>
    </c:if>
</tags:master>