<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
  <p>
    Cart: ${cart}
  </p>
  <c:if test="${not empty param.add_message}">
    <div class="success">
        ${param.add_message}
    </div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="error">
      Item not added to card
    </div>
  </c:if>
  <p>
    ${product.description}
  </p>
  <form method="post">
    <table>
        <tr>
          <td>Image</td>
          <td><img src="${product.imageUrl}"></td>
        </tr>
        <tr>
          <td>code</td>
          <td>${product.code}</td>
        </tr>
      <tr>
        <td>stock</td>
        <td>${product.stock}</td>
      </tr>
        <tr>
          <td>Price</td>
          <td class="price">
            <fmt:formatNumber value="${product.price.currentPrice}" type="currency" currencySymbol="${product.price.currency.symbol}"/>
          </td>
        <tr>
          <td>quantity</td>
          <td>
            <input name="quantity" class="price" value="${not empty param.quantity ? param.quantity : 1}">
            <c:if test="${not empty error}">
              <div class="error">
                ${error}
              </div>
            </c:if>
          </td>
        </tr>
      </tr>
    </table>
    <button>Add to card</button>
    <c:if test="${not empty viewed.history}">
      <p>Recently viewed :</p>
        <table>
          <tr>
            <c:forEach items="${viewed.history}" var="product_viewed">
              <td>
                <img class="product-history" src="${product_viewed.imageUrl}">
                <div class="viewedText">
                <a href="${pageContext.servletContext.contextPath}/products/${product_viewed.id}">
                    ${product_viewed.description}
                </a>
                </div>
                <div class="viewedText">
                <a href="${pageContext.servletContext.contextPath}/pricehistory/${product_viewed.id}">
                  <fmt:formatNumber value="${product_viewed.price.currentPrice}" type="currency" currencySymbol="${product_viewed.price.currency.symbol}"/>
                </a>
                </div>
                </td>
            </c:forEach>
         </tr>
      </table>
    </c:if>
  </form>
</tags:master>