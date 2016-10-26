<%@tag pageEncoding="UTF-8" %>
<%@attribute name="commentList" type="java.util.List" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="dat" uri="/WEB-INF/date_value.tld" %>
<a href="${pageContext.request.contextPath}/application/comment?page=${requestScope.page.currentPage}&items=${requestScope.page.itemsOnPage}&id=${requestScope.id}"
   role="button" class="btn btn-default col-xs-12" name="add-comment">
    <fmt:message bundle="${ui}" key="control.comment.label"/>
</a>
<c:forEach var="comment" items="${commentList}">
    <div class="col-xs-12">
        <hr style="margin: 10px 0;">
        <c:if test="${not empty comment.photoId}">
            <div class="col-xs-12">
                <div style="width: 100%; text-align: center;">
                    <img id="image-${comment.id}" src="${pageContext.request.contextPath}/file/download/photo?id=${comment.photoId}" alt="<fmt:message bundle="${ui}" key="message.photo-empty"/>"
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
                            <img id="image-${comment.user.id}" src="${pageContext.request.contextPath}/file/download/avatar?id=${comment.user.avatarId}"
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