<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>客户列表</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="client-list-container ml30">
			<form id="search_condition" class="search-condition">
	            <a href="${domainUrl}/manage/clientAdd" class="btn-search blue font-white mr25">添加客户</a>
	            <label>软件类型：</label>
	            <select class="n-select mr25" name="type" id="type">
	            </select>
	            <label>状态：</label>
	            <select class="n-select mr25" name="status">
	                <option value="2">全部</option>
                    <option value="1">正常</option>
                    <option value="0">到期</option>
	            </select>
	            <select class="n-select mr25" name="keys">
	                <option value="companyName">企业名称</option>
                    <option value="clientName">客户姓名</option>
                    <option selected="selected" value="contactWay">联系电话</option>
                    <option value="username">用户名</option>
	            </select>
	            <input type="text" class="n-input mr5" name="values">
	            <a id="search_btn" class="btn-search blue font-white enter-btn">查询</a>
			</form>
	        <table class="table" id="clientList">
			</table>
		</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	<script>
        seajs.use("manage/controller/client-list");
	</script>
</body>
</html>