/**
 * 平台管理
 * */
define(function(require, exports, module) {
	var Service = require("core/prototype.Service");
	
	function platform() {};
	
	/**
	 * 发布消息
	 * */
	platform.prototype.addmsg = function(sendObjects,content,link){
		var service = new Service;
		service.path = "/manage/message/save";
		service.method="POST";
		service.data={sendObjects:sendObjects,content:content,link:link};
		return service.send();
	};
	
	/**
	 * h5场景管理列表 删除信息
	 * */
	platform.prototype.h5tempDel = function(id){
		var service = new Service;
		service.path = "/manage/scene/delectScene";
		service.method="POST";
		service.data=id;
		return service.send();
	};
	
	/**
	 * h5场景管理列表 新增
	 * */
	platform.prototype.addh5temp = function(id,title,picture,url){
		var service = new Service;
		service.path = "/manage/scene/addType";
		service.method="POST";
		service.data={id:id,title:title,picture:picture,url:url};
		return service.send();
	};
	
	module.exports = new platform;
});
