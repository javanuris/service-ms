<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.profile.edit"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/profile/view"/>
        <div class="container-fluid" style="margin: 70px 0 50px 0;">
            <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <form name="edit-form" action="" method="POST">
                            <tags:form-control type="text" name="firstName" label="control.first-name.label" value="${requestScope.firstName}"/>
                            <tags:form-control type="text" name="middleName" label="control.middle-name.label" value="${requestScope.middleName}"/>
                            <tags:form-control type="text" name="lastName" label="control.last-name.label" value="${requestScope.lastName}"/>
                            <tags:form-control type="avatar" name="avatarDownload" label="control.avatar.label" value="${requestScope.avatarDownload}"/>
                            <tags:form-button type="submit" name="submitSaveProfile" label="control.submit.save-profile.label" messageList="${requestScope.formMessageList}"/>
                            <tags:form-button type="submit" name="submitRemoveAvatar" label="control.submit.remove-avatar.label"/>
                            <tags:form-button type="button" name="buttonGotoProfile" label="control.button.goto-profile.label" action="${pageContext.request.contextPath}/profile/view"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <tags:footer/>
    </body>
</html>