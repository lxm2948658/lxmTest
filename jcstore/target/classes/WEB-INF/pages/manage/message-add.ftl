<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>添加消息</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="add-message formBox">
			<div class="form_title">
				<span class="title_img"></span>
				<span>消息发送</span>
			</div>
			<form id='message-add' class='message-add'>
				<div class="form-group">
					<span class='msgtitle'>发布对象:</span>
					<textarea id='add-name' class='add-name textarea' name='add-name'></textarea>
					<span class='tips'>提示：输入口令为“@”，间隔符号为“，”，群发口令包含了“@全部用户”和“@全部商家”。</span>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<span class='msgtitle'>发布内容:</span>
					<textarea id='add-content' class='add-content textarea' name='add-content'></textarea>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<span class='msgtitle'>URL:</span>
					<input type='text' id='add-link' class='add-link link-input' name='add-link'/>
					<span class='tips'>提示：如 http://www.qianfan365.com</span>
					<span class="valid-wrap"></span>
				</div>
				<div class="mt20">
					<a class="btn-big blue font-white add_save" id="add_save">确&nbsp;&nbsp;&nbsp;定</a>
				</div>
			</form>
			
		</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	<script>
		seajs.use("manage/controller/message-add");
	</script>
</body>
</html>