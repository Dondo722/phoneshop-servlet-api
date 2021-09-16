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
        <td>
          <h4 style="padding-left:2em">
            <span style="display:inline-block; width:5em;">Start date:</span>
            <span style="display:inline-block; width:5em;">Price:</span>

          </h4>
        <c:forEach items="${product.price.priceHistory}" var="price">
          <p style="padding-left:2em">
            <span style="display:inline-block; width:5em;">
              <fmt:formatDate value="${price.key}" pattern="yyyy-MM-dd"/>
            </span>
            <span style="display:inline-block; width:5em;">
            <fmt:formatNumber value="${price.value}" type="currency" currencySymbol="${product.price.currency.symbol}"/>
            </span>
          </p>
        </c:forEach>
        </td>
      </tr>
  </table>
</tags:master>