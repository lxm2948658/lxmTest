<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>${shop.name!}</title>
	<#include "com-mobile.ftl">
</head>
<body class="">
<#if shop??>
	<div class="nt-main shop-detail bgf1f1f1">
		<div class='shop-top'>
			<div class='shop-top-bg'>
				<img src='${domainUrl}/static/mobile/images/shop-top-bg.png'/>
			</div>
			<#if shop.logo??>
			<img class='shop-img' src='${shop.logo!}'/>
			<#else>
			<img class='shop-img' src='${domainUrl}/static/mobile/images/img-logo.png'/>
			</#if>
			<div class='shop-info'>
				<span class='ellipsis'>${shop.name!}</span>
				<hr/>
				<span class='newline'>${shop.introduction!}</span>
			</div>
			<div class='address-info'>
				<i class='i-address'></i>
				<span>${shop.address!}</span>
				<a id='tel' data-tel='${shop.tel!''}' href='tel:${shop.tel!}'><i class='i-tel'></i></a>
			</div>
		</div>
		
		<div class='product-list mt10'>
			<ul class='classify'>
				<li class='select ellipsis'>默认排序<span></span>
				</li>
				<i></i>
				<li class='select ellipsis'>全部分组<span></span>
				</li>
			</ul>
			<div class='tab_content'>
				<ul id="order_type" class='classifylist'>
					<li class='active' data-id="0"><span class='sel-circle'></span>默认排序<i></i></li>
					<li data-id="1"><span class='sel-circle'></span>按价格从高到低</li>
					<li data-id="2"><span class='sel-circle'></span>按价格从低到高</li>
				</ul>
				<ul id="group" class='classifylist'>
					<li class='active' data-id='0' ><span class='sel-circle'></span>全部分组<i></i></li>
					<#list productGroup as group>
					<li data-id="${group.id!?c}"><span class='sel-circle'></span>${group.name!}</li>
					</#list>
				</ul>
			</div>
			<ul class='p-list' id='p-list'>
				<div id="Loading"></div>
			</ul>		
		</div>
	</div>
	<!--遮罩-->
	<div id="mask" class="mask"></div> 
<#else>
	<p class="tip">很遗憾，你来晚了~<br>店铺已不存在。</p>
</#if>
<script type="text/javascript" src='${domainUrl}/static/mobile/js/jquery.js'></script>
<script type="text/javascript" src='${domainUrl}/static/mobile/js/handlebars.min.js'></script>
<script type="text/javascript" src='${domainUrl}/static/mobile/js/handlebar.js'></script>
<script type="text/javascript" src='${domainUrl}/static/mobile/js/webScroll.js'></script>
<script type='text/x-handlebars-template' id='datatpl'>
    {{#each this}} 
    <li class='endways '>
	    <a href="${domainUrl}/${linkedURL!}{{id}}&productname={{name}}&productimg={{images.[0]}}&shopname=${shop.name}&salePrice={{salePrice}}">
			{{#ex "{{image}}==='default' || {{image}}=='' "}} 
		    <div class='productImg'>
		    	<img src='${domainUrl}/static/mobile/images/img-logo.png'/>
			</div>
		  	{{else}}
		    <div class='productImg' >
				<img src='{{image}}'/>
			</div>
		  	{{/ex}}
			<div class='msg-box'>
				<h3 class='newline'>{{name}}</h3>
				<span class='spec ellipsis'>规格型号：{{standard}}</span>
				<span class='price'>￥{{_moneyLimit salePrice}}</span>
			</div>
		</a>
	</li>
    {{/each}}
</script>
<script>

$(function(){
//给价格添加小数点
	function decimal(){
		var price = $('.price').html();
		var price2=Number(price).toFixed(2);
		$('.price').html('￥'+price2);
	}
	
	//列表刷新
	var groupId = '0';
	var orderType = '0';
    emitDate();
    //decimal()
	$('#order_type li').click(function () {
		orderType = $(this).attr('data-id')
        emitDate();
    });
    $('#group li').click(function () {
		groupId = $(this).attr('data-id');
        emitDate();
    })
    function emitDate(){		
		var dataParam = {
			orderType:orderType,
			shopId:'#{shop.id!}'
		};
		groupId!='0' && (dataParam.groupId = groupId);
		webScroll({
            limit:10,
            start:1,
            bottom:100,
            param:dataParam,
            api:domainUrl+"/wap/product/find",
            template:function(data){
                return Handlebars.compile($('#datatpl').html())(data);
            },
            onPage:function () {
            	
            },
            onComplete:function(page){
                if(+page===1){
                    if(!$("#p-list li").length){
                        $("#p-list").prepend("<p style='text-align: center; margin-top:30px; color:#333; font-size:14px;'>很抱歉，这里还没有商品</p>")
                    }
                }
            }
        });
	};

	
	//tabs
	var $menu =$('.select');
	$menu.click(function(){
		var index = $menu.index(this);
		if($(this).hasClass('active')){
			$(this).removeClass('active');
			$('.classifylist').eq(index).hide();
			hideMask()
		}else{
			$(this).addClass('active').siblings().removeClass('active');
			$('.classifylist').eq(index).show().siblings().hide();
			showMask()
		}
	})
	
	$('.classifylist li').click(function(){
		var text = $(this).text();
		var index = $(this).parent().index();
		$(this).parent().hide();
		$('.select').eq(index).html(text+'<span></span>');
		$('.select').eq(index).removeClass('active');
		$(this).addClass('active').siblings().removeClass('active');
		$(this).append('<i></i>').siblings().find('i').remove();
		hideMask()
	});
	
    //显示遮罩层    
    function showMask(){     
        $("#mask").css("height",$(document).height());     
        $("#mask").css("width",$(document).width());     
        $("#mask").show();     
    }  
    //隐藏遮罩层  
    function hideMask(){     
        $("#mask").hide();     
    }  
	
	$('#mask').click(function(){
		var index = $menu.index(this);
		$('.select').removeClass('active');
		$('.classifylist').hide();
		hideMask()
	})
})	
</script>
</body>
</html>