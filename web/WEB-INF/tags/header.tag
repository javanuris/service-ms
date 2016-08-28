<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag pageEncoding="UTF-8" %>
<nav class="navbar navbar-default navbar-static-top">
    <div class="container">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <div class="navbar-header">
            <a class="navbar-brand" href="#">
                <img alt="Brand" src="${pageContext.request.contextPath}/static/brand.png">
            </a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <c:if test="${sessionScope.navArray.size() > 0}">
                <ul class="nav navbar-nav">
                    <c:forEach var="item" items="${sessionScope.navArray}">
                        <li role="presentation" class="${item.active}"><a href="${item.link}">${item.name}</a></li>
                    </c:forEach>
                </ul>
            </c:if>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="#">Link</a></li>
            </ul>
        </div>
    </div>
</nav>