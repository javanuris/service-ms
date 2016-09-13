<%@tag pageEncoding="UTF-8" %>
<%@attribute name="listComponent" type="com.epam.java.rt.lab.component.ListComponent" %>
<%@attribute name="pageComponent" type="com.epam.java.rt.lab.component.PageComponent" %>
<%@attribute name="uriWithParameterPrefix" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="list-group">
    <li class="list-group-item col-xs-12" style="border-top: hidden; border-left: hidden; border-right: hidden;">
        <c:forEach var="listColumn" items="${listComponent.listColumnList}">
            <div class="col-xs-${listColumn.width}">
                <strong><fmt:message bundle="${ui}" key="${listColumn.header}"/></strong>
            </div>
        </c:forEach>
    </li>
    <c:choose>
        <c:when test="${listComponent.valueMapList.size() > 0}">
            <c:forEach var="item" items="${listComponent.valueMapList}">
                <a href="${item.get('href')}" class="list-group-item col-xs-12" style="border-left: hidden; border-right: hidden;">
                    <c:forEach var="listColumn" items="${listComponent.listColumnList}">
                        <div class="col-xs-${listColumn.width}">${item.get(listColumn.fieldName)}</div>
                    </c:forEach>
                </a>
            </c:forEach>
            <tags:page pageComponent="${pageComponent}" uriWithParameterPrefix="${uriWithParameterPrefix}"/>
        </c:when>
        <c:otherwise>
            <li class="list-group-item col-xs-12" style="text-align: center;" disabled="">
                <em><fmt:message bundle="${ui}" key="list.empty.label"/></em>
            </li>
        </c:otherwise>
    </c:choose>
</div>