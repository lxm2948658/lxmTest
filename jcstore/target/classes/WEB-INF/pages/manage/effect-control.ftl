<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>营销效果监控</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="ml30">
				<form id="" class="search-condition" autocomplete="off">
		            <label>分类：</label>
		            <select class="n-select mr25 effect-sel" name="type" id="type">
		            	<option value="0">全部分类</option>
	                    <option value="3">广告营销</option>
	                    <option value="1">店铺分享</option>
	                    <option value="2">商品分享</option>
		            </select>
		            <label>排序：</label>
		            <select class="n-select mr25" name="orderType" id='orderType'>
		                <option value="0">默认排序</option>
	                    <option value="1">按点击量从高到低</option>
	                    <option value="2">按点击量从低到高</option>
		            </select>
				</form>
				<table class="table" id="messageList">
				</table>
			</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	<script>
    	seajs.use("manage/controller/effect-control");
	</script>
</body>
</html>