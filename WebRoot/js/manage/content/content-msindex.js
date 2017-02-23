function query(){
    RS.loadPageData({
        isarray: true, //是否为数组
        url: "/sys/msindex/getMsindexList",  //数据源地址
        params: serializeFormToJson("query_form"), // 参数，页码字段统一为page（不用传，该方法内部自行控制）
        container: "tbodyList", //列表容器
        textareaid: "textareaTemp", //textarea模板id
        loadcallback: function (data) { //每次查询回调方法，data为查询到的数据结果
        }
    });
}

function save(_this,type){//y("#status  option:selected").text();
	var flag = true;
	if(type==1){//热门品牌保存
		var obj = $(_this).parent().parent();
		var msid = $(obj).attr("msid");
		
		var columnName = $(obj).find("input[name=column_name]").val();
		var status = $(obj).find("#status").val();

		if(columnName==""){
			$(obj).find("input[name=column_name]").css("border-color","#D9534F");
			flag = false;
		}
		
		if(!flag){
			return false;
		}
		
		$.post("/sys/msindex/updateMS",{type:type,id:msid,columnName:columnName,status:status},function(data){
			
		},"json");
	}else if(type==2){//普通栏目保存
		var obj = $(_this).parent().parent();
		var sort = $(obj).find("input[name=sort]").val();
		var columnNo = $(obj).find("input[name=columnNo]").val();
		var columnName = $(obj).find("input[name=columnName]").val();
		var status = $(obj).find("#status").val();
		var msid = $(obj).attr("msid");
		

		if(sort==""){
			$(obj).find("input[name=sort]").css("border-color","#D9534F");
			flag = false;
		}
		if(columnNo==""){
			$(obj).find("input[name=columnNo]").css("border-color","#D9534F");
			flag = false;
		}
		if(columnName==""){
			$(obj).find("input[name=columnName]").css("border-color","#D9534F");
			flag = false;
		}
		
		if(!flag){
			return false;
		}
		
		$.post("/sys/msindex/updateMS",{type:type,id:msid,columnNo:columnNo,columnName:columnName,status:status,sort:sort},function(data){
			if(data.errcode==0){
				window.location.reload();
			}else{
				alertMsg(data.errmsg,2);
			}
		},"json");
	}
}

function deleteMS(id){
	alertConfirm("确定删除？",function(){
    	post("/sys/msindex/deleteMS",{id:id},function(data){
    		if(data.errcode==0){
				window.location.reload();
			}else{
				alertMsg(data.errmsg,2);
			}
    	});
	});
}

function deleteTooltip(_this){
	$(_this).css("border-color","#ccc");
} 