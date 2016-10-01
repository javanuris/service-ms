<%@tag pageEncoding="UTF-8" %>
<%@attribute name="active" type="java.lang.String" %>
<%@attribute name="href" type="java.lang.String" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<li class="${active}">
    <a href="${href}">${sessionScope.user.name}
        <c:if test="${empty sessionScope.user.name}">
            <fmt:message bundle="${ui}" key="message.user"/>
        </c:if>
    </a>
</li>
