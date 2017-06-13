define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var filter=require("utils/data.filter");
	var serialize = require("utils/form.serialize");
	var editionService = require("manage/service/edition");
	require("jqueryValidate");
	var id=filter.getQueryString("id");
	var moduleId;
	var name;

	editionService.moduleInfo().ready(function(data){
		var modules = data.aaData;
		var modulesHtml="";
		for(var i=0;i<modules.length;i++){
			var submodules = modules[i].submodules;
			modulesHtml += '<tr><th><input type="checkbox" name="module" value="' + modules[i].id + '" /><span>' + modules[i].name + '</span></th><td>';
			for (var j=0; j<submodules.length; j++){
				modulesHtml += '<input type="checkbox" name="module" value="' + submodules[j].id + '"/>' + submodules[j].name;
			}
			modulesHtml += '</td></tr>';
		}
		$("#module").html(modulesHtml);
		if(id){
			editionService.getInfo(id).ready(function(data){
				var edition = data.aaData;
				$('#edition_name').val(edition.name);
				modules = edition.moduleId.split(',');
				for (var i = 0;i<modules.length; i++){
					console.log(modules[i])
					$('input[value='+modules[i]+']').prop('checked',true);
				}
			})
		}
	})

	$("#add_save").click(function(){
		if(!$("#edition_form").valid()){
			return
		}
		$('input:checked').each(function () {
			if(moduleId){
				moduleId += ',' + $(this).val();
			}else {
				moduleId = $(this).val();
			}
		})
		name = $('#edition_name').val();
		var data={
			moduleId:moduleId,
			name:name
		}
		if(id){
			data.id = id;
		}
		editionService.save(data).ready(function(data){
			if(data.status=="1"){
				window.location.href = domainUrl + "/manage/editionList";
			}else{
				moduleId = '';
				var dialogTip = '<p style="text-align: center; font-size: 16px;margin-top: 10px;">' + data.statusMsg + '</p>';
				var tipDialog = dialog.dialog({
					content:dialogTip,
					title:"操作提示",
					button:{
						"确定":function(){
							tipDialog.close();
						}
					}
				}).open();
			}
		})
	});

	var validate =  $("#edition_form").validate({
		onkeyup:false,
		 onfocusout: function(element){
		        $(element).valid();
		    },
		rules:{
			"name":{
				required:true,
				maxlength:8
			},
			"module":{
				required:true
			}
		},
		messages:{
			"name":{
				required:"请输入版本名称！",
				maxlength:"版本名称不能超过8字"
			},
			"module":{
				required:"请选择功能 !"
			}
		}
	});

	$(document).on('click','td input',function () {
		if($(this).is(":checked")){
			$(this).parent('td').prev('th').children('input').prop('checked',true);
		}
	});
	$(document).on('click','th input',function () {
		if(!$(this).is(":checked")){
			$(this).parent('th').next('td').children('input').prop('checked',false);
		}
	});
	$(document).on('click','input[value="6"]',function () {
		if($(this).is(":checked")){
			$('input[value="11"]').prop('checked',true);
		}
	});
	$(document).on('click','input[value="11"]',function () {
		if(!$(this).is(":checked")){
			$('input[value="6"]').prop('checked',false);
		}
	});
	$(document).on('click','input[value="26"]',function () {
		if($(this).is(":checked")){
			$('input[value="4"]').prop('checked',true);
		}
	});
	$(document).on('click','input[value="4"]',function () {
		if(!$(this).is(":checked")){
			$('input[value="26"]').prop('checked',false);
			$('input[value="27"]').prop('checked',false);
		}
	});
});