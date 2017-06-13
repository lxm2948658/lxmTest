<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>意见反馈管理</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
	        <table class="table" id="clientList">
			</table>
		</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	<script>
        seajs.use("manage/controller/feedback");
	</script>
</body>
</html>