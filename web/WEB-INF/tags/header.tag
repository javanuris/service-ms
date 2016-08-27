<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag pageEncoding="UTF-8" %>
<div class="page-header" style="border-bottom: none;">
    <h2 style="text-align: center;">Service Management System</h2>
    <c:if test="${sessionScope.navArray.size() > 0}">
        <ul class="nav nav-tabs nav-justified">
            <c:forEach var="item" items="${sessionScope.navArray}">
                <li role="presentation" class="${item.active}"><a href="${item.link}">${item.name}</a></li>
            </c:forEach>
        </ul>
    </c:if>
</div>
