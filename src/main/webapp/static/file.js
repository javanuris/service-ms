function uploadToServer(upload) {
    var label = document.getElementById(upload.id + '-label');
    var text = document.getElementById(upload.id + '-hidden');
    var file = upload.files[0];
    label.innerHTML = upload.getAttribute("placeholder");
    var formData = new FormData();
    formData.append('upload', file, file.name);
    var xhr = new XMLHttpRequest();
    xhr.open('POST', upload.name + '/' + file.name, true);
    xhr.onload = function () {
        if (xhr.status === 200) {
            label.innerHTML = file.name;
            text.value = xhr.responseText;
        } else {
            label.innerHTML = text.getAttribute('placeholder');
            text.value = '';
        }
    };
    xhr.send(formData);
};
