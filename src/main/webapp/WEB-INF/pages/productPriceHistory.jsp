<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
  <p>
    ${product.description}
  </p>
  <table>
      <tr>
        <td>Image</td>
        <td><img src="${product.imageUrl}"></td>
      </tr>
      <tr>
        <td>Price History:</td>
        <c:choose>
          <c:when test="${product.price.priceHistory == null || product.price.priceHistory.isEmpty()}">
            <td>
              No price history
            </td>
            <br />
          </c:when>
          <c:otherwise>
              <td>
              <h4 >
                <span class="priceHistory">Start date:</span>
                <span class="priceHistory">Price:</span>
              </h4>
              <c:forEach items="${product.price.priceHistory}" var="price">
                <p>
                  <span class="priceHistory">
              <fmt:formatDate value="${price.key}" pattern="yyyy-MM-dd"/>
                  </span>
                  <span class="priceHistory">
            <fmt:formatNumber value="${price.value}" type="currency" currencySymbol="${product.price.currency.symbol}"/>
                  </span>
                </p>
              </c:forEach>
            </td>
            <br />
          </c:otherwise>
        </c:choose>
      </tr>
  </table>
</tags:master>