<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.profile.edit</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/profile/view</jsp:attribute>
    <jsp:body><tags:profile-form form="${requestScope.editForm}"/></jsp:body>
</tags:template>