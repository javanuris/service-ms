<%@tag pageEncoding="UTF-8" %>
<%@attribute name="validationMessageList" type="java.lang.Object" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${not empty validationMessageList}">
    <div style="margin: 5px 0 0 0;">
        <c:forEach var="message" items="${validationMessageList}">
            <div class="alert alert-danger"><fmt:message bundle="${ui}" key="${message}"/></div>
        </c:forEach>
    </div>
</c:if>