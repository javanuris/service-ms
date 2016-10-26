<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dat" uri="/WEB-INF/date_value.tld" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.application.list"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/application/list"/>
        <div class="container-fluid" style="margin: 70px 0 30px 0;">
            <div class="col-xs-12">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <c:if test="${'authorized'.equals(sessionScope.user.role.name)}">
                            <a href="${pageContext.request.contextPath}/application/add?page=${requestScope.page.currentPage}&items=${requestScope.page.itemsOnPage}"
                               role="button" class="btn btn-default" name="add-category">
                                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                                <fmt:message bundle="${ui}" key="control.add-application.label"/>
                            </a>
                        </c:if>
                        <div class="list-group" style="margin: 0;">
                            <li class="list-group-item col-xs-12" style="border-top: hidden; border-left: hidden; border-right: hidden;">
                                <div class="col-xs-3">
                                    <strong><fmt:message bundle="${ui}" key="control.table.header.created.label"/></strong>
                                </div>
                                <div class="col-xs-3">
                                    <strong><fmt:message bundle="${ui}" key="control.table.header.user.label"/></strong>
                                </div>
                                <div class="col-xs-3">
                                    <strong><fmt:message bundle="${ui}" key="control.table.header.category.label"/></strong>
                                </div>
                                <div class="col-xs-3">
                                    <strong><fmt:message bundle="${ui}" key="control.table.header.message.label"/></strong>
                                </div>
                            </li>
                            <c:choose>
                                <c:when test="${requestScope.applicationList.size() > 0}">
                                    <c:forEach var="item" items="${requestScope.applicationList}">
                                        <a href="${pageContext.request.contextPath}/application/view?page=${requestScope.page.currentPage}&items=${requestScope.page.itemsOnPage}&id=${item.id}"
                                           class="list-group-item col-xs-12" style="border-left: hidden; border-right: hidden;">
                                            <div class="col-xs-3">
                                                <dat:dateValue locale="${language}" stringValue="${item.created}"/>
                                            </div>
                                            <div class="col-xs-3">
                                                    ${item.user.name}
                                            </div>
                                            <div class="col-xs-3">
                                                    ${item.category.name}
                                            </div>
                                            <div class="col-xs-3">
                                                    ${item.message}
                                            </div>
                                        </a>
                                    </c:forEach>
                                    <tags:page page="${page}" uriWithQuestionMark="${pageContext.request.contextPath}/application/list?"/>
                                </c:when>
                                <c:otherwise>
                                    <li class="list-group-item col-xs-12" style="text-align: center;" disabled="">
                                        <em><fmt:message bundle="${ui}" key="message.info.empty"/></em>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <tags:footer/>
    </body>
</html>