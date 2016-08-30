<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:layout>
    <jsp:attribute name="title"><fmt:message bundle="${ui}" key="main.title"/></jsp:attribute>
    <jsp:body>home</jsp:body>
</tags:layout>