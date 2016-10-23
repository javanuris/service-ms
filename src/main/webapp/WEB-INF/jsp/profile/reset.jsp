<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.profile.reset-password"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/profile/view"/>
        <div class="container-fluid" style="margin: 70px 0 50px 0;">
            <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <form name="reset-form" action="" method="POST">
                            <tags:form-control type="password" name="password" label="control.current-password.label" value="${requestScope.password}"/>
                            <tags:form-control type="password" name="newPassword" label="control.new-password.label" value="${requestScope.newPassword}"/>
                            <tags:form-control type="password" name="repeatPassword" label="control.repeat-password.label" value="${requestScope.repeatPassword}"/>
                            <tags:form-button type="submit" name="submitReset" label="control.submit.reset-password.label" messageList="${requestScope.formMessageList}"/>
                            <tags:form-button type="button" name="buttonGotoProfile" label="control.button.goto-profile.label" action="${pageContext.request.contextPath}/profile/view"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <tags:footer/>
    </body>
</html>