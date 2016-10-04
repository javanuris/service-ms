<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.user.edit</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/user/list</jsp:attribute>
    <jsp:body><tags:form-panel form="${requestScope.editForm}"/></jsp:body>
</tags:template>