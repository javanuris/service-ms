<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.rbac.user.list</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/rbac/user/list</jsp:attribute>
    <jsp:body>
        <tags:nav navCurrent="/rbac/user/list"/>
        ${requestScope.userList.addColumn(1, 'list.header.id.label', 'id')}
        ${requestScope.userList.addColumn(4, 'list.header.user-name.label', 'name')}
        ${requestScope.userList.addColumn(3, 'list.header.role-name.label', 'role.name')}
        ${requestScope.userList.addColumn(3, 'list.header.login-email.label', 'login.email')}
        ${requestScope.userList.addColumn(1, 'list.header.login-attempt-left.label', 'login.attemptLeft')}
        <tags:list listComponent="${requestScope.userList}" pageComponent="${requestScope.userListPage}" uriWithQuestionMark="${pageContext.request.contextPath}/rbac/user/list?"/>
    </jsp:body>
</tags:template>