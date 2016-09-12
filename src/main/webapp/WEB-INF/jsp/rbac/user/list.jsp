<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.rbac.user.list</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/rbac/user/list</jsp:attribute>
    <jsp:body>
        <tags:nav navCurrent="/rbac/user/list"/>
        <tags:list listComponent="${requestScope.userList}"/>
        <p>rbac.user.list</p>
    </jsp:body>
</tags:template>