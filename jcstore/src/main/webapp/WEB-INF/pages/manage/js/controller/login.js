define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var login=require("manage/service/login");
	
//	登陆
	$(document).on('click','.submit',function(){
		var username = $.trim($('#username').val());
		var password = $.trim($('#password').val());
		if(username =='' || password ==''){
			dialog.alert('请输入用户名或密码！',"系统提示");
		}else{
			login.loginin(username,password).ready(function(data){
				if(data.status=="1"){
					window.location.href = domainUrl + '/manage/clientList';
				}else{
					dialog.alert('登录失败，用户名密码错误！',"系统提示");
				}
			})
		}
	});
});