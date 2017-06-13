define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var contentService=require("manage/service/content");
	var oldContent = '';
	
	var ueditorConfig = {
			toolbars: [
				        [
				         'preview', '|',//预览save-btn
				        'source', 'undo', 'redo','|',
				        'bold',
				        'italic', //斜体
				        'underline', //下划线
				        'strikethrough', //删除线
				        '|',
				        'fontfamily', //字体
				        'fontsize', //字号
				        'forecolor', //字体颜色
				        'backcolor', //背景色
				        '|',
				        'paragraph', //段落格式
				        'justifyleft', //居左对齐
				        'justifyright', //居右对齐
				        'justifycenter', //居中对齐
				        'justifyjustify',//两端对齐
				        'indent',
				        '|',
				        'imagecenter', //居中
				        'simpleupload', //单图上传
				        'insertimage', //多图上传
				        'link', //超链接
				        'emotion', //表情
				        'spechars', //特殊字符
				        '|',
				        'cleardoc', //清空文档
				        'fullscreen',
							'wordimage'
				        ]
				    ],
				"zIndex":1,
				"autoFloatEnabled":false
		};
	UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
	UE.Editor.prototype.getActionUrl = function(action) {
	    if (action == 'uploadimage' || action == 'catchimage' || action == 'uploadscrawl' || action == 'uploadimage' || action ==  "uploadfile") {
	    	return domainUrl+'/uploadEditorFile';
	        return 'http://a.b.com/video.php';
	    } else {
	        return this._bkGetActionUrl.call(this, action);
	    }
	}
	
	var investmentUeditor = UE.getEditor('investment',ueditorConfig);
	var id=$("#other_select").val();
	investmentUeditor.ready(function() {
		$("#other_select").trigger("change")
    });
	
	$("#other_select").change(function(){
		var id=$(this).val()
		contentService.check(id).ready(function(data){
			oldContent = data.content;
			investmentUeditor.setContent(data.content)
		})
	})
	
//	save
	$("#save").click(function(){
		var id=$("#other_select").val();
		var content=investmentUeditor.getContent();
		contentService.save(id,content).ready(function(data){
			if(data.status=="1"){
				dialog.lite('保存成功')
			}else{
				dialog.lite('保存失败')
			}
		})
		
	});
	
//	cancel
	$('#cancel').click(function(){
		investmentUeditor.setContent(oldContent)
	})
	
});