<%@tag pageEncoding="UTF-8" %>
<%@attribute name="navCurrent" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${requestScope.navItemArray != null}">
    <ul class="nav nav-tabs nav-justified">
        <c:forEach var="item" items="${requestScope.navItemArray}">
            <c:choose>
                <c:when test="${navCurrent.equals(item.link)}">
                    <li role="presentation" class="active"><a href="#"><fmt:message bundle="${ui}" key="${item.name}"/></a></li>
                </c:when>
                <c:otherwise>
                    <li role="presentation"><a href="${item.link}"><fmt:message bundle="${ui}" key="${item.name}"/></a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>
</c:if>