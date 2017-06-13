<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>续费&升级</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
	<style>
		.layui-layer{max-width:420px!important;}
	</style>
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="uping_formBox formBox">
				<div class="uping_form_title">
					<span class="uping_title_img"></span>
					<span>续费&升级</span>
				</div>
				<form id="uping_form">
					<div class="form-group">
								<label>&nbsp用&nbsp户&nbsp名:</label>
								<span id="username"></span>
								<span class="valid-wrap"></span>
						</div>
						<div class="form-group">
								<label>客户姓名:</label>
								<span id="clientName"></span>
								<span class="valid-wrap"></span>
						</div>
						<div class="form-group">
								<label>企业名称:</label>
								<span id="companyName"></span>
								<span class="valid-wrap"></span>
						</div>
						<div class="form-group">
								<label>联系电话:</label>
								<span id="contactWay"></span>
								<span class="valid-wrap"></span>
						</div>
						<div class="form-group">
							<label>软件类型:</label>
							<select class="n-select w150" id="type">
							</select>
							<span class="valid-wrap"></span>
						</div>
						<div class="form-group">
							<label>续费时间:</label>
							<input type="text"class="n-select w142 no-padding" maxlength="3" value="0" id="months" name="months"/> 月
							<span class="valid-wrap"></span>
						</div>
						<div class="form-group">
							<label>使用人数:</label>
							<input type="text"class="n-select w142 no-padding"maxlength="5" id="userNumber" name="userNumber"/> 人
							<span class="valid-wrap"></span>
						</div>
						<div class="form-group mt20">
							<a href="javascript:void(0)" class="btn-big blue font-white enter-btn" id="renew_save">确&nbsp;&nbsp;&nbsp;定</a>
							<a class="btn-big  border white font-black"  href="${domainUrl}/manage/clientList">取&nbsp;&nbsp;消</a>
						</div>
				</form>
			</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	<script>
		seajs.use("manage/controller/uping-client");
	</script>
		<script id="renewDialog" type="text/x-handlebars-template">
			<div class="renewfrom">
				<div class="form-group">
						<label>软件类型：</label>
						<span>{{type}}；</span>
					</div>
					<div class="form-group">
							<label>使用人数：</label>
							<span>{{userNumber}}人；</span>
					</div>
					<div class="form-group">
							<label>续费时间：</label>
							<span id="clientName">{{months}}个月；</span>
					</div>
					<div class="form-group">
							<span id="clientName">有效期截止至{{expiredTime}}。</span>
					</div>
			</div>		
	</script>
</body>
</html>