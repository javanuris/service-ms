<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<tags:layout>
    <jsp:attribute name="title"><fmt:message bundle="${ui}" key="navbar.profile.login"/></jsp:attribute>
    <jsp:body>
        <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
            <div class="panel panel-default">
                <div class="panel-body">
                    <tags:form formComponent="${sessionScope.loginForm}"/>
                </div>
            </div>
        </div>
    </jsp:body>
</tags:layout>