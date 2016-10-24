<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:locale/>
<html lang="${language}">
<tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.profile.restore-password"/>
        </jsp:attribute>
</tags:head>
<body>
<tags:header current="/profile/restore"/>
<div class="container-fluid" style="margin: 70px 0 30px 0;">
    <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
        <div class="panel panel-default">
            <div class="panel-body">
                <form name="restore-form" action="" method="POST">
                    <tags:form-control type="password" name="newPassword" label="control.new-password.label" value="${requestScope.newPassword}"/>
                    <tags:form-control type="password" name="repeatPassword" label="control.repeat-password.label" value="${requestScope.repeatPassword}"/>
                    <tags:form-button type="submit" name="submitRestore" label="control.submit.restore-password.label" messageList="${requestScope.formMessageList}"/>
                    <tags:form-button type="button" name="buttonGotoLogin" label="control.button.goto-login.label" action="${pageContext.request.contextPath}/profile/login"/>
                </form>
            </div>
        </div>
    </div>
</div>
<tags:footer/>
</body>
</html>