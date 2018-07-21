/**
 * 
 */
//var JSZip = require("jszip");

var formId = document.getElementById("multiFilesFormId");
var statusFilesId = formId.elements["statusFiles"];
var motionFilesId = formId.elements["motionFiles"];
formId.elements["summit"].onclick = filterFunction;
function filterFunction(){
	//Judgement on the input
	var statusFiles = statusFilesId.files;
	var motionFiles = motionFilesId.files;
	if(statusFiles.length == motionFiles.length){
		var formData = new FormData();
		//var statusFilesZip = new JSZip();
		//var motionFilesZip = new JSZip();
		var TAG = true;
		
		for(var i=0; i<statusFiles.length; i++){
			var statusFileUser = statusFiles[i].name.split("_")[0];
        	var statusFileTimeStamp = (statusFiles[i].name.split("_"))[1] + statusFiles[i].name.split("_")[2].substring(0, 6);
        	var motionFileUser = motionFiles[i].name.split("_")[0];
        	var motionFileTimeStamp = motionFiles[i].name.split("_")[1] + motionFiles[i].name.split("_")[2];
        	if(statusFileUser!=motionFileUser || statusFileTimeStamp!=motionFileTimeStamp){
        		TAG = false;
        	}else{
        		formData.append(statusFiles[i].name, statusFiles[i]);
        		formData.append(motionFiles[i].name, motionFiles[i]);
        		//statusFilesZip.file(statusFiles[i]);
        	}
		}
		if(TAG){
			$.ajax({
				url: jsRoutes.controllers.UploadFilesController.upload().url,
				data: formData,
				type: 'POST',
				async: false,
				cache: false,
				processData: false,
				contentType: false,
				enctype: 'multipart/form-data',
				success: function(){
					alert("Upload success!");
				},
				error: function(){
					console.log("Make call failed");
				}
			});
			//alert("Files of status and motion match");
		}else{
			alert("Files of status and motion does not match");
		}
	}else{
	alert("The amount of status files and motion files does not pair");
	}
}
