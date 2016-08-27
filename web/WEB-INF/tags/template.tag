<%@tag pageEncoding="UTF-8" %>
<%@attribute name="title" fragment="true" %>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link href="${pageContext.request.contextPath}/webjars/bootstrap/3.3.7/css/bootstrap.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/webjars/bootstrap/3.3.7/js/bootstrap.js"></script>
    <title><jsp:invoke fragment="title"/></title>
</head>
<body>
    <div class="container-fluid">
        <tags:header/>
        <jsp:doBody/>
        <tags:footer/>
    </div>
</body>
</html>