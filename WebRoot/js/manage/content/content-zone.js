$(function() {

});

//type:添加1-修改2-删除3
function updateZone(_this,type){
	var obj = $(_this).parent().parent();
	var id = $(obj).attr("zoneId");
	var sort = $(obj).find("#sort").val();
	var name = $(obj).find("#name").val();
	var link = $(obj).find("#link").val();
	
	var flag = true;
	if(sort=="" && type!="3"){
		$(obj).find("#sort").css("border-color","#D9534F");
		flag = false;
	}
	if(name=="" && type!="3"){
		$(obj).find("#name").css("border-color","#D9534F");
		flag = false;
	}
	if(link=="" && type!="3"){
		$(obj).find("#link").css("border-color","#D9534F");
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
			$.post("/sys/zone/updateZone",{id:id,sort:sort,name:name,link:link,type:type},function(data){
				if(data.errcode==0){
					window.location.reload();
				}else{
					alertMsg(data.errmsg,2);
				}
			});
		});
	}else{
		$.post("/sys/zone/updateZone",{id:id,sort:sort,name:name,link:link,type:type},function(data){
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
