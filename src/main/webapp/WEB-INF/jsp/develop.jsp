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
            <input type="text" class="form-control"/>
            <label class="btn btn-default btn-file" style="position: relative; overflow: hidden; width: 100%; text-align: center;">
                <input id="upload" onchange="uploadToServer(this)" type="file" class="form-control" placeholder="LOADING..."
                       style="display: none; position: absolute; top: 0; right: 0; min-width: 100%; min-height: 100%;
                                font-size: 100px; text-align: right; filter: alpha(opacity=0); opacity: 0;
                                outline: none; background: white; cursor: inherit; display: block;"/>
                <div id="upload-label">BROWSE FILE...</div>
                <input id="upload-hidden" type="text" placeholder="BROWSE FILE..." hidden/>
            </label>
            <button type="submit">SUBMIT</button>
        </form>
    </jsp:body>
</tags:template>

<script type="application/javascript">

    function uploadToServer(upload) {
        var label = document.getElementById(upload.id + '-label');
        var text = document.getElementById(upload.id + '-hidden');
        var file = upload.files[0];
        label.innerHTML = upload.getAttribute("placeholder");
        var formData = new FormData();
        formData.append('upload', file, file.name);
        var xhr = new XMLHttpRequest();
        xhr.open('POST', '${pageContext.request.contextPath}/upload', true);
        xhr.onload = function () {
            if (xhr.status === 200) {
                label.innerHTML = file.name;
            } else {
                label.innerHTML = text.getAttribute('placeholder');
            }
        };
        xhr.send(formData);
    };

//    var form = document.getElementById('file-form');
//    var upload = document.getElementById('upload');
//    var uploadButton = document.getElementById('upload-button');
//
//    uploadToServer() {
//
//    };
//
//    upload.onclick = function () {
//        this.value = null;
//    }
//
//    upload.onchange = function(){
//        upload.innerHTML = this.value;
//    }

//    form.onsubmit = function(event) {
//        event.preventDefault();
//
//        // Update button text.
//        uploadButton.innerHTML = 'Uploading...';
//
//        // Get the selected files from the input.
//        var files = upload.files;
//
//        // Create a new FormData object.
//        var formData = new FormData();
//
//        // Loop through each of the selected files.
//        for (var i = 0; i < files.length; i++) {
//            var file = files[i];
//
//            // Check the file type.
//            if (!file.type.match('image.*')) {
//                continue;
//            }
//
//            // Add the file to the request.
//            formData.append('photos[]', file, file.name);
//        }
//
//        // Set up the request.
//        var xhr = new XMLHttpRequest();
//
//        // Open the connection.
//        xhr.open('POST', '/upload', true);
//
//        // Set up a handler for when the request finishes.
//        xhr.onload = function () {
//            if (xhr.status === 200) {
//                // File(s) uploaded.
//                uploadButton.innerHTML = 'Upload';
//                alert(xhr.responseText);
//            } else {
//                alert('An error occurred!');
//            }
//        };
//
//        // Send the Data.
//        xhr.send(formData);
//    }

</script>
