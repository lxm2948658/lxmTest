<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>全景列表</title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
</head>
<body>
	<#include "com-header.ftl">
	
	<div class="wrapper clearfix">
		<#include "com-sidebar.ftl">
		<div class="main-body">
			<div class="ml30">
                <a href="javascript:void(0)" class="btn-search blue font-white mb30 dpib add">新增全景</a>
				<table class="table" id="messageList">
				</table>
			</div>
		</div>
	</div>
	
	<#include "com-footer.ftl">
	<script>
        seajs.use("manage/controller/overall-list");
	</script>
	<script id="addDialog" type="text/x-handlebars-template">
		<form id='overallfrom' class="H5from">
			<div class="form-group">
				<label class=''>标题：</label>
				<input id='title' class='' type='text' name='title' value=''/>
				<span class="valid-wrap"></span>
			</div>
			<div class="form-group">
				<label>链接地址：</label>
				<input id='addressurl' type='text' name='addressurl' value=''/>
				<span class="valid-wrap"></span>
			</div>
			<div class="form-group mt20">	
				<label>图片：</label>
				<div class='left' id='uploadDiv'>
					<input type='hidden' value='' class='imgurlvalid' name='imgurl'/>
					<a>上传图片</a>
					<input id='upload' name='__avatar1' accept='image/png, image/jpeg' class='upload' type='file' value='上传图片' onchange="uploadHeadImageFn(event,this);"/>
					<div id='uploadImg' class='uploadImg right'><img class='imgurl' src=''/></div>
					<span class='tips'>建议尺寸180像素*250像素</span>
					<span id='valid-warp' class="valid-wrap"></span>
				</div>
			</div>
			<div class="form-group mt30">
				<div class='inner'>
					<label class=''>使用说明：</label>
					<span>1、先使用第三方工具制作全景效果并上传服务器。</span>
				</div>
				<div class='inner'>
					<label class=''> </label>
					<span class=''>2、获取链接后输入链接开始配置。</span>
				</div>	
				<div class='inner'>
					<label class=''> </label>
					<span class=''>3、配置成功后，客户在APP端自动生成。</span>
				</div>
			</div>
		</form>		
	</script>
</body>
</html>