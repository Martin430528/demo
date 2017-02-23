function edit(roleId) {
    var menuIds = [];
    $("input[name='checkbox']:checked").each(function (i, d) {
        menuIds.push(Number($(this).val()));
    });
    $("input[name='parent']:checked").each(function (i, d) {
    	menuIds.push(Number($(this).val()));
    });
    var data = {
        "roleId": roleId,
        "menuIds": menuIds.join(",")
    };
    post("/sys/purview/ajaxEdit", data, function (msg) {
        if (msg.errcode === 0) {
        	alertMsg("保存成功！", 1, function(){
                location.reload();
            });
        } else {
            alertMsg(msg.errmsg, 2);
        }
    });
}
//全选
function checkAll() {
    if ($("#checkAll").is(":checked")) {
        $("[name='checkbox']").each(function () {
            $(this).prop('checked', true);
        });
    } else {
        $("[name='checkbox']").each(function () {
            $(this).removeAttr('checked');
        });
    }
}
//模块全不选
function checkAllByP(pId){
    if (!$("#parent_"+pId).is(":checked")) {
    	$("[name='checkbox']").each(function () {
            var parentId=$(this).attr("parentId");
            if(pId==parentId){
                $(this).removeAttr('checked');
            }
        });
    }
}