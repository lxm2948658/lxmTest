<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>开单凭证</title>
	<#include "com-mobile.ftl">
</head>
<body>
<img id="loading" class="loading" src="${domainUrl}/static/mobile/images/loading.gif">
<img id="certificate" class="certificate" src="">
</body>
<script>
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }
	$.ajax({
		url:domainUrl+'/mobile/share/capture/proof',
		method:'get',
		data:{
			oid:getUrlParam('oid'),
			proof:getUrlParam('proof')
		},
		success:function (data) {
			if(data.status == 1){
				$('#loading').hide();
                $('#certificate').attr('src',data.path).show();
			}else{
			}
        }
	})
</script>
</html>