define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var clientService = require("manage/service/platform");
	require("jqueryValidate");
	
//	发送
	$("#add_save").click(function(){
		if(!$("#message-add").valid()){
			return
		}
		var aName = $('#add-name').val().replace(/，/g, ',');
		var pNameArr = []; 
		var aNameArr = aName.split(",");
		$.each(aNameArr,function(idx,item){
			pNameArr.push($.trim(item));
		});
		var sendObjects = pNameArr.join(",");
		var content = $.trim($('#add-content').val());
		var link = $('#add-link').val();
		clientService.addmsg(sendObjects,content,link).ready(function(data){
			if(data.status=="1" && data.wrong==''){
				dialog.alert("消息发送成功！",'系统提示','/manage/platform/toMessageList');
			}else if(data.status=="1" && data.wrong!=''){
				dialog.alert("消息发送成功，以下对象发送失败"+'<br/>'+data.wrong,'系统提示','/manage/platform/toMessageList');
			}else{
				dialog.alert("消息发送失败！",'系统提示');
			}
		})
	});

	var validate =  $("#message-add").validate({
		rules:{
			"add-name":{
				required:true 
			},
			"add-content":{
				required:true,
				maxlength:100
			},
			"add-link":{
				urlHttp:true,
				maxlength:150
			}
		},
		messages:{
			"add-name":{
				required:"发布对象不能为空！"
			},
			"add-content":{
				required:"发布内容不能为空！",
				maxlength:"发布内容不能大于100字！"
			},
			"add-link":{
				urlHttp:"请输入正确的URL！",
				maxlength:"不可超过150字符！"
			}
		},
	});
});