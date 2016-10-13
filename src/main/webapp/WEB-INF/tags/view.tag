<%@tag pageEncoding="UTF-8" %>
<%@attribute name="view" type="com.epam.java.rt.lab.web.component.view.View" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="dat" uri="/WEB-INF/date_value.tld" %>
<c:forEach var="control" items="${view.iterator()}">
    <div class="col-xs-12" style="padding: 5px 0;">
        <c:choose>
            <c:when test="${control.type.equals('button')}">
                <a href="${control.action}" role="${control.type}" class="btn btn-default col-xs-12" id="${control.label}">
                    <fmt:message bundle="${ui}" key="${control.label}"/>
                </a>
            </c:when>
            <c:when test="${control.type.equals('image')}">
                <div class="col-xs-4" style="color: darkgray; text-align: right;"><fmt:message bundle="${ui}" key="${control.label}"/>:</div>
                <div class="col-xs-8" style="text-align: center;">
                    <img src="${control.value}" alt="<fmt:message bundle="${ui}" key="message.avatar-empty"/>" class="img-thumbnail" style="max-width: 100%; min-width: 100%; height: auto; align-content: center;">
                </div>
            </c:when>
            <c:otherwise>
                <div class="col-xs-4" style="color: darkgray; text-align: right;"><fmt:message bundle="${ui}" key="${control.label}"/>:</div>
                <div class="col-xs-8" style="font-size: medium;">
                    <c:choose>
                        <c:when test="${'date'.equals(control.localePrefix)}">
                            <dat:dateValue locale="${language}" stringValue="${control.value}"/>
                        </c:when>
                        <c:when test="${control.localePrefix.length() > 0}">
                            <fmt:message bundle="${ui}" key="${control.localePrefix.concat('.').concat(control.value)}"/>
                        </c:when>
                        <c:otherwise>${control.value}</c:otherwise>
                    </c:choose>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</c:forEach>
