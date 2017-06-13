<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>商品详情</title>
	<#include "com-mobile.ftl">
</head>
<body class="nt-wrapper pt0">
<!--商品已删除-->
<#if result.status!=1>
	<div class="nt-main product-detail tip-page">
		${result.statusMsg!}
	</div>
<#else>
	<div class="nt-main product-detail bgf1f1f1">
	<!--	<div class='bbddd line' ></div>-->
		<div class="msg-box bbddd">
			<div class="root">
				<span class='left'>商品名称</span>
				<span id='' class='right lh25 mtb15 maxW70'>${result.product.name!}</span>
			</div>
			<div class="root">
				<span class='left'>商品编码</span>
				<span id='' class='right'><#if (result.product.code!)=="">-<#else>${result.product.code!"-"}</#if></span>
			</div>
			<div class="root">
				<span class='left'>商品条码</span>
				<span id='' class='right'><#if (result.product.barCode!)=="">-<#else>${result.product.barCode!"-"}</#if></span>
			</div>
			<#if result.product.groupName?exists>
				<div class="root">
					<span class='left'>商品分组</span>
					<span id='' class='right'><#if result.product.groupName=="">无<#else>${result.product.groupName!"无"}</#if></span>
				</div>
			</#if>
			<div class="root root2">
				<div class='product-img'>
					<#if (result.product.image!)=="" || result.product.images?size == 1>
						<a class='product-imger' href='${domainUrl}/mobile/order/imageFn?imgurl=${result.product.image!}&type=3' style='background:url(<#if (result.product.image!)=="default" || (result.product.image!)=="">${domainUrl}/static/mobile/images/img-logo.png<#else>${result.product.image!'${domainUrl}/static/mobile/images/img-logo.png'}</#if>) center center; background-size:cover' ></a>
					<#else>
					<div class='mulp'>
						<#list result.product.images as image>
							<a class='a_link product-imger' href='${domainUrl}/mobile/order/imageFn?imgurl=${image!}&type=3' style="background:url(<#if (image!)=="default" || (image!)=="">${domainUrl}/static/mobile/images/img-logo.png<#else>${image!'${domainUrl}/static/mobile/images/img-logo.png'}</#if>) center center;background-size:cover"></a>
						</#list>
					</div>
					</#if>
				</div>
			</div>
		</div>
		<div class='line'></div>
		<div class='line mt15'></div>
		<div class="msg-box bbddd">
			<div class="root">
				<span class='left'>规格型号</span>
				<span id='' class='right specifications'><#if (result.product.standard!)=="">-<#else>${result.product.standard!"-"}</#if></span>
			</div>
			<#if result.view_corpus==1>
				<div class="root">
					<span class='left'>成本价</span>
					<span id='' class='right cff1300'>¥${result.product.costPrice?string("0.00")}</span>
				</div>
			</#if>
			<div class="root">
				<span class='left'>销售价</span>
				<span id='' class='right cff1300'>¥${result.product.salePrice?string("0.00")}</span>
			</div>
			<div class="root">
				<span class='left'>库存</span>
				<span id='' class='right '>${result.product.inventory?c!}</span>
			</div>
		</div>
		<div class=' line' ></div>
		<#assign json=result.product.customDescription?eval />
		<#if json.customDescription?size gt 0>
		<div class='line mt15'></div>
			<div class="msg-box bbddd">
			<#list json.customDescription as custom>
				<div class="root">
					<span class='left maxW120'>${custom.name!}</span>
					<span id='' class='right'><#if (custom.info!)=="">-<#else>${custom.info!"-"}</#if></span>
				</div>
			</#list>
			</div>
		
			<div class='line'></div>
		</#if>
	</div>
</#if>
<script type="text/javascript" src='${domainUrl}/static/mobile/js/jquery.js'></script>
<script type="text/javascript">
	$(function(){
		Height();
		//获取高度等于宽度
		function Height(){
			var _width = $('.product-imger').width();
			_height = _width;  
		    $('.product-imger').css({height:_height}); 
		};
	});
</script>
</body>
</html>