<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:template>
    <jsp:attribute name="title">title.category.view</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/category/list</jsp:attribute>
    <jsp:body>
        <div class="col-lg-offset-4 col-lg-4 col-md-offset-3 col-md-6 col-sm-offset-2 col-sm-8 col-xs-offset-1 col-xs-10">
            <div class="panel panel-default">
                <div class="panel-body">
                    <tags:view view="${requestScope.viewCategory}"/>
                </div>
            </div>
        </div>
    </jsp:body>
</tags:template>