<ul class='slider'>
	<#if ma_head_nav?? && ma_head_nav == 'client' >
		<#if ma_sider_nav?? && ma_sider_nav != 'scene' && ma_sider_nav != 'overall' >
			<li <#if ma_sider_nav?? && ma_sider_nav == 'client' > class='current' </#if>>
				<a  href='${domainUrl}/manage/clientList'>客户列表</a>
				<div class='chart'></div>
			</li>
			
	        <li <#if ma_sider_nav?? && ma_sider_nav == 'experience' > class='current' </#if>>
	            <a  href='${domainUrl}/manage/experienceList'>体验账号列表</a>
	            <div class='chart'></div>
	        </li>
        </#if>
        <#if ma_sider_nav?? && ma_sider_nav == 'scene' >
	        <li <#if ma_sider_nav?? && ma_sider_nav == 'scene' > class='current' </#if>>
	            <a  href='javascript:void(0)'>场景列表</a>
	            <div class='chart'></div>
	        </li>
        </#if>
        <#if ma_sider_nav?? && ma_sider_nav == 'overall' >
	        <li <#if ma_sider_nav?? && ma_sider_nav == 'overall' > class='current' </#if>>
	            <a  href='javascript:void(0)'>全景列表</a>
	            <div class='chart'></div>
	        </li>
        </#if>
	</#if>
	<#if ma_head_nav?? && ma_head_nav == 'contentsetting' >
		<li <#if ma_sider_nav?? && ma_sider_nav == 'contents' > class='current' </#if>>
			<a  href='${domainUrl}/manage/content/toContent'>内容设置</a>
			<div class='chart'></div>
		</li>
		<li <#if ma_sider_nav?? && ma_sider_nav == 'version' > class='current' </#if>>
			<a  href='${domainUrl}/manage/version/setting'>版本检测</a>
			<div class='chart'></div>
		</li>
	</#if>
	<#if ma_head_nav?? && ma_head_nav == 'platform' >
		<li <#if ma_sider_nav?? && ma_sider_nav == 'message' > class='current' </#if>>
			<a  href='${domainUrl}/manage/platform/toMessageList'>消息管理</a>
			<div class='chart'></div>
		</li>
		<li <#if ma_sider_nav?? && ma_sider_nav == 'feedback' > class='current' </#if>>
			<a  href='${domainUrl}/manage/platform/toFeedback'>意见反馈管理</a>
			<div class='chart'></div>
		</li>
		<li <#if ma_sider_nav?? && ma_sider_nav == 'control' > class='current' </#if>>
			<a  href='${domainUrl}/manage/platform/toEffectcontrol'>营销效果监控</a>
			<div class='chart'></div>
		</li>
		<li <#if ma_sider_nav?? && ma_sider_nav == 'h5manage' > class='current' </#if>>
			<a  href='${domainUrl}/manage/platform/toH5List'>H5模板管理</a>
			<div class='chart'></div>
		</li>
	</#if>
	<#if ma_head_nav?? && ma_head_nav == 'edition' >
		<li <#if ma_sider_nav?? && ma_sider_nav == 'edition' > class='current' </#if>>
			<a  href='${domainUrl}/manage/editionList'>版本管理</a>
			<div class='chart'></div>
		</li>
	</#if>
</ul>