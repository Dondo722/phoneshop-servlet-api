<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="viewed" required="true" %>

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
