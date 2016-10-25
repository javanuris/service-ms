<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dat" uri="/WEB-INF/date_value.tld" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.category.list"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/category/list"/>
        <div class="container-fluid" style="margin: 70px 0 30px 0;">
            <div class="col-xs-12">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <a href="${pageContext.request.contextPath}/category/add?page=${requestScope.page.currentPage}&items=${requestScope.page.itemsOnPage}"
                           role="button" class="btn btn-default" name="add-category">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                            <fmt:message bundle="${ui}" key="control.add-category.label"/>
                        </a>
                        <div class="list-group" style="margin: 0;">
                            <li class="list-group-item col-xs-12" style="border-top: hidden; border-left: hidden; border-right: hidden;">
                                <div class="col-xs-3">
                                    <strong><fmt:message bundle="${ui}" key="control.table.header.created.label"/></strong>
                                </div>
                                <div class="col-xs-9">
                                    <strong><fmt:message bundle="${ui}" key="control.table.header.name.label"/></strong>
                                </div>
                            </li>
                            <c:choose>
                                <c:when test="${requestScope.categoryList.size() > 0}">
                                    <c:forEach var="item" items="${requestScope.categoryList}">
                                        <a href="${pageContext.request.contextPath}/category/view?page=${requestScope.page.currentPage}&items=${requestScope.page.itemsOnPage}&id=${item.id}"
                                           class="list-group-item col-xs-12" style="border-left: hidden; border-right: hidden;">
                                            <div class="col-xs-3">
                                                <dat:dateValue locale="${language}" stringValue="${item.created}"/>
                                            </div>
                                            <div class="col-xs-9">
                                                ${item.name}
                                            </div>
                                        </a>
                                    </c:forEach>
                                    <tags:page page="${page}" uriWithQuestionMark="${pageContext.request.contextPath}/category/list?"/>
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