<%@tag pageEncoding="UTF-8" %>
<%@attribute name="type" type="java.lang.String" %>
<%@attribute name="name" type="java.lang.String" %>
<%@attribute name="label" type="java.lang.String" %>
<%@attribute name="value" type="com.epam.java.rt.lab.web.component.FormControlValue" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="label"><fmt:message bundle="${ui}" key="${label}"/></c:set>
<c:choose>
    <c:when test="${'checkbox'.equals(type)}">
        <div class="form-group">
            <div class="checkbox">
                <label>
                    <input type="checkbox" name="${name}" ${value.value}>
                    ${label}
                </label>
            </div>
        </div>
    </c:when>
    <c:when test="${'select'.equals(type)}">
        <div class="form-group${not empty value.validationMessageList ? ' has-error' : ''}">
            <label for="${name}">${label}</label>
            <select id="${name}" class="form-control" name="${name}">
                <c:forEach var="option" items="${value.availableValueList}">
                    <c:choose>
                        <c:when test="${option.value.equals(value.value)}">
                            <option value="${option.value}" selected>${option.label}</option>
                        </c:when>
                        <c:otherwise>
                            <option value="${option.value}">${option.label}</option>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </select>
            <tags:alert messageList="${value.validationMessageList}"/>
        </div>
    </c:when>
    <c:when test="${'avatar'.equals(type) || 'photo'.equals(type)}">
        <div class="form-group${not empty value.validationMessageList ? ' has-error' : ''}">
            <label for="${name}">${label}</label>
            <label class="btn btn-default btn-file">
                <input id="${name}" onchange="uploadToServer(this)" type="file" name="${pageContext.request.contextPath}/file/upload/${type}"/>
                <div style="width: 100%; text-align: center;">
                    <img id="${name}-image" src="${value.value}" alt="<fmt:message bundle="${ui}" key="message.avatar-empty"/>"
                         class="img-thumbnail" style="max-width: 100%; min-width: 100%; height: auto; align-content: center;">
                </div>
                <div id="${name}-label"><fmt:message bundle="${ui}" key="message.browse-file"/></div>
                <input id="${name}-hidden" name="${name}" value="${value.value}" hidden/>
            </label>
            <tags:alert messageList="${value.validationMessageList}"/>
        </div>
        <div id="ui-local-browse-file" hidden><fmt:message bundle="${ui}" key="message.browse-file"/></div>
        <div id="ui-local-uploading-file" hidden><fmt:message bundle="${ui}" key="message.file-uploading"/></div>
        <div id="ui-local-upload-error" hidden><fmt:message bundle="${ui}" key="message.upload-error"/></div>
    </c:when>
    <c:otherwise>
        <div class="form-group${not empty value.validationMessageList ? ' has-error' : ''}">
            <label for="${name}">${label}</label>
            <input id="${name}" type="${type}" class="form-control" name="${name}" placeholder="${label}" value="${value.value}"/>
            <tags:alert messageList="${value.validationMessageList}"/>
        </div>
    </c:otherwise>
</c:choose>
