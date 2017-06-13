define(function(require, exports, module) {
	var listTable = require("listTable");
	//列表
	window.clientList = new listTable({
		'el':'#messageList',
		'url':domainUrl+'/manage/message/list',
		'page':true,
		'limit':10,
		'columns':[
			{ "title": "发送时间","render":function(d,t,row){
				var sendTime = row.createtime.substr(0,10) + '<br/>' + row.createtime.substr(11,8);
				return sendTime;
			},'width':'200px'},
			{ "title": "发送对象","render":function(d,t,row){
				var sendObj = '<p class="send-obj">' + row.sendObjects.split(',')[0] + '<span><i></i>' + row.sendObjects + '</span></span></p>';
				return sendObj;
			},'width':'200px'},
			{ "title": "消息内容","render":function(d,t,row){
				return row.content;
			},'width':'362px'},
			{ "title": "URL配置","render":function(d,t,row){
				return row.link;
			},'width':'400px'}
		]
	});

	$(document).on('mouseenter','.send-obj',function () {
		$(this).children('span').show();
	});
	$(document).on('mouseleave','.send-obj',function () {
		$(this).children('span').hide();
	});
});

