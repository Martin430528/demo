/*******************************************************************************
 * 上传图片
 * 
 */
var _kindEnitor={};
$.loadImgInit=function (callback){	
	var upload=$("[data-tag=uploadImg]");
	if (upload.length<=0){ return;}
	$(".show_image").hover(function(){
		$(this).parent().find("div.type-file-preview").show();
	}, function(){
		$(this).parent().find("div.type-file-preview").hide();
	});
	loadKindEdiet(upload, callback);
}


//加载编辑器
function loadKindEdiet(upload, callback){
	/** 加载JS结束 **/
	KindEditor.ready(function(K) {	
		upload.each(function (){
			var _btn=$(this);// 上传按钮
			_btn.css({"cursor":"pointer"});
			var _class=_btn.attr("data-class"); // 获取图片样式
			if (isNull(_class)){
				alertMsg("上传图片参数出错...",2);
				return;
			}
			var kindeditor_upload_img =null;
			if (isNull(_kindEnitor[_class])){
				kindeditor_upload_img = K.editor({
					uploadJson : '/upload/kindeditorJson?maxWidth=900' 
				});
				_kindEnitor[_class]=kindeditor_upload_img;
			}else {
				kindeditor_upload_img=_kindEnitor[_class];				
			}			
			var _url=$("."+_class+":hidden").val();
			
			if (!isNull(_url)){
				_btn.parents(".upload-div").find("img[class="+_class+"]").attr("src", _url);
				_btn.parents(".upload-div").find("."+_class+":input").val(_url);
			}
			$(this).parents(".upload-div").find(":button,:text,."+_class+",[data-tag=uploadImg]").on("click",function() {
				kindeditor_upload_img.loadPlugin('image', function() {
					kindeditor_upload_img.plugin.imageDialog( { 
						showRemote : false, 
						imageUrl : $("."+_class+":image").attr("src"), 
						clickFn : function(data) {
							_btn.parents(".upload-div").find("."+_class+":input").val(data);
							_btn.parents(".upload-div").find("img[class="+_class+"]").attr("src", data);
							kindeditor_upload_img.hideDialog();
							if (callback!=null && callback!=undefined){
								callback(data);
							}
						}
					});
				});
			});
		});
	});	
}