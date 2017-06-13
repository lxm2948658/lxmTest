<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>预览</title>
<#--头部单独使用，不使用com-mobile，请勿修改-->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${(domainUrl?starts_with("https"))?string(domainUrl,'https'+ domainUrl?substring(4))}/static/mobile/css/common.css">
    <link rel="stylesheet" type="text/css" href="${(domainUrl?starts_with("https"))?string(domainUrl,'https'+ domainUrl?substring(4))}/static/mobile/css/preview.css">
    <script>
        var _contextPath = "${domainUrl}";
        window.domainUrl = '${domainUrl}';
    </script>
    <script src="${(domainUrl?starts_with("https"))?string(domainUrl,'https'+ domainUrl?substring(4))}/static/common/js/plug/jquery.min.js"></script>
    <script src="${(domainUrl?starts_with("https"))?string(domainUrl,'https'+ domainUrl?substring(4))}/static/common/js/sea.js"></script>
</head>
<body id="body">
<#if html??>
<#if adv_info.fullScreenImg??>
<div class="whole-page">
    <img src="${adv_info.fullScreenImg!}">
    <div class="count"><span id="count">5</span>s</div>
</div>
</#if>
<div id="main_page" class="hide">
<#if adv_info.topAdvImg??>
    <header>
        <#if (adv_info.topAdvUrl!)!="">
            <a href="${adv_info.topAdvUrl!}">
        </#if>
                <img src="${adv_info.topAdvImg!}">
        <#if (adv_info.topAdvUrl!)!="">
            </a>
        </#if>
    </header>
</#if>
<#if adv_info.headAdvImg??>
    <div class="banner">
    <#if (adv_info.headAdvUrl!)!="">
        <a href="${adv_info.headAdvUrl!}">
    </#if>
            <img src="${adv_info.headAdvImg!}">
    <#if (adv_info.headAdvUrl!)!="">
        </a>
    </#if>
    </div>
</#if>
    <div class="page-content">
    ${html!}
    </div>
    <footer>
        <ul class="connect">
        <#if adv_info.address??>
            <li><img id="position" src="${domainUrl}/static/mobile/images/position.png"></li>
        </#if>
        <#if adv_info.contactway??>
            <li><img id="mobile" src="${domainUrl}/static/mobile/images/mobile.png"></li>
        </#if>
        <#if adv_info.QRCode??>
            <li><img id="wechat"  src="${domainUrl}/static/mobile/images/wechat.png"></li>
        </#if>
            <li class="address"><img src="${domainUrl}/static/mobile/images/tri.png">地址：${adv_info.address!}</li>
        </ul>
    <#if adv_info.bottomAdvImg??>
        <div id="footer_ad" class="posr">
        <#if (adv_info.bottomAdvUrl!)!="">
            <a href="${adv_info.bottomAdvUrl!}">
        </#if>
                <img class="w100p show" src="${adv_info.bottomAdvImg!}">
        <#if (adv_info.bottomAdvUrl!)!="">
            </a>
        </#if>
            <a href="javascript:;" class="close"></a>
        </div>
    </#if>
    </footer>
    <div id="wechat_code" class="mask">
        <div class="code"><img src="${adv_info.QRCode!}"></div>
    </div>
    <div id="mobile_num" class="mask">
        <div class="num">
            <a href="javascript:;">${adv_info.contactway!}</a>
            <a href="tel:${adv_info.contactway!}">拨打电话</a>
            <a href="sms:${adv_info.contactway!}">发送短信</a>
            <a class="cancel" href="javascript:;">取消</a>
        </div>
    </div>
</div>
<#else >
<p class="tip">很遗憾，你来晚了~<br>文章内容可能已被删除</p>
</#if>
</body>
<script>
    if($('.whole-page').length == 0){
        $('#main_page').show();
    }else {
        var count = 4;
        var timer = setInterval(function () {
            if(count>0){
                $('#count').text(count);
                count--;
            }else {
                clearInterval(timer);
                $('#main_page').show();
                $('.whole-page').fadeOut();
            }
        },1000);
    }
    $('.rich_media_meta_list').after($('.banner'));
    $('.banner').show();
    document.title = $('#activity-name').text();
    $(document).on('click','#wechat',function () {
        $('#wechat_code').fadeIn('fast');
    })
    $('#wechat_code').click(function () {
        $('#wechat_code').fadeOut('fast');
    })
    $(document).on('click','#mobile',function () {
        $('#mobile_num').fadeIn('fast');
    })
    $(document).on('click','.cancel',function () {
        $('#mobile_num').fadeOut('fast');
    })
    $(document).on('click','#position',function () {
        $('.address').fadeIn('fast');
    })
    document.getElementById('body').addEventListener('touchend',function (e) {
        if(e.target.id != 'position'){
            $('.address').hide();
        }
    })
    $('body').on('click','p,span,section,div',function () {
        $('.address').hide();
    })
    $('#footer_ad').on('click','.close',function () {
        $('#footer_ad').hide();
    })
</script>
</html>
