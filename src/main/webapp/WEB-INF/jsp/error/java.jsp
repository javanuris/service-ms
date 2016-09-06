<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:template>
    <jsp:attribute name="title">title.error.java</jsp:attribute>
    <jsp:body>
        <div class="col-lg-offset-2 col-lg-8 col-md-offset-1 col-md-10 col-sm-offset-1 col-sm-10 col-xs-offset-0 col-xs-12">
            <div class="panel panel-danger">
                <div class="panel-body" style="text-align: center; color: red;">
                    <h1>${pageContext.errorData.statusCode} <small><fmt:message bundle="${ui}" key="message.error"/></small></h1>
                    <h4><small><fmt:message bundle="${ui}" key="message.requested"/></small> ${pageContext.errorData.requestURI}</h4>
                    <c:if test="${true.equals(debugMode)}">
                        <h5>${pageContext.errorData.throwable.message}<small>${pageContext.errorData.throwable.cause}</small></h5>
                        <div class="col-xs-12" style="word-wrap: break-word; text-align: left; color: dimgrey;">
                            <c:forEach var="stackTrace" items="${pageContext.errorData.throwable.stackTrace}">
                                ${stackTrace}
                            </c:forEach>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </jsp:body>
</tags:template>