<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title></title>
	<#include "../common/com-pc.ftl">
	<#include "com-manage.ftl">
	<style>
		.specification p,h1{padding-bottom:10px;}
	</style>
</head>
<body class="ui-demo-body">
		<div class="specification">
			<h1>css规范：</h1>
			<p style="font-weight:bold;color:red">*页面中不能有行内样样式和内嵌样式,公共ui必须使用本页中定义好的ui，如果样式有差异，可以自己在自己样式内部修改，不能直接修改公共样式</p>
			<p>1、css命名：例：<span style="font-weight:bold;color:red;">"form-box"</span> 用中划线链接，不宜过长，一般用简写，比如bouttn-big,写成btn-big即可</p>
			<p style="font-weight:bold;">2、css样式表里面，不能用"#form-box",不能用id来定义css样式，id只能用来在js里或其他的交互中使用</p>
			<p>3、公共的，例如width,height,margin、padding、hide,show,....等定义，放到common/css/common.css中，具体格式参照common.css</p>
			<p>4、其他公共样式，放到各个系统中的common.css中。例如:manage/css/common.css</p>
			<p>5、其他非公共样式，放到各个系统中的main.css中。例如:manage/css/main.css</p>
			<h1>js规范：</h1>
			<p style="font-weight:bold;color:red">*除数据外，页面中不能内嵌js</p>
			<p>1、js命名：例：clien-add.ftl对应controller-<span style="font-weight:bold;color:red;">"controller/client-add.js"</span>,对应<span style="font-weight:bold;color:red;">"service-service/client"</span> 用中划线链接，不宜过长</p>
			<p>2、controller中的命名，和页面名称一样，service中的命名，是页面的中划线前面的名称，module中的命名，为功能名称，但是<span style="font-weight:bold;color:red;">"必须是小写字母或小写字母加中划线"</span></p>
		</div>
		<select class="n-select">
			<option>100人套餐</option>
			<option>1</option>
			<option>1</option>
			<option>1</option>
		</select>
		<button class="btn-big blue font-white">确&nbsp;&nbsp;&nbsp;定</button>
		<button class="btn-big  border white font-black">取&nbsp;&nbsp;消</button>
		<button class="btn-small blue font-white">确&nbsp;&nbsp;&nbsp;定</button>
		<button class="btn-small red font-white small">重置密码</button>
		<button class="btn-small white border  font-black">取&nbsp;&nbsp;&nbsp;消</button>
		<button class="btn-table blue font-white">续费&升级</button>
		<button class="btn-table blue font-white">修改</button>
		<button class="btn-table red font-white">删除</button>
		<button class="btn-search blue font-white">添加客户</button>
		<button class="btn-search blue font-white">查询</button>
		<input class="n-input"></input>
		
		<div class="form-box ">
			<div class="form-title">
				<span class="title-img"></span>
				<span>添加客户</span>
			</div>
			<form id="add-form">
				<div class="form-group">
					<label>客户姓名:</label>
					<input  name="clientName" class ="n-input"></input>
					<span class="valid-wrap">
						<span>客户姓名不能为空,这层span不要拷贝，这里只是做个demo</span>
					</span>
				</div>
				<div class="form-group">
					<label>企业名称:</label>
					<input name="companyName" class ="n-input"></input>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<label>联系电话:</label>
					<input name="contactWay" class ="n-input"></input>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<label>&nbsp用&nbsp户&nbsp名:</label>
					<input name="username" id="user_name" class="n-input"></input>
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
					<select class="n-select w150" id="months" name="months">

					</select>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<label>开通人数:</label>
					<select class="n-select w150" id="userNumber" name="userNumber">

					</select>
					<span class="valid-wrap"></span>
				</div>
				<div class="form-group">
					<label>发布内容:</label>
					<textarea class='textarea'> </textarea>
				</div>
				<div class="form-group mt20">
					<a class="btn-big blue font-white enter-btn" id="add_save">确&nbsp;&nbsp;&nbsp;定</a>
					<a class="btn-big  border white font-black" href="${domainUrl}/manage/clientList" id="add_canle">取&nbsp;&nbsp;消</a>
				</div>
			</form>
		</div>
</body>
</html>