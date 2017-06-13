define(function(require, exports, module) {
	var http = require("common/http");
	var dialog = require("common/dialog");
	var loginService = require("manage/service/login");

	//退出
	$(".loginout").click(function (){
		loginService.loginout().ready(function(data){
			if(data.status = "1"){
	    		dialog.alert('退出成功！',"操作提示",'/manage/toLogin');
	    	}
		});
	});
	
	//屏蔽enter提交
	$(window).keydown(function(e) {
		var nodeName = e.target.nodeName
	    if (e.keyCode == 13) {
	    	var triggerBtn = $(".enter-btn").length >0? $(".enter-btn") : "";
	    	//分页跳转
	    	var tClass = e.target.className;
	    	if(tClass == "jumoPage"){
	    		triggerBtn = $(".jumoPage").parents(".pagination").find(".jumpBtn");
	    	}
	    	
	    	if(triggerBtn && triggerBtn.length >0){
	    		triggerBtn.trigger("click");
	    		return false;
	    	}
	    	//页面中有表格的跳转
	    	if(nodeName != "TEXTAREA"){
	    		return false;
	    	}
	    }
	});
});