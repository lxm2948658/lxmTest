<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>商品导入</title>
<#include "../common/com-pc.ftl">
<#include "com-manage.ftl">
</head>
<body>
<#include "com-header.ftl">

<div class="wrapper clearfix">
<#include "com-sidebar.ftl">
    <div class="main-body">
        <div class="upload_formBox formBox">
            <div class="uping_form_title">
                <span class="upload_title_img"></span>
                <span>商品导入</span>
            </div>
            <form id="upload_form">
                <div class="form-group">
                    <label>用&nbsp户&nbsp名&nbsp:</label>
                    <span id="username"></span>
                    <span class="valid-wrap"></span>
                </div>
                <div class="form-group">
                    <label>客户姓名:</label>
                    <span id="clientName"></span>
                    <span class="valid-wrap"></span>
                </div>
                <div class="form-group">
                    <label>企业名称:</label>
                    <span id="companyName"></span>
                    <span class="valid-wrap"></span>
                </div>
                <div class="form-group">
                    <label>商品文件:</label>
                    <a href="${domainUrl}/manage/product/download" class="down-upload">示例下载</a>
                    <a href="#" id="select_file" class="down-upload">选择文件</a>
                    <div class="uploadinput-container">
                    <input type="file" accept=".xlsx" class="uploadinput" id="upload_input" onchange="uploadInputFn()" name="file">
                    </div>
                    <span class="valid-wrap"></span>
                    <p class="filename"></p>
                </div>
                <div class="form-group">
                    <label>注意事项:</label>
                    <ol>
                        <li> 1.只能上传后缀为.xlsx文件，且最多不超过300条数据</li>
                        <li>2.商品名称、商品编码、规格/型号、销售价为必填项</li>
                        <li>3.本操作只支持新增，不支持修改、删除商品信息</li>
                        <li>4.商品分组文本须与店铺的分组一致，填写错误不能保存商品分组信息</li>
                    </ol>
                </div>
                <div class="form-group mt20">
                    <button type="button" disabled="disabled" class="btn-big mr20 font-white upload-btn" id="goods_upload">导入</button>
                    <a class="btn-big  border white font-black"  href="${domainUrl}/manage/clientList">取消</a>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="mask">
    <img src="${domainUrl}/static/manage/images/loading.gif">
</div>
<#include "com-footer.ftl">
<script src="${domainUrl}/static/common/js/ajaxfileupload.min.js"></script>
<script>
    seajs.use("manage/controller/goods-upload");
</script>
</body>
</html>