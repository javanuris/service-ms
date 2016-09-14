<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage="/WEB-INF/jsp/error/java.jsp" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:template>
    <jsp:attribute name="title">title.develop</jsp:attribute>
    <jsp:attribute name="navbarCurrent">/develop</jsp:attribute>
    <jsp:body>
        <tags:nav navCurrent="/develop"/>
        <form action="${pageContext.request.contextPath}/develop" method="POST">
            <div class="form-control">
                <input type="text" class="form-control"/>
            </div>
            <div class="form-control">
                <div id="label-browse-file" hidden>BROWSE FILE...</div>
                <div id="label-file-uploading" hidden>FILE UPLOADING...</div>
                <div id="label-upload-error" hidden>FILE UPLOAD ERROR</div>
                <label class="btn btn-default btn-file" style="position: relative; overflow: hidden; width: 100%; text-align: center;">
                    <input id="upload-file" type="file" name="/file/upload"/>
                    <span id="upload-label">NO FILE SELECTED</span>
                    <input id="upload-hidden" type="text" hidden/>
                </label>
            </div>
            <button type="submit">SUBMIT</button>
        </form>
    </jsp:body>
</tags:template>

<script type="application/javascript">
    $(function() {
        $('#upload-label').text($('#label-browse-file').text());
        $('#upload-file').change(function() {
            var $file = $(this)[0].files[0];
            $('#upload-label').text($file.name + ' - ' + $('#label-file-uploading').text());
            var formData = new FormData();
            formData.append('file', $file);
            var xhr = new XMLHttpRequest();
            var async = true;
            alert($(this).name);
            xhr.open('POST', name, async);
            xhr.send(formData);
            xhr.onload = function () {
                var pair = xhr.responseText.split('=');
                if (pair[0] = 'filePath') {
                    $('#upload-hidden').val(pair[1]);
                    $('#upload-label').text(pair[1])
                } else {
                    $('#upload-hidden').val('');
                    $('#upload-label').text($('#label-upload-error').text())
                }
            };
        });
    });
</script>
