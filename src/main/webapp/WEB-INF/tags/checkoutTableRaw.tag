<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>

<tr>
    <td>${label}<span class="required">*</span></td>
    <td>
        <input name="${name}" value="${not empty errors[name] ? param.name : order[name]}">
        <c:if test="${not empty errors}">
            <div class="error">
                    ${errors[name]}
            </div>
        </c:if>
    </td>
</tr>
