/** 配送范围 */

var area = {};
area.val = {
	province : {
		id : 20,
		name : "广东省"
	},
	city : {
		id : 234,
		name : "深圳市"
	},
	area : {
		id : 2334,
		name : "南山区"
	}
};

$(function() {
	// 显示省
	post("/region/queryByState", {
		pid : 1
	}, function(msg) {
		if (msg.code == 1) {
			var list = msg.list;
			var html = [];
			$(list).each(
					function(i, d) {
						html.push('<h3 id="province' + d[0]
								+ '"><a href="javascript:void(0)" data-value="'
								+ d[0] + '">' + d[1] + '</a></h3>');
					});
			$("div.arealist").html(html.join(""));

			$("div.arealist h3 a").click(function() { // 查询省下面的市
				var id = $(this).attr("data-value");
				if ($("ul[pid=" + id + "]").is(":visible")) {
					$("ul[pid=" + id + "]").remove();
				} else {
					showCities($(this).attr("data-value"));
					area.val.province.id = $(this).attr("data-value");
					area.val.province.name = $(this).text();
				}
			});
		}
	});
});

// 显示城市
function showCities(pid) {
	post(
			"/region/queryByState",
			{
				pid : pid
			},
			function(msg) {
				if (msg.code == 1) {
					var list = msg.list;
					var html = [];
					html.push('<ul class="citylist" pid="' + pid + '">');
					$(list)
							.each(
									function(i, d) {
										html
												.push('<li id="city'
														+ d[0]
														+ '" class="city"><a href="javascript:void(0)" data-value="'
														+ d[0] + '">' + d[1]
														+ '</a></li>');
									});
					html.push('</ul>');
					
					$("#province" + pid).after(html.join(""));

					$("ul.citylist[pid=" + pid + "] li a").click(function() { // 查询市下面的区
						var id = $(this).attr("data-value");
						if ($("ul[pid=" + id + "]").is(":visible")) {
							$("ul[pid=" + id + "]").remove();
						} else {
							showAreas($(this).attr("data-value"));
							area.val.city.id = $(this).attr("data-value");
							area.val.city.name = $(this).text();
						}
					});
				}
			});
}

// 显示区
function showAreas(pid) {
	post("/region/queryByState", {
		pid : pid
	}, function(msg) {
		if (msg.code == 1) {
			var list = msg.list;
			var html = [];
			html.push('<ul class="arealist" pid="' + pid + '">');
			html.push('<li><span  pid="' + pid
					+ '" class="checkbox">全选</span></li>');
			$(list).each(
					function(i, d) {
						html.push('<li><a href="javascript:void(0)" pid="'
								+ pid + '" data-value="' + d[0] + '" class="'
								+ (d[4] == 1 ? 'on' : '') + '">' + d[1]
								+ '</a></li>');
					});
			html.push('</ul>');
			$("#city" + pid).after(html.join(""));

			$("ul.arealist[pid=" + pid + "] li a").click(
				function() { // 选择区
					area.val.city.id = pid;
					if (!$(this).hasClass("checkbox")) {// 非全选按钮
						var opType = -1;
						var data = "";
						if ($(this).hasClass("on")) { // 取消
							$(this).removeClass("on");
							opType = 0;
							data = {
								"idArr" : $(this).attr("data-value"),
								"opType" : opType,
								"provRegion" : area.val.province.id,
								"cityRegion" : area.val.city.id
							};
						} else { // 添加
							$(this).addClass("on");
							opType = 1;
							data = {
								"idArr" : area.val.province.id + ","
										+ area.val.city.id + ","
										+ $(this).attr("data-value"),
								"opType" : opType
							};
						}
						post("/region/ajaxModify", data, function(msg) {
							if (!msg.success) {
								alertmsg(msg.msg);
							}
						});
					}
				});
			
			//判断是否已经全选
			var flag = true;
			$("ul.arealist[pid=" + pid + "] li a").each(function(i, o){
				if(!$(this).hasClass("on")){
					flag = false;
					return false;
				}
			});
			if(flag){
				$("span.checkbox[pid=" + pid + "]").addClass("on");
			}
			
			// 全选
			$("span.checkbox[pid=" + pid + "]").click(
					function() {
						var idArr = new Array();
						$("li a[pid=" + $(this).attr("pid") + "]").each(
								function(i, d) {
									idArr.push(Number($(this)
											.attr("data-value")));
								});
						var opType = -1;
						if ($(this).hasClass("on")) { // 取消
							$(this).removeClass("on");
							$("li a[pid=" + $(this).attr("pid") + "]")
									.removeClass("on");
							opType = 0;
						} else { // 添加
							idArr.push(Number(area.val.province.id));
							idArr.push(Number(area.val.city.id));
							opType = 1;
							$(this).addClass("on");
							$("li a[pid=" + $(this).attr("pid") + "]")
									.addClass("on");
						}
						post("/region/ajaxModify", {
							"idArr" : jsonToString(idArr),
							"opType" : opType,
							"provRegion" : area.val.province.id,
							"cityRegion" : area.val.city.id
						}, function(msg) {
							if (!msg.success) {
								alertmsg(msg.msg);
							}
						});
					});
		}
	});
}