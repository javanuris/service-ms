<%@tag pageEncoding="UTF-8" %>
<%@attribute name="page" type="com.epam.java.rt.lab.web.component.Page" %>
<%@attribute name="uriWithQuestionMark" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div style="width: 100%; text-align: center;">
    <div class="btn-group" role="group" style="margin-top: 20px;">
        <c:choose>
            <c:when test="${page.currentPage > 1}">
                <a href="${uriWithQuestionMark.concat(page.getRequestOnPage(1))}" type="button" class="btn btn-default btn-sm">
                    <span class="glyphicon glyphicon-step-backward" aria-hidden="true" style="margin: 3px 0;"></span>
                </a>
                <a href="${uriWithQuestionMark.concat(page.getRequestOnPage(page.currentPage - 1))}" type="button" class="btn btn-default btn-sm">
                    <span class="glyphicon glyphicon-triangle-left" aria-hidden="true" style="margin: 3px 0;"></span>
                </a>
            </c:when>
            <c:otherwise>
                <a href="#" type="button" class="btn btn-default disabled btn-sm">
                    <span class="glyphicon glyphicon-step-backward" aria-hidden="true" style="margin: 3px 0;"></span>
                </a>
                <a href="#" type="button" class="btn btn-default disabled btn-sm">
                    <span class="glyphicon glyphicon-triangle-left" aria-hidden="true" style="margin: 3px 0;"></span>
                </a>
            </c:otherwise>
        </c:choose>
        <div class="btn-group dropup" role="group">
            <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="min-width: 56px;">
                    ${page.currentPage}
            </button>
            <ul class="dropdown-menu" style="text-align: center; min-width: 55px; overflow-x: hidden; overflow-y: auto; max-height: 150px;">
                <c:forEach var="index" begin="1" end="${page.countPages}">
                    <li><a href="${uriWithQuestionMark.concat(page.getRequestOnPage(index))}">${index}</a></li>
                </c:forEach>
            </ul>
        </div>
        <c:choose>
            <c:when test="${page.currentPage < page.countPages}">
                <a href="${uriWithQuestionMark.concat(page.getRequestOnPage(page.currentPage + 1))}" type="button" class="btn btn-default btn-sm">
                    <span class="glyphicon glyphicon-triangle-right" aria-hidden="true" style="margin: 3px 0;"></span>
                </a>
                <a href="${uriWithQuestionMark.concat(page.getRequestOnPage(page.countPages))}" type="button" class="btn btn-default btn-sm">
                    <span class="glyphicon glyphicon-step-forward" aria-hidden="true" style="margin: 3px 0;"></span>
                </a>
            </c:when>
            <c:otherwise>
                <a href="#" type="button" class="btn btn-default disabled btn-sm">
                    <span class="glyphicon glyphicon-triangle-right" aria-hidden="true" style="margin: 3px 0;"></span>
                </a>
                <a href="#" type="button" class="btn btn-default disabled btn-sm">
                    <span class="glyphicon glyphicon-step-forward" aria-hidden="true" style="margin: 3px 0;"></span>
                </a>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="btn-group dropup" role="group" style="margin-top: 20px;">
        <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="min-width: 60px;">
            ${page.itemsOnPage}
        </button>
        <ul class="dropdown-menu" style="text-align: center; min-width: 55px; overflow: auto; max-height: 250px;">
            <c:forEach var="index" begin="${page.itemsOnPageDefault}" end="${page.itemsOnPageDefault * 5}" step="${page.itemsOnPageDefault}">
                <li><a href="${uriWithQuestionMark.concat(page.getRequestOnItems(index))}">${index}</a></li>
            </c:forEach>
        </ul>
    </div>
</div>