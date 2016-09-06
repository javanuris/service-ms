<%@tag pageEncoding="UTF-8" %>
<%@attribute name="viewComponent" type="com.epam.java.rt.lab.component.ViewComponent" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:forEach var="item" items="${viewComponent.viewItemArray}">
    <div class="col-xs-12" style="padding: 5px 0;">
        <c:choose>
            <c:when test="${item.type.equals('button')}">
                <a href="${item.value}" role="${item.type}" class="btn btn-default col-xs-12" id="${item.label}">
                    <fmt:message bundle="${ui}" key="${item.label}"/>
                </a>
            </c:when>
            <c:when test="${item.type.equals('checkbox')}">
                <h4><small><fmt:message bundle="${ui}" key="${item.label}"/></small>${item.value}</h4>
            </c:when>
            <c:otherwise>
                <div class="col-xs-4" style="color: darkgray; text-align: right;"><fmt:message bundle="${ui}" key="${item.label}"/>:</div>
                <div class="col-xs-8" style="font-size: medium;">${item.value}</div>
            </c:otherwise>
        </c:choose>
    </div>
</c:forEach>
