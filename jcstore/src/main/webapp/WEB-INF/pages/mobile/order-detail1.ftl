<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>订单详情1</title>
	<#--include "../common/com-pc.ftl"-->
	<#include "com-mobile.ftl">
	<style>
		/*test*/
		.operate{position:absolute; top: 18px; right: 4%; display:block; width:44px; 
			line-height:17px; border:1px solid #d7000f; color:#d7000f; font-size:12px; 
			text-align:center;}
		.product-img{ }
		.product-img a i{display:none; width:17px; height:17px;
			position:absolute; top:-7px; right: -8px;
			background:url(${domainUrl}/static/mobile/images/icons.png) -344px -6px;
			background-size: 2500% 2500%; z-index:1;}
		.upfile{ position:relative; width:16%; margin-right: 4%; }
		.upfile input{ position:absolute;top:0; left:0; width:100%; height:100%; 
			border:1px solid red;
			filter:alpha(opacity:0);opacity: 0;}
		.product-img a{border:1px solid red; height:40px;}
		.msg-box .root2 img{ height:50px;}
		.insert-img{display:none;}
	</style>
</head>
<body class="nt-wrapper">
	<div class="nt-main order-detail">
		<div class='line' ></div>
		<span>访问这个页面的路径是：http://localhost/mobile/
		message/od1/orderdetail1</span>
		<div class='title-status'>
			<div class="nt-status nt-status-cancel">
				<span><i></i>订单已作废</span>
				<span class='time'>更新时间：2016.7.7</span>
				<div class='line'></div>
			</div>
			<a class='check-ewm'>查看单据二维码</a>
			<div class='reason'>作废原因：无</div>
		</div>
		<div class='line mb5'></div>
		<div class='line'></div>
		<div class="msg-tlt msg-first">
			主订单信息
			<span class='t-icon'></span>
		</div>
		<div class='line'></div>
		<div class="main-box">
			<div class="msg-box">
				<div class="root root2">
					<p class=' mt10 mb15'>订单附件</p>
					<a class='operate' data-text='edit'>编辑</a>
					<div class='upfile right'>
						<img src="${domainUrl}/static/mobile/images/upfile.png"/>
						<input id='upfile-input' class='upfile-input' type='file'/>
					</div>
					<div id='product-img' class='product-img'>
						<a href='javascript:void(0)'>
							<i class='del'></i>
							<img id='' class='right' src="${domainUrl}/static/mobile/images/img-logo.png" />
						</a>
						<a href='javascript:void(0)'>
							<i class='del'></i>
							<img id='' class='right' src="${domainUrl}/static/mobile/images/img-logo.png" />
						</a>
					</div>
					
				</div>
			</div>
			<div class='line'></div>
		</div>
	</div>
<script type="text/javascript" src='${domainUrl}/static/mobile/js/jquery.js'></script>
<script type="text/javascript">
$(function(){
	$('.msg-first').on('click',function(){
		$(".t-icon").toggleClass('close');
		$(this).parent().children(".main-box").toggle();
	});
	
	//订单附件 方法1
	//	$('.del').hide();
//	$(document).on('touchstart','.operate',function(){
//		var text = $('.operate').text();
//		if(text == '编辑'){
//			$(this).text('保存');
//			$('.product-img a i').show();
//		$(document).on('touchstart','.del',function(){
//			$(this).parent('a').remove();
//		});
//		}else{
//			$(this).text('编辑');
//			$('.product-img a i').hide();
//		}
//	});


//订单附件 方法2
var operJson = {
	'edit':{
		'text':'保存',
		'dataText':'save'
	},
	'save':{
		'text':'编辑',
		'dataText':'edit'
	}
};
function delFn(data){
	if(data == 'edit'){
		$('.del').show();
	}else{
		$('.del').hide();
	};
};
function editFn(data){
	for( var k in operJson){
		if(k == data){
			$('.operate').text(operJson[k]['text']).attr('data-text',operJson[k]['dataText']);
		}
	};
	delFn(data);
};
function clickFn(){
	$(document).on('touchstart','.operate',function(){
		var data = $(this).attr('data-text');
		editFn(data);
	});
	$(document).on('touchstart','.del',function(){
		$(this).parent('a').remove();	
	})
}
function init(){
	clickFn();
};
init();

//上传图片
//$('.upfile').change(function(){
//	var files = $('.upfile-input').val();
//	var temp="<a href='javascript:void(0)'>"+
//		"<i class='del'></i>"+
//		"<img id='' class='right' src='"+files+"' />"+
//		"</a>";
//$('.product-img a:last').after(temp);
//});







//var result = "<a id='insert-img'></a>";
$('#product-img').append("<a class='insert-img'></a>")
var result= document.getElementsByClassName("insert-img")[0];
console.log(result);
$('.insert-img').hide();
var input = document.getElementById("upfile-input");
console.log(input)
if(typeof FileReader === 'undefined'){
    result.innerHTML = "抱歉，你的浏览器不支持 FileReader";
    input.setAttribute('disabled','disabled');
}else{
    input.addEventListener('change',readFile,false);
    $('#product-img').append("<a class='insert-img'></a>")
}  
           
function readFile(){
    var file = this.files[0];
    if(!/image\/\w+/.test(file.type)){
        alert("请确保文件为图像类型");
        return false;
    }
    var reader = new FileReader();
    reader.readAsDataURL(file);
    
    reader.onload = function(e){
        //alert(3333)
        //alert(this.result);
        //result.innerHTML = '<img src="'+this.result+'" alt=""/>';
        $('#product-img').append(result);
        console.log(result);
        console.log(this.result);
        result.innerHTML = "<i class='del'></i><img id='' class='right' src='"+this.result+"' />";
        $('.insert-img').show();
    };
};


















});
</script> 
</body>
</html>