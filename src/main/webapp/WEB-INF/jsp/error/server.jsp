<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:template>
    <jsp:attribute name="title">title.error.server</jsp:attribute>
    <jsp:body>
        <div class="col-lg-offset-2 col-lg-8 col-md-offset-1 col-md-10 col-sm-offset-1 col-sm-10 col-xs-offset-0 col-xs-12">
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
</tags:template>