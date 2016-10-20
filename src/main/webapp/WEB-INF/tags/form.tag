<%@tag pageEncoding="UTF-8" %>
<%@attribute name="form" type="com.epam.java.rt.lab.web.component.form.Form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form name="${form.name}" action="<c:url value="${form.action}"/>" method="POST">
    <c:forEach var="control" items="${form.iterator()}">
        <c:choose>
            <c:when test="${'submit'.equals(control.type) || 'button'.equals(control.type)}">
                <div class="col-xs-12" style="padding: 5px 0;">
                    <tags:alert validationMessageList="${control.validationMessageList}"/>
                    <c:choose>
                        <c:when test="${'submit'.equals(control.type)}">
                            <button type="${control.type}" class="btn btn-default col-xs-12" name="${control.name}">
                                <fmt:message bundle="${ui}" key="${control.label}"/>
                            </button>
                        </c:when>
                        <c:when test="${'button'.equals(control.type)}">
                            <a href="${control.action}${control.actionParameters}" role="${control.type}" class="btn btn-default col-xs-12" name="${control.name}">
                                <fmt:message bundle="${ui}" key="${control.label}"/>
                            </a>
                        </c:when>
                    </c:choose>
                </div>
            </c:when>
            <c:when test="${'checkbox'.equals(control.type)}">
                <div class="form-group">
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="${control.name}" value="${control.value}">
                            <fmt:message bundle="${ui}" key="${control.label}"/>
                        </label>
                    </div>
                </div>
            </c:when>
            <c:when test="${'multi-select'.equals(control.type)}">
                <div class="form-group${not empty control.validationMessageList ? ' has-error' : ''}">
                    <label for="${control.name}"><fmt:message bundle="${ui}" key="${control.label}"/></label>
                    <select multiple class="form-control" name="${control.name}">
                        <c:forEach var="option" items="${control.availableValueList}">
                            <c:choose>
                                <c:when test="${control.genericValue.contains(option.label)}">
                                    <option value="${option.value}" selected>${option.label}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${option.value}">${option.label}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <tags:alert validationMessageList="${control.validationMessageList}"/>
                </div>
            </c:when>
            <c:when test="${'select'.equals(control.type)}">
                <div class="form-group${not empty control.validationMessageList ? ' has-error' : ''}">
                    <label for="${control.name}"><fmt:message bundle="${ui}" key="${control.label}"/></label>
                    <select class="form-control" name="${control.name}">
                        <c:forEach var="option" items="${control.availableValueList}">
                            <c:choose>
                                <c:when test="${option.value.equals(control.value)}">
                                    <option value="${option.value}" selected>${option.label}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${option.value}">${option.label}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <tags:alert validationMessageList="${control.validationMessageList}"/>
                </div>
            </c:when>
            <c:when test="${'file'.equals(control.type)}">
                <div class="form-group${not empty control.validationMessageList ? ' has-error' : ''}">
                    <label for="${control.name}"><fmt:message bundle="${ui}" key="${control.label}"/></label>
                    <label class="btn btn-default btn-file">
                        <input id="${control.label}" onchange="uploadToServer(this)" type="file" name="${control.placeholder}"/>
                        <div id="${control.label}-label"><fmt:message bundle="${ui}" key="message.browse-file"/></div>
                        <input id="${control.label}-hidden" name="${control.label}" value="${control.value}" hidden/>
                    </label>
                    <tags:alert validationMessageList="${control.validationMessageList}"/>
                </div>
            </c:when>
            <c:when test="${'image'.equals(control.type)}">
                <div class="form-group${not empty control.validationMessageList ? ' has-error' : ''}">
                    <label for="${control.name}"><fmt:message bundle="${ui}" key="${control.label}"/></label>
                    <label class="btn btn-default btn-file">
                        <input id="${control.name}" onchange="uploadToServer(this)" type="file" name="${control.action}"/>
                        <div style="width: 100%; text-align: center;">
                            <img id="${control.name}-image" src="${control.value}" alt="<fmt:message bundle="${ui}" key="message.avatar-empty"/>"
                                 class="img-thumbnail" style="max-width: 100%; min-width: 100%; height: auto; align-content: center;">
                        </div>
                        <div id="${control.name}-label"><fmt:message bundle="${ui}" key="message.browse-file"/></div>
                        <input id="${control.name}-hidden" name="${control.name}" value="${control.value}" hidden/>
                    </label>
                    <tags:alert validationMessageList="${control.validationMessageList}"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="form-group${not empty control.validationMessageList ? ' has-error' : ''}">
                    <label for="${control.name}"><fmt:message bundle="${ui}" key="${control.label}"/></label>
                    <c:set var="placeholder"><fmt:message bundle="${ui}" key="${control.placeholder}"/></c:set>
                    <input type="${control.type}" class="form-control" name="${control.name}" placeholder="${placeholder}" value="${control.value}"/>
                    <tags:alert validationMessageList="${control.validationMessageList}"/>
                </div>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <div id="ui-local-browse-file" hidden><fmt:message bundle="${ui}" key="message.browse-file"/></div>
    <div id="ui-local-uploading-file" hidden><fmt:message bundle="${ui}" key="message.file-uploading"/></div>
    <div id="ui-local-upload-error" hidden><fmt:message bundle="${ui}" key="message.upload-error"/></div>
</form>