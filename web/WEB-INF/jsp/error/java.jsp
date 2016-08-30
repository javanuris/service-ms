<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:layout>
    <jsp:attribute name="title">error.title.java</jsp:attribute>
    <jsp:body>
        <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
            <div class="panel panel-danger">
                <div class="panel-body" style="text-align: center; color: red;">
                    <h1>${pageContext.errorData.statusCode}<small> error</small></h1>
                    <h4><small>requested </small>${pageContext.errorData.requestURI}</h4>
                    <h5>${requestScope.error}</h5>
                    <h5>${pageContext.errorData.throwable.cause}</h5>
                    <div class="col-xs-12" style="word-wrap: break-word; text-align: left; color: dimgrey;">
                        <c:forEach var="stackTrace" items="${pageContext.errorData.throwable.stackTrace}">
                            ${stackTrace}
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</tags:layout>