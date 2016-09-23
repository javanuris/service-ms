<%@tag pageEncoding="UTF-8" %>
<%@attribute name="navCurrent" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${requestScope.navItemArray != null}">
    <ul class="nav nav-tabs nav-justified">
        <c:forEach var="formControl" items="${requestScope.navItemArray}">
            <c:choose>
                <c:when test="${navCurrent.equals(formControl.link)}">
                    <li role="presentation" class="active"><a href="#"><fmt:message bundle="${ui}" key="${formControl.name}"/></a></li>
                </c:when>
                <c:otherwise>
                    <li role="presentation"><a href="${formControl.link}"><fmt:message bundle="${ui}" key="${formControl.name}"/></a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>
</c:if>