<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.category.list</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/category/list</jsp:attribute>
    <jsp:body>
        ${requestScope.categoryList.addColumn(1, 'control.table.header.id.label', 'id')}
        ${requestScope.categoryList.addColumn(3, 'control.table.header.created.label', 'created')}
        ${requestScope.categoryList.addColumn(8, 'control.table.header.name.label', 'name')}
        <tags:table table="${requestScope.categoryList}" page="${requestScope.categoryListPage}" uriWithQuestionMark="${pageContext.request.contextPath}/category/list?"/>
    </jsp:body>
</tags:template>