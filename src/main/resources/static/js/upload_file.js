'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

function uploadSingleFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadFile", false);

    xhr.onload = function() {
        var response = JSON.parse(xhr.responseText);
        var responseString = JSON.parse(JSON.stringify(xhr.responseText));
            var fn = response.fileName
            var ext = fn.slice((fn.lastIndexOf(".") - 1 >>> 0) + 2)
        if (ext != "pcap") {
                    singleFileUploadError.innerHTML = "Please select a .pcap file";
                    singleFileUploadError.style.display = "block";
        }
        if(xhr.status == 200 && ext == "pcap") {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p> Uploaded Successfully.</p>";
                var xhr2 = new XMLHttpRequest();
                xhr2.open("GET", "/readUploaded");
                xhr2.send(formData);
                window.location.assign("https://tcpviz.herokuapp.com/readUploaded");
        } else if (xhr.status == 200 && ext != "pcap") {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>Error: Not a .pcap file.</p>";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Upload Error Occurred";
        }
    }
    xhr.send(formData);
}

singleUploadForm.addEventListener('submit', function(event){
    var files = singleFileUploadInput.files;
    if(files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a .pcap file";
        singleFileUploadError.style.display = "block";
    }
    uploadSingleFile(files[0]);
    event.preventDefault();
}, true);