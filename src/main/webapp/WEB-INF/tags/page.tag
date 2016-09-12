<%@tag pageEncoding="UTF-8" %>
<%@attribute name="pageComponent" type="com.epam.java.rt.lab.component.PageComponent" %>
<%@attribute name="uriWithParameterPrefix" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${pageComponent.countPages > 1}">
    <div style="width: 100%; text-align: center;">
        <div class="btn-group" role="group" aria-label="First" style="margin: 10px 0;">
            <c:choose>
                <c:when test="${pageComponent.currentPage > 1}">
                    <a href="${uriWithParameterPrefix.concat(1)}" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-step-backward" aria-hidden="true" style="margin: 3px -2px;"></span>
                    </a>
                    <a href="${uriWithParameterPrefix.concat(pageComponent.currentPage - 1)}" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-triangle-left" aria-hidden="true" style="margin: 3px -2px;"></span>
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="#" type="button" class="btn btn-default disabled">
                        <span class="glyphicon glyphicon-step-backward" aria-hidden="true" style="margin: 3px -2px;"></span>
                    </a>
                    <a href="#" type="button" class="btn btn-default disabled">
                        <span class="glyphicon glyphicon-triangle-left" aria-hidden="true" style="margin: 3px -2px;"></span>
                    </a>
                </c:otherwise>
            </c:choose>
            <div class="btn-group dropup" role="group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="min-width: 50px;">
                        ${pageComponent.currentPage}
                </button>
                <ul class="dropdown-menu" style="text-align: center; min-width: 50px;">
                    <c:forEach var="index" begin="1" end="${pageComponent.countPages}">
                        <li><a href="${uriWithParameterPrefix.concat(index)}">${index}</a></li>
                    </c:forEach>
                </ul>
            </div>
            <c:choose>
                <c:when test="${pageComponent.currentPage < pageComponent.countPages}">
                    <a href="${uriWithParameterPrefix.concat(pageComponent.currentPage + 1)}" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-triangle-right" aria-hidden="true" style="margin: 3px -2px;"></span>
                    </a>
                    <a href="${uriWithParameterPrefix.concat(pageComponent.countPages)}" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-step-forward" aria-hidden="true" style="margin: 3px -2px;"></span>
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="#" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-triangle-right disabled" aria-hidden="true" style="margin: 3px -2px;"></span>
                    </a>
                    <a href="#" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-step-forward disabled" aria-hidden="true" style="margin: 3px -2px;"></span>
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</c:if>