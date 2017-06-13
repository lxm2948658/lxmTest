define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var listTable = require("listTable");
	var editionService = require("manage/service/edition");
    var searchData={};
	//列表
	window.clientList = new listTable({
		'el':'#editionList',
		'url':domainUrl+'/manage/softType/findsoft',
		'page':true,
		'limit':20,
		'columns':[
			{ "title": "版本名称","render":function(d,t,row){
				return row.name;
			},'width':'275px'},
			{ "title": "状态",'width':'220px',"render":function(d,t,row){
				var statusBtn;
				if(row.status == '1'){
					statusBtn = '<input type="button" value="停用" data-isUse="0" data-id="'+row.id+'" class="btn-table blue font-white statusBtn">';
				}else{
					statusBtn = '<input type="button" value="启用" data-isUse="1" data-id="'+row.id+'" class="btn-table border white font-black statusBtn">';
				}
				return statusBtn;
				
			}},
			{ "title": "更新日期",'width':'230px',"render":function(d,t,row){
				return row.updatetime.substr(0,10);
			}},
			{ "title": "操作","render":function(d,t,row){
				var operHtml ='';
				operHtml +='<a href="'+domainUrl+'/manage/editionEdit?id='+row.id+'" class="btn-table font-white oper"><i class="edit"></i></a>';	//编辑
				return operHtml;
			},'width':'125px'}
		]
	});

	$(document).on('click','.DTTTFooter a',function () {
		$(window).scrollTop(0);
	});
	
	//状态
	$(document).on('click','.statusBtn',function(){
		var text = $(this).val();
		var id = $(this).attr('data-id'); 
		var status = $(this).attr('data-isUse'); 
		var Dialog = dialog.dialog({
			content:'<p style="text-align: center; color: #000; font-size: 16px;margin-top: 10px;">确认'+text+'当前版本?  </p>',
			width:378,
			title:text,
			button:{
				"确定":function(){
					editionService.change(id,status).ready(function(data){
						if(data.status=="1"){
							Dialog.close();
							clientList.draw();
						}else{
							dialog.lite(data.statusMsg)
						}
					});
				},
				"取消":function(){}
			}
		});
		Dialog.open();
	});
});