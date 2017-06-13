<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>登录页面</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<img class="bg" src="${domainUrl}/static/manage/images/bg.jpg">
	<div class="login">
		<img class="logo" src="${domainUrl}/static/manage/images/logo.png">
		<span>运营管理系统</span>
		<from id="managefrom">
			<input type="text" id='username' value="" placeholder='请输入用户名' />
			<input type="password" id='password' value="" placeholder='请输入密码' />
			<input id='submit' class="submit enter-btn" type="button" value="登   录" />
		</from>
	</div>
	<div class="clfff footer">©qianfan365.com</div>
	<script>
        seajs.use("manage/controller/login");
    </script>
</body>
</html>