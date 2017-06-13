<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>消息列表</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="ml30">
                <a href="${domainUrl}/manage/platform/toMessageAdd" class="btn-search blue font-white mb30 dpib">发布新消息</a>
				<table class="table" id="messageList">
				</table>
			</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	<script>
        seajs.use("manage/controller/message-list");
	</script>
</body>
</html>