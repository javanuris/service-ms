<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.rbac.user.list</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/rbac/user/list</jsp:attribute>
    <jsp:body>
        <tags:nav navCurrent="/rbac/user/list"/>
        ${requestScope.userList.addColumn(1, 'list.header.id.label', 'id')}
        ${requestScope.userList.addColumn(5, 'list.header.user-name.label', 'name')}
        ${requestScope.userList.addColumn(3, 'list.header.role-name.label', 'role')}
        ${requestScope.userList.addColumn(3, 'list.header.login-email.label', 'login')}
        <tags:list>
            <jsp:attribute name="listComponent">${requestScope.userList}</jsp:attribute>
            <jsp:attribute name="pageComponent">"${requestScope.userListPage}</jsp:attribute>
            <jsp:attribute name="uriWithParameterPrefix">${pageContext.request.contextPath}/rbac/user/list?page=</jsp:attribute>
        </tags:list>
    </jsp:body>
</tags:template>