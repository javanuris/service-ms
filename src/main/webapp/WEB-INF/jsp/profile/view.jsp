<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.profile.view"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/profile/view"/>
        <div class="container-fluid" style="margin: 70px 0 50px 0;">
            <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <tags:view-element type="text" label="control.first-name.label" value="${requestScope.firstName}"/>
                        <tags:view-element type="text" label="control.middle-name.label" value="${requestScope.middleName}"/>
                        <tags:view-element type="text" label="control.last-name.label" value="${requestScope.lastName}"/>
                        <tags:view-element type="image" label="control.avatar.label" value="${requestScope.avatarDownload}"/>
                        <tags:form-button type="button" label="control.button.reset-password.label" action="${pageContext.request.contextPath}/profile/reset" />
                        <tags:form-button type="button" label="control.button.edit-profile.label" action="${pageContext.request.contextPath}/profile/edit" />
                    </div>
                </div>
            </div>
        </div>
        <tags:footer/>
    </body>
</html>