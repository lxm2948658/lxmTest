define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var listTable = require("listTable");
	var clientService = require("manage/service/client");
	var clientId;
	var searchData = {};
	clientService.resource(0).ready(function(data){
		var typesHtml='<option value="0">全部类型</option>';
		for(var i=0;i<data.types.length;i++){
			typesHtml+="<option value="+data.types[i].id+">"+data.types[i].name+"</option>"
		}
		$("#type").html(typesHtml);
	})
	$("#search_btn").on("click",function(){
		searchData ={};
		searchData['status'] = $('select[name="status"]').val();
		searchData['type'] = $('select[name="type"]').val();
		if($('input[name="values"]').val().length > 0){
			searchData['keys'] = $('select[name="keys"]').val();
			searchData['values'] = $('input[name="values"]').val();
		}
		clientList.draw(1);
	});
	//列表
	window.clientList = new listTable({
		'el':'#clientList',
		'url':domainUrl+'/manage/client/search',
		'page':true,
		'limit':20,
		'addAttr':function () {
			return searchData;
		},
		'columns':[
			{ "title": "用户名","render":function(d,t,row){
				return row.username;
			},'width':'115px'},
			{ "title": "客户姓名",'width':'115px',"render":function(d,t,row){
				return row.clientName;
			}},
			{ "title": "企业名称","render":function(d,t,row){
				return row.companyName;
			},'width':'215px'},
			{ "title": "联系电话","render":function(d,t,row){
				return row.contactWay;
			},'width':'130px'},
			{ "title": "软件类型","render":function(d,t,row){
				return row.type;
			},'width':'90px'},
			{ "title": "到期时间","render":function(d,t,row){
				return row.expiredTime.substr(0,10);
			},'width':'115px'},
			{ "title": "使用人数","render":function(d,t,row){
				return row.userNumber;
			},'width':'88px'},
			{ "title": "状态","render":function(d,t,row){
				return row.status;
			},'width':'75px'},
			{ "title": "操作","render":function(d,t,row){
				var operHtml ='';
				operHtml +='<a title="续费&升级" href="' + domainUrl +'/manage/clientUping?id='+row.id+'" class="btn-table font-white oper"><i class="renew"></i></a>';	//续费升级
				operHtml +='<a title="修改" href="' + domainUrl +'/manage/clientEdit?id='+row.id+'" class="btn-table font-white oper"><i class="edit"></i></a>';	//修改
				operHtml +='<a title="删除" data-id="'+row.id+'" href="javascript:void(0)" class="btn-table font-white delete-btn oper"><i class="dele"></i></a>';//删除
				operHtml +='<a title="商品导入" data-id="'+row.id+'" href="'+domainUrl+'/manage/toGoodsUpload?id='+row.id+'" class="btn-table font-white oper"><i class="export"></i></a>';//导出excel
				operHtml +='<a title="场景营销" href="' + domainUrl +'/manage/client/toH5clientlist?uid='+row.uid+'" class="btn-table font-white oper"><i class="h5"></i></a>';	//场景营销
				operHtml +='<a title="全景展示" href="' + domainUrl +'/manage/client/toOveralllist?uid='+row.uid+'" class="btn-table font-white oper"><i class="overall"></i></a>';	//3D全景
				return operHtml;
			},'width':'400px'}
		]
	});

	$(document).on('click','.delete-btn',function () {
		clientId = $(this).attr('data-id');
		deletDialog.open();
	})

	var deletDialog = dialog.dialog({
		content:'<p style="text-align: center; color: #f00; font-size: 16px;margin-top: 10px;">确认删除客户信息？请慎重操作！</p>',
		title:"删除客户信息",
		width:378,
		button:{
			"确定":function(){
				clientService.del({clientId:clientId}).ready(function(data){
					if(data.status=="1"){
						deletDialog.close();
						clientList.draw();
					}else{
						dialog.lite(data.statusMsg)
					}
				})
			},
			"取消":function(){}
		}
	})

	$(document).on('click','.DTTTFooter a',function () {
		$(window).scrollTop(0);
	})
});