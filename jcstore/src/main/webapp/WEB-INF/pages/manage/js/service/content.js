/**
 * 内容设置
 * */
define(function(require, exports, module) {
	var Service = require("core/prototype.Service");
	
	function content() {};
	
	/**
	 * 内容查看
	 * */
	content.prototype.check = function(id){
		var service = new Service;
		service.path = "/manage/content/find";
		service.method="GET";
		service.data={id:id};
		return service.send();
	};
	
	/**
	 * 保存内容
	 * */
	content.prototype.save = function(id,content){
		var service = new Service;
		service.path = "/manage/content/save";
		service.method="POST";
		service.data={id:id,content:content};
		return service.send();
	};
	
	/**1.2**/
	/**
	 * 获取当前app版本信息
	 * */
	content.prototype.version = function(){
		var service = new Service;
		service.path = "/manage/version/get";
		service.method="GET";
		return service.send();
	};
	
	/**
	 * 更新当前app版本信息
	 * */
	content.prototype.versionchange = function(jsonData){
		var service = new Service;
		service.path = "/manage/version/change";
		service.method="POST";
		service.data = jsonData;
		return service.send();
	};
	
	
	module.exports = new content;
});
