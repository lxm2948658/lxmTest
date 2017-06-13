<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>定制订单详情</title>
	<#include "com-mobile.ftl">
</head>
<body class="nt-wrapper custom-body">
<#if result.status!=1>
	<div class='tip-page'>
		${result.statusMsg!}
	</div>
<#else>
	<div class="nt-main order-detail">
		<div class='line' ></div>
			<div class='title-status'>
				<#if result.order.orderStatus==1>
					<div class="nt-status">
						<div class = "state">
							订单状态
						  <span class = "_c"><i><img src="${domainUrl}/static/mobile/images/await.png"></img></i>订单待完成 </span>
						</div>
						<div class = "_time">
							<span class='time'>订单编号：<p>${result.order.orderNumber!}</p></span>
							<span class='time'>更新时间：<p>${result.order.updatetime?string("yyyy-MM-dd HH:mm")!}</p></span>
							<span class='time'>下单时间：<p>${result.order.createtime?string("yyyy-MM-dd HH:mm")!}</p></span>
							<span class = "cq"></span>
							<span class = "me"></span>
						</div>
					</div>
				<#elseif result.order.orderStatus==2>
				 	<div class="nt-status nt-status-finish">
						<span class = "_c"><i><img src="${domainUrl}/static/mobile/images/finish.png"></img></i>订单已完成 </span>
						<span class='time'>订单编号：${result.order.orderNumber!}</span>
						<span class='time'>更新时间：${result.order.updatetime?string("yyyy-MM-dd HH:mm")!}</span>
						<span class='time'>下单时间：${result.order.createtime?string("yyyy-MM-dd HH:mm")!}</span>
					</div>
				<#elseif result.order.orderStatus==3>
					<div class="nt-status nt-status-cancel">
						<span class = "_c"><i><img src="${domainUrl}/static/mobile/images/out.png"></img></i>订单已作废</span>
						<span class='time'>订单编号：${result.order.orderNumber!}</span>
						<span class='time'>更新时间：${result.order.updatetime?string("yyyy-MM-dd HH:mm")!}</span>
						<span class='time'>下单时间：${result.order.createtime?string("yyyy-MM-dd HH:mm")!}</span>
						<span class='time cd7000f'>作废原因：<#if (result.order.cancelReason!)=="">无<#else>${result.order.cancelReason!}</#if></span>
					</div>
				</#if>
			<a class='check-ewm' href='${domainUrl}/mobile/order/imageEWM?oid=${result.order.id?c!}'><img src="${domainUrl}/static/mobile/images/erweima.png"><p>查看单据二维码</p><img class ="_img2" src="${domainUrl}/static/mobile/images/_right.png"><i></i></a>
			</div>
		<div class='line mb5'></div>
		<#--<div class='line'></div>-->
		<div class="msg-tlt noimg msg-info pl15">
			<h3 class='mb5'><img src="${domainUrl}/static/mobile/images/style.png"><p>商品样式风格</p><img class ="_img2" src="${domainUrl}/static/mobile/images/upper.png"></h3>
			<span class='newline'><p>${result.order.bespokeDetail.name!}</p><div class='msg-img' style="background:url(${result.order.bespokeDetail.image!}) center center no-repeat;background-size:cover;"></span>

			</div>
		</div>
		<#--<div class='line mb5'></div>-->
		<#--<div class='line'></div>-->
		<div class="msg-tlt noimg pl15 _flex">
			<img src="${domainUrl}/static/mobile/images/amount.png">
			量尺信息
		</div>
		<#--<div class='line'></div>-->
		<div class="main-box">
			<div class="msg-box minH45">
		<#assign json=result.order.bespokeDetail.measureInfo?eval />
		<#if json.custom?size gt 0>
			<#list json.custom as custom>
			<div class="root">
				<span class='left'>${custom.standard!}</span>
				<span id='' class='right'>${custom.millimetre!}mm</span>
			</div>
			</#list>
		</#if>
			</div>
		</div>
		<#--<div class='line mb5'></div>-->
		<#--<div class='line'></div>-->
		<div class="msg-tlt noimg pl15 _flex">
            <img src="${domainUrl}/static/mobile/images/price.png">
			价格
		</div>
		<#--<div class='line'></div>-->
		<div class="main-box">
			<div class="msg-box">
				<div class="root">
					<span class='left'>订单金额</span>
					<span id='' class='right cd7000f'><#if result.order.receiceAmoun??>¥${result.order.receiceAmoun!?string("0.00")}</#if></span>
				</div>
				<div class="root">
					<span class='left'>预付定金</span>
					<span id='' class='right cd7000f'><#if result.order.amountAdvanced??>¥${result.order.amountAdvanced!?string("0.00")}</#if></span>
				</div>
			</div>
		</div>
		<#--<div class='line mb5'></div>-->
		<#--<div class='line'></div>-->
		<div class="msg-tlt msg-first noimg pl15 _flex">
            <img src="${domainUrl}/static/mobile/images/client.png">
			 客户信息
			<!--<span class='t-icon'></span>-->
		</div>
		<#--<div class='line'></div>-->
		<div class="main-box _border">
			<div class="msg-box">
				<div class="root">
					<span class='left'>姓名</span>
					<span id='' class='right'>${result.order.customer!}</span>
				</div>
				<div class="root">
					<span class='left'>电话</span>
					<span id='' class='right'>${result.order.phone!}</span>
				</div>
				<div class="root">
					<span class='left'>地址</span>
					<span id='' class='right lh25 mtb10 max-W75'>${result.order.contactWith!}</span>
				</div>
			</div>
			<#--<div class='line'></div>-->
			<#--<div class='line mt5'></div>-->
			<div class="msg-box">
				<div class="root">
					<span class='left'>业务员</span>
					<span id='' class='right ellipsis'>${result.order.salesman!}</span>
				</div>
				<div class="root">
					<span class='left'>交付日期</span>
					<span id='' class='right'><#if result.order.expectedDeliveryDate??>${result.order.expectedDeliveryDate?string("yyyy-MM-dd")!}</#if></span>
				</div>
                <#--<div class="_txt">-->
                    <#--<span class='_txt_1'>是否需要客户签字</span>-->
                    <#--<span id='' class='_txt_2'><span class = "_y"></span></span>-->
                <#--</div>-->
			</div>
			<#--<div class='line mb5'></div>-->
			<#--<div class='line'></div>-->
			<div class="msg-tlt noimg pl15 _flex">
                <img src="${domainUrl}/static/mobile/images/remark.png">
				备注
			</div>
			<#--<div class='line'></div>-->
			<#--<div class="msg-box">-->
				<div class="root minH45 _remark">
					<span class='newline left lh25 maxW100 mtb10 c999 remark1'>${result.order.description!}暂无备注</span>
				</div>
			</div>
			<#--<div class='line mb5'></div>-->
			<#--<div class='line'></div>-->
			<div class="msg-tlt noimg pl15 _flex">
                <img src="${domainUrl}/static/mobile/images/design.png">
				设计图
			</div>
			<#--<div class='line'></div>-->
			<div class="msg-box">
					<div class="root root2">
						<div class='product-img mt10'>
							<#list result.order.adjunctImgs as img>
								<a class='left' href='${domainUrl}/mobile/order/imageAttachment?imgurl=${img!}'>
									<i class='del'></i>
									<div class='order-img' style="width:100%; background:url(${img!}) center center;background-size:cover">
									</div>
								</a>
							</#list>
						</div>
					</div>
				</div>
				<#if result.order.signatureImg??>
				<#--<div class="line"></div>-->
				<div class="msg-box">
					<div class="root">
						<span class='left'>客户签名</span>
						<div id='' class='right photos' >
							<img src='${result.order.signatureImg!}'/> 
						</div>
					</div>
				</#if>
			</div>
			<#--<div class="line"></div>-->
	</div>
	<#if result.finish_order==1>
		<div class="btn btn3 col1">
			<a class="btn-finish" href="${domainUrl}/mobile/order/finishBespokeFn?id=${result.order.id?c!}&type=1&signatureFlag=${result.order.signatureFlag!}">确认完成</a>
		</div>
	</#if>
</#if>
<script type="text/javascript" src='${domainUrl}/static/mobile/js/jquery.js'></script>
<script type="text/javascript">
$(function(){
	//获取高度等于宽度
	function Height(){
		var _width = $('.order-img').width();
		
		_height = _width;  
	    $('.order-img').css({height:_height}); 
	}
	Height();
	
	//获取商品样式图片
	function Height1(){
		var _height = $('.msg-img img').height();
		_height = -_height/2;
		$('.msg-img img').css('margin-top',_height)
	}
	Height1();
});
</script>
</body>
</html>