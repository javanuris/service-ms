<%@tag pageEncoding="UTF-8" %>
<%@attribute name="navbarCurrent" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<nav class="navbar navbar-default navbar-fixed-top navbar-inverse" style="background-color: RGBA(75, 75, 75, 25);">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#app-navbar-collapse" aria-expanded="false">
                <span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<c:url value="/"/>" style="padding: 10px;">
                <img alt="Brand" src="<c:url value="/static/brand.png"/>" style="width: 30px; height: 30px;">
            </a>
        </div>
        <div class="collapse navbar-collapse" id="app-navbar-collapse" aria-expanded="false">
            <c:if test="${sessionScope.navbarItemArray != null}">
                <ul class="nav navbar-nav">
                    <c:forEach var="item" items="${sessionScope.navbarItemArray}">
                        <c:choose>
                            <c:when test="${navbarCurrent.equals(item.link)}">
                                <li class="active"><a href="#"><fmt:message bundle="${ui}" key="${item.name}"/></a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="<c:url value="${item.link}"/>"><fmt:message bundle="${ui}" key="${item.name}"/></a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </ul>
            </c:if>
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${not empty sessionScope.userId}">
                        <c:choose>
                            <c:when test="${navbarCurrent.equals('/profile/view')}">
                                <c:set var="usernameActive" value="active"/>
                                <c:set var="usernameHref" value="#"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="usernameActive" value=""/>
                                <c:set var="usernameHref" value="${pageContext.request.contextPath}/profile/view"/>
                            </c:otherwise>
                        </c:choose>
                        <tags:username active="${usernameActive}" href="${usernameHref}"/>
                        <li><a href="${pageContext.request.contextPath}/profile/logout">
                            <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>
                        </a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/profile/login">
                            <span class="glyphicon glyphicon-log-in" aria-hidden="true"></span>
                        </a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>