define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var listTable = require("listTable");
	var clientService = require("manage/service/client");
	var clientId;
	var searchData = {};
	//列表
	window.clientList = new listTable({
		'el':'#clientExperienceList',
		'url':domainUrl+'/manage/client/listTrial',
		'page':true,
		'limit':20,
		'columns':[
			{ "title": " ","render":function(d,t,row){
				var operHtml = '<input class="select" data-id="'+row.id+'" type="checkbox"/>';
				return operHtml;
			},'width':'40px'},
			{ "title": "用户名","render":function(d,t,row){
				return row.username;
			},'width':'160px'},
			{ "title": "客户姓名",'width':'290px',"render":function(d,t,row){
				return row.clientName;
			}},
			{ "title": "企业名称","render":function(d,t,row){
				return row.companyName;
			},'width':'290px'},
			{ "title": "开通日期","render":function(d,t,row){
				return row.createtime.substr(0,10);
			},'width':'290px'},
			{ "title": "操作","render":function(d,t,row){
				var operHtml = '<a title="删除" data-id="'+row.id+'" href="javascript:void(0)" class="btn-table font-white delete-btn oper"><i class="dele"></i></a>';
				return operHtml;
			},'width':'124px'}
		]
	});

	$(document).on('click','.delete-btn',function () {
		clientId = $(this).attr('data-id');
		deletDialog.open();
	})

	$(document).on('click','.delete-all a',function () {
		if($("input[type='checkbox']:checked").val()){
			var clients = [];
			$('.select').each(function () {
				var that = $(this);
				if(that.is(':checked')){
					clients.push(that.attr('data-id'));
				}
			})
			clientId = clients.join(',');
			deletDialog.open();
		}else {
			tipDialog.open();
		}
	})

	var flag=false;
	var deletDialog = dialog.dialog({
		content:'<p style="text-align: center; font-size: 16px;margin-top: 10px;">确认删除体验账号？请慎重操作！</p>',
		title:"删除体验账号",
		width:378,
		button:{
			"确定":function(){
				if(flag) {
					return;
				}
				flag = true;
				clientService.delTrial({clientIds:clientId}).ready(function(data){
					flag = false;
					if(data.status=="1"){
						deletDialog.close();
						clientList.draw(1);
						$(".select-all").prop('checked',false);
					}else{
						dialog.lite(data.statusMsg)
					}
				})
			},
			"取消":function(){}
		}
	})

	var tipDialog = dialog.dialog({
		content:'<p style="text-align: center; font-size: 16px;margin-top: 10px;">请选择账号后删除！</p>',
		title:"操作提示",
		width:378,
		button:{
			"确定":function(){
				tipDialog.close();
			}
		}
	})

	$(document).on('click','.DTTTFooter a',function () {
		$(window).scrollTop(0);
	})

	//全选
	var selectFlag = false;
	$('.select-all').change(function () {
		if($(this).is(':checked')){
			$("input[type='checkbox']").prop('checked',true);
			selectFlag = true;
		}else {
			$("input[type='checkbox']").prop('checked',false);
			selectFlag = false;
		}
	})

	$(document).on('change','.select',function () {
		if($(this).is(':checked')){
			selectFlag = true;
			$('.select').each(function () {
				if(!$(this).is(':checked')){
					selectFlag = false;
				}
			})
		}else {
			selectFlag = false;
		}
		if(selectFlag){
			$('.select-all').prop('checked',true);
		}else {
			$('.select-all').prop('checked',false);
		}
	})

	$(document).on('click','#searchable_paginate a',function () {
		$('.select-all').prop('checked',false);
	})

});