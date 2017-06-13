
var _nowTime = '';
define(function(require, exports, module) {
	var dialog = require("common/dialog");
	//退出重定向
	function redirectToLogin(){
    	var _url = location.href;
		if(/manage/.test(_url)){
    		return '/manage/toLogin';
    	}
	}
	(function($){
		var isAjaxSubmit = false,oldUrl,newUrl; //防止二次提交

		$.ajaxSetup({cache:false});

		$.ajaxSettings.beforeSend = function(xhr, options) {
			//防止二次提交
			var timeIndex = options.url.indexOf("?");
			newUrl = options.url.substring(0,timeIndex);
			if (isAjaxSubmit && (newUrl==oldUrl)) {
				return false;
			}
			oldUrl = options.url.substring(0,timeIndex);
		}
	
		$(document).ajaxSend(function(event, xhr, options) {
			//防止二次提交
			isAjaxSubmit = true;
		});
		$(document).ajaxComplete(function(e, x, d) {
			var httpStatus = (+x.status);
			
			isAjaxSubmit = false;
			//加载时候的进度条
			
			if(httpStatus===0){
				return false;
			}
			/**
			 * 判断http响应状态的 4** 5**；
			 * */
			
			if(httpStatus && httpStatus>=400 && httpStatus<600){
				if(newUrl.indexOf("upload/avatar")>=0){
					if(httpStatus==413){
						win.alert("上传失败，单张图片请勿超过2M");
						return false;
					}else{
						win.alert("网络错误，上传失败");
						return false;
					}
				}
				dialog.alert('服务器开小差！',"系统提示");
				return false;
			}
			
			/**
			 * http响应成功，responseText状态全局拦截；
			 * */
			if(typeof(x.responseText) === 'undefined'){
				return;
			}else {
				x = JSON.parse(x.responseText);
				if (x.status && x.status == "700") {
					var url= redirectToLogin();
					dialog.alert('登录已超时，请登录后进行相关操作！<br/>点击确定跳转至登录页面！',"系统提示",url);
					return false;
				}else if(x.status && x.status == '10040'){
					dialog.lite("输入内容包含敏感词汇，请检查后重新输入");
					return false;
				}else if(x.status && x.status == 600){
					dialog.alert("店铺关停中 无法进行操作","系统提示",'/dealer/shop/shutdown');
					return false;
				}
			}
			//防止二次提交
			
		});	
	})($);
	
	//状态码匹配
	function getServerMsg(string){
		var arr1 = [0,1,10000,10001,10010,10011,10012,10013,11111,10020,10030,10040,10014,10015,10016,10086,10100,10087,10088,10089,10090,10091,10092,10093,10097];
		var arr2 = ['FAIL','OK','登录验证失败','登录失败，用户名密码错误','用户名已被注册','手机号已被注册','验证码验证失败','手机号不存在','服务器忙','旧密码错误','用户已被冻结','信息包含违规内容','验证码超过次数','密码与旧密码相同','注册俩次密码输入不一致','数据不存在','参数错误','商品已下架','商品库存不足','收货地址不能为空','请获取旧手机验证码','请获取新手机验证码','请获取手机验证码','手机号错误','收货人不能为空'];
		var num = contains(arr1,string);
		function contains(arr, obj) {  
			obj = +obj;
		    var i = arr.length;  
		    while (i--) {  
		        if (arr[i] === obj) {  
		            return i;  
		        }  
		    }  
		    return false;  
		}  
		return arr2[num];
	}
	
	
	//escapeHtml
	String.prototype.escapeHtml = function (){
		var arrEntities = {'<':'&lt;','>':'&gt;','&':'&amp;','"':'&quot;'};
		return this.replace(/(<|>|&|\")/ig,function(all,t){
			return arrEntities[t];
		});
	};
	//反转义
	String.prototype.unescapesHtml = function (){
		var arrEntities = {'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"'};
		return this.replace(/\&(lt|gt|nbsp|amp|quot);/ig,function(all,t){
			return arrEntities[t];
		});
	};
});