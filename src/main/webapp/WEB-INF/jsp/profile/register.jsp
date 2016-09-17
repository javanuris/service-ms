<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:template>
    <jsp:attribute name="title">title.profile.register</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/profile/register</jsp:attribute>
    <jsp:body><tags:profile-form formComponent="${requestScope.registerForm}"/></jsp:body>
</tags:template>