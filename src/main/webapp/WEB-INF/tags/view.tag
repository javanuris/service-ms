<%@tag pageEncoding="UTF-8" %>
<%@attribute name="viewComponent" type="com.epam.java.rt.lab.component.ViewComponent" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:forEach var="formControl" items="${viewComponent.viewItemArray}">
    <div class="col-xs-12" style="padding: 5px 0;">
        <c:choose>
            <c:when test="${formControl.type.equals('button')}">
                <a href="${formControl.value}" role="${formControl.type}" class="btn btn-default col-xs-12" id="${formControl.label}">
                    <fmt:message bundle="${ui}" key="${formControl.label}"/>
                </a>
            </c:when>
            <c:when test="${formControl.type.equals('image')}">
                <div class="col-xs-4" style="color: darkgray; text-align: right;"><fmt:message bundle="${ui}" key="${formControl.label}"/>:</div>
                <div class="col-xs-8" style="text-align: center;">
                    <img src="${formControl.value}" alt="<fmt:message bundle="${ui}" key="message.avatar-empty"/>" class="img-thumbnail" style="max-width: 100%; min-width: 100%; height: auto; align-content: center;">
                </div>
            </c:when>
            <c:otherwise>
                <div class="col-xs-4" style="color: darkgray; text-align: right;"><fmt:message bundle="${ui}" key="${formControl.label}"/>:</div>
                <div class="col-xs-8" style="font-size: medium;">${formControl.value}</div>
            </c:otherwise>
        </c:choose>
    </div>
</c:forEach>
