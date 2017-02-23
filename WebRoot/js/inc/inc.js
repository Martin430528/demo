/*common js*/

/*ajax post*/
function post(url, data, callback) {
	try {
		var async = true;
		if(arguments.length>=4){
			async = arguments[3];
		}
		$.ajax({
			async:  async,
			type : 'POST',
			dataType : 'json',
			url : url,
			data : data,
			success : callback
		});
	} catch (e) {
		alert(e.message);
		return;
	}
}

function post(url, data, callback, dataType) {
	try {
		var async = true;
		if(arguments.length>=5){
			async = arguments[4];
		}
		$.ajax({
			async:  async,
			type : 'POST',
			dataType : dataType,
			url : url,
			data : data,
			success : callback
		});
	} catch (e) {
		alert(e.message);
		return;
	}
}



//获取URL参数值
function getURLParam(name) {
	var reg = new RegExp("(^|\\?|&)" + name + "=([^&]*)(\\s|&|$)", "i");
	if (reg.test(location.href))
		return unescape(RegExp.$2.replace(/\+/g, " "));
	return "";
}

//为输入 框添加内容提示(输入框内容为空时显示灰色提示，点击即消失)
function promptText(id, prompt){
	var obj = $("#"+id);
	var color = obj.css("color");
	if($.trim(obj.val()).length == 0){
		obj.val(prompt);
		obj.css("color", "#bbb");
	}
	obj.focus(function(){
		if(prompt == $(this).val()){
			$(this).val("");
			obj.css("color", color);
		}
	});
	obj.blur(function(){
		if($.trim($(this).val()).length == 0){
			$(this).val(prompt);
			obj.css("color", "#bbb");
		}
	});
}

//过滤字符串
function filterW(str){
	return str.replaceAll("\n","-@-").replaceAll("\\\\","\/").replaceAll("-@-","\\n");
	//.replaceAll("\"","“").replaceAll("\'","‘");
}

/**------------------------------------------------表单--------------------------------------------------*/
/*序列化表单为Json对象*/
function serializeFormToJson(containerId){
	var form = $('#'+containerId);
	var json = {};
	form.find("input[type=text]").each(function(d, i){
		json[$(this).attr('id')] = $(this).val();
	});
	form.find("input[type=date]").each(function(d, i){
		json[$(this).attr('id')] = $(this).val();
	});
	form.find("input[type=time]").each(function(d, i){
		json[$(this).attr('id')] = $(this).val();
	});
	form.find("input[type=tel]").each(function(d, i){
		json[$(this).attr('id')] = $(this).val();
	});
	form.find("input[type=password]").each(function(d, i){
		json[$(this).attr('id')] = $(this).val();
	});
	form.find("input[type=hidden]").each(function(d, i){
		json[$(this).attr('id')] = $(this).val();
	});
	form.find(":radio:checked").each(function(d, i){
		json[$(this).attr('name')] = $(this).val();
	});
	form.find("textarea").each(function(d, i){
		json[$(this).attr('id')] = $(this).val();
	});
	form.find("select").each(function(d, i){
		json[$(this).attr('id')] = $(this).val();
	});
	return json;
}
//提交普通表单，提交前验证表单
function submitForm(formId){
	if(!validForm(formId)){
		return false;
	}
	$("#"+formId).submit();
}
/*ajax提交表单
 * isValidForm:true,验证表单; false,不验证表单
 * */
function ajaxSubmit(url, formId, isValidForm, callback){
	if(isValidForm){
		if(!validForm(formId)){
			return false;
		}
	}
	var data = serializeFormToJson(formId);
	post(url, data, callback);
}
//验证表单
function validForm(id){
	var valid = true;
	var text_not_null = "必填项";
	var msg_number = "必须是数字";
	var msg_format = "格式不对";
	var text_error = "长度不对";
	var class_msg = "class_msg"; 
	//不为空
	$("#"+id).find("input[type=text][valid=not-null]:visible,input[type=time][valid=not-null]:visible,input[type=date][valid=not-null]:visible,input[type=tel][valid=not-null]:visible,input[type=password][valid=not-null],textarea[valid=not-null]:visible,select[valid=not-null]:visible").each(function(){
		var val = $(this).val();
		if($.trim(val).length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(text_not_null));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//数字，带小数点
	$("#"+id).find("input[type=text][valid=number]:visible").each(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(isNaN(val)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(msg_number));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//手机号码  valid=mobile
	$("#"+id).find("input[type=text][valid=mobile]:visible").each(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(!val.match(/^(13|14|15|16|17|18)\d{9}$/)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(msg_format));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//整型
	$("#"+id).find("input[type=text][valid=integer]:visible,input[type=tel][valid=integer]:visible").each(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(!/^[\-0-9]*$/.test(val)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必须是整数"));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//验证字符长度是否合格
	$("#"+id).find("input[type=text][validation=len],input[type=password][valid=length]:visible").each(function(){
		var val = $(this).val();
		var len = $(this).attr("len");
		if($.trim(val).length > len){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(text_error));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//验证邮箱valid=email
	$("#"+id).find("input[type=text][valid=email]:visible").each(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(!val.match(/^[A-Za-z0-9d]+([-_.][A-Za-z0-9d]+)*@([A-Za-z0-9d]+[-.])+[A-Za-z0-9d]{2,5}$/)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(msg_format));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//身份证valid=card
	$("#"+id).find("input[type=text][valid=card]:visible").each(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(!isCardID(val)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(msg_format));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//验证姓名格式 valid=realname
	$("#"+id).find("input[type=text][valid=realname]:visible").blur(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(!val.match(/[\u4E00-\u9FA5A-Za-z\s]{2,16}$/)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("格式不对"));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	if(!valid){
		return false;
	}
	return true;
}
//即时
function blurValidForm(id){
	var valid = true;
	var text_not_null = "必填项";
	var msg_number = "必须是数字";
	var msg_format = "格式不对";
	var text_error = "长度不对";
	var class_msg = "class_msg"; 
	//不为空
	$("#"+id).find("input[type=text][valid=not-null]:visible,input[type=tel][valid=not-null]:visible,input[type=password][valid=not-null],textarea[valid=not-null],select[valid=not-null]:visible").blur(function(){
		var val = $(this).val();
		if($.trim(val).length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(text_not_null));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//数字，带小数点
	$("#"+id).find("input[type=text][valid=number]:visible").blur(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(isNaN(val)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(msg_number));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//手机号码  valid=mobile
	$("#"+id).find("input[type=text][valid=mobile]:visible").blur(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(!val.match(/^(13|14|15|16|17|18)\d{9}$/)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(msg_format));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//整型
	$("#"+id).find("input[type=text][valid=integer]:visible,input[type=tel][valid=integer]:visible").blur(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(!/^[\-0-9]*$/.test(val)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必须是整数"));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//验证字符长度是否合格
	$("#"+id).find("input[type=text][validation=len],input[type=password][valid=length]:visible").blur(function(){
		var val = $(this).val();
		var len = $(this).attr("len");
		if($.trim(val).length > len){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(text_error));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//验证邮箱valid=email
	$("#"+id).find("input[type=text][valid=email]:visible").blur(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(!val.match(/^[A-Za-z0-9d]+([-_.][A-Za-z0-9d]+)*@([A-Za-z0-9d]+[-.])+[A-Za-z0-9d]{2,5}$/)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(msg_format));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//身份证valid=card
	$("#"+id).find("input[type=text][valid=card]:visible").each(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(!isCardID(val)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error(msg_format));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	//验证姓名格式 valid=realname
	$("#"+id).find("input[type=text][valid=realname]:visible").blur(function(){
		var val = $.trim($(this).val());
		if(val.length == 0){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("必填项"));
		}else if(!val.match(/[\u4E00-\u9FA5A-Za-z\s]{2,16}$/)){
			valid = false;
			updateInnerHTML(this, "."+class_msg, error("格式不对"));
		}else{
			updateInnerHTML(this, "."+class_msg, ok("OK"));
		}
	});
	if(!valid){
		return false;
	}
	return true;
}

/*修改某个标志的html,如错误信息，不存在则新增
 * jqueryObj jquery对象
 * tag jquery定位对象的标签， 如 P\#id\.class
 * html 内容
 */
function updateInnerHTML(obj, tag, html){
	if(isMobile()){ //手机端wxalert提示
		if(html.indexOf("ok")>=0){return false;} wxalert($(obj).attr("placeholder")); return false;
	}
	var jqueryObj = $(obj).parent();
	if(jqueryObj.find(tag).length>0){
		var span = $(html);
		jqueryObj.find(tag).html(span.text());
		jqueryObj.find(tag).css("color", span.css("color"));
	}else{
		jqueryObj.append(html);
	}
}

//输出错误信息
function error(msg){return "<span class='class_msg' style='color:#DD0000;'>"+msg+"</span>";}
//输出正确信息
function ok(msg){msg = "";return "<span class='class_msg' style='color:#00CC00;' ok='true'>"+msg+"</span>";}


/**============日期处理=====================*/
//字符串转date,格式必须为yyyy-MM-dd或yyyy-MM-dd HH:mm:ss
function parsedate(datestr){
	if(datestr=="" || datestr.indexOf("-")<0){
		alert("解析日期错误：日期格式不正确！");
		return null;
	}
	if(datestr.indexOf(":")<0){
		datestr += " 00:00:00";
	}
	datestr = datestr.replace(/-/g,"/"); //转为格式：yyyy/MM/dd HH:mm:ss
	return new Date(datestr);
}

/**============字符串方法====================*/
//判断字符串是否为空值
function isEmpty(value){
	if(typeof(value) == 'undefined' || value == null || value == "null" || value== undefined || value == "undefined" || value == ''){
		return true; }
	return false;
}

//替换字符串
String.prototype.replaceAll = function(s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
};

//判断是否为json格式字符串
function isJsonStr(str){
	try{$.parseJSON(str);	return true;}catch(ex){return false;}
}
//字符串转json，失败则返回null
function strToJson(str){
	try{return $.parseJSON(str);}catch(ex){return null;}
}

//json对象转字符串
function jsonToString (obj){   
    var THIS = this;    
    switch(typeof(obj)){   
        case 'string':   
            return '"' + obj.replace(/(["\\])/g, '\\$1') + '"';   
        case 'array':   
            return '[' + obj.map(THIS.jsonToString).join(',') + ']';   
        case 'object':   
             if(obj instanceof Array){   
                var strArr = [];   
                var len = obj.length;   
                for(var i=0; i<len; i++){   
                    strArr.push(THIS.jsonToString(obj[i]));   
                }   
                return '[' + strArr.join(',') + ']';   
            }else if(obj==null){   
                return 'null';   
  
                }else{   
                    var string = [];   
                    for (var property in obj) string.push(THIS.jsonToString(property) + ':' + THIS.jsonToString(obj[property]));   
                return '{' + string.join(',') + '}';   
            }   
        case 'number':   
            return obj;   
        case false:   
            return obj;   
    }   
}

String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
	  return true;
	else
	  return false;
	return true;
}

String.prototype.endWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length) {return false; }
	if(this.substring(this.length-str.length)==str) {return true;}
	else{  return false;}
}
//取字符串的前length位
String.prototype.cut = function(length){
	if(str==null||str==""||this.length==0)
		  return this;
	var max = this.length > length ? length : this.length;
	return this.substring(0, max);
}

//=======================数据模板========================
/**获取模板数据html*/
function getListTplHTML(datalist, tpl){
	console.log(datalist);
	var html = [];
	$(datalist).each(function(i, d){
		var tp = $(tpl).html();
		for(var key in d){
			tp = tp.replaceAll("\{\{"+key+"\}\}", d[key]);		
		}
		//内循环
		var eh = $(tpl).find("foreach");
		var ehhtml = [];
		console.log("attr:"+eh.attr("data"));
		$(d[eh.attr("data")]).each(function(j, s){
			
			var ehtp = eh.html();
			for(var key2 in s){
				ehtp = ehtp.replaceAll("\{\{"+key2+"\}\}", s[key2]);		
			}
			ehhtml.push(ehtp);
		});
		//console.log(ehhtml.join(''));
		//tp = tp.replace(/^<foreach(.*)foreach>$/g, ehhtml.join(''));
		var tpel = $("#sss").html(tp);
		tpel.find("foreach").prop('outerHTML', ehhtml.join(''));
		//tp = tp.replace(/<foreach\s*<\/foreach>/gi, ehhtml.join(''));
		console.log("tp:"+tpel.html());
		html.push(tpel.html());
	});
	return html.join("");
}

/**=================信息提示框 弹窗2.0(微信样式)======================*/
//信息提示框callback,回调函数
var _wxalertcallback;
var _wxconfirmcallback;
function wxalert(msg){ //msg，消息内容, [1]回调方法, [2]提示标题
	if($("#wxalertdialog").length > 0) {return false;} _wxalertcallback = null; if(arguments[1]){ _wxalertcallback = arguments[1]; }
	var html = [], wxtitle = "提示";  if(arguments[2]){ wxtitle = arguments[2]; }
	html.push('<div class="weui_dialog_alert" id="wxalertdialog" style=""><div class="weui_mask"></div>');
	html.push('<div class="weui_dialog"><div class="weui_dialog_hd"><strong class="weui_dialog_title">'+wxtitle+'</strong></div>');
	html.push('<div class="weui_dialog_bd">'+msg+'</div><div class="weui_dialog_ft">');
	html.push('<a href="javascript:wxalerthide()" class="weui_btn_dialog primary">确定</a></div></div></div>');
	$("body").append(html.join(""));
}

//点击wxalert确定按钮
function wxalerthide(){ $('#wxalertdialog').remove(); if(_wxalertcallback != null){ eval('_wxalertcallback()'); } }

/**=================信息提示框 弹窗1.0(黑底白字)======================*/
//信息提示框callback,回调函数
var _alertcallback;
var _confirmCancelback;//取消确定回调
//覆盖
function alertmsg(msg){
	if($("#alertmask").length > 0) {return false;}
	_alertcallback = null; if(arguments[1]){ _alertcallback = arguments[1]; }
	var html = [];
	html.push('<div id="alertmask" style="position:fixed;top:0;z-index:8100000000;width:100%;height:100%;background:#333;filter:Alpha(opacity=70);-moz-opacity:0.7;-khtml-opacity: 0.7;opacity: 0.7;"></div>');
	html.push('<div id="alertbox" style="position:fixed;top:170px;width:300px;height:140px;left:50%;margin-left:-150px;border-radius:0px;background:rgba(0,0,0,0.4);z-index:8100000000;border-radius:5px;');
	html.push('border:solid 1px #fff;">');
	html.push('<p style="padding:18px 10px 8px 10px;text-align:center;font-size:14px;line-height:25px;color:#fff;">');
	html.push(msg);
	html.push('</p>');
	html.push('<p style="position:absolute;text-align:center;width:100%;bottom:15px;" align="center">');
	html.push('<a id="#alertbtn" href="javascript:alerthide();" style="position:relative;display:block;width:110px;left:95px;height:40px;line-height:40px;font-size:16px;background:#fff;color:#333;text-align:center;text-decoration:none;text-shadow:none;border-radius:5px;">确 定</a>');
	html.push('</p>');
	html.push('</div>');
	$("body").append(html.join(""));
	$("#alertbox").hide();
	$("#alertbox").fadeIn();
}

//确定操作对话框
function confirmmsg(msg){
	_confirmCancelback = null;
	if(arguments[1]){
		_confirmCancelback = arguments[1];
	}
	var html = [];
	html.push('<div id="alertmask" style="position:fixed;top:0;z-index:1000;width:100%;height:100%;background:#333;filter:Alpha(opacity=70);-moz-opacity:0.7;-khtml-opacity: 0.7;opacity: 0.7;"></div>');
	html.push('<div id="alertbox" style="position:fixed;top:170px;width:300px;height:140px;left:50%;margin-left:-150px;border-radius:5px;background:#FFF;z-index:10000;');
	html.push('border:solid 1px #ff9900;">');
	html.push('<p style="padding:18px 10px 8px 10px;text-align:center;font-size:14px;line-height:25px;color:#333;">');
	html.push(msg);
	html.push('</p>');
	html.push('<p style="position:absolute;text-align:center;width:100%;bottom:15px;" align="center">');
	html.push('<a id="#alertbtn" href="javascript:confirmYes();" style="position:relative;display:inline-block;width:110px;left:0px;height:40px;line-height:40px;font-size:16px;background:#ff9900;color:#FFF;text-align:center;">确 定</a>');
	html.push('<a id="#alertbtn" href="javascript:confirmNo();" style="position:relative;display:inline-block;width:110px;margin-left:30px;height:40px;line-height:40px;font-size:16px;background:#cccccc;color:#FFF;text-align:center;">取 消</a>');
	html.push('</p>');
	html.push('</div>');
	$("body").append(html.join(""));
}
function wxconfirm(msg) { //confirm，确认操作,[0]提示信息 [1]确认回调方法,[2]取消回调方法, [3]提示标题
	if ($("#wxalertdialog").length > 0) {
		return false;
	}
	_wxalertcallback = null;
	if (arguments[1]) { _wxalertcallback = arguments[1];}
	if (arguments[2]) { _wxconfirmcallback = arguments[2];}
	var html = [], wxtitle = "";if (arguments[3]) {wxtitle = arguments[3];}
	html.push('<div class="weui_dialog_confirm" id="wxalertdialog" style=""><div class="weui_mask"></div>');
	html.push('<div class="weui_dialog"><div class="weui_dialog_hd"><strong class="weui_dialog_title">' + wxtitle + '</strong></div>');
	html.push('<div class="weui_dialog_bd">' + msg + '</div><div class="weui_dialog_ft">');
	html.push('<a href="javascript:wxconfirmcancel()" class="weui_btn_dialog default">取消</a><a href="javascript:wxalerthide()" class="weui_btn_dialog primary">确定</a></div></div></div>');
	$("body").append(html.join(""));
}
function wxinput(wxtitle) { //弹出输入框，确认操作,[0]title [1]确认回调方法,[2]取消回调方法
	if ($("#wxinputdialog").length > 0) {
		return false;
	}
	_wxalertcallback = null;
	if (arguments[1]) { _wxalertcallback = arguments[1];}
	if (arguments[2]) { _wxconfirmcallback = arguments[2];}
	var html = [];
	html.push('<div class="weui_dialog_confirm" id="wxinputdialog" style=""><div class="weui_mask"></div>');
	html.push('<div class="weui_dialog"><div class="weui_dialog_hd"><strong class="weui_dialog_title">' + wxtitle + '</strong></div>');
	html.push('<div class="weui_dialog_bd"><textarea id="wxinput" style="width:100%;height:90px;"></textarea></div><div class="weui_dialog_ft">');
	html.push('<a href="javascript:wxinputcancel()" class="weui_btn_dialog default">取消</a><a href="javascript:wxinputok()" class="weui_btn_dialog primary">确定</a></div></div></div>');
	$("body").append(html.join(""));
}
//点击wxalert确定按钮
function wxalerthide() {
	$('#wxalertdialog').remove();if (_wxalertcallback != null) {eval('_wxalertcallback()');}
}
//confirm取消
function wxconfirmcancel() {
	$('#wxalertdialog').remove();if (_wxconfirmcallback != null) {eval('_wxconfirmcallback()');}
}
//点击wxinput确定按钮
function wxinputcancel() {
	$('#wxinputdialog').remove();/*if (_wxalertcallback != null) {eval('_wxalertcallback()');}*/
}
//点击wxinput确定按钮
function wxinputok() {
	if (_wxalertcallback != null) {eval('_wxalertcallback()');}
}
//点击alert确定按钮
function alerthide(){
	$('#alertmask').remove();
	$('#alertbox').remove();
	if(_alertcallback != null){
		eval('_alertcallback()');
	}
}
//确定
function confirmYes(){
	$('#alertmask').remove();
	$('#alertbox').remove();
	if(_confirmCancelback != null){
		eval('_confirmCancelback()');
	}
}
//取消
function confirmNo(){
	$('#alertmask').remove();
	$('#alertbox').remove();
}

//浮动自动消失弹窗
function floatmsg(msg){
	var html = [];
	html.push('<div id="floatbox" style="display:none;position:fixed;width:260px;top:50%;left:50%;margin-left:-130px; margin-top:-40px;border-radius:5px;background:#000;z-index:10000;padding: 0 10px 0 10px;');
	html.push('border:solid 1px #333;opacity: 0.80;filter:alpha(opacity=80);">');
	html.push('<p style="padding:8px 10px 8px 10px;text-align:center;font-size:14px;line-height:25px;color:#fff;margin:0;">');
	html.push(msg);
	html.push('</p>');
	$("body").append(html.join(""));
	$('#floatbox').slideDown();
	setTimeout(function(){$('#floatbox').fadeOut(function(){$('#floatbox').remove();});}, 1000);
}

//弹窗显示html
function showHtmlBox(str){
	var html = [];
	html.push('<div id="htmlmask" style="position:fixed;top:0;z-index:1000;width:100%;height:100%;background:#333;filter:Alpha(opacity=70);-moz-opacity:0.7;-khtml-opacity: 0.7;opacity: 0.7;"></div>');
	html.push('<div id="htmlbox" style="position:fixed;top:100px;width:300px;min-height:140px;overflow:auto;left:50%;margin-left:-150px;border-radius:0px;background:#FFF;z-index:10000;');
	html.push('border:solid 1px #A10000;">');
	html.push('<p style="padding:18px 10px 8px 10px;">');
	html.push(str);
	html.push('</p>');
	html.push('</div>');
	$("body").append(html.join(""));
	$('#htmlbox').hide();
	$('#htmlbox').fadeIn();
	$("#htmlbox").click(function(){
		$('#htmlmask').remove();
		$('#htmlbox').fadeOut(function(){
			$('#htmlbox').remove();
		});
	});
	$("#htmlmask").click(function(){
		$('#htmlmask').remove();
		$('#htmlbox').fadeOut(function(){
			$('#htmlbox').remove();
		});
	});
}
//弹窗显示html
function showHtmlBox(str, width){
	var html = [];
	html.push('<div id="htmlmask" style="position:fixed;top:0;z-index:1000;width:100%;height:100%;background:#333;filter:Alpha(opacity=70);-moz-opacity:0.7;-khtml-opacity: 0.7;opacity: 0.7;"></div>');
	html.push('<div id="htmlbox" style="position:fixed;top:100px;width:300px;min-height:140px;overflow:auto;left:50%;margin-left:-150px;border-radius:0px;background:#FFF;z-index:10000;');
	html.push('border:solid 1px #A10000;">');
	html.push('<p style="padding:18px 10px 8px 10px;">');
	html.push(str);
	html.push('</p>');
	html.push('</div>');
	$("body").append(html.join(""));
	$("#htmlbox").css("width", width);
	$("#htmlbox").css("marginLeft", -1*(width/2));
	$('#htmlbox').hide();
	$('#htmlbox').fadeIn();
	$("#htmlbox").click(function(){
		$('#htmlmask').remove();
		$('#htmlbox').fadeOut(function(){
			$('#htmlbox').remove();
		});
	});
	$("#htmlmask").click(function(){
		$('#htmlmask').remove();
		$('#htmlbox').fadeOut(function(){
			$('#htmlbox').remove();
		});
	});
}

/**显示模态弹窗,仿bootstrap
 * <div id="myModal" class="modal hide fade" tabindex="-1" style="width:500px;height:300px;">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    <h3 id="myModalLabel">Modal header</h3>
  </div>
  <div class="modal-body">
    <p>One fine body…</p>
  </div>
  <div class="modal-footer">
    <button class="btn btn-primary">保存</button>
    <button class="btn" data-dismiss="modal" aria-hidden="true" btn-type="close">关闭</button>
  </div>
</div>
 */
/*function showModal(id){
	if($("#modalmask").is(":visible")){
		$("#modalmask").hide();
		$("#"+id).hide();
	}else{
		var modal = $("#"+id);
		if($("#modalmask").length>0){
			$("#modalmask").show();
			modal.show();
		}else{
			var html = [];
			html.push('<div id="modalmask" style="position:fixed;top:0;z-index:1000;width:100%;height:100%;background:#333;filter:Alpha(opacity=70);-moz-opacity:0.7;-khtml-opacity: 0.7;opacity: 0.7;"></div>');
			$("body").append(html.join(""));
			modal.show();
			$("body").append('<style>.closeicon{float:right;line-height:1;font-weight: bold;background: none;border: none;font-size: 25px;color:#666;}</style>');
		}
		modal.css("position", "absolute").css("top", "100px").css("z-index", 200000000).css("background", "#FFF")
		.css("overflow-y", "auto").css("left", "50%").css("margin-left", -1*Number(modal.css("width").replace("px", "")) / 2);
		modal.removeClass("fade").removeClass("hide");
		modal.find("button[class=closeicon]").unbind("click");
		modal.find("button[btn-type=close]").unbind("click");
		modal.find("button[class=closeicon]").bind("click", function(){showModal(id)});
		modal.find("button[btn-type=close]").bind("click", function(){showModal(id)});
	}
}*/

//==============================effects 效果=================
/**图片向左不断滚动,需要jquery ui
注意ul的position不能为空
selector:jquery 选择器
*/
function rollbox(selector){
	var thiz = $(selector);
	var ul = $(selector).find("ul");
	var sublist = ul.find("li");
	var sublength = sublist.length;
	var subwidth = $(ul.find("li")[0]).width();
	var timesp = 5000; //滚动间隔ms
	var timeeffect = timesp - 2000; //效果时间
	var leftoffset = 0;
	var index = 0;
	
	setInterval(function(){
		if(index>=sublist.length-1){
			index = 0;
		}
		ul.css("width", ul.width()+subwidth);
		ul.append($(sublist[index]).clone());
		ul.animate({left:leftoffset-subwidth}, timeeffect);
		index++;
		leftoffset -= subwidth;
	}, timesp);
}

//复制文本 copy
function copyToClipboard(_sTxt){
    try{  
        if(window.clipboardData) {  
            window.clipboardData.setData("Text", _sTxt);  
        } else if(window.netscape) {  
            netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');  
            var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);  
            if(!clip) return;  
            var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);  
            if(!trans) return;  
            trans.addDataFlavor('text/unicode');  
            var str = new Object();  
            var len = new Object();  
            var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);  
            var copytext = _sTxt;  
            str.data = copytext;  
            trans.setTransferData("text/unicode", str, copytext.length*2);  
            var clipid = Components.interfaces.nsIClipboard;  
            if (!clip) return false;  
            clip.setData(trans, null, clipid.kGlobalClipboard);  
        }  
    }catch(e){}  
}   

//验证时间大小
function dateCompare(date1,date2){
	date1 = date1.replace(/\-/gi,"/");
	date2 = date2.replace(/\-/gi,"/");
	var time1 = new Date(date1).getTime();
	var time2 = new Date(date2).getTime();
	if(time1 > time2){
		return 1;
	}else if(time1 == time2){
		return 2;
	}else{
		return 3;
	}
}
//货币格式化
function formatCurrency(num) {
	num = num.toString().replace(/\$|\,/g, '');
	if (isNaN(num)){
		num = "0";
	}
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num * 100 + 0.50000000001);
	cents = num % 100;
	num = Math.floor(num / 100).toString();
	if (cents < 10){
		cents = "0" + cents;
	}
	for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
		num = num.substring(0, num.length - (4 * i + 3)) + ','
				+ num.substring(num.length - (4 * i + 3));
	return (((sign) ? '' : '-') + num + '.' + cents);
}

/**========================微信方法===============*/
function isWeiXin(){
	var ua = window.navigator.userAgent.toLowerCase();if(ua.match(/MicroMessenger/i) == 'micromessenger'){return true;}else{ return false;}
}

//身份证验证
function isCardID(sId) {
	var iSum = 0;
	if (!/^\d{17}(\d|x)$/i.test(sId)){
		return false;
	}
	sId = sId.replace(/x$/i, "a");
	if (aCity[parseInt(sId.substr(0, 2))] == null){
		return false;
	}
	sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2)) + "-" + Number(sId.substr(12, 2));
	var d = new Date(sBirthday.replace(/-/g, "/"));
	if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate())){
		return false;
	}
	for (var i = 17; i >= 0; i--){
		iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11);
	}
	if (iSum % 11 != 1){
		return false;
	}
	return true;
}

// 下载文件
function download(path, fileName){
	if(isEmpty(path)){
		alertmsg("路径不能为空");
		return;
	}
	var url = "/download?path=" + path;
	if(!isEmpty(fileName)){
		url += "&fileName=" + fileName;
	}
	window.location.href= url;
}
//加载文件,css,js
function loadfile(filetype, filename){
    if(filetype == "js"){
        var fileref = document.createElement('script');fileref.setAttribute("type","text/javascript");fileref.setAttribute("src",filename);
    }else if(filetype == "css"){
        var fileref = document.createElement('link');fileref.setAttribute("rel","stylesheet");fileref.setAttribute("type","text/css");fileref.setAttribute("href",filename);
    }
   if(typeof fileref != "undefined"){document.getElementsByTagName("head")[0].appendChild(fileref);} 
}

//生成二维码
function createBitCodeImg(curl,width,height){
	if(isEmpty(width)){
		width = 300;
	}
	if(isEmpty(height)){
		height = 300;
	}
	var url = "/bitcode/build?widthStr="+width+"&heightStr="+height+"&text=" + curl;
	return url;
}

//-================设备信息判断=====================
window.isMobile = function() { var check = false; (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4)))check = true})(navigator.userAgent||navigator.vendor||window.opera);return check; }

loadfile("css", "/js/inc/inc.project.css"); //加载inc样式文件

/**发送手机验证码
 * btnId : 发送按钮ID
 * mobilePhone : 手机号
 * sendType : 发送模块ID 1.注册  2.找回密码。。等（不传默认注册）
 * type : 验证码类别 1.短信 2.手机语音（不传默认短信）
 * */
function sendPhoneCode(btnId, mobilePhone, sendType, type) {
	var reg = /^(13|14|15|16|17|18)\d{9}$/;
	if (!reg.test(mobilePhone)) {
		wxalert('请填写有效的手机号码！');
		return;
	}
	var data = {
		"mobilePhone" : mobilePhone,
		"sendType" : sendType,
		"type" : type
	};
	post("/valid/sendCode", data, function(msg) {
		if (msg.code == 1) {
			if(isEmpty(type) || type == 1){
				wxalert("短信验证码发送成功");
				sendCodeTime(btnId);
			}else{
				wxalert("请留意来电！");
			}
		} else {
			wxalert(msg.msg+"，兴许是您请求验证码过于频繁，稍候再试。");
		}
	}, false);
}

/**验证码倒计时*/
var _wait = 60;
function sendCodeTime(btnId) {
	if (_wait == 0) {
		$("#" + btnId).attr("onclick","sendPhoneCode();");
		if($("#" + btnId).is("input")){
			$("#" + btnId).val("发送验证码");
		}else{
			$("#" + btnId).html("发送验证码");
		}
		_wait = 300;
	} else {
		$("#" + btnId).removeAttr("onclick");
		if($("#" + btnId).is("input")){
			$("#" + btnId).val("重新发送(" + _wait + ")");
		}else{
			$("#" + btnId).html("重新发送(" + _wait + ")");
		}
		_wait--;
		setTimeout(function() {
			sendCodeTime(btnId);
		}, 1000);
	}
}

/**
 * 判断第一个参数是否为空 为空返回第二个参数
 * @param parm 判断对象
 * @param oarm 默认值
 */
function ifnull(parm, oarm){
	if(isEmpty(parm)){
		return oarm;
	}
	return parm;
}

/**格式化时间戳*/
function date(l,type)   {
	var now = new Date(l); 
	var year = now.getFullYear();       
	var month = now.getMonth()+1;       
	var date = now.getDate();       
	var hour = now.getHours();   
	var minute = now.getMinutes();       
	var second = now.getSeconds();
	var dateStr = year+"-"+add0(month)+"-"+add0(date);
	if(type == 1){
		return dateStr;
	}
	return dateStr +" "+add0(hour)+":"+add0(minute)+":"+add0(second);   
}
function add0(m){return m<10?'0'+m:m;}

/*
 * 判断是否为数字
 * @param s 判断的值
 */
function IsNum(s)
{
    if (s!=null && s!="")
    {
        return !isNaN(s);
    }
    return false;
}
/*
 * 打开弹窗
 * @param title 窗口标题
 * @param width 宽
 * @param height 高
 * @param content 窗口内容链接
 */
function openWin(title, width, height, content){
	layer.open({
	  type: 2,
	  title: title,
	  closeBtn: 1,	//关闭按钮风格：1，2
	  shift :0,		//动画效果，0~6
	  area: [width + 'px', height + 'px'],
	  fix: false, //不固定
	  maxmin: true,
	  content: content//引入哪个页面
	});
}

/*
 * 初始化图片上传
 * 1、上传按钮：class=fileupload(绑定上传事件) type=file(文件选择按钮) name=files(对应后台的文件名称) value=1(限制上传张数)
 */
function initImageUpload(){
	$('.fileupload').fileupload({
		forceIframeTransport: true,
		url: "/file/upload",
		dataType: 'json',
		secureuri: false,
		sequentialUploads: true,
		done: function (e, data) {//设置文件上传完毕事件的回调函数
			var res = data.result;
			var num = e.target.defaultValue;
			if (!isEmpty(res) && res.code == 1) {
				addImage(this, res.data[0], num);
			} else {
				alertMsg("文件上传失败", 2);
			}
		}
	});
}
function addImage(_this, imgPath, num){
	var obj = $(_this).parent().parent().parent().find("li:not('.addbtn')");
	var len = obj.length;
	if(len >= num){
		alertMsg("最多只能上传"+num+"张图！", 2);
		return false;
	}

	if(imgPath == null || imgPath == ""){
		alertMsg("请先上传图片再添加！", 2);
		return false;
	}
	var html = "<li><img src='"+imgPath+"' /><span class='icon-remove' onclick='removeFun(this)'></span></li>";
	$(_this).parent().parent().parent().prepend(html);
}
function removeFun(dom){
	$(dom).parent().remove();
}
/*
 * 删除
 * @param id 删除的对象ID
 * @param url 删除的链接
 */
function delFun(id, url){
	alertConfirm("确认删除吗？此操作不可恢复！", function(){
		post(url, {id : id}, function(res){
			if(res.errcode === 0){
				alertMsg(res.errmsg, 1, query());
			}else{
				alertMsg(res.errmsg, 2);
			}
		});
	});
}
/*
 * 修改状态
 * @param id 修改的对象ID
 * @param status 目标状态
 * @param tips 确认框提示信息
 * @param url 修改的链接地址
 */
function updateStatus(id, status, tips, url){
	alertConfirm(tips, function(){
		post(url, {id: id, status:status}, function(res){
			if(res.errcode === 0){
				alertMsg(res.errmsg, 1, query());
			}else{
				alertMsg(res.errmsg, 2);
			}
		});
	});
}