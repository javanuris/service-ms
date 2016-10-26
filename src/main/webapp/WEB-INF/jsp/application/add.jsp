<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.application.add"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/application/list"/>
        <div class="container-fluid" style="margin: 70px 0 30px 0;">
            <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <form name="add-form" action="" method="POST">
                            <tags:form-control type="select" name="parentCategory" label="control.parent-category.label" value="${requestScope.parentCategory}"/>
                            <tags:form-control type="text" name="applicationMessage" label="control.message.label" value="${requestScope.applicationMessage}"/>
                            <tags:form-button type="submit" name="submitAddApplication" label="control.submit.add-application.label" messageList="${requestScope.formMessageList}"/>
                            <tags:form-button type="button" name="buttonGotoApplicationList" label="control.button.goto-application-list.label" action="${pageContext.request.contextPath}/application/list?page=${requestScope.page.currentPage}&items=${requestScope.page.itemsOnPage}"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <tags:footer/>
    </body>
</html>