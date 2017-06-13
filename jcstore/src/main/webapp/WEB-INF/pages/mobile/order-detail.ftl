<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>订单详情</title>
<#include "com-mobile.ftl">
</head>
<body class="nt-wrapper order-body">
<#if result.status!=1>
<div class='tip-page'>
${result.statusMsg!}
</div>
<#else>
<div class="nt-main order-detail">
    <div class='line'></div>
	<#if result.order.minimalFlag==0>
        <div class="new-skin-wrap">
            <div class="o-info ">
                <div class="o-info-inner">
                <#if result.order.orderStatus==1>
                    <div class="nt-status">
                        <div class="root">
                            <span class="left">订单状态</span>
                            <span class="right"><i></i>订单待完成 </span>
                        </div>

                        <span class='time'>订单编号：${result.order.orderNumber!}</span>
                        <span class='time'>更新时间：${result.order.updatetime?string("yyyy-MM-dd HH:mm")!}</span>
                        <span class='time'>下单时间：${result.order.createtime?string("yyyy-MM-dd HH:mm")!}</span>
                    </div>
                <#elseif result.order.orderStatus==2>
                        <div class="oi-item clearfix oi-finish oi-title">
                            <span class="fleft">订单状态</span>
                            <span class="fright"><i></i>订单已完成</span>
                        </div>
                        <div class="oi-item clearfix">
                            <span class="fleft">订单编号</span>
                            <span class="fright">${result.order.orderNumber!}</span>
                        </div>
                        <div class="oi-item clearfix">
                            <span class="fleft">更新时间</span>
                            <span class="fright">${result.order.updatetime?string("yyyy-MM-dd HH:mm")!}</span>
                        </div>
                        <div class="oi-item clearfix">
                            <span class="fleft">下单时间</span>
                            <span class="fright">${result.order.createtime?string("yyyy-MM-dd HH:mm")!}</span>
                        </div>
                <#elseif result.order.orderStatus==3>
                    <div class="nt-status nt-status-cancel">
                        <span><i></i>订单已作废</span>
                        <span class='time'>订单编号：${result.order.orderNumber!}</span>
                        <span class='time'>更新时间：${result.order.updatetime?string("yyyy-MM-dd HH:mm")!}</span>
                        <span class='time'>下单时间：${result.order.createtime?string("yyyy-MM-dd HH:mm")!}</span>
                    </div>
                    <div class='cd7000f reason'>作废原因：<#if (result.order.cancelReason!)=="">无<#else>${result.order.cancelReason!}</#if></div>
                </#if>
                </div>
            </div>
            <a class='check-ewm-new' href='${domainUrl}/mobile/order/imageEWM?oid=${result.order.id?c!}'>查看单据二维码</a>
        </div>
	<#else>
        <!-- 极简开始 -->
		<#if result.order.orderStatus==1>
            <div class="nt-status mini">
                <span><i></i>订单待完成</span>
                <span class='time'>订单编号：${result.order.orderNumber!}</span>
                <span class='time'>更新时间：${result.order.updatetime?string("yyyy-MM-dd HH:mm")!}</span>
            	<span class='time'>下单时间：${result.order.createtime?string("yyyy-MM-dd HH:mm")!}</span>
            </div>
		<#elseif result.order.orderStatus==2>
            <div class="nt-status mini nt-status-finish">
                <span><i></i>订单已完成</span>
                <span class='time'>订单编号：${result.order.orderNumber!}</span>
                <span class='time'>更新时间：${result.order.updatetime?string("yyyy-MM-dd HH:mm")!}</span>
            	<span class='time'>下单时间：${result.order.createtime?string("yyyy-MM-dd HH:mm")!}</span>
            </div>
		<#elseif result.order.orderStatus==3>
            <div class="nt-status mini nt-status-cancel">
                <span><i></i>订单已作废</span>
                <span class='time'>订单编号：${result.order.orderNumber!}</span>
                <span class='time'>更新时间：${result.order.updatetime?string("yyyy-MM-dd HH:mm")!}</span>
                <span class='time'>下单时间：${result.order.createtime?string("yyyy-MM-dd HH:mm")!}</span>
                <div class='reason-inner cd7000f'>作废原因：<#if (result.order.cancelReason!)=="">无<#else>${result.order.cancelReason!}</#if></div>
            </div>
		</#if>
        <!-- 极简结束 -->
	</#if>
    <div class='line mb5'></div>
    <div class='line'></div>
    <div class="msg-tlt msg-first">
        主订单信息
        <span class='t-icon'></span>
    </div>
    <div class='line'></div>
    <div class="main-box">
        <div class="msg-box">
			<#if result.order.minimalFlag==0>
                <div class="root">
                    <span class='left'>客户姓名</span>
                    <span id='' class='right'>${result.order.customer!}</span>
                </div>
                <div class="root">
                    <span class='left'>联系电话</span>
                    <span id='' class='right'>${result.order.phone!}</span>
                </div>
                <div class="root">
                    <span class='left'>地址</span>
                    <span id='' class='right lh25 mtb10 max-W75'>${result.order.contactWith!}</span>
                </div>
			<#else>
                <!--极简开始-->
                <div class="root root3">
                    <span class='left'>单据图片</span>
                    <a href='${domainUrl}/mobile/order/imageBill?imgurl=${result.order.invoiceImg!}'>
                        <!--<img class='right invoices-img' src='${result.order.invoiceImg!}'/>-->
                        <div class='right invoices-img' style=" background:url(${result.order.invoiceImg!}) center center;background-size:cover">
                            <!--	<img src='${result.order.invoiceImg!}'/>
								<img src='${domainUrl}/static/mobile/images/123.png' style/>-->
                        </div>
                    </a>
                </div>
                <!--极简结束-->
			</#if>
        </div>
        <div class='line'></div>
        <div class='line mt5'></div>
        <div class="msg-box">
			<#if result.order.minimalFlag==0>
                <div class="root h30">
                    <span class='left'>商品明细</span>
                    <span id='' class='right'>合计¥${result.order.total?string("0.00")!}</span>
                </div>
				<#list result.order.detail as detail>
                    <div class="root ">
                        <p class=' lh25 mtb10'>${detail.name!}</p>
                        <span class='price'><span class='cd7000f'>¥${detail.price?string("0.00")!}</span> x ${detail.number?c!}</span>
                    </div>
				</#list>
			</#if>
            <div class="root">
                <span class='left'>订单金额</span>
                <span id='' class='right cd7000f'>¥${result.order.receiceAmoun!?string("0.00")}</span>
            </div>
            <div class="root">
                <span class='left'>预付金额</span>
                <span id='' class='right cd7000f'>¥${result.order.amountAdvanced!?string("0.00")}</span>
            </div>
            <div class="root">
                <span class='left'>业务员</span>
                <span id='' class='right ellipsis'>${result.order.salesman!}</span>
            </div>
        </div>
        <div class='line'></div>
        <div class='line mt5'></div>
        <div class="msg-box">
            <div class="root">
                <span class='left'>预计发货时间</span>
                <span id='' class='right'><#if result.order.expectedDeliveryDate??>${result.order.expectedDeliveryDate?string("yyyy-MM-dd")!}</#if></span>
            </div>
            <div class="root ">
                <span class='left'>备注</span>
                <span id='' class='right lh25 mtb10 c999 maxW79'>${result.order.description!}</span>
            </div>
			<#if result.order.minimalFlag==0>
                <div class=''></div>
			</#if>
        </div>
		<#if result.order.minimalFlag==1>
            <div class='line'></div>
		</#if>
        <div class="msg-box">
			<#if result.order.minimalFlag==0>
                <div class="root root2">
                    <p class=' mt10 mb5'>订单附件</p>
				<#-- if result.order.adjunctImgs?size gt 0>
                </#if -->
                    <!--<a class='operate'>编辑</a>-->
                    <div class='product-img'>
						<#list result.order.adjunctImgs as img>
                            <a class='left' href='${domainUrl}/mobile/order/imageAttachment?imgurl=${img!}'>
                                <i class='del'></i>
                                <!--	<img id='' class='order-img right' src="${img!}" />-->
                                <div class='order-img' style="width:100%; background:url(${img!}) center center;background-size:cover">
                                </div>
                            </a>
						</#list>
                    </div>
                </div>
			</#if>
			<#if result.order.signatureImg??>
                <div class="root">
                    <span class='left'>客户签名</span>
                    <div id='' class='right photos' >
                        <img src='${result.order.signatureImg!}'/>
                    </div>
                </div>
			</#if>
        </div>
		<#if result.order.minimalFlag==0>
            <div class="line"></div>
		<#else>
		</#if>
    </div>

	<#if result.order.refundBean?size gt 0>
        <div class='line mt10'></div>
        <div class="msg returned-report refund-icon">
            <span class='msg-title vamiddle'>关联退货单</span>
        </div>
        <div class='line'></div>
        <div class="returned-report-box">
            <div class="msg-box">
				<#list result.order.refundBean as refund>
                    <a class="root" href="${domainUrl}/mobile/order/refundDetailPage?id=${refund.id?c!}&type=1&minimalFlag=${result.order.minimalFlag!}">
                        <span class='left'>退货单号:${refund.refundNum!}</span>
                        <span id=${refund.id?c!} class='right cart-icon'><#if refund.status==1>待完成<#elseif refund.status==2>已完成<#elseif refund.status==3>已作废</#if></span>
                    </a>
				</#list>
            </div>
        </div>
        <div class='line'></div>
	</#if>
</div>

<div class="btn btn3 col${result.buttonNum!}">
	<#if result.order_cancel==1>
        <a class="btn-cancel"  href="${domainUrl}/mobile/order/cancelPage?id=${result.order.id?c!}&type=1">作废</a>
	</#if>
	<#if result.refund==1>
        <a class="btn-return" href="${domainUrl}/mobile/order/refundPage?id=${result.order.id?c!}&type=1&minimalFlag=${result.order.minimalFlag!}">退货</a>
	</#if>
	<#if result.finish_order==1>
        <a class="btn-finish" href="${domainUrl}/mobile/order/finishFn?id=${result.order.id?c!}&type=1&signatureFlag=${result.order.signatureFlag!}">确认完成</a>
	</#if>
</div>
</#if>
<script type="text/javascript" src='${domainUrl}/static/mobile/js/jquery.js'></script>
<script type="text/javascript">
    $(function(){
        $('.msg-first').on('click',function(){
            $(".t-icon").toggleClass('close');
            $(this).parent().children(".main-box").toggle();
        });

        //获取高度等于宽度
        function Height(){
            var _width = $('.order-img').width();

            _height = _width;
            $('.order-img').css({height:_height});
        }
        Height();
    });
</script>
</body>
</html>
