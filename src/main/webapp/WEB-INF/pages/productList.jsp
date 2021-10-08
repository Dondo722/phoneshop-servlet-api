<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:if test="${not empty param.message}">
    <p class="success">
        ${param.message}
    </p>
  </c:if>
  <c:if test="${not empty errors}">
  <p class="error">
    An error occurred while adding item to card
  </p>
  </c:if>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
  <form action="${pageContext.servletContext.contextPath}/productsSearch">
  <p>
  <button>Advanced search</button>
  </p>
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
          <tags:sortLink sort="description" order ="asc" />
          <tags:sortLink sort="description" order ="desc" />
        </td>
        <td class="quantity">Quantity</td>
        <td class="price">
          Price
          <tags:sortLink sort="price" order ="asc" />
          <tags:sortLink sort="price" order ="desc" />
        </td>
        <td></td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}" varStatus="status">
      <form method="post" action="${pageContext.servletContext.contextPath}/products">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
            ${product.description}
          </a>
        </td>
        <td>
          <input name="quantity" value="${not empty errors[product.id] ? param.quantity : 1}" class="quantity">
          <c:if test="${not empty errors}">
          <div class="error">
              ${errors[product.id]}
          </div>
          </c:if>
          <input type="hidden" name="productId" value="${product.id}">

        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/pricehistory/${product.id}">
            <fmt:formatNumber  value="${product.price.currentPrice}" type="currency" currencySymbol="${product.price.currency.symbol}"/>
          </a>
        </td>
        <td>
          <button>
            Add to cart
          </button>
        </td>
      </tr>
      </form>
    </c:forEach>
  </table>
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
</tags:master>