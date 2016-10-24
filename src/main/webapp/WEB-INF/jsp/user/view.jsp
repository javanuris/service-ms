<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.user.view"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/user/list"/>
        <div class="container-fluid" style="margin: 70px 0 30px 0;">
            <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <tags:view-element type="text" label="control.first-name.label" value="${requestScope.firstName}"/>
                        <tags:view-element type="text" label="control.middle-name.label" value="${requestScope.middleName}"/>
                        <tags:view-element type="text" label="control.last-name.label" value="${requestScope.lastName}"/>
                        <tags:view-element type="image" label="control.avatar.label" value="${requestScope.avatarDownload}"/>
                        <tags:view-element type="text" label="control.email.label" value="${requestScope.loginEmail}"/>
                        <tags:view-element type="dictionary.role" label="control.role.label" value="${requestScope.roleName}"/>
                        <tags:view-element type="text" label="control.attempt-left.label" value="${requestScope.loginAttemptLeft}"/>
                        <tags:view-element type="dictionary.status" label="control.status.label" value="${requestScope.loginStatus}"/>
                        <tags:form-button type="button" label="control.button.edit-profile.label" action="${pageContext.request.contextPath}/user/edit?page=${requestScope.page.currentPage}&items=${requestScope.page.itemsOnPage}&id=${requestScope.id}"/>
                        <tags:form-button type="button" label="control.button.goto-user-list.label" action="${pageContext.request.contextPath}/user/list?page=${requestScope.page.currentPage}&items=${requestScope.page.itemsOnPage}"/>
                    </div>
                </div>
            </div>
        </div>
        <tags:footer/>
    </body>
</html>