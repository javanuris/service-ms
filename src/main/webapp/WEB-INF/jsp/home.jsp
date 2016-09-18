<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:template>
    <jsp:attribute name="title">title.home</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/home</jsp:attribute>
    <jsp:body>
        <div class="jumbotron" style="text-align: center">
            <c:choose>
                <c:when test="${not empty sessionScope.activationRef}">
                    <h2><fmt:message bundle="${ui}" key="message.activation.congratulation"/></h2>
                    <h4><fmt:message bundle="${ui}" key="message.activation.info"/></h4>
                    <a href="${sessionScope.activationRef}">
                        <fmt:message bundle="${ui}" key="message.activation.link"/>${sessionScope.activationEmail}
                    </a>
                </c:when>
                <c:otherwise>
                    <h1><fmt:message bundle="${ui}" key="title.home"/></h1>
                    <h2><fmt:message bundle="${ui}" key="message.footer"/></h2>
                </c:otherwise>
            </c:choose>
        </div>
    </jsp:body>
</tags:template>