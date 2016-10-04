<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.application.list</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/application/list</jsp:attribute>
    <jsp:body>
        ${requestScope.userList.addColumn(1, 'control.table.header.id.label', 'id')}
        ${requestScope.userList.addColumn(3, 'control.table.header.created.label', 'created')}
        ${requestScope.userList.addColumn(3, 'control.table.header.user.label', 'user.name')}
        ${requestScope.userList.addColumn(3, 'control.table.header.service.label', 'service.name')}
        ${requestScope.userList.addColumn(2, 'control.table.header.message.label', 'message')}
        <tags:table table="${requestScope.applicationList}" page="${requestScope.applicationListPage}" uriWithQuestionMark="${pageContext.request.contextPath}/application/list?"/>
    </jsp:body>
</tags:template>