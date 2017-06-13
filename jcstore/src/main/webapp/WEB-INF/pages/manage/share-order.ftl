<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>分享</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no" />
    <meta name="renderer" content="webkit" />
    <link rel="stylesheet" type="text/css" href="${localDomainUrl}/static/manage/css/share.css">
</head>
<body>
	<div class="share-container">
		<div class="section share-head">
			<img src="${shop.logo!}">
			<p>${shop.name!}</p>
		</div>
        <div class="section">
			<div class="item">
				<label class="cl999">订单编号</label>
				<span class="cl333 fr">${order.orderNumber!}</span>
			</div>
            <div class="item">
                <label class="cl999">下单时间</label>
                <span class="cl333 fr">${order.orderDate!?string("yyyy-MM-dd HH:mm")}</span>
            </div>
            <div class="item">
                <label class="cl999">预计发货时间</label>
                <span class="cl333 fr"><#if order.expectedDeliveryDate??>${order.expectedDeliveryDate!?string("yyyy-MM-dd")}</#if></span>
            </div>
            <div class="item">
                <label class="cl999 pab">店铺地址：</label>
                <span class="cl999 ml58r brkword">${shop.address!}</span>
            </div>
        </div>
        <div class="section">
        
            <div class="item">
                <label class="cl999">商品明细</label>
                <span class="cl333 fr">合计￥${order.total!?string('0.00')}</span>
            </div>
        <#list order.detail as detail>
            <div class="item-container">
                <div class="item">
                    <span class="cl333 brkword">${detail.name!}</span>
                </div>
                <div class="item">
                    <span class="fr"><span class="cl999">单价数量：</span><span class="clf00">￥${detail.price!?string('0.00'	)}</span><span class="cl333">×${detail.number!}</span> </span>
                </div>
            </div>
        </#list>
        </div>
        <div class="section">
            <div class="item">
                <label class="cl999">实收金额</label>
                <span class="clf00 fr bold">￥${order.receiceAmoun!?string('0.00')}</span>
            </div>
        </div>
        <div class="section">
            <div class="item">
                <label class="cl999">业务员：</label>
                <span class="cl333">${order.salesman!} ${user.mobile!}</span>
            </div>
            <div class="item">
                <label class="cl999 pab">备注：</label>
                <span class="cl333 ml3r brkword">${order.description!}</span>
            </div>
        </div>
        
	</div>
</body>
</html>