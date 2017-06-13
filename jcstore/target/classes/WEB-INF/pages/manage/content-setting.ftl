<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>内容设置</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body setting">
			<div>
				<select class="n-select" id="other_select">
					<option value='1'>联系我们</option>
					<option value='2'>关于我们</option>
					<option value='3'>使用帮助</option>
				</select>
			</div>
			<textarea class='shop-investment' id="investment" style='height:300px;' ></textarea>
			<div class="col-sm-offset-4 col-sm-10 btn-box mt12">
				<button type="submit" class="btn-big blue font-white mr10" id="save">保存</button>
				<button type="button" class="btn-big  border white font-black" id='cancel'>取消</button>        
			</div> 
		</div>
	</div>
	<#include "com-footer.ftl">
    <script src="${domainUrl}/static/common/js/ueditor/ueditor.config.js"></script>
	<script src="${domainUrl}/static/common/js/ueditor/ueditor.all.min.js"></script>
	<script>
        seajs.use("manage/controller/content-setting");
    </script>
</body>
</html>