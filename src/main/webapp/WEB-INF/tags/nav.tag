<%@tag pageEncoding="UTF-8" %>
<%@attribute name="nav" type="com.epam.java.rt.lab.web.component.navigation.Navigation" %>
<%@attribute name="navCurrent" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${nav != null && navCurrent != null}">
    <ul class="nav nav-tabs nav-justified">
        <c:forEach var="control" items="${nav.iterator()}">
            <c:choose>
                <c:when test="${navCurrent.equals(control.uri)}">
                    <li role="presentation" class="active"><a href="#"><fmt:message bundle="${ui}" key="${control.label}"/></a></li>
                </c:when>
                <c:otherwise>
                    <li role="presentation"><a href="${control.uri}"><fmt:message bundle="${ui}" key="${control.label}"/></a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>
</c:if>