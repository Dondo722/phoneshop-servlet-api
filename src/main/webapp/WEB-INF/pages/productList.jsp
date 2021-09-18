<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
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
        <td class="price">
          Price
          <tags:sortLink sort="price" order ="asc" />
          <tags:sortLink sort="price" order ="desc" />
        </td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
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
          <a href="${pageContext.servletContext.contextPath}/pricehistory/${product.id}">
          <fmt:formatNumber value="${product.price.currentPrice}" type="currency" currencySymbol="${product.price.currency.symbol}"/>
          </a>
        </td>
      </tr>
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