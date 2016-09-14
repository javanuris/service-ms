<%@tag pageEncoding="UTF-8" %>
<%@attribute name="formComponent" type="com.epam.java.rt.lab.component.FormComponent" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form name="${formComponent.name}" action="<c:url value="${formComponent.action}"/>" method="POST">
    <c:forEach var="item" items="${formComponent.formItemArray}">
        <c:choose>
            <c:when test="${item.type.equals('submit') || item.type.equals('button')}">
                <div class="col-xs-12" style="padding: 5px 0;">
                    <tags:validation validationMessageArray="${item.validationMessageArray}"/>
                    <c:choose>
                        <c:when test="${item.type.equals('submit')}">
                            <button type="${item.type}" class="btn btn-default col-xs-12" id="${item.label}">
                                <fmt:message bundle="${ui}" key="${item.label}"/>
                            </button>
                        </c:when>
                        <c:when test="${item.type.equals('button')}">
                            <a href="${item.placeholder}" role="${item.type}" class="btn btn-default col-xs-12" id="${item.label}">
                                <fmt:message bundle="${ui}" key="${item.label}"/>
                            </a>
                        </c:when>
                    </c:choose>
                </div>
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
            <c:when test="${item.type.equals('file')}">
                <div class="form-group${not empty item.validationMessageArray ? ' has-error' : ''}">
                    <label for="${item.label}"><fmt:message bundle="${ui}" key="${item.label}"/></label>
                    <label class="btn btn-default btn-file">
                        <input id="${item.label}" onchange="uploadToServer(this)" type="file" name="${item.placeholder}"/>
                        <div id="${item.label}-label"><fmt:message bundle="${ui}" key="message.browse-file"/></div>
                        <input id="${item.label}-hidden" name="${item.label}" hidden/>
                        <tags:validation validationMessageArray="${item.validationMessageArray}"/>
                    </label>
                </div>
            </c:when>
            <c:when test="${item.type.equals('image')}">
                <div class="form-group${not empty item.validationMessageArray ? ' has-error' : ''}">
                    <label for="${item.label}"><fmt:message bundle="${ui}" key="${item.label}"/></label>
                    <label class="btn btn-default btn-file">
                        <input id="${item.label}" onchange="uploadToServer(this)" type="file" name="${item.placeholder}"/>
                        <c:if test="${not empty item.value}">
                            <div style="width: 100%; text-align: center;">
                                <img id="${item.label}-image" src="${item.value}" alt="avatar" class="img-thumbnail" style="max-width: 100%; min-width: 100%; height: auto; align-content: center;">
                            </div>
                        </c:if>
                        <div id="${item.label}-label"><fmt:message bundle="${ui}" key="message.browse-file"/></div>
                        <input id="${item.label}-hidden" name="${item.label}" hidden/>
                        <tags:validation validationMessageArray="${item.validationMessageArray}"/>
                    </label>
                </div>
            </c:when>
            <c:otherwise>
                <div class="form-group${not empty item.validationMessageArray ? ' has-error' : ''}">
                    <label for="${item.label}"><fmt:message bundle="${ui}" key="${item.label}"/></label>
                    <c:set var="placeholder"><fmt:message bundle="${ui}" key="${item.placeholder}"/></c:set>
                    <input type="${item.type}" class="form-control" name="${item.label}" placeholder="${placeholder}" value="${item.value}"/>
                    <tags:validation validationMessageArray="${item.validationMessageArray}"/>
                </div>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <div id="ui-local-browse-file" hidden><fmt:message bundle="${ui}" key="message.browse-file"/></div>
    <div id="ui-local-uploading-file" hidden><fmt:message bundle="${ui}" key="message.file-uploading"/></div>
    <div id="ui-local-upload-error" hidden><fmt:message bundle="${ui}" key="message.upload-error"/></div>
</form>