<%@tag pageEncoding="UTF-8" %>
<%@attribute name="table" type="com.epam.java.rt.lab.web.component.Table" %>
<%@attribute name="page" type="com.epam.java.rt.lab.web.component.Page" %>
<%@attribute name="uriWithQuestionMark" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="val" uri="/WEB-INF/reflect_value.tld" %>
<%@ taglib prefix="dat" uri="/WEB-INF/date_value.tld" %>
<div class="list-group">
    <li class="list-group-item col-xs-12" style="border-top: hidden; border-left: hidden; border-right: hidden;">
        <c:forEach var="listColumn" items="${table.listColumnList}">
            <div class="col-xs-${listColumn.width}">
                <strong><fmt:message bundle="${ui}" key="${listColumn.header}"/></strong>
            </div>
        </c:forEach>
    </li>
    <c:choose>
        <c:when test="${table.entityList.size() > 0}">
            <c:forEach var="control" items="${table.entityList}">
                <a href="${table.hrefPrefix.concat(control.id)}" class="list-group-item col-xs-12" style="border-left: hidden; border-right: hidden;">
                    <c:forEach var="listColumn" items="${table.listColumnList}">
                        <div class="col-xs-${listColumn.width}">
                            <c:set var="value"><val:reflectValue entityMethod="${listColumn.fieldName}" entityObject="${control}"/></c:set>
                            <c:choose>
                                <c:when test="${'date'.equals(listColumn.localePrefix)}">
                                    <dat:dateValue locale="${language}" stringValue="${value}"/>
                                </c:when>
                                <c:when test="${listColumn.localePrefix.length() > 0}">
                                    <fmt:message bundle="${ui}" key="${listColumn.localePrefix.concat('.').concat(value)}"/>
                                </c:when>
                                <c:otherwise>${value}</c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>
                </a>
            </c:forEach>
            <tags:page page="${page}" uriWithQuestionMark="${uriWithQuestionMark}"/>
        </c:when>
        <c:otherwise>
            <li class="list-group-item col-xs-12" style="text-align: center;" disabled="">
                <em><fmt:message bundle="${ui}" key="message.info.empty"/></em>
            </li>
        </c:otherwise>
    </c:choose>
</div>