define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var filter=require("utils/data.filter");
	var clientService = require("manage/service/client");
	var Handlebars = require("common/handlebar");
	require("jqueryValidate"); 
	var renewHtml="";
	var id=filter.getQueryString("id");
	var renewDialogHtml=$("#renewDialog").html();
	var renewTemplate = Handlebars.compile(renewDialogHtml);
	var $username=$("#username");
	var $clientName=$("#clientName");
	var $companyName=$("#companyName");
	var $contactWay=$("#contactWay");
	var checked=true;
	clientService.resource(id).ready(function(data){
		clientService.get(id).ready(function(new_data){
			var aaData=new_data.aaData;
			var expiresHtml="<option value='0'>不改变</option>";
			var typesHtml="";
			var userNumbersHtml="";
			for(var i=0;i<data.types.length;i++){
				if(aaData.type == data.types[i].id){
					typesHtml+="<option selected='selected' value="+data.types[i].id+">"+data.types[i].name+"</option>"
				}else{
					typesHtml+="<option value="+data.types[i].id+">"+data.types[i].name+"</option>"
				}
			}
			
			$("#type").html(typesHtml);
			$("#userNumber").val(aaData.userNumber);
			$username.html(aaData.username);
			$clientName.html(aaData.clientName);
			$contactWay.html(aaData.contactWay);
			$companyName.html(aaData.companyName);
			var TimeDate = new Date(Date.parse(aaData.expiredTime.replace(/-/g, "/")));
			$("#months").attr("renewTime",filter.formatDate(TimeDate,'YYYY-MM-DD'));
			$("#userNumber").attr("userNumber",aaData.userNumber);
		})
		
	})
	
	$("#renew_save").click(function(){
		if(!checked||!$("#uping_form").valid()){
			return
		}
		var renewData={};
		renewData.type=$("#type option:selected").text();
		console.log(renewData.type)
		renewData.userNumber=$("#userNumber").val();
		renewData.months=$("#months").val();
		renewData.expiredTime=addMoth($("#months").attr("renewTime"),Number($("#months").val()))
		
		console.log(renewData)
		renewHtml = renewTemplate(renewData);
		var ajaxData={};
		ajaxData.type=$("#type").val();
		ajaxData.months=$("#months").val();
		ajaxData.userNumber=$("#userNumber").val();
		ajaxData.clientId=id;
		dialog.dialog({
			title:"服务内容变更为",
			height:"338",
			width:"420",
			content:renewHtml,
			name:"save",
			button:{
				"确定":function(){
					clientService.renew(ajaxData).ready(function(data){
						if(data.status=="1"){
							dialog.lite("操作成功","/manage/clientList")
							checked=false;
						}else{
							dialog.lite(data.statusMsg)
						}
						
					})
				},
				"取消":function(){

				}
			}
		}).open()
	})
	
	var validate =  $("#uping_form").validate({
		onkeyup:false,
		 onfocusout: function(element){
		        $(element).valid();
		    },
		rules:{
			"months":{
				required:true,
				checkupmonths:true,
			},
			"userNumber":{
				required:true,
				checkupNumber:true
			}
		},
		messages:{
			"months":{
				required:"请输入自然数！",
				checkupmonths:"请输入自然数！"
			},
			"userNumber":{
				required:"请输入不小于原使用人数的整数！",
				checkupNumber:"请输入不小于原使用人数的整数！"
			}
			
		},
	});
	
	
	
	function addMoth(d,m){
		   var ds=d.split('-'),_d=ds[2]-0;
		   var nextM=new Date( ds[0],ds[1]-1+m+1, 0 );
		   var max=nextM.getDate();
		   d=new Date( ds[0],ds[1]-1+m,_d>max? max:_d );
		   var y=d.getFullYear();
		   var m=(d.getMonth() + 1).toString();
		   var d=(d.getDate()).toString();
		   //var dd=d.toLocaleDateString().split("/") 这个函数 搜狗浏览器解析方式有问题
		   return y+"年"+m+"月"+d+"日";
		}
});