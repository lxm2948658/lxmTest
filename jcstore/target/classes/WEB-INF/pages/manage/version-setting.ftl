<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>版本检测</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body version">
		<form id ='formIOS'>
			<h3><i></i>版本升级检测</h3>
			<div class="form-group">
				<div class='version-box IOS'>
					<div class='edit' type='ios' id='editIOS'></div>
					<label><i></i>IOS</label>
					<input id='iosVersion' class='version version1' name='iosVersion' type='text' value=' ' readonly="readonly"/>
					<span class="valid-wrap"></span>
					<div class="form-el" id="form-el" data-checkstatus=''>
						<div class="toggler"><span class='Copen'>开</span><span class='Cclose'>关</span></div>
						<div class="handle"></div>
					</div>
				</div>
			</div>
		</form>
			<p class='tips'>提示：开启状态下可以在线监测IOS版本是否需要升级，请在新版本审核前关闭此选项。</p>
		<form id ='formAndriod'>
			<div class="form-group">
				<div class='version-box Android'>
					<div class='edit' type='andriod' id='editAndroid'></div>
					<label><i></i>Android</label>
					<input id='androidVersion' class='version verson1' name='androidVersion' type='text' value=' ' readonly="readonly"/>
					<span class="valid-wrap"></span>
				</div>	
			</div>
		</form>
		<form id ='formAndriodpad'>
			<div class="form-group">
				<div class='version-box Androidpad'>
					<div class='edit' type='andriodpad' id='editAndroidpad'></div>
					<label><i></i>Android Pad</label>
					<input id='androidpadVersion' class='version verson1' name='androidpadVersion' type='text' value=' ' readonly="readonly"/>
					<span class="valid-wrap"></span>
				</div>	
			</div>
		</form>
		</div>
	</div>
	<#include "com-footer.ftl">
	<script>
    	seajs.use("manage/controller/version-setting");
    </script>
</body>
</html>