<%@tag pageEncoding="UTF-8" %>
<%@attribute name="active" type="java.lang.String" %>
<%@attribute name="href" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<li class="${active}">
    <a href="${href}">${sessionScope.userName}
        <c:if test="${empty sessionScope.userName}">
            <fmt:message bundle="${ui}" key="message.no-name"/>
        </c:if>
    </a>
</li>
