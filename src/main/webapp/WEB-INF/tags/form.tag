<%@tag pageEncoding="UTF-8" %>
<%@attribute name="form" type="com.epam.java.rt.lab.web.component.form.Form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form name="${form.name}" action="<c:url value="${form.action}"/>" method="POST">
    <c:forEach var="formControl" items="${form.iterator()}">
        <c:choose>
            <c:when test="${formControl.type.equals('submit') || formControl.type.equals('button')}">
                <div class="col-xs-12" style="padding: 5px 0;">
                    <tags:validation validationMessageArray="${formControl.validationMessageArray}"/>
                    <c:choose>
                        <c:when test="${formControl.type.equals('submit')}">
                            <button type="${formControl.type}" class="btn btn-default col-xs-12" name="${formControl.placeholder}">
                                <fmt:message bundle="${ui}" key="${formControl.label}"/>
                            </button>
                        </c:when>
                        <c:when test="${formControl.type.equals('button')}">
                            <a href="${formControl.placeholder}" role="${formControl.type}" class="btn btn-default col-xs-12" name="${formControl.placeholder}">
                                <fmt:message bundle="${ui}" key="${formControl.label}"/>
                            </a>
                        </c:when>
                    </c:choose>
                </div>
            </c:when>
            <c:when test="${formControl.type.equals('checkbox')}">
                <div class="form-group">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="${formControl.label}" value="${formControl.value}">
                            <fmt:message bundle="${ui}" key="${formControl.label}"/>
                        </label>
                    </div>
                </div>
            </c:when>
            <c:when test="${formControl.type.equals('file')}">
                <div class="form-group${not empty formControl.validationMessageArray ? ' has-error' : ''}">
                    <label for="${formControl.label}"><fmt:message bundle="${ui}" key="${formControl.label}"/></label>
                    <label class="btn btn-default btn-file">
                        <input id="${formControl.label}" onchange="uploadToServer(this)" type="file" name="${formControl.placeholder}"/>
                        <div id="${formControl.label}-label"><fmt:message bundle="${ui}" key="message.browse-file"/></div>
                        <input id="${formControl.label}-hidden" name="${formControl.label}" hidden/>
                    </label>
                    <tags:validation validationMessageArray="${formControl.validationMessageArray}"/>
                </div>
            </c:when>
            <c:when test="${formControl.type.equals('image')}">
                <div class="form-group${not empty formControl.validationMessageArray ? ' has-error' : ''}">
                    <label for="${formControl.label}"><fmt:message bundle="${ui}" key="${formControl.label}"/></label>
                    <label class="btn btn-default btn-file">
                        <input id="${formControl.label}" onchange="uploadToServer(this)" type="file" name="${formControl.placeholder}"/>
                        <div style="width: 100%; text-align: center;">
                            <img id="${formControl.label}-image" src="${formControl.value}" alt="<fmt:message bundle="${ui}" key="message.avatar-empty"/>"
                                 class="img-thumbnail" style="max-width: 100%; min-width: 100%; height: auto; align-content: center;">
                        </div>
                        <div id="${formControl.label}-label"><fmt:message bundle="${ui}" key="message.browse-file"/></div>
                        <input id="${formControl.label}-hidden" name="${formControl.label}" hidden/>
                    </label>
                    <tags:validation validationMessageArray="${formControl.validationMessageArray}"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="form-group${not empty formControl.validationMessageArray ? ' has-error' : ''}">
                    <label for="${formControl.label}"><fmt:message bundle="${ui}" key="${formControl.label}"/></label>
                    <c:set var="placeholder"><fmt:message bundle="${ui}" key="${formControl.placeholder}"/></c:set>
                    <input type="${formControl.type}" class="form-formControl" name="${formControl.label}" placeholder="${placeholder}" value="${formControl.value}"/>
                    <tags:validation validationMessageArray="${formControl.validationMessageArray}"/>
                </div>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <div id="ui-local-browse-file" hidden><fmt:message bundle="${ui}" key="message.browse-file"/></div>
    <div id="ui-local-uploading-file" hidden><fmt:message bundle="${ui}" key="message.file-uploading"/></div>
    <div id="ui-local-upload-error" hidden><fmt:message bundle="${ui}" key="message.upload-error"/></div>
</form>