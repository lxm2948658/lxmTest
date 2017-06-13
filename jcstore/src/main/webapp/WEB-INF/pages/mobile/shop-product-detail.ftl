<!DOCTYPE html>
<html lang="en">
<head>
	<title>${product.name!}</title>
	<#include "com-mobile.ftl">
</head>
<body class="bgf7f5">
<#if product??>
	<div class=" shop-product-detail">
		<i class='arc'></i>
		<ul id="slides" class="slides banner">
		<#if (product.image!)=="default" || (product.image!)=="">
			<li class="swiper-slide" style="width:100%; background:url(${domainUrl}/static/mobile/images/img-logo.png) center center;background-size:cover">
			</li>
		<#else>
			<#list product.images as image>
			<li class="swiper-slide" style="width:100%; background:url(${image!}) center center;background-size:cover">
			</li>
			</#list>
		</#if>
		</ul>
		<div class='detail-info'>
			<h3>${product.name!}</h3>
			<span class='price'>${product.salePrice!}</span>
			<div class='msg pt16 pb70'>
				<span class='newline'>商品编码：<#if (product.code!)=="">-<#else>${product.code!}</#if></span>
				<span class='newline'>商品条码：<#if (product.barCode!)=="">-<#else>${product.barCode!}</#if></span>
				<span class='newline'>规格型号：<#if (product.standard!)=="">-<#else>${product.standard!}</#if></span>
				<span class='price2' class='newline'>${product.salePrice!}</span>
			</div>
		</div>
		<div class='enter-shop'>
		<#if shop.logo??>
			<img src='${shop.logo!}'/>
		<#else>
			<img src='${domainUrl}/static/mobile/images/img-logo.png'/>
		</#if>
			<span class='newline'>${shop.name}</span>
			<a href='${domainUrl}/${linkedURL!}'>进入店铺</a>
		</div>
	</div>
<#else>
	<p class="tip">很遗憾，你来晚了~<br>商品已不存在</p>
</#if>
	
<script type="text/javascript" src='${domainUrl}/static/mobile/js/jquery.js'></script>
<script type="text/javascript" src='${domainUrl}/static/mobile/js/jquery.slides.min.js'></script>
<script>
	//获取高度等于宽度
	function Height(){
		var winWidth = $('.banner').width();
		var _width = winWidth;
		var _height = _width; 
		$('.banner li,.slidesjs-control').height(_height+'px')
	}
	Height();

	var isSlide = $(".swiper-slide").length >1?true:false;
	if(isSlide){
		$('#slides').slidesjs({
		    width:751,
		    height: 751,
		    play: {
		        interval: 3000,
		        pauseOnHover:false,
		        swap: false
		    }
		});
	};
	
	//给价格添加小数点
	var price = $('.price').html();
	price = price.split(',');
	price = +price.join('');
	price=Number(price).toFixed(2);
	$('.price').html('￥'+price);
	$('.price2').html('销售价格：￥'+price);
	
	//获取圆点位置
	var cirwidth = -$('.slidesjs-pagination').width()/2;
	$('.slidesjs-pagination').css('margin-left',cirwidth);
	
</script>
</body>
</html>