<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.user.list"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/user/list"/>
        <div class="container-fluid" style="margin: 70px 0 30px 0;">
            <div class="col-xs-12">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="list-group" style="margin: 0;">
                            <li class="list-group-item col-xs-12" style="border-top: hidden; border-left: hidden; border-right: hidden;">
                                <div class="col-xs-4">
                                    <strong><fmt:message bundle="${ui}" key="control.table.header.user.label"/></strong>
                                </div>
                                <div class="col-xs-3">
                                    <strong><fmt:message bundle="${ui}" key="control.table.header.role.label"/></strong>
                                </div>
                                <div class="col-xs-3">
                                    <strong><fmt:message bundle="${ui}" key="control.table.header.email.label"/></strong>
                                </div>
                                <div class="col-xs-2">
                                    <strong><fmt:message bundle="${ui}" key="control.table.header.attempts.label"/></strong>
                                </div>
                            </li>
                            <c:choose>
                                <c:when test="${requestScope.userList.size() > 0}">
                                    <c:forEach var="item" items="${requestScope.userList}">
                                        <a href="${pageContext.request.contextPath}/user/view?page=${requestScope.page.currentPage}&items=${requestScope.page.itemsOnPage}&id=${item.id}"
                                           class="list-group-item col-xs-12" style="border-left: hidden; border-right: hidden;">
                                            <div class="col-xs-4">
                                                ${item.name}
                                            </div>
                                            <div class="col-xs-3">
                                                <fmt:message bundle="${ui}" key="dictionary.role.${item.role.name}"/>
                                            </div>
                                            <div class="col-xs-3">
                                                ${item.login.email}
                                            </div>
                                            <div class="col-xs-2">
                                                ${item.login.attemptLeft}
                                            </div>
                                        </a>
                                    </c:forEach>
                                    <tags:page page="${page}" uriWithQuestionMark="${pageContext.request.contextPath}/user/list?"/>
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