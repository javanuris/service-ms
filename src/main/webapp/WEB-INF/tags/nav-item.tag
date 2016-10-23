<%@tag pageEncoding="UTF-8" %>
<%@attribute name="uri" type="java.lang.String" %>
<%@attribute name="label" type="java.lang.String" %>
<%@attribute name="active" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:choose>
    <c:when test="${active}">
        <li class="active"><a href="#"><fmt:message bundle="${ui}" key="${label}"/></a></li>
    </c:when>
    <c:otherwise>
        <li><a href="${uri}"><fmt:message bundle="${ui}" key="${label}"/></a></li>
    </c:otherwise>
</c:choose>
