var parent_all_list = [];// 数组
var parent_json = {};// 数组
var idx = 0;
(function($) {
	/**
	 * 联动
	 */
	jQuery.multilevel = { Init : function(select_id, callback) {
		var max = 3;// 下拉框最大数量
		var ajaxURL = "";// 异步路径
		var value_list = [];// 获取值数组		
		var hidden_list = null;
		var ID_Tag = "";//ID标签
		var NAME_Tag = "";//名称标签
		var SHOW_Tag = "";//所有下拉框是否默认显示
		var div = null;
		var type_tag = "";//选择器类型
		var init = function() {
			// 根据传入值获取下拉框
			div = $(select_id);
			// 设置最大值
			var _max = parseInt(div.attr("data-max"));
		
			if (!isNaN(_max))
				max = _max;
			// 获取异步路径
			ajaxURL = div.attr("data-url");
			if (isNull(ajaxURL)) {
				alertMsg("回调路径有误...", 2);
			}
			ID_Tag = div.attr("data-id");
			if (isNull(ID_Tag)) {
				alertMsg("参数配置出错...", 2);
			}
			NAME_Tag = div.attr("data-name");
			if (isNull(NAME_Tag)) {
				alertMsg("参数配置出错...", 2);
			}
			type_tag = div.attr("data-type");
			// 设置选中值
			hidden_list = div.find(":hidden");
			hidden_list.each(function(i_) {
				var _value = $(this).val();
				if (isNull(_value)) {
					_value = -1;
				}
				value_list[i_] = _value;
			});
			//设置联动分类默认全部显示
			SHOW_Tag = div.attr("data-show");
			if (!isNull(SHOW_Tag) && SHOW_Tag=="3") {
				hidden_list.each(function(i_) {
					var _default_topname = $(this).attr("default-topname");
					if (isNull(_default_topname)) {
						_default_topname="请选择";
					}
					div.append("<select idx="+idx+"><option value='-1'>"+_default_topname+"</option></select>");// 创建下拉框
					idx++;
					if(i_==0){
						var _this = div.find("select").last();// 获取最后一个下拉框					
						_select(_this, 0);// 设置值
					}
				});
			}else{
				// 新建下拉框
				if(type_tag=='area'){
					div.append("<select idx="+idx+"><option value='-1'>请选择省</option></select>");// 创建下拉框
				}else{
					div.append("<select idx="+idx+"><option value='-1'>请选择</option></select>");// 创建下拉框
				}
				idx++;
				var _this = div.find("select").last();// 获取最后一个下拉框					
				_select(_this, 0);// 设置值
			}
		};
		// 设置下拉框
		function _select(_this, parent_id) {
			if (parent_id == -1)
				return;
			parent_all_list = parent_json[ajaxURL];
			if (parent_all_list == null || parent_all_list == undefined) {
				_ajax(_this, parent_id);// 异步获取集合
				return;
			}
			var list = parent_all_list[parent_id];// 判断值是否为空
			if (list == null || list == undefined) {
				_ajax(_this, parent_id);// 异步获取集合
				return;
			}
			// 迭代集合
			$.each(list, function() {
				_this.append("<option value='" + this[ID_Tag] + "' >" + this[NAME_Tag] + "</option>");
			});
			
			_this.change(function() {
				var index = $(this).index() - hidden_list.length;
				//设置隐藏域				
				hidden_list.eq(index).val($(this).val());
				hidden_list.eq(index).nextAll("[type=hidden]").each(function() {
					if (hidden_list.eq(index).val() != value_list[index]) {
						$(this).val("-1");
					}
				});
				//设置下拉框
				$(this).nextAll("select").remove();
				var thisSelectedVal = $(this).val();
				if (div.find("select").length < max){
					if (!isNull(SHOW_Tag) && SHOW_Tag=="3") {
						hidden_list.each(function(i_) {
							var _default_topname = $(this).attr("default-topname");
							if (isNull(_default_topname)) {
								_default_topname="请选择";
							}
							if(i_>index){
								div.append("<select idx="+idx+"><option value='-1'>"+_default_topname+"</option></select>");// 创建下拉框
								idx++;
								if(i_==index+1){
									var last_select = div.find("select").last();
									_select(last_select, thisSelectedVal);
								}
							}
						});
					}else {
						if(thisSelectedVal != -1){
							// 新建下拉框
							if(type_tag=='area'){
								var level = div.find("select").length;
								if(level==2){
									div.append("<select idx="+idx+"><option value='-1'>请选择区</option></select>");
								}else{
									div.append("<select idx="+idx+"><option value='-1'>请选择市</option></select>");	
								}
							}else{
								div.append("<select idx="+idx+"><option value='-1'>请选择</option></select>");
							}
							idx++;
							var last_select = div.find("select").last();
							_select(last_select, $(this).val());
						}
					}
				}
				if (callback != null) {
					callback($(this));
				}
			});
			var index = $(_this).attr("idx");
			_this.val(value_list[index]);
			if (!isNull(value_list[index]) && value_list[index] != -1) {
				_this.change();
			}
			value_list[index] = null;
		}
		// 异步加载
		function _ajax(_this, parent_id) {
			param = {};
			param["pid"] = parent_id; // 父級ID
			$.post(ajaxURL, param, function(data) {
				if (parent_json[ajaxURL] != null) {
					parent_all_list = parent_json[ajaxURL];
					if (parent_all_list == null || parent_all_list == undefined) {
						parent_all_list = [];
					}
				} else {
					parent_all_list = [];
				}
				parent_all_list[parent_id] = data.list;
				parent_json[ajaxURL] = parent_all_list;
				_select(_this, parent_id);
			});
		}
		init();
	} };
})(jQuery);