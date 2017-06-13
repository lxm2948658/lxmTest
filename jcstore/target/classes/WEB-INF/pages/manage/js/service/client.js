/**
 * 客户添加 修改续费模块
 * */
define(function(require, exports, module) {
	var Service = require("core/prototype.Service");

	function client() {};
	
	/**
	 * 添加客户
	 * */
	client.prototype.add = function(data){
		var service = new Service;
		service.path = "/manage/client/add";
		service.method="POST";
		service.data = data;
		return service.send();
	};

	/**
	 * 删除客户
	 * */
	client.prototype.del = function(clientId){
		var service = new Service;
		service.path = "/manage/client/delete";
		service.method="GET";
		service.data = clientId;
		return service.send();
	};
	
	client.prototype.get = function(id){
		var service = new Service;
		service.path = "/manage/client/get";
		service.method="GET";
		service.data = {clientId:id};
		return service.send();
	};
	
	client.prototype.update = function(companyName,contactWay,id){
		var service = new Service;
		service.path = "/manage/client/update";
		service.method="POST";
		service.data = {companyName:companyName,contactWay:contactWay,id:id};
		return service.send();
	};
	
	
	client.prototype.reset = function(clientId ){
		var service = new Service;
		service.path = "/manage/client/reset";
		service.method="GET";
		service.data = {clientId:clientId};
		return service.send();
	};
	
	client.prototype.resource = function(clientId){
		var service = new Service;
		service.path = "/manage/client/resource";
		service.method="GET";
		service.data = {clientId:clientId};
		return service.send();
	};
	
	client.prototype.renew = function(data){
		var service = new Service;
		service.path = "/manage/client/renew";
		service.method="POST";
		service.data = data;
		return service.send();
	};

	client.prototype.upload = function(data){
		var service = new Service;
		service.path = "/manage/product/import";
		service.method="POST";
		service.data = data;
		return service.send();
	};

	client.prototype.delTrial = function(clientIds){
		var service = new Service;
		service.path = "/manage/client/deleteTrial";
		service.method="GET";
		service.data = clientIds;
		return service.send();
	};
	
	/*场景列表  新增*/
	client.prototype.addh5client = function(id,belong,title,picture,url){
		var service = new Service;
		service.path = "/manage/scene/addScene";
		service.method="POST";
		service.data = {
						id:id,
						belong:belong,
						title:title,
						picture:picture,
						url:url
						};
		return service.send();
	};
	
	/*场景列表 删除*/
	client.prototype.delh5client = function(id){
		var service = new Service;
		service.path = "/manage/scene/delectScene";
		service.method="POST";
		service.data = id;
		return service.send();
	};
	
	/*全景列表  添加 */
	client.prototype.addoverall = function(id,belongs,title,url,pic){
		var service = new Service;
		service.path = "/manage/panorama/add";
		service.method="POST";
		service.data = {
				id:id,
				belongs:belongs,
				title:title,
				url:url,
				pic:pic
		};
		return service.send();
	};
	
	/*全景列表  删除 */
	client.prototype.deloverall = function(id){
		var service = new Service;
		service.path = "/manage/panorama/delete";
		service.method="POST";
		service.data = id;
		return service.send();
	};
	
	module.exports = new client;
});
