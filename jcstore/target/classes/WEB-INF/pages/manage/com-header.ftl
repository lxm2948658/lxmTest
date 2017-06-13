<header class='header'>
	<div class='header-top'>
		<a href='${domainUrl}/manage/clientList'><img src='${domainUrl}/static/manage/images/logo.png'/></a>
		<ul>
			<li><a <#if ma_head_nav?? && ma_head_nav == 'client' > class='current' </#if> href='${domainUrl}/manage/clientList'>客户管理</a></li>
			<li><a  <#if ma_head_nav?? && ma_head_nav == 'contentsetting' > class='current' </#if>href='${domainUrl}/manage/content/toContent'>内容设置</a></li>
			<li><a  <#if ma_head_nav?? && ma_head_nav == 'platform' > class='current' </#if>href='${domainUrl}/manage/platform/toMessageList'>平台管理</a></li>
			<li><a  <#if ma_head_nav?? && ma_head_nav == 'edition' > class='current' </#if>href='${domainUrl}/manage/editionList'>版本管理</a></li>
            <li><a href='http://yingxiao.yikaidan.cn/admin.php' target="_blank">微营销</a></li>
		</ul>
		<div class='right user-action'>
			<span>${user_admin.username}</span> - 
			<a href="javascript:void(0);" class="loginout">退出</a>
		</div>
	</div>
</header>