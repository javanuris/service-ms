<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:locale/>
<html lang="${language}">
    <tags:head>
        <jsp:attribute name="title">
            <fmt:message bundle="${ui}" key="title.application.comment"/>
        </jsp:attribute>
    </tags:head>
    <body>
        <tags:header current="/application/list"/>
        <div class="container-fluid" style="margin: 70px 0 30px 0;">
            <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <form name="comment-form" action="" method="POST">
                            <tags:form-control type="photo" name="photoDownload" label="control.photo.label" value="${requestScope.photoDownload}"/>
                            <tags:form-control type="text" name="commentMessage" label="control.message.label" value="${requestScope.commentMessage}"/>
                            <tags:form-button type="submit" name="submitAddComment" label="control.submit.add-comment.label" messageList="${requestScope.formMessageList}"/>
                            <tags:form-button type="button" name="buttonGotoApplication" label="control.button.goto-application.label" action="${pageContext.request.contextPath}/application/view?page=${requestScope.page.currentPage}&items=${requestScope.page.itemsOnPage}&id=${requestScope.id}"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <tags:footer/>
    </body>
</html>