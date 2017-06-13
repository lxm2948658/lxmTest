<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>商城订单详情</title>
	<#include "com-mobile.ftl">
</head>
<body class="bgf1f1f1">
	<div class='section'>
		<div class="item">
			<span class='titl'>订单编号</span>
			<span class='value'>${result.order.orderNumber!}</span>
		</div>
        <div class="item">
            <span class='titl'>下单时间</span>
            <span class='value'>${result.order.createtime?string("yyyy-MM-dd HH:mm:ss")!}</span>
        </div>
        <div class="item">
            <span class='titl'>客户姓名</span>
            <span class='value'>${result.order.customer!}</span>
        </div>
        <div class="item">
            <span class='titl'>联系电话</span>
            <span class='value'>${result.order.phone!}</span>
        </div>
        <div class="item long">
            <span class='titl'>收货地址</span>
            <span class='value'>${result.order.contactWith!}</span>
        </div>
	</div>
    <div class='section'>
        <div class="item">
            <span class='titl'>订单金额</span>
            <span class='value clff1300'>￥${result.order.receiceAmoun!?string("0.00")}</span>
        </div>
        <div class="item">
            <span class='titl'>支付流水号</span>
            <span class='value'>${result.order.payNumber!}</span>
        </div>
        <div class="item">
            <span class='titl'>付款方式</span>
            <span class='value'>${result.order.payType!}</span>
        </div>
        <div class="item long">
            <span class='titl'>备注</span>
            <span class='value'>${result.order.description!}</span>
        </div>
    </div>
    <div class="section goods-list">
        <h2>商品明细</h2>
        <ul>
        <#list result.order.detail as detail>
            <li>
                <img src="${detail.img!}">
                <h3>${detail.name!}</h3>
                <p><span>单品价格：￥${detail.price?string("0.00")!}</span>数量：x${detail.number?c!}</p>
            </li>
        </#list>
        </ul>
    </div>
<script type="text/javascript" src='${domainUrl}/static/mobile/js/jquery.js'></script>
<script type="text/javascript">
</script>
</body>
</html>