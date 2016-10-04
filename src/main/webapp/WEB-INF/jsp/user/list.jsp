<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.user.list</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/user/list</jsp:attribute>
    <jsp:body>
        <%--<tags:nav nav="${requestScope.nav}" navCurrent="/user/list"/>--%>
        ${requestScope.userList.addColumn(1, 'control.table.header.id.label', 'id')}
        ${requestScope.userList.addColumn(3, 'control.table.header.user.label', 'name')}
        ${requestScope.userList.addColumn(3, 'control.table.header.role.label', 'role.name')}
        ${requestScope.userList.addColumn(3, 'control.table.header.email.label', 'login.email')}
        ${requestScope.userList.addColumn(2, 'control.table.header.attempts.label', 'login.attemptLeft')}
        <tags:table table="${requestScope.userList}" page="${requestScope.userListPage}" uriWithQuestionMark="${pageContext.request.contextPath}/user/list?"/>
    </jsp:body>
</tags:template>