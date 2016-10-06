<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.category.list</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/category/list</jsp:attribute>
    <jsp:body>
        <a href="${requestScope.addCategoryRef}" role="button" class="btn btn-default" name="add-category">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
            <fmt:message bundle="${ui}" key="control.add-category.label"/>
        </a>
        ${requestScope.categoryList.addColumn(3, 'control.table.header.created.label', 'created', 'date')}
        ${requestScope.categoryList.addColumn(9, 'control.table.header.name.label', 'name', null)}
        <tags:table table="${requestScope.categoryList}" page="${requestScope.categoryListPage}" uriWithQuestionMark="${pageContext.request.contextPath}/category/list?"/>
    </jsp:body>
</tags:template>