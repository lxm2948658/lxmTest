<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>新增编辑版本</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="add_formBox formBox w1000">
			<div class="form_title">
				<span class="title_img edition-img"></span>
				<span>新增/编辑版本</span>
			</div>
			<form id="edition_form" class="edition-form">
				<div class="form-group">
					<label><span class="require">*</span>版本名称:</label>
					<input id="edition_name" type="text" name="name"/>
					<span class="valid-wrap"></span>
				</div>
                <div class="form-group">
                    <label><span class="require">*</span>功能选择:</label>
                    <span class="lh35">根据需求勾选功能模块，设置当前版本功能</span>
                    <span class="valid-wrap"></span>
					<table id="module" class="module-table">
					</table>
                </div>
				<div class="form-group">
					<a class="btn-big blue font-white ml80" id="add_save">保&nbsp;&nbsp;&nbsp;存</a>
					<a class="btn-big  border white font-black" href="${domainUrl}/manage/editionList" id="add_canle">取&nbsp;&nbsp;消</a>
				</div>
			</form>
		</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	<script>
		seajs.use("manage/controller/edition-edit");
	</script>
</body>
</html>