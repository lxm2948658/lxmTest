<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>详情</title>
	<#include "com-mobile.ftl">
</head>
<body class="nt-wrapper">
<#--<#if result.status!=1>-->
	<#--<div class='tip-page'>-->
		<#--${result.statusMsg!}-->
	<#--</div>-->
<#--</#if>-->
	<div class='tip-page'>
		<#if result.status==700>
			请返回上一级重新登录后再试
		<#else>
			${result.statusMsg!}
		</#if>
	</div>
</body>
</html>
