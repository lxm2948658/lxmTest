<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>版本管理</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="client-list-container ml30">
			<div class=" mb30 mt7">
	        	<a href="${domainUrl}/manage/editionEdit" class="btn-big blue font-white">新增</a>
	        </div>
	        <table class="table" id="editionList">
			</table>
		</div>
		</div>
	</div>
	<#include "com-footer.ftl">
	<script>
        seajs.use("manage/controller/edition-list");
	</script>
</body>
</html>