<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.home"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/home"/>
        <div class="container-fluid" style="margin: 70px 0 30px 0;">
            <div class="jumbotron" style="text-align: center">
                <c:choose>
                    <c:when test="${not empty sessionScope.activationRef}">
                        <h2><fmt:message bundle="${ui}" key="message.info.activate.header"/></h2>
                        <h4><fmt:message bundle="${ui}" key="message.info.activate.text"/></h4>
                        <a href="${sessionScope.activationRef}">
                            <fmt:message bundle="${ui}" key="message.info.activate.link"/>${sessionScope.activationEmail}
                        </a>
                    </c:when>
                    <c:when test="${not empty sessionScope.restoreRef}">
                        <h2><fmt:message bundle="${ui}" key="message.info.restore.header"/></h2>
                        <h4><fmt:message bundle="${ui}" key="message.info.restore.text"/></h4>
                        <a href="${sessionScope.restoreRef}">
                            <fmt:message bundle="${ui}" key="message.info.restore.link"/>${sessionScope.restoreEmail}
                        </a>
                    </c:when>
                    <c:when test="${not empty requestScope.message}">
                        <h4><fmt:message bundle="${ui}" key="${requestScope.message}"/></h4>
                    </c:when>
                    <c:otherwise>
                        <h2><fmt:message bundle="${ui}" key="title.home"/></h2>
                        <h3><fmt:message bundle="${ui}" key="message.info.java-lab-19"/></h3>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <tags:footer/>
    </body>
</html>