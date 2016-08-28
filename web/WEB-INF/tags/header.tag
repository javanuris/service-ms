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
            <c:if test="${sessionScope.navbar != null}">
                <ul class="nav navbar-nav">
                    <c:set var="active" value="${sessionScope.navbarActive}"/>
                    <c:set var="index" value="0"/>
                    <c:forEach var="item" items="${sessionScope.navbar}">
                        <c:choose>
                            <c:when test="${active==index}"><li class="active"></c:when>
                            <c:otherwise><li></c:otherwise>
                        </c:choose>
                            <a href="${item.link}">${item.name}</a>
                        </li>
                        <c:set var="index" value="${index+1}"/>
                    </c:forEach>
                </ul>
            </c:if>
            <ul class="nav navbar-nav navbar-right">
                <c:if test="${sessionScope.user != null}">
                    <li><a href="/profile">user.name</a></li>
                    <li><a href="/profile"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span></a></li>
                </c:if>
                <c:if test="${sessionScope.user == null}">
                    <li><a href="/login"><span class="glyphicon glyphicon-log-in" aria-hidden="true"></span></a></li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>