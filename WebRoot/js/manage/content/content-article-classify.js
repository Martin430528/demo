
//type:添加1-修改2-删除3
function updateAC(_this,type){
	var html = "<div class='tooltip top in' style='display:bolck;top:-32px;left:-5px;'><div class='tooltip-arrow'></div><div class='tooltip-inner'>请填写</div></div>";
	
	
	var obj = $(_this).parent().parent();
	var id = $(obj).attr("acId");
	var sort = $(obj).find("#sort").val();
	var classify_name = $(obj).find("#classify_name").val();
	var status = $(obj).find("#status").val();
	
	var flag = true;
	if(sort=="" && type!="3"){
		//$(obj).find("#sort").after(html);
		$(obj).find("#sort").css("border-color","#D9534F");
		flag = false;
	}
	if(classify_name=="" && type!="3"){
		//$(obj).find("#classify_name").after(html);
		$(obj).find("#classify_name").css("border-color","#D9534F");
		flag = false;
	}

	if(typeof(id) == "undefined"){
		id = null;
	}
	
	if(!flag){
		return false;
	}
	
	if(type=="3"){
		alertConfirm("确定删除该数据？",function(){
			$.post("/sys/article/updateAC",{id:id,type:type},function(data){
				if(data.errcode==0){
					window.location.reload();
				}else{
					alertMsg(data.errmsg,2);
				}
			});
		});
	}else{
		$.post("/sys/article/updateAC",{id:id,sort:sort,classifyName:classify_name,status:status,type:type},function(data){
			if(data.errcode==0){
				window.location.reload();
			}else{
				alertMsg(data.errmsg,2);
			}
		});
	}
	
}

function deleteTooltip(_this){
	$(_this).css("border-color","#ccc");
} 
