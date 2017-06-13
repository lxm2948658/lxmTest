<!DOCTYPE html>
<html lang="en" class="refund-html">
<head>
	<meta charset="UTF-8">
	<title>退货详情</title>
	<#include "com-mobile.ftl">
</head>
<body class="nt-wrapper refund-body">
<#if result.status!=1>
	<div class='tip-page'>
		${result.statusMsg!}
	</div>
<#else>
	<div class="nt-main">
		<div class="msg bbddd refund-icon">
			<span class='msg-title vamiddle'>退货信息</span>
			<#if result.refund.status==3>
				<span class='cancel'><i></i>已作废</span>
			<#else>
                <span class='icon-toggle upper'></span>
			</#if>
		</div>
		<div class='line'></div>
		<div class="main-box">
			<div class="msg-box bbddd pb20">
				<div class="root">
					<span class='left'>主订单号</span>
					<span id='' class='right'>${result.refund.number!}</span>
				</div>
				<div class="root">
					<span class='left'>退货单号</span>
					<span id='' class='right'>${result.refund.refundNum!}</span>
				</div>
				<#if result.refund.minimalFlag==0>
					<div class="root">
						<span class='left'>客户姓名</span>
						<span id='' class='right'>${result.refund.customer!}</span>
					</div>
					<div class="root">
						<span class='left'>联系电话</span>
						<span id='' class='right'>${result.refund.phone!}</span>
					</div>
					<div class="root">
						<span class='left'>地址</span>
						<span id='' class='right  max-W75'>${result.refund.contactWith!}</span>
					</div>
				<#else>
					<div class="root root3">
						<span class='left'>单据图片</span>
						<a href='${domainUrl}/mobile/refund/imageBill?imgurl=${result.refund.invoiceImg!}'>
						<div class='right invoices-img' style=" background:url(${result.refund.invoiceImg!}) center center;background-size:cover">
						<!--	<img src='${result.refund.invoiceImg!}'/>-->
						</div>
						</a>
					</div>
				</#if>
			</div>
			<div class='line'></div>
			<div class="rpd-box bgf8">
				<#if result.refund.minimalFlag==0>
                    <div class="root1">
                        <p>商品明细</p>
                        <p>合计：<span class="cff1300 fsize17">¥ ${result.refund.refundTotal?string("0.00")!}</span></p>
                    </div>
					<#list result.details as detail>
                        <div class="root rpd-detail clearfix bgf8">
                            <a class='rpda-detail' href='javascript:void(0);' style='background:url(<#if (detail.img!)=="default" || (detail.img!)=="">${domainUrl}/static/mobile/images/img-logo.png<#else>${detail.img!'${domainUrl}/static/mobile/images/img-logo.png'}</#if>) center center; background-size:cover' ></a>
                            <p class='lh22'>${detail.name!}</p>
                            <span class='price'><span class='cff1300'>￥${detail.price?string("0.00")!}</span> x ${detail.number!}</span>
                        </div>
					</#list>
				</#if>
			</div>
            <div class='line'></div>
			<div class="msg-box pb20">
				<div class="root">
					<span class='left'>退货金额</span>
					<span id='' class='right cff1300'>¥${result.refund.refundFee?string("0.00")!}</span>
				</div>
				<div class="root">
					<span class='left'>业务员</span>
					<span id='' class='right'>${result.refund.salesman!}</span>
				</div>
				<div class="root">
					<span class='left'>退货单生成时间</span>
					<span id='' class='right'>${result.refund.createtime?string("yyyy-MM-dd HH:mm")!}</span>
				</div>
				<div class="root">
					<span class='left'>预计退货时间</span>
					<span id='' class='right'><#if result.refund.expectedDate??>${result.refund.expectedDate?string("yyyy-MM-dd")!}</#if></span>
				</div>
				<div class="root">
					<span class='left'>备注</span>
					<span id='' class='right maxW79'>${result.refund.refundDescription!}</span>
				</div>
			</div>
		</div>
	</div>
	<!-- <div class="btn btn1">
		<a class="btn-cancel">作废</a>
	</div> -->
	<div class="btn btn2 btddd col${result.buttonNum!}">
		<#if result.order_cancel==1>
			<a class="btn-cancel"  href="${domainUrl}/mobile/order/cancelFn?id=${result.refund.id?c}&type=2">作废</a>
		</#if>
		<#if result.finish_order==1>
			<a class="btn-finish" href="${domainUrl}/mobile/order/finishFn?id=${result.refund.id?c}&type=2">退货完成</a>
		</#if>
	</div>
</#if>
</body>
</html>