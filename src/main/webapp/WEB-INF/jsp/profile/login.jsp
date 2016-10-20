<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.profile.login"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/profile/login"/>
        <div class="container-fluid" style="margin: 70px 0 50px 0;">
            <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <form name="login-form" action="" method="POST">
                            <tags:form-control type="text" name="email" label="control.email.label" value="${requestScope.email}"/>
                            <tags:form-control type="password" name="password" label="control.password.label" value="${requestScope.password}"/>
                            <tags:form-control type="checkbox" name="rememberMe" label="control.remember-me.label" value="${requestScope.rememberMe}"/>
                            <tags:form-button type="submit" name="submitLogin" label="control.submit.login.label" messageList="${requestScope.formMessageList}"/>
                            <tags:form-button type="submit" name="submitRestore" label="control.submit.restore-password.label"/>
                            <tags:form-button type="button" name="buttonRegister" label="control.submit.register.label" action="${pageContext.request.contextPath}/profile/register"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <tags:footer/>
    </body>
</html>