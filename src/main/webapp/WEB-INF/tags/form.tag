<%@tag pageEncoding="UTF-8" %>
<%@attribute name="formComponent" type="com.epam.java.rt.lab.component.FormComponent" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form name="${formComponent.name}" action="<c:url value="${formComponent.action}"/>" method="POST">
    <c:forEach var="item" items="${formComponent.formItemArray}">
        <c:choose>
            <c:when test="${item.type.equals('submit') || item.type.equals('button')}">
                <c:if test="${not empty item.validationMessageArray}">
                    <c:forEach var="message" items="${item.validationMessageArray}">
                        <div class="alert alert-danger"><fmt:message bundle="${ui}" key="${message}"/></div>
                    </c:forEach>
                </c:if>
                <c:choose>
                    <c:when test="${item.type.equals('submit')}">
                        <button type="${item.type}" class="btn btn-default col-xs-12" id="${item.label}">
                            <fmt:message bundle="${ui}" key="${item.label}"/>
                        </button>
                    </c:when>
                    <c:when test="${item.type.equals('button')}">
                        <a href="${item.value}" role="${item.type}" class="btn btn-default col-xs-12" id="${item.label}">
                            <fmt:message bundle="${ui}" key="${item.label}"/>
                        </a>
                    </c:when>
                </c:choose>
            </c:when>
            <c:when test="${item.type.equals('checkbox')}">
                <div class="form-group">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="${item.label}" value="${item.value}">
                            <fmt:message bundle="${ui}" key="${item.label}"/>
                        </label>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="form-group${not empty item.validationMessageArray ? ' has-error' : ''}">
                    <label for="${item.label}"><fmt:message bundle="${ui}" key="${item.label}"/></label>
                    <c:set var="placeholder"><fmt:message bundle="${ui}" key="${item.placeholder}"/></c:set>
                    <input type="${item.type}" class="form-control" name="${item.label}" placeholder="${placeholder}" value="${item.value}"/>
                    <c:if test="${not empty item.validationMessageArray}">
                        <c:forEach var="message" items="${item.validationMessageArray}">
                            <span class="help-block"><fmt:message bundle="${ui}" key="${message}"/></span>
                        </c:forEach>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</form>