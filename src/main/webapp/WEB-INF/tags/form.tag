<%@tag pageEncoding="UTF-8" %>
<%@attribute name="formComponent" type="com.epam.java.rt.lab.component.FormComponent" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form name="${formComponent.name}" action="${formComponent.action}" method="POST">
    <c:forEach var="item" items="${formComponent.formItemArray}">
        <c:choose>
            <c:when test="${item.type=='submit'}">
                <button type="${item.type}" class="btn btn-default col-xs-12" id="${item.label}">
                    <fmt:message bundle="${ui}" key="${item.label}"/>
                </button>
            </c:when>
            <c:otherwise>
                <div class="form-group">
                    <label for="${item.label}"><fmt:message bundle="${ui}" key="${item.label}"/></label>
                    <c:set var="placeholder"><fmt:message bundle="${ui}" key="${item.placeholder}"/></c:set>
                    <input type="${item.type}" class="form-control" name="${item.label}" placeholder="${placeholder}" value="${item.value}"/>
                </div>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</form>