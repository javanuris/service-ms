<%@tag pageEncoding="UTF-8" %>
<%@attribute name="validationMessageArray" type="java.lang.String[]" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty validationMessageArray}">
    <c:forEach var="message" items="${validationMessageArray}">
        <div class="alert alert-danger"><fmt:message bundle="${ui}" key="${message}"/></div>
    </c:forEach>
</c:if>