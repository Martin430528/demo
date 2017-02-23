/**图片文件上传*/
var _upload = new RSUpload();
function RSUpload(){}
RSUpload.prototype.uploadImg = function(callback){
	$("body").append('<div id="uploadblock"></div>');
	$("#uploadblock").append('<form id="uploadForm" method="post" enctype="multipart/form-data">');
	$("#uploadblock").append('<input type="file" name="file" id="inputfile" class = "uploadInput" style = "display:none;" onchange="uploadFile('+callback+');">');
	$("#uploadblock").append('</form>');
	$("#inputfile").click();
}

/**上传*/
function uploadFile(callback){
	var formData = new FormData($("#uploadForm")[0]);
	$.ajax({
		url : '/upload/kindeditorJson',
		type : 'POST',
		data : formData,
		async : false,
		cache : false,
		contentType : false,
		processData : false,
		success : function(data) {
			if (data.error == 0) {
				floatmsg("图片上传成功")
				if(callback) callback(data.url);
			}else{
				wxalert("图片上传失败");
			}
		},
		error:function(XmlHttpRequest,textStatus, errorThrown)
		{
			//alert("保存失败;"+XmlHttpRequest.responseText);
		}
	});
}
