<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.application.list</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/application/list</jsp:attribute>
    <jsp:body>
        <a href="${requestScope.addApplicationRef}" role="button" class="btn btn-default" name="add-application">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
            <fmt:message bundle="${ui}" key="control.add-application.label"/>
        </a>
        ${requestScope.applicationList.addColumn(3, 'control.table.header.created.label', 'created')}
        ${requestScope.applicationList.addColumn(3, 'control.table.header.user.label', 'user.name')}
        ${requestScope.applicationList.addColumn(3, 'control.table.header.category.label', 'category.name')}
        ${requestScope.applicationList.addColumn(3, 'control.table.header.message.label', 'message')}
        <tags:table table="${requestScope.applicationList}" page="${requestScope.applicationListPage}" uriWithQuestionMark="${pageContext.request.contextPath}/application/list?"/>
    </jsp:body>
</tags:template>