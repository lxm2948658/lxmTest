<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>体验账号列表</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="client-list-container">
				<div class="delete-all">
                    <input type="checkbox" class="select-all">
					<span>全选</span>
					<a href="javascript:;">删除</a>
				</div>
				<table class="table" id="clientExperienceList">
				</table>
                <div class="delete-all mt-90">
                    <input type="checkbox" class="select-all">
                    <span>全选</span>
                    <a href="javascript:;">删除</a>
                </div>
			</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	<script>
        seajs.use("manage/controller/client-experience-list");
	</script>
</body>
</html>