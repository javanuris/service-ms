<%@tag pageEncoding="UTF-8" %>
<%@attribute name="formComponent" type="com.epam.java.rt.lab.component.FormComponent" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
    <div class="panel panel-default">
        <div class="panel-body">
            <tags:form formComponent="${formComponent}"/>
        </div>
    </div>
</div>
