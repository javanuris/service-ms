<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag pageEncoding="UTF-8" %>
<nav class="navbar navbar-default navbar-fixed-top navbar-inverse" style="background-color: RGBA(75, 75, 75, 25);">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#app-navbar-collapse" aria-expanded="false">
                <span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/" style="padding: 10px;">
                <img alt="Brand" src="${pageContext.request.contextPath}/static/brand.png" style="width: 30px; height: 30px;">
            </a>
        </div>
        <div class="collapse navbar-collapse" id="app-navbar-collapse" aria-expanded="false">
            <c:if test="${sessionScope.navArray.size() > 0}">
                <ul class="nav navbar-nav">
                    <c:forEach var="item" items="${sessionScope.navArray}">
                        <li class="${item.active}"><a href="${item.link}">${item.name}</a></li>
                    </c:forEach>
                </ul>
            </c:if>
            <c:if test="${sessionScope.user != null}">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="/profile">user.name</a></li>
                    <li><a href="/profile"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span></a></li>
                </ul>
            </c:if>
            <c:if test="${sessionScope.user == null}">
                <li><a href="/login"><span class="glyphicon glyphicon-log-in" aria-hidden="true"></span></a></li>
            </c:if>
        </div>
    </div>
</nav>