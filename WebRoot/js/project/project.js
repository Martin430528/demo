/**融星科技前端内部框架
 * @CopyRights 版权所有 侵权必究
 */

/**RS为全局对象*/
var RS = new RonsaiJS();

/**对象定义*/
function RonsaiJS(){ this.isReady = false; }
RonsaiJS.prototype.ready ; //ready方法
RonsaiJS.prototype.readyListen = function(){};
RonsaiJS.prototype.scrollDownFns = [];
RonsaiJS.prototype.addReadyListener = function(){ //监听事件
	$(window).scroll(function () {
		var scrollTop = $(this).scrollTop();
		var scrollHeight = $(document).height();
		var windowHeight = $(this).height();
		if (scrollTop + windowHeight == scrollHeight) {
			//此处是滚动条到底部时候触发的事件，在这里写要加载的数据，或者是拉动滚动条的操作
			//逐一执行注册事件
			for(var i=0; i<RS.scrollDownFns.length; i++){ RS.scrollDownFns[i].call();}
		}
	});
};
RonsaiJS.prototype.type = "ronsai";
RonsaiJS.prototype.test = function(){alert('test');};
RonsaiJS.prototype.init = function(){this.loadFile("/js/jquery/jquery-1.9.1.min.js", "js");};
//是否为数组
RonsaiJS.prototype.isArray = function(obj) { return Object.prototype.toString.call(obj) === '[object Array]'; };
//提交表单，默认为ajax方式
RonsaiJS.prototype.submit = function(config){
	var defaultConfig = {ajax:true, url:'', params:{}, formId:'form', valid:true, callback:null /*回调方法*/};
	for(key in defaultConfig){
		defaultConfig[key] = (config[key]==undefined) ? defaultConfig[key] : config[key];
	}
	if(defaultConfig.valid){if(!validForm(defaultConfig.formId)){return false;}}
	var data = serializeFormToJson(defaultConfig.formId);
	if(config['params']!=undefined) data = config['params']; //如果指定了参数，那么传指定参数
	post(config.url, data, defaultConfig.callback);
};
//加载数据源并显示，一般为列表
RonsaiJS.prototype.loadDataView = function(config){
	var defaultConfig = {isarray:true, url:'', params:{}, container:'#container', pulldownload:false, page:1, pageSize: 30,
		isloading:false, textareaid:null, loadcallback:null /*每次查询完回调方法*/};
	for(key in defaultConfig){
		defaultConfig[key] = (config[key]==undefined) ? defaultConfig[key] : config[key];
	}

	//创建临时数据模板
	var datatempid = 'list_data_temp'+Math.floor(Math.random()*10000);
	var textareatempid = 'textareaTemp'+Math.floor(Math.random()*10000);
	//如果是tbody得在table之后append
	var afterobj =  ($(config.container)[0].tagName.toUpperCase()=='TBODY') ? $(config.container).parent('table') : $(config.container);
	if(defaultConfig.textareaid != null) {//页面已指定textarea模板
		textareatempid = defaultConfig.textareaid;
	}else{ //创建动态的textarea模板
		afterobj.after('<textarea  id="'+textareatempid+'" style="display:none;"></textarea >');
		$("#"+textareatempid).html($(config.container).html());
	}
	afterobj.after('<table id="'+datatempid+'" style="display:none;"><tbody></tbody></table>');
	$(config.container).html('');
	$(config.container).show();
	queryData(defaultConfig); //开始查询数据
	if(defaultConfig.pulldownload){ //注册下拉加载方法
		RS.scrollDownFns.push(function(){queryData(defaultConfig);}); //添加到注册事件
	}
	function queryData(config){ //定义查询数据方法
		try {
			config.isloading = true;
			afterobj.after('<div class="pulldownloading" style="width:100%;height:50px;"><img src="/wap/images/icon/loading.gif" style="display:block;margin:auto;height:100%;"/></div>');
			$(".nodata").hide();
			var params = config.params;
			params.page = config.page;
			params.pageSize = config.pageSize;
			post(config.url, params, function (data) {
				$(".pulldownloading").remove();
				if (data.list) {
					if (!data.list.length || data.list.length == 0) {
						$(".nodata").remove();
						afterobj.after('<div class="nodata" style="width:100%;height:50px;text-align:center;">没有更多了</div>');
						return false;
					}
					config.isloading = false;
					$("#" + datatempid + ">tbody").setTemplateElement(textareatempid);
					//自定义方法 js 方法在inc.js 里面 参数根据js定义参数传值
					$("#" + datatempid + ">tbody").setParam("ifnull", ifnull);//用法：{$P.ifnull($T.curro.img,'/wap/images/chipsimg.png') }
					$("#" + datatempid + ">tbody").setParam("date", date);//用法：{$P.date($T.curro.createDate, 2) }
					$("#" + datatempid + ">tbody").processTemplate(data);
					var html = $("#" + datatempid).find("tbody").html();  //显示数据
					$(config.container).append(html);
					//$("#"+datatempid).find("tbody").html("");
					config.page++; //页码+1
				} else {
					//alert(data.msg);
				}
				if (config.loadcallback) {
					config.loadcallback(data);
				} //每次查询完的回调方法
			});
		}catch (e){queryData(config);  }
	}
};
//加载数据源并显示，一般为列表[用于PC版分页列表]
RonsaiJS.prototype.loadPageData = function(config){
	var defaultConfig = {isarray:true, url:'', params:{}, container:'#container', isPaging:true, pulldownload:false, page:1, pageSize: 20,
		isloading:false, textareaid:null, loadcallback:null /*每次查询完回调方法*/};
	for(key in defaultConfig){
		defaultConfig[key] = (config[key]==undefined) ? defaultConfig[key] : config[key];
	}
	queryData(defaultConfig); //开始查询数据

	function queryData(config){ //定义查询数据方法
		try {
			config.isloading = true;
			$(".nodata").hide();
			var params = config.params;
			if(config.isPaging){//默认分页
				params.page = config.page;
				params.pageSize = config.pageSize;
			}
			post(config.url, params, function (data) {
				$(".pulldownloading").remove();
				if (data.list) {
					if (!data.list.length || data.list.length == 0) {
						$(".nodata").remove();
						//return false;
					}
					config.isloading = false;
					$("#"+config.container).setTemplateElement(config.textareaid);
					//自定义方法 js 方法在inc.js 里面 参数根据js定义参数传值
					$("#"+config.container).setParam("ifnull", ifnull);//用法：{$P.ifnull($T.curro.img,'/wap/images/chipsimg.png') }
					$("#"+config.container).setParam("date", date);//用法：{$P.date($T.curro.createDate, 2) }
					$("#"+config.container).processTemplate(data);
					if(config.isPaging) {//默认分页
						$("#pagination").myPagination({
							currPage : data.page, // 当前页
							totalPage : data.totalPage, // 总共页数
							pageNum : data.pageSize, // 每页显示的数量
							pageSize : 6,
							cssStyle : 'base_page', // 样式
							inputPageMethod : function() {
								config.page = parseInt($("#fypage").val());
								queryData(config);
							},
							inputPageNumMethod : function() {
								config.page = parseInt($("#fynum").val());
								queryData(config);
							},
							onchange : function(currPage) {
								config.page = currPage;
								queryData(config);
							},
							info : {
								first : '首页',
								first_on : true,
								last : '尾页',
								last_on : true,
								prev : '上一页',
								prev_on : true,
								num_on : true,
								next : '下一页',
								next_on : true,
								msg_on : false, // 为false 可以关闭 跳转栏
								link : 'javascript:void(0);',
								msg : '<span>&nbsp;&nbsp;跳转{curr}/{sum}页,每页显示{pageNum}</span>'
							}
						});
					}
				} else {
					//alert(data.msg);
				}
				if (config.loadcallback) {
					config.loadcallback(data);
				} //每次查询完的回调方法
			});
		}catch (e){queryData(config);  }
	}
};
//tab导航
RonsaiJS.prototype.tab = function(container, activetag, callfun){
	$(container).find(activetag).bind('click', function(){
		$(container).find(activetag).removeClass("active");
		$(this).addClass("active");
		if($(this).parents(".tabcontent").length>0){//如果是tab下的一个子tab
			$($(this).attr("for")).find(".tabcontent").hide();
		}else
			$(".tabcontent").hide(); //隐藏所有显示内容区
		$($(this).attr("for")).show();
		var index = $(container+" "+activetag).index(this);
		if(callfun != null)
			callfun(index);
	});
};
//引入css/js/插件模块
RonsaiJS.prototype.importres = function(modals){
	if(!this.isArray(modals)){return false;}
	var jsfiles = [], str;
	for(index in modals){ //加载css
		str = modals[index];
		if(str.endWith(".css")){this.loadFile(str, "css");}
		else if(str.endWith(".js")){jsfiles.push(str);}
		else if(str=="font"){this.loadFile("/js/plugins/font-awesome/css/font-awesome.min.css", "css");}
		else if(str=="bootstrap"){this.loadFile("/js/plugins/bootstrap/v3.3/css/bootstrap.min.css", "css");
			jsfiles.push("/js/plugins/bootstrap/v3.3/js/bootstrap.min.js");}
		else if(str=="inc"){jsfiles.push("/js/inc/inc.js");}
		else if(str=="layer"){this.loadFile("/js/plugins/layer2.3/skin/layer.css", "css");
			jsfiles.push("/js/plugins/layer2.3/layer.js"); }
		else if(str=="jtemplates"){jsfiles.push("/js/plugins/jTemplates_0_8_4/jquery-jtemplates.js");}
		else if(str=="myPagination"){this.loadFile("/js/plugins/myPagination/page.css", "css");
			jsfiles.push("/js/plugins/myPagination/jquery.myPagination.js");};
	}
	//加载js文件
	var testJqueryTimer=setInterval(function(){
		try{var index = 0;
			function loadScript(){
				try{
					if(index >= jsfiles.length){RS.ready.call(); RS.addReadyListener(); /*调用ready方法*/return false;}
					str = jsfiles[index];
					$.getScript(str, function(){index ++;loadScript();});  }catch (ex){}
			}
			if(jQuery.isReady){window.clearInterval(testJqueryTimer); loadScript();/**加载js*/}
		}catch(e){};
	}, 20);
};
//加载文件,css,js
RonsaiJS.prototype.loadFile = function(filename, filetype){
	if(filetype == "js"){var fileref = document.createElement('script');fileref.setAttribute("type","text/javascript");fileref.setAttribute("src",filename);
	}else if(filetype == "css"){var fileref = document.createElement('link');fileref.setAttribute("rel","stylesheet");fileref.setAttribute("type","text/css");fileref.setAttribute("href",filename);}
	if(typeof fileref != "undefined"){document.getElementsByTagName("head")[0].appendChild(fileref);}
};
//添加额外的页面css
RonsaiJS.prototype.appendCss = function(css){$("body").append('<style>'+css+'</style>');};


RS.init(); //初始化对象
/**-----------------其他--------------------------*/
/**-----------------String--------------------------*/
String.prototype.endWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length) {return false; }
	if(this.substring(this.length-str.length)==str) {return true;}
	else{  return false;}
};
String.prototype.trim=function(){
	return this.replace(/(^\s*)|(\s*$)/g, "");
};


/**----------------弹出层-----------------------------*/
//弹出层时间
var alertTime = 3;
//弹出层标题
var alertTitle = "温馨提示";
window.alert = function(msg, icon, callback) {
	/**
	 *       用例：
	 *        eq1：alert("我是一个测试弹出层");
	 *        eq2：alert("我是一个测试弹出层",-1,function(){
	 *               alertMsg("我要刷新页面");
	 *             });
	 */
	if (icon == undefined || icon == null) {
		icon = -1;
	}
	var index = 0;
	if (callback != undefined && callback != null) {
		index = parent.layer.alert(msg, icon, function() {
			callback();
		});
	} else {
		index = parent.layer.alert(msg, icon);
	}
	parent.layer.title(alertTitle, index);
};
/**
 *
 *  简单的弹出层
 *  msg       :  消息内容（必填）
 *  icon      :  图标(可选) -1没符号 1正确 2错误  
 *  callback  :  回调方法(可选)
 *;
 */
var alertMsg = function(msg, icon, callback) {
	/**
	 *       用例：
	 *        eq1：alertMsg("我是一个测试弹出层");
	 *        eq2：alertMsg("我是一个测试弹出层",-1,function(){
	 *               alertMsg("我要刷新页面");
	 *             });
	 */
	if (icon == undefined || icon == null) {
		icon = -1;
	}
	if (callback != undefined && callback != null) {
		parent.layer.msg(msg, {icon:icon}, function() {
			callback();
		});
	} else {
		parent.layer.msg(msg, {icon:icon});
	}
};
/**
*
*  弹出确认弹出层
*  msg  :  消息内容
*  yes_callback  : 正确的回调方法 (可选)
*  no_callback  :  错误的回调方法(可选)
*
*/
var alertConfirm = function(msg, yes_callback, no_callback) {
	/**
	 *       用例：
	 *        eq1：alertConfirm("弹出确认提示层");
	 *        eq2：alertConfirm("弹出确认提示层",function(){
	 *               alertMsg("确认");
	 *             });
	 *        eq3：alertConfirm("弹出确认提示层",function(){
	 *               alertMsg("确认");
	 *             },function(){
	 *               alertMsg("取消"); 
	 *             });
	 */
	if (yes_callback == undefined || yes_callback == null) {
		yes_callback = function() {};
	}
	if (no_callback == undefined || no_callback == null) {
		no_callback = function() {};
	}
	var index = parent.layer.confirm(msg, function() {
		yes_callback();
		parent.layer.close(index);
	}, function(index) {
		no_callback();
	});
	parent.layer.title(alertTitle, index);
};
//icon 对应数字
//parent.layer.alert(msg, -1); 没符号 
//parent.layer.alert(msg, 0);  感叹号
//parent.layer.alert(msg, 1);  正确
//parent.layer.alert(msg, 2);  错误
//parent.layer.alert(msg, 3);  禁止
//parent.layer.alert(msg, 4);  问号
//parent.layer.alert(msg, 5);  减号
//parent.layer.alert(msg, 6);  赞
//parent.layer.alert(msg, 7);  锁 
//parent.layer.alert(msg, 8);  哭脸
//parent.layer.alert(msg, 9);  笑脸
//parent.layer.alert(msg, 10); 正确
//parent.layer.alert(msg, 11); 闹钟
//parent.layer.alert(msg, 12); 消息
//parent.layer.alert(msg, 13); 米田共
//parent.layer.alert(msg, 14); 邮箱发送箭头
//parent.layer.alert(msg, 15); 鼠标下箭头
//parent.layer.alert(msg, 16); 加载

/** 验证是否是空 true 空 false 不是空 */
var isNull = function(text) {
	if (text == null || text == undefined || $.trim(text).length <= 0) {
		text = text + "";
		if (isNaN(text)) {
			return true;
		} else {
			if ($.trim(text).length <= 0) {
				return true;
			}
		}
	}
	return false;
};

/**------------------------绑定事件-------------------*/
/**
 * 单个事件执行
 */
var _fromtSingleData = function() {
	$("[data-tag=single_handle]").click(function() {
		/** 回调路径 */
		var url = $(this).attr("data-url");
		if (isNull(url)) {
			alertMsg("回调路径有误", 2);
			return;
		}
		/** 参数 */
		var _param = $(this).attr("data-param");
		var confirmMsg = $(this).attr("data-confirmmsg");
		var msg = $(this).attr("data-msg");
		if (isNull(confirmMsg)) {
			handleData(url, "", _param, msg);
		} else {
			alertConfirm(confirmMsg, function() {
				handleData(url, "", _param, msg);
			});
		}
	});
};
/**
 * 
 * 操作数据
 * 
 */
var handleData = function(url, ids, _param, msg) {
	param = _paramValues(_param);
	if (!isNull(ids)) {
		param["ids"] = ids;
	}
	post(url, param, function(data) {
		if (data.errcode === 0) {
			if (!isNull(msg)) {
				alertMsg(msg, 1, function() {
					query();
				});
			} else {
				query();
			}
		} else if(data.errcode === 2){//2为返回自定义消息
			alertMsg(data.errmsg, 2, function() {
				query();
			});
		} else {
			/** 删除之后 */
			alertMsg("系统繁忙，请稍后再试！", 2);
		}
	});
};
function _paramValues(str) {
	param = {};
	if (!isNull(str)) {
		var list = str.split(",");
		for ( var i = 0; i < list.length; i++) {
			var val = list[i];
			var values = val.split(":");
			if (values.length == 2) {
				param[values[0]] = values[1];
			}
		}
	}
	return param;
}

