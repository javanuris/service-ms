<%@tag pageEncoding="UTF-8" %>
<%@attribute name="commentList" type="java.util.List" %>
<%@attribute name="addCommentRef" type="java.lang.String" %>
<%@attribute name="allCommentRef" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="dat" uri="/WEB-INF/date_value.tld" %>
<div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
    <div class="panel panel-default">
        <div class="panel-body">
            <a href="${addCommentRef}" role="button" class="btn btn-default col-xs-12" name="add-comment">
                <fmt:message bundle="${ui}" key="control.comment.label"/>
            </a>
            <c:forEach var="comment" items="${commentList}">
                <div class="col-xs-12">
                    <hr style="margin: 10px 0;">
                    <c:if test="${not empty comment.photoId}">
                        <div class="col-xs-12">
                            <div style="width: 100%; text-align: center;">
                                <img id="image-${comment.id}" src="${requestScope.commentPhotoRef}${comment.photoId}" alt="<fmt:message bundle="${ui}" key="message.photo-empty"/>"
                                     class="img-thumbnail" style="max-width: 100%; min-width: 100%; height: auto; align-content: center;">
                            </div>
                        </div>
                    </c:if>
                    <div class="col-xs-12" style="font-size: medium; margin: 5px 0;">${comment.message}</div>
                    <div class="col-xs-12" style="padding: 5px; margin-top: 5px; background-color: whitesmoke; border-radius: 5px;">
                        <div class="row" style="display: flex; justify-content: center;">
                            <div class="col-xs-5" style="text-align: left; align-self: center;">
                                <small><dat:dateValue locale="${language}" stringValue="${comment.created}"/></small>
                            </div>
                            <c:choose>
                                <c:when test="${not empty comment.user.avatarId}">
                                    <div class="col-xs-5" style="text-align: right; align-self: center;">
                                        <small>${comment.user.name}</small>
                                    </div>
                                    <div class="col-xs-2">
                                        <img id="image-${comment.user.id}" src="${requestScope.userAvatarRef}${comment.user.avatarId}"
                                             class="img-rounded" style="max-width: 100%; min-width: 100%; height: auto; align-content: center;">
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="col-xs-7" style="text-align: right; align-self: center;">
                                        <small>${comment.user.name}</small>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
            <c:if test="${not empty allCommentRef}">
                <div class="col-xs-12" style="margin-top: 10px;">
                    <a href="${allCommentRef}" role="button" class="btn btn-default col-xs-12" name="all-comment">
                        <fmt:message bundle="${ui}" key="control.goto-comment-history.label"/>
                    </a>
                </div>
            </c:if>
        </div>
    </div>
</div>