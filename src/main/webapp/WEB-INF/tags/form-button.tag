<%@tag pageEncoding="UTF-8" %>
<%@attribute name="type" type="java.lang.String" %>
<%@attribute name="name" type="java.lang.String" %>
<%@attribute name="label" type="java.lang.String" %>
<%@attribute name="action" type="java.lang.String" %>
<%@attribute name="messageList" type="java.lang.Object" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="col-xs-12" style="padding: 5px 0;">
<tags:alert messageList="${messageList}"/>
<c:choose>
    <c:when test="${'submit'.equals(type)}">
        <button type="submit" class="btn btn-default col-xs-12" name="${name}">
            <fmt:message bundle="${ui}" key="${label}"/>
        </button>
    </c:when>
    <c:when test="${'button'.equals(type)}">
        <a href="${action}" role="button" class="btn btn-default col-xs-12" name="${name}">
            <fmt:message bundle="${ui}" key="${label}"/>
        </a>
    </c:when>
</c:choose>