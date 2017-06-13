/**
 * 后台登录、退出
 * */
define(function(require, exports, module) {
	var Service = require("core/prototype.Service");
	
	function login() {};
	
	/**
	 * 登录
	 * */
	login.prototype.loginin = function(username,password){
		var service = new Service;
		service.path = "/manage/login";
		service.method="POST";
		service.data={username:username,password:password};
		return service.send();
	};
	
	/**
	 * 退出
	 * */
	login.prototype.loginout = function(){
		var service = new Service;
		service.path = "/manage/logout";
		service.method="POST";
		service.data={content:1};
		return service.send();
	};
	
	module.exports = new login;
});
