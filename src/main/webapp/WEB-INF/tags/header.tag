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
            <c:if test="${not empty sessionScope.user}">
                <ul class="nav navbar-nav">
                    <c:if test="${sessionScope.user.role.verifyPermission('/application/list')}">
                        <tags:nav-item uri="/application/list" label="nav.application" active="${'/application/list'.equals(current)}"/>
                    </c:if>
                    <c:if test="${sessionScope.user.role.verifyPermission('/category/list')}">
                        <tags:nav-item uri="/category/list" label="nav.category" active="${'/category/list'.equals(current)}"/>
                    </c:if>
                    <c:if test="${sessionScope.user.role.verifyPermission('/user/list')}">
                        <tags:nav-item uri="/user/list" label="nav.user" active="${'/user/list'.equals(current)}"/>
                    </c:if>
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