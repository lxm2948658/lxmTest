<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>修改</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="edit_formBox formBox">
				<div class="edit_form_title">
					<span class="edit_title_img"></span>
					<span>修改</span>
				</div>
				<form id="edit_form">
						
					</form>
			</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	
	<script>
		seajs.use("manage/controller/edit-client");
	</script>
	<script id="editHtml" type="text/x-handlebars-template">
		<div class="form-group">
					<label>&nbsp用&nbsp户&nbsp名:</label>
					<span>{{username}}</span>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
						<label>客户姓名:</label>
						<span id="clientName">{{clientName}}</span>
						<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
						<label>企业名称:</label>
						<input value="{{companyName}}" name="firm" id="companyName"></input>
						<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
						<label>联系电话:</label>
						<input value="{{contactWay}}" name="phone" id="contactWay"></input>
						<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
						<label>软件类型:</label>
						<span id="type">{{softTypeName}}</span>
						<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
						<label>使用人数:</label>
						<span id="userNumber">{{userNumber}}人</span>
						<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
						<label>到期时间:</label>
						<span>{{formatDate expiredTime}}</span>
						<span class="valid-wrap"></span>
				</div>
				<div class="form-group mt30">
					<a class="btn-small blue font-white"id="edit_save">确&nbsp;&nbsp;&nbsp;定</a>
					<a class="btn-small red font-white small" id="edit_reset">重置密码</a>
					<a class="btn-small white border  font-black" href="${domainUrl}/manage/clientList" id="edit_clance">取&nbsp;&nbsp;&nbsp;消</a>
				</div>
		</script>
</body>
</html>