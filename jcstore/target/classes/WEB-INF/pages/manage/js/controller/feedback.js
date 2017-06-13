define(function(require, exports, module) {
	var listTable = require("listTable");
	//列表
	window.clientList = new listTable({
		'el':'#clientList',
		'url':domainUrl+'/manage/feedback/list',
		'page':true,
		'limit':10,
		'columns':[
		    { "title": "序号",'width':'50px','data':'serialNumber'},
			{ "title": "反馈人用户名",'width':'102px','data':'username'},
			{ "title": "系统版本",'width':'88px','data':'systemVersion'},
			{ "title": "手机型号",'width':'80px','data':'phoneModel'},
			{ "title": "网络环境",'width':'80px','data':'networkEnvironment'},
			{ "title": "手机号",'width':'113px','data':'phoneNumber'},
			{ "title": "来源公司",'width':'175','data':'companyName'},
			{ "title": "反馈信息","render":function(d,t,row){

				return row.feedbackContent;
			},'width':'329px'},
			{ "title": "时间",'width':'150px','data':'createtime'},
		]
	});
});

