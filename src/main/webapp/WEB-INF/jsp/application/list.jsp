<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:template>
    <jsp:attribute name="title">title.application.list</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/application/list</jsp:attribute>
    <jsp:body>
        <c:if test="${user.role.name.equals('authorized')}">
            <a href="${requestScope.addApplicationRef}" role="button" class="btn btn-default" name="add-application">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                <fmt:message bundle="${ui}" key="control.add-application.label"/>
            </a>
        </c:if>
        ${requestScope.applicationList.addColumn(3, 'control.table.header.created.label', 'created', 'date')}
        ${requestScope.applicationList.addColumn(3, 'control.table.header.user.label', 'user.name', null)}
        ${requestScope.applicationList.addColumn(3, 'control.table.header.category.label', 'category.name', null)}
        ${requestScope.applicationList.addColumn(3, 'control.table.header.message.label', 'message', null)}
        <tags:table table="${requestScope.applicationList}" page="${requestScope.applicationListPage}" uriWithQuestionMark="${pageContext.request.contextPath}/application/list?"/>
    </jsp:body>
</tags:template>