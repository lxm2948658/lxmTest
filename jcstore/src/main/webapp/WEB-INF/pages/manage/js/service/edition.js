/**
 * 版本管理模块
 * */
define(function(require, exports, module) {
	var Service = require("core/prototype.Service");

	function edition() {};
	
	/**
	 * 保存版本
	 * */
	edition.prototype.save = function(data){
		var service = new Service;
		service.path = "/manage/softType/save";
		service.method="POST";
		service.data = data;
		return service.send();
	};

	/**
	 * 获取模块信息
	 * */
	edition.prototype.moduleInfo = function(){
		var service = new Service;
		service.path = "/manage/softType/moduleInfo";
		service.method="GET";
		return service.send();
	};

	/**
	 * 获取版本信息
	 * */
	edition.prototype.getInfo = function(id){
		var service = new Service;
		service.path = "/manage/softType/get";
		service.method="GET";
		service.data = {id:id};
		return service.send();
	};
	
	/**
	 * 启用停用版本
	 * */
	edition.prototype.change = function(id,status){
		var service = new Service;
		service.path = "/manage/softType/change";
		service.method="POST";
		service.data={id:id,status:status};
		return service.send();
	};
	
	module.exports = new edition;
});
