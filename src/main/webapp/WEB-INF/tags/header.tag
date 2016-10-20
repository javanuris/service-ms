<%@tag pageEncoding="UTF-8" %>
<%@attribute name="current" type="java.lang.String" %>
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
            <c:if test="${sessionScope.navbar != null}">
                <ul class="nav navbar-nav">
                    <c:forEach var="item" items="${sessionScope.navbar}">
                        <c:choose>
                            <c:when test="${current.equals(item.uri)}">
                                <li class="active"><a href="#"><fmt:message bundle="${ui}" key="${item.label}"/></a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="<c:url value="${item.uri}"/>"><fmt:message bundle="${ui}" key="${item.label}"/></a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </ul>
            </c:if>
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <c:choose>
                            <c:when test="${current.equals('/profile/view')}">
                                <tags:username active="active" href="#"/>
                            </c:when>
                            <c:otherwise>
                                <tags:username active="" href="${pageContext.request.contextPath}/profile/view"/>
                            </c:otherwise>
                        </c:choose>
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