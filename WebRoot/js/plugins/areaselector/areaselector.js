/**
 * 地址选择器，如配送区域选择
 */
/**
 * Div区块选择对象
 */
function AreaBlock(config){
	var obj = new Object();
	
	obj.init = function()	{ //初始化方法
		obj.config = config;
		if($.isEmptyObject(obj.config)){
			obj.config = {state:0};
		}
		//事件
		$("#store-selector div.text").click(function(){ //查询省
			obj.showProvinces();
		});
		$($("ul.tab li a")[0]).click(function(){ //查询省
			obj.showProvinces();
		});
		$($("ul.tab li a")[1]).click(function(){ //查询市
			obj.showCities(obj.val.province.id);
		});
		$($("ul.tab li a")[2]).click(function(){ //查询区
			obj.showAreas(obj.val.city.id);
		});
		$("#summary-stock div.close").click(function(){
			$("#store-selector .content").hide();
			$('#store-selector').removeClass('hover');
		});
		//obj.showProvinces();
		$("#store-selector .text div").text(obj.val.province.name+obj.val.city.name+obj.val.area.name);
		$("#store-selector .content").hide();
		$('#store-selector').removeClass('hover');
	};
	 //选择的值>>
	obj.val = {province:{id:20, name:"广东省"}, city:{id:234, name:"深圳市"}, area:{id:2334, name:"南山区"}};
	obj.showProvinces = function(){ //显示省份
		post("/t/region/query", {pid:1, state:obj.config.state}, function(msg){
			if(msg.success){
				var list = $.parseJSON(msg.data);
				var html = [];
				$(list).each(function(i, d){
					html.push('<li><a href="javascript:void(0)" data-value="'+d[0]+'">'+d[1]+'</a></li>');
				});
				$("#stock_province_item ul.area-list").html(html.join(""));
				$("#stock_province_item").show();
				$("#stock_city_item").hide();
				$("#stock_area_item").hide();
				$($("ul.tab li").removeClass("curr")[0]).addClass("curr");
				$("#store-selector .content").show();
				$('#store-selector').addClass('hover');
				$($("ul.tab li")[0]).show();
				$($("ul.tab li")[1]).hide();
				$($("ul.tab li")[2]).hide();
				$("#stock_province_item ul.area-list li a").click(function(){ //查询省下面的市
					obj.showCities($(this).attr("data-value"));
					obj.val.province.id = $(this).attr("data-value");
					obj.val.province.name = $(this).text();
					$($("ul.tab li").removeClass("curr")[0]).find("a em").text(obj.val.province.name); //显示省份名称
				});
			}
		});
	};
	obj.showCities = function(pid){//显示城市
		post("/t/region/query", {pid:pid, state:obj.config.state}, function(msg){
			if(msg.success){
				var list = $.parseJSON(msg.data);
				var html = [];
				$(list).each(function(i, d){
					html.push('<li><a href="javascript:void(0)" data-value="'+d[0]+'">'+d[1]+'</a></li>');
				});
				$("#stock_city_item ul.area-list").html(html.join(""));
				$("#stock_city_item").show();
				$("#stock_province_item").hide();
				$("#stock_area_item").hide();
				$($("ul.tab li")[1]).show();
				$($("ul.tab li")[2]).hide();
				$($("ul.tab li").removeClass("curr")[1]).addClass("curr");
				
				$("#stock_city_item ul.area-list li a").click(function(){ //查询市下面的区
					obj.showAreas($(this).attr("data-value"));
					obj.val.city.id = $(this).attr("data-value");
					obj.val.city.name = $(this).text();
					$($("ul.tab li").removeClass("curr")[1]).find("a em").text(obj.val.city.name); //显示城市名称
				});
			}
		});
	};
	obj.showAreas = function(pid){//显示区
		post("/t/region/query", {pid:pid, state:obj.config.state}, function(msg){
			if(msg.success){
				var list = $.parseJSON(msg.data);
				var html = [];
				$(list).each(function(i, d){
					html.push('<li><a href="javascript:void(0)" data-value="'+d[0]+'">'+d[1]+'</a></li>');
				});
				$("#stock_area_item ul.area-list").html(html.join(""));
				$("#stock_area_item").show();
				$("#stock_city_item").hide();
				$("#stock_province_item").hide();
				$($("ul.tab li")[2]).show(); //地区标签
				$($("ul.tab li").removeClass("curr")[2]).addClass("curr");
				$("#stock_area_item ul.area-list li a").click(function(){ //查询市下面的区
					obj.val.area.id = $(this).attr("data-value");
					obj.val.area.name = $(this).text();
					$($("ul.tab li").removeClass("curr")[2]).find("a em").text(obj.val.area.name); //显示地区名称
					$("#store-selector .text div").text(obj.val.province.name+obj.val.city.name+obj.val.area.name);
					$("#store-selector .content").hide();
					$('#store-selector').removeClass('hover');
				});
			}
		});
	};
	obj.init();
	return obj;
}


