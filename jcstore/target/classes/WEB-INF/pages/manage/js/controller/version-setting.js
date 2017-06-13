define(function(require, exports, module) {
	var contentService=require("manage/service/content");
	var dialog = require("common/dialog");
	require("jqueryValidate"); 
	
//	获取版本
	contentService.version().ready(function(data){
		$('#androidVersion').val(data.androidVersion); 
		$('#iosVersion').val(data.iosVersion);
		$('#androidpadVersion').val(data.androidPad);
		$('#form-el').attr('data-checkStatus',data.checkStatus);
		var defaultStatus = $('#form-el').attr('data-checkStatus');
		if(data.checkStatus =='1'){
			setRadioStatus();
		}
	});
	
//	ios开关按钮
	$('#form-el').click(function(){
		setRadioStatus('click');
//		ios开关请求
		var androidVersion = $.trim($('#androidVersion').val());
		var iosVersion = $.trim($('#iosVersion').val());
		var checkStatus = $('#form-el').attr('data-checkStatus');
		var jsonData = {};
		jsonData['checkStatus'] =checkStatus;
		contentService.versionchange(jsonData).ready(function(data){
			if(data.status=="1"){
			}else{
				dialog.lite(data.statusMsg);
			}
		});
	});
	
	function setRadioStatus(type){
		var position = $('.handle').position().left;
		if(position=='1'){
			$('#form-el').attr('data-checkStatus','1');
			$('.toggler').addClass('handleopen');
			if(type =='click'){
				$('.handle').stop(true,true).animate({left:'29px'},function(){
					$('.Cclose').css('display','none');
				});
			}else{
				$('.handle').css('left','29px');
				$('.Cclose').css('display','none');
			}
			
		}else{
			$('#form-el').attr('data-checkStatus','0');
			$('.toggler').removeClass('handleopen');
			$('.handle').stop(true,true).animate({left:'1px'});
			$('.Cclose').css('display','block');
		};
	};
	
// 编辑按钮点击ios
	$('#editIOS').click(function(){
		var _this = $(this)
		if($(this).hasClass('edit-save') ==false){
			$(this).addClass('edit-save');
			$(this).parent().children('.version').removeAttr('readonly').addClass('number-focus').focus();
		}else{
//			保存
//			ajax
			if(!$("#formIOS").valid()){
				return
			};
			saveRus(_this);
		}
	});
	
	// 编辑按钮点击andriod
	$('#editAndroid').click(function(){
		var _this = $(this)
		if($(this).hasClass('edit-save') ==false){
			$(this).addClass('edit-save');
			$(this).parent().children('.version').removeAttr('readonly').addClass('number-focus').focus();
		}else{
//			保存
//			ajax
			if(!$("#formAndriod").valid()){
				return
			};
			saveRus(_this);
		}
	});
	
	// 编辑按钮点击andriodpad
	$('#editAndroidpad').click(function(){
		var _this = $(this)
		if($(this).hasClass('edit-save') ==false){
			$(this).addClass('edit-save');
			$(this).parent().children('.version').removeAttr('readonly').addClass('number-focus').focus();
		}else{
//			保存
//			ajax
			if(!$("#formAndriodpad").valid()){
				return
			};
			saveRus(_this);
		}
	});
	
	function saveRus(_this){
		var androidVersion = $.trim($('#androidVersion').val());
		var iosVersion = $.trim($('#iosVersion').val());
		var androidpadVersion = $.trim($('#androidpadVersion').val());
		var checkStatus = $('#form-el').attr('data-checkStatus');
		var type = _this.attr('type');
		var jsonData = {}
		if(type == 'ios'){
			jsonData.iosVersion = iosVersion;
			jsonData.checkStatus = checkStatus;
		}else if(type == 'andriod'){
			jsonData.androidVersion = androidVersion;
		}else{
			jsonData.padVersion = androidpadVersion;
		};
		contentService.versionchange(jsonData).ready(function(data){
			if(data.status=="1"){
				dialog.lite('保存成功');
				setTimeout(function(){
				},3000);
			}else{
				dialog.lite(data.statusMsg);
			}
		});
		_this.removeClass('edit-save');
		_this.parent().children('.version').attr("readonly","readonly").removeClass('number-focus') ;
	};

//	验证
	var IOSvalidate =  $("#formIOS").validate({
		rules:{
			"iosVersion":{
				versionVal:true,
				maxlength:10
			}
		},
		messages:{
			"iosVersion":{
				maxlength:'最多可以输入10个字符'
			}
		},
	});
	
	var Androidvalidate =  $("#formAndriod").validate({
		rules:{
			"androidVersion":{
				versionVal:true,
				maxlength:10
			}
		},
		messages:{
			"androidVersion":{
				maxlength:'最多可以输入10个字符'
			}
		},
	});
	
	var Androidpadvalidate =  $("#formAndriodpad").validate({
		rules:{
			"androidpadVersion":{
				versionVal:true,
				maxlength:10
			}
		},
		messages:{
			"androidpadVersion":{
				maxlength:'最多可以输入10个字符'
			}
		},
	});

});