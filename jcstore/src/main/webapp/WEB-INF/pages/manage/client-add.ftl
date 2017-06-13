<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>添加客户</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="add_formBox formBox">
			<div class="form_title">
				<span class="title_img"></span>
				<span>添加客户</span>
			</div>
			<form id="add_form">
				<div class="form-group">
					<label>客户姓名:</label>
					<input  name="clientName"></input>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<label>企业名称:</label>
					<input name="companyName"></input>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<label>联系电话:</label>
					<input name="contactWay"></input>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<label>&nbsp用&nbsp户&nbsp名:</label>
					<input name="username" id="user_name"></input>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<label>软件类型:</label>
					<select class="n-select w150" id="type" name="type">
					</select>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<label>开通时间:</label>
					<input type="text"class="n-select w142 no-padding" maxlength="3" name="months"/> 月
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<label>使用人数:</label>
					<input type="text"class="n-select w142 no-padding" maxlength="5" name="userNumber"/> 人
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group mt20">
					<a class="btn-big blue font-white" id="add_save">确&nbsp;&nbsp;&nbsp;定</a>
					<a class="btn-big  border white font-black" href="${domainUrl}/manage/clientList" id="add_canle">取&nbsp;&nbsp;消</a>
				</div>
			</form>
		</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	<script>
		seajs.use("manage/controller/add-client");
	</script>
</body>
</html>