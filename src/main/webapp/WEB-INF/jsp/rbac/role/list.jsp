<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.rbac.role.list</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/rbac/user/list</jsp:attribute>
    <jsp:body>
        <tags:nav nav="${requestScope.nav}" navCurrent="/rbac/role/list"/>
        ${requestScope.roleList.addColumn(1, 'control.table.header.id.label', 'id')}
        ${requestScope.roleList.addColumn(3, 'control.table.header.role.label', 'name')}
        ${requestScope.roleList.addColumn(8, 'control.table.header.permission.label', 'uris')}
        <tags:table table="${requestScope.roleList}" page="${requestScope.roleListPage}" uriWithQuestionMark="${pageContext.request.contextPath}/rbac/role/list?"/>
    </jsp:body>
</tags:template>