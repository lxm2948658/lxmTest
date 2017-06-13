define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var serialize = require("utils/form.serialize");
	var clientService = require("manage/service/client");
	require("jqueryValidate");
	var check=true;
	clientService.resource(-1).ready(function(data){
		var expiresHtml="";
		var typesHtml="";
		var userNumbersHtml="";
		for(var i=0;i<data.types.length;i++){
			typesHtml+="<option value="+data.types[i].id+">"+data.types[i].name+"</option>"
		}
		$("#type").html(typesHtml);
	})
	
	
	$("#add_save").click(function(){
		if(!check||!$("#add_form").valid()){
			return
		}
		var data=serialize.serialize($("#add_form"));
		clientService.add(data).ready(function(data){
			if(data.status=="1"){
				dialog.lite("添加成功","/manage/clientList")
				check=false;
			}else{
				dialog.lite(data.statusMsg)
			}
			
		})
		
	});

	var validate =  $("#add_form").validate({
		onkeyup:false,
		 onfocusout: function(element){
		        $(element).valid();
		    },
		rules:{
			"clientName":{
				required:true,
				maxlength:20
			},
			"companyName":{
				required:true,
				maxlength:40
			},
			"contactWay":{
				required:true,
				maxlength:40
				
			},
			"username":{
				required:true,
				getusername:true,
				checkusername:true
				
			},
			"months":{
				required:true,
				checkmonths:true,
			},
			"userNumber":{
				required:true,
				checkuserNumber:true,
			},
			'type':{
				required:true
			}
		},
		messages:{
			"clientName":{
				required:"客户姓名不能为空！",
				maxlength:"客户姓名不能超过20字！"
			},
			"companyName":{
				required:"企业名称不能为空！",
				maxlength:"企业名称不能超过40字！"
			},
			"contactWay":{
				required:"联系电话不能为空！",
				maxlength:"联系电话不能超过40字！"
			},
			"username":{
				required:"用户名不能为空！",
				getusername:"用户名已存在，请重新修改",
				checkusername:"请输入4-20位英文、数字或组合"
					
			},
			"userNumber":{
				required:"请输入5以上正整数",
				checkuserNumber:"请输入5以上正整数"
			},
			"months":{
				required:"请输入正整数",
				checkmonths:"请输入正整数"
			},
			"type":{
				required:"请选择软件类型！"
			}
		},
	});
});