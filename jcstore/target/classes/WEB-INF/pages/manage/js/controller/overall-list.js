//商品图片的上传
function uploadHeadImageFn(event,element){
    var uploadId = $(element).attr("id");
    $.ajaxFileUpload({
        fileElementId: uploadId,
        url:domainUrl+"/upload/avatars?t="+new Date().getTime(),
        secureuri: false,
        dataType: 'json',
        success: function(data, status) {
        	$('#uploadDiv a').text('重新上传');
        	if(data[0].status =='0'){
        		$('#uploadImg').hide();
        		$(".imgurlvalid").val("");
        		$('.imgurl').attr('src',"");
        		$('#valid-warp').show().html('上传失败，请重新上传！');
        	}else{
            $('.imgurl').attr('src',data[0].path);
            $('#uploadImg').show();
            $('.imgurl').show();
            $('.imgurlvalid').val('abc');
            $('#valid-warp').hide()
        	};
        },
        error: function(data, status, e) {
            if(data.responseText.indexOf("413")>=0){
                layer.msg("上传失败，请重新上传！");
            }else{
                layer.msg("上传失败，请重新上传！");
            }
        }
    });
};

define(function(require, exports, module) {
	var listTable = require("listTable");
	var dialog = require("common/dialog");
	var Handlebars = require("common/handlebar");
	var client = require("manage/service/client");
	var filter=require("utils/data.filter");
	var belongs=filter.getQueryString("uid");
	require("common/ajaxfileupload");
	require("jqueryValidate");
	var id;
	var title;
	var url;
	var picture;
	//列表
	window.clientList = new listTable({
		'el':'#messageList',
		'url':domainUrl+'/manage/panorama/find?belongs='+belongs,
		'page':true,
		'limit':10,
		'columns':[
			{ "title": "标题","render":function(d,t,row){
				var title = '<div class="word-wrap">'+row.title+'</div>';
				return title;
			},'width':'600px'},
			{ "title": "封面","render":function(d,t,row){
				var img1 = '<img class="listimg" src="'+row.pic+'"/>';
				return img1;
			},'width':'200px'},
			{ "title": "发布时间","render":function(d,t,row){
				return row.createTime;
			},'width':'300px'},
			{ "title": "链接地址","render":function(d,t,row){
				return row.url;
			},'width':'400px'},
			{ "title": "操作","render":function(d,t,row){
				var operHtml ='<a title="修改" href="javascript:void(0)" data-id="'+row.id+'" data-title="'+row.title+'" data-picture="'+row.pic+'" data-url="'+row.url+'" class="btn-table font-white oper editBtn"><i class="edit"></i></a>'+
					'<a title="删除" href="javascript:void(0)" data-id="'+row.id+'" class="btn-table font-white oper del"><i class="dele"></i></a>';
				return operHtml;
			},'width':'200px'}
		]
	});
	
//	点击按钮新增修改函数
	function ClickBtn(btn,tit){
		$(document).on('click',btn,function(){
			addDialog.title = tit;
			if(btn == '.add'){
				id = null;
				addDialog.open(function(){
		            $("#overallfrom").validate(validateRule).form(false);
		        });
			}else{
				id = this.getAttribute("data-id");
				title = $(this).attr('data-title');
				url = $(this).attr('data-url');
				picture = $(this).attr('data-picture');
				addDialog.open(function(){
		            $("#overallfrom").validate(validateRule).form(false);
		        });
				$('.imgurlvalid').val('abc');
				$('#title').val(title);
				$('#addressurl').val(url);
				$('.imgurl').attr('src',picture);
				$('.H5from .uploadImg').show();
				$('#uploadDiv a').text('重新上传');
			};
		});	
	};
//	新增
	ClickBtn('.add','新增全景');
//	修改
	ClickBtn('.editBtn','修改全景');
	
//	删除
	$(document).on('click','.del',function(){
		id = $(this).attr('data-id');
		deletDialog.open();
	});	
	
//	handlebars获取页面html
	var addDialogHtml=$("#addDialog").html();
	var addTemplate = Handlebars.compile(addDialogHtml);
	var context = {};
	var html = addTemplate(context);
	
//	新增修改dialog	
	var addDialog = dialog.dialog({
		width:605,
		height:530,
		content:html,
		isValidForm:true,
		name:"save",
		button:{
			"配置全景":function(){
				if(!$("#overallfrom").valid()){
                    return false
                }
				var title = $('#title').val();
				var url = $('#addressurl').val();
				var pic = $('.imgurl').attr('src');
				client.addoverall(id,belongs,title,url,pic).ready(function(data){
					if(data.status=="1"){
						if(id == null){
							dialog.lite("添加成功");
						}else{
							dialog.lite("修改成功");
						}
						clientList.draw();
						
					}else{
						dialog.lite(data.statusMsg)
					}
				})
			},
			"取消":function(){}
		}
	});
	
//	删除dialog
	var deletDialog = dialog.dialog({
		content:'<p style="text-align: center; color: #f00; font-size: 16px;margin-top: 10px;">确认删除该条全景？</p>',
		title:"系统提示",
		width:378,
		button:{
			"确定":function(){
				client.deloverall({id:id}).ready(function(data){
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
	});
	
//	验证
	var validateRule = {
		rules:{
			"title":{
				required:true,
				maxlength:30
			},
			"addressurl":{
				required:true,
				maxlength:100,
				urlHttp:true,
			},
			"imgurl":{
				required:true
			}
		},
		messages:{
			"title":{
				required:"标题不能为空！",
				maxlength:"标题不可超过30个字！"
			},
			"addressurl":{
				required:"链接地址不能为空！",
				maxlength:"链接不可超过100个字！",
				urlHttp:"请输入正确的链接！",
			},
			"imgurl":{
				required:"请上传图片！"
			}
		}
	};
});

