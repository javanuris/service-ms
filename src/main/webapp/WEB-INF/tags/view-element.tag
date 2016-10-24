<%@tag pageEncoding="UTF-8" %>
<%@attribute name="type" type="java.lang.String" %>
<%@attribute name="label" type="java.lang.String" %>
<%@attribute name="value" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="dat" uri="/WEB-INF/date_value.tld" %>
<div class="col-xs-12" style="padding: 5px 0;">
    <c:choose>
        <c:when test="${'image'.equals(type)}">
            <div class="col-xs-4" style="color: darkgray; text-align: right;"><fmt:message bundle="${ui}" key="${label}"/>:</div>
            <div class="col-xs-8" style="text-align: center;">
                <img src="${value}" alt="<fmt:message bundle="${ui}" key="message.avatar-empty"/>" class="img-thumbnail" style="max-width: 100%; min-width: 100%; height: auto; align-content: center;">
            </div>
        </c:when>
        <c:otherwise>
            <div class="col-xs-4" style="color: darkgray; text-align: right;"><fmt:message bundle="${ui}" key="${label}"/>:</div>
            <div class="col-xs-8" style="font-size: medium;">
                <c:choose>
                    <c:when test="${'date'.equals(type)}">
                        <dat:dateValue locale="${language}" stringValue="${value}"/>
                    </c:when>
                    <c:when test="${type.startsWith('dictionary') && not empty value}">
                        <fmt:message bundle="${ui}" key="${type.concat('.').concat(value)}"/>
                    </c:when>
                    <c:otherwise>${value}</c:otherwise>
                </c:choose>
            </div>
        </c:otherwise>
    </c:choose>
</div>