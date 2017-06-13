define(function(require, exports, module) {
	var listTable = require("listTable");
	var searchData={};
	
//	筛选
	$('#type,#orderType').change(function(){
		searchData = {};
		searchData['type'] = $('select[name="type"]').val();
		searchData['orderType'] = $('select[name="orderType"]').val();
		window.clientList.draw(1);
	});
	
	//列表
	window.clientList = new listTable({
		'el':'#messageList',
		'url':'/manage/marketing/list',
		'page':true,
		'limit':10,
		'addAttr':function () {
			return searchData;
		},
		'columns':[
			{ "title": "序号",'data':'serialNum','width':'200px'},
			{ "title": "分享页面名称","render":function(d,t,row){
				var title = '<div class="word-wrap">'+row.name+'</div>';
				return title;
			},'width':'400px'},
			{ "title": "分类","render":function(d,t,row){
				var title = '';
				if(row.type == '1'){
					 title = '店铺分享';
				}else if(row.type == '2'){
					title = '商品分享';
				}else if(row.type == '3'){
					title = '广告营销';
				}
				return title;
			},'width':'200px'},
			{ "title": "来源",'data':'shopName','width':'400px'},
			{ "title": "发布时间",'data':'createtime','width':'300px'},
			{ "title": "点击量",'data':'pageView','width':'200px'}
		]
	});

});

