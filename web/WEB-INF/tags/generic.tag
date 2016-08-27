<%@tag pageEncoding="UTF-8" %>
<%@attribute name="title" fragment="true" %>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${pageContext.request.contextPath}/webjars/bootstrap/3.3.7/css/bootstrap.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/webjars/bootstrap/3.3.7/js/bootstrap.js"></script>
    <%--<link href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.min.css" rel="stylesheet">--%>
    <%--<script src="${pageContext.request.contextPath}/static/bootstrap/js/bootstrap.min.js"></script>--%>
    <title><jsp:invoke fragment="title"/></title>
</head>
<body>
    <div>
        <jsp:invoke fragment="header"/>
    </div>
    <div>
        <jsp:doBody/>
    </div>
    <div>
        <jsp:invoke fragment="footer"/>
    </div>
</body>
</html>