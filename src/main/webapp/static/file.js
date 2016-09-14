function uploadToServer(upload) {
    var $uploadId = upload.id.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
    var $imageId = '#' + $uploadId + '-image';
    var $labelId = '#' + $uploadId + '-label';
    var $hiddenId = '#' + $uploadId + '-hidden';
    var $file = upload.files[0];
    $($labelId).text($file.name + ' - ' + $('#ui-local-uploading-file').text());
    var $formData = new FormData();
    $formData.append('file', $file);
    var $xhr = new XMLHttpRequest();
    var async = true;
    $xhr.open('POST', upload.name, async);
    $xhr.onload = function() {
        var pair = $xhr.responseText.split('=');
        if (pair[0] == "filePath") {
            $($hiddenId).val(pair[1]);
            $($labelId).text($file.name);
            var src = $($imageId).attr("src");
            var path = src.split("/download/");
            $($imageId).attr("src", path[0] + "/download/pre-avatar?path=" + pair[1]);
        } else {
            $($hiddenId).val('');
            $($labelId).text($('#ui-local-upload-error').text());
        }
    };
    $xhr.send($formData);
};
