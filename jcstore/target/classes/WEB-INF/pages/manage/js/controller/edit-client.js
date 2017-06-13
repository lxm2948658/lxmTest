define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var filter=require("utils/data.filter");
	var clientService = require("manage/service/client");
	var Handlebars = require("common/handlebar");
	var editHtml=$("#editHtml").html();
	var editTemplate = Handlebars.compile(editHtml);
	var check=true;
	require("jqueryValidate");
	
	var id=filter.getQueryString("id");
	clientService.get(id).ready(function(data){
		var obj=data.aaData;
		var html = editTemplate(obj);
		$('#edit_form').append(html);
	})
	$("body").on("click","#edit_save",function(){
		if(!check||!$("#edit_form").valid()){
			return
		}
		var companyName=$("#companyName").val();
		var contactWay=$("#contactWay").val();
		
		clientService.update(companyName,contactWay,id).ready(function(data){
			if(data.status=="1"){
				dialog.lite("修改成功","/manage/clientList");
				check=false
			}else{
				dialog.lite(data.statusMsg);
			}
		})
		
	});
	
	$("body").on("click","#edit_reset",function(){
		clientService.reset(id).ready(function(data){
			if(data.status=="1"){
				dialog.lite("密码重置成功");
			}else{
				dialog.lite("密码重置失败");
			}
			
		})
	})
	
	
	var validate =  $("#edit_form").validate({
		 onfocusout: function(element){
		        $(element).valid();
		    },
		rules:{
			"firm":{
				required:true,
				maxlength:40
			},
			"phone":{
				required:true,
				maxlength:40
				
			}
			
		},
		messages:{
			"firm":{
				required:"企业名称不能为空！",
				maxlength:"企业名称不能超过40字！"
			},
			"phone":{
				required:"联系电话不能为空！",
				maxlength:"联系电话不能超过40字！"
			}
		},
	});
});