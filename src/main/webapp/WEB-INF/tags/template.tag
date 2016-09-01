<%@tag pageEncoding="UTF-8" %>
<%@attribute name="title" type="java.lang.String" %>
<%@attribute name="navbarCurrent" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="../i18n.ui" var="ui" scope="session"/>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta id="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no, zoom=fixed">
    <link href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.css"/>" rel="stylesheet">
    <script src="<c:url value="/webjars/jquery/1.11.1/jquery.js"/>"></script>
    <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.js"/>"></script>
    <title><fmt:message bundle="${ui}" key="${title}"/></title>
</head>
<body>
    <tags:header>
        <jsp:attribute name="navbarCurrent">${navbarCurrent}</jsp:attribute>
    </tags:header>
    <div class="container-fluid" style="margin: 70px 0 50px 0;">
        <jsp:doBody/>
    </div>
    <tags:footer/>
</body>
</html>