<%@tag pageEncoding="UTF-8" %>
<%@attribute name="title" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=0.80, maximum-scale=1, user-scalable=no, zoom=fixed">
    <link href="<c:url value="/static/file.css"/>" rel="stylesheet">
    <link href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.css"/>" rel="stylesheet">
    <script src="<c:url value="/webjars/jquery/1.11.1/jquery.js"/>"></script>
    <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.js"/>"></script>
    <script src="<c:url value="/static/file.js"/>"></script>
    <title>${title}</title>
</head>
