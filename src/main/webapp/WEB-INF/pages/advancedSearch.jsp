<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <h1>
    Advanced search, products found: ${products.size()}
  </h1>
  <form>
    <table>
      <tr>
        <td>
        Description
        </td>
        <td>
        <input name="description" value="${param.description}">

        <select name="searchParam">
          <c:forEach var="searchParam" items="${searchParams}">
            <c:choose>
              <c:when test="${searchParam == param.searchParam}">
                <option selected>${searchParam}</option>
              </c:when>
              <c:otherwise>
                <option>${searchParam}</option>
              </c:otherwise>
            </c:choose>
          </c:forEach>
        </select>
        </td>
      </tr>
      <tr>
        <td>
        Min price
        </td>
        <td>
          <input name="minPrice" value="${param.minPrice}">
          <c:if test="${not empty errors['minPrice']}">
            <div class="error">
                ${errors['minPrice']}
            </div>
          </c:if>
        </td>
      </tr>
      <tr>
        <td>
        Max price
        </td>
        <td>
        <input name="maxPrice" value="${param.maxPrice}">
          <c:if test="${not empty errors['maxPrice']}">
            <div class="error">
                ${errors['maxPrice']}
            </div>
          </c:if>
        </td>
      </tr>
    </table>
    <p>
    <button>Search</button>
    </p>
  </form>
  <c:if test="${not empty products}">
  </form>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
        </td>
        <td class="price">
          Price
        </td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}" varStatus="status">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
          <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
            ${product.description}
          </a>
        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/pricehistory/${product.id}">
            <fmt:formatNumber  value="${product.price.currentPrice}" type="currency" currencySymbol="${product.price.currency.symbol}"/>
          </a>
        </td>
      </tr>
    </c:forEach>
  </table>
  </c:if>
</tags:master>