<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>登录页面</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	作为app端自动登陆------给前端手机测试用
	<script>
        $.ajax({
			url:domainUrl + '/mobile/login',
			type:'POST',
			data:{
				username:'bncf',
				password:'yikaidan'
			}
		})
    </script>
</body>
</html>