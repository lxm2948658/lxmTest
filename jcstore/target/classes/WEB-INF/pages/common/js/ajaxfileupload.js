(function(jQuery){
	jQuery.extend({
	    createUploadIframe: function(id, uri) {
	        //create frame
	        var frameId = 'jUploadFrame' + id;
	        var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '" style="position:absolute; top:-9999px; left:-9999px"';
	        if (window.ActiveXObject) {
	            if (typeof uri == 'boolean') {
	                iframeHtml += ' src="' + 'javascript:false' + '"';

	            } else if (typeof uri == 'string') {
	                iframeHtml += ' src="' + uri + '"';

	            }
	        }
	        iframeHtml += ' />';
	        jQuery(iframeHtml).appendTo(document.body);

	        return jQuery('#' + frameId).get(0);
	    },
	    createUploadForm: function(id, fileElementId, data) {
	        //create form	
	        var formId = 'jUploadForm' + id;
	        var fileId = 'jUploadFile' + id;
	        var form = jQuery('<form  action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');
	        if (data) {
	            for (var i in data) {
	                jQuery('<input type="hidden" name="' + i + '" value="' + data[i] + '" />').appendTo(form);
	            }
	        }
	        var oldElement = jQuery('#' + fileElementId);
	        var newElement = jQuery(oldElement).clone();
	        jQuery(oldElement).attr('id', fileId);
	        jQuery(oldElement).before(newElement);
	        jQuery(oldElement).appendTo(form);



	        //set attributes
	        jQuery(form).css('position', 'absolute');
	        jQuery(form).css('top', '-1200px');
	        jQuery(form).css('left', '-1200px');
	        jQuery(form).appendTo('body');
	        return form;
	    },

	    ajaxFileUpload: function(s) {
	        // TODO introduce global settings, allowing the client to modify them for all requests, not only timeout		
	        s = jQuery.extend({}, jQuery.ajaxSettings, s);
	        var id = new Date().getTime()
	        var form = jQuery.createUploadForm(id, s.fileElementId, (typeof(s.data) == 'undefined' ? false : s.data));
	        var io = jQuery.createUploadIframe(id, s.secureuri);
	        var frameId = 'jUploadFrame' + id;
	        var formId = 'jUploadForm' + id;
	        // Watch for a new set of requests
	        if (s.global && !jQuery.active++) {
	            jQuery.event.trigger("ajaxStart");
	        }
	        var requestDone = false;
	        // Create the request object
	        var xml = {}
	        if (s.global)
	            jQuery.event.trigger("ajaxSend", [xml, s]);
	        // Wait for a response to come back
	        var uploadCallback = function(isTimeout) {
	                var io = document.getElementById(frameId);
	                try {
	                    if (io.contentWindow) {
	                        xml.responseText = io.contentWindow.document.body ? io.contentWindow.document.body.innerText : null;
	                        xml.responseText =  xml.responseText ?  xml.responseText : io.contentWindow.document.body.textContent;
	                        xml.responseXML = io.contentWindow.document.XMLDocument ? io.contentWindow.document.XMLDocument : io.contentWindow.document;
	                    } else if (io.contentDocument) {
	                        xml.responseText = io.contentDocument.document.body ? io.contentDocument.document.body.innerHTML : null;
	                        xml.responseXML = io.contentDocument.document.XMLDocument ? io.contentDocument.document.XMLDocument : io.contentDocument.document;
	                    }
	                } catch (e) {
	                    jQuery.handleError(s, xml, null, e);
	                }
	                if (xml || isTimeout == "timeout") {
	                    requestDone = true;
	                    var status;
	                    try {
	                        status = isTimeout != "timeout" ? "success" : "error";
	                        // Make sure that the request was successful or notmodified
	                        if (status != "error") {
	                            // process the data (runs the xml through httpData regardless of callback)
	                            var data = jQuery.uploadHttpData(xml, s.dataType);
	                            // If a local callback was specified, fire it and pass it the data
	                            if (s.success){
	                                s.success(data, status);
	                            }
	                            	

	                            // Fire the global callback
	                            if (s.global)
	                                jQuery.event.trigger("ajaxSuccess", [xml, s]);
	                        } else
	                            jQuery.handleError(s, xml, status);
	                    } catch (e) {
	                        status = "error";
	                        jQuery.handleError(s, xml, status, e);
	                    }

	                    // The request was completed
	                    if (s.global)
	                        jQuery.event.trigger("ajaxComplete", [xml, s]);

	                    // Handle the global AJAX counter
	                    if (s.global && !--jQuery.active)
	                        jQuery.event.trigger("ajaxStop");

	                    // Process result
	                    if (s.complete)
	                        s.complete(xml, status);

	                    jQuery(io).unbind()

	                    setTimeout(function() {
	                        try {
	                            jQuery(io).remove();
	                            jQuery(form).remove();

	                        } catch (e) {
	                            jQuery.handleError(s, xml, null, e);
	                        }

	                    }, 100)

	                    xml = null

	                }
	            }
	            // Timeout checker
	        if (s.timeout > 0) {
	            setTimeout(function() {
	                // Check to see if the request is still happening
	                if (!requestDone) uploadCallback("timeout");
	            }, s.timeout);
	        }
	        try {

	            var form = jQuery('#' + formId);
	            jQuery(form).attr('action', s.url);
	            jQuery(form).attr('method', 'POST');
	            jQuery(form).attr('target', frameId);
	            if (form.encoding) {
	                jQuery(form).attr('encoding', 'multipart/form-data');
	            } else {
	                jQuery(form).attr('enctype', 'multipart/form-data');
	            }
	            jQuery(form).submit();

	        } catch (e) {
	            jQuery.handleError(s, xml, null, e);
	        }

	        jQuery('#' + frameId).load(uploadCallback);
	        return {
	            abort: function() {}
	        };

	    },
	    handleError: function( s, xhr, status, e )      {  
	        // If a local callback was specified, fire it  
	                if ( s.error ) {  
	                    s.error.call( s.context || s, xhr, status, e );  
	                }  
	  
	                // Fire the global callback  
	                if ( s.global ) {  
	                    (s.context ? jQuery(s.context) : jQuery.event).trigger( "ajaxError", [xhr, s, e] );  
	                }  
	    },
	    uploadHttpData: function(r, type) {
	        var data = !type;
	        data = type == "xml" || data ? r.responseXML : r.responseText;
	        // If the type is "script", eval it in global context
	        if (type == "script")
	            jQuery.globalEval(data);
	        // Get the JavaScript object, if JSON is used.
	        if (type == "json")
	            eval("data = " + data);
	        // evaluate scripts within html
	        if (type == "html")
	            jQuery("<div>").html(data).evalScripts();

	        return data;
	    }
	})
})($)

var ajaxUpload = (function() {
    var ajaxUpload = function(cfg) {
        //私有属性
        this.el = $(cfg.el); //上传的input
        this.url = cfg.url; //上传路径
        this.withWH = cfg.withWH;//是否带宽高
        this.callbackFn = cfg.callbackFn; //回掉函数
        this.uploadWrap = this.el.parents(".ajaxupload-wrap");//当前上传的父元素
        this.image = this.uploadWrap.find(".ajaxupload-image");//显示出来的图片
        this.uploadText = this.uploadWrap.find(".ajaxupload-upload-text");//显示的文字
        this.uploadBtn = this.uploadWrap.find(".uploadfile");//上传按钮
        this.deleteBtn = this.uploadWrap.find(".ajaxupload-delete-btn");//删除按钮
        this.imageWrap = this.uploadWrap.find(".ajaxupload-image-wrap");//图片区域
        this.postImage = this.uploadWrap.find(".upload-image");//隐藏域
        this.init(); //初始化
    };

    ajaxUpload.prototype.init = function() {
        var _this = this;
        var uploadId = this.el.attr("id");
        this.imageWrap.removeClass("ajaxupload-hide").css({
        	"height":"130px",
        	"line-height":"130px"
        });
        this.image.attr("src",domainUrl+"/assets/common/loading.gif");
        _this.ajaxFileUpload(uploadId);

        // 点击删除
        this.deleteBtn.off("click").on("click",function (){
            _this.deleteFn();
        });
    };
    //返回函数
    ajaxUpload.prototype.setCallbackFn = function(callbackdata) {
    	this.deleteBtn.removeClass("ajaxupload-hide");
    	this.setCallbackData(callbackdata.path);
        this.imageWrap.removeClass("ajaxupload-hide").css({
            "height":"auto",
            "line-height":"auto"
        });
    };
    //设置返回的数据
    ajaxUpload.prototype.setCallbackData = function(imgurl){
        this.image.attr("src",imgurl);
        this.postImage.val(imgurl);
        this.uploadText.text(imgurl.length >0 ?"重新上传":"上传图片");
    }
    //点击删除
    ajaxUpload.prototype.deleteFn = function(){
        this.imageWrap.addClass("ajaxupload-hide");
        this.deleteBtn.addClass("ajaxupload-hide");
        this.setCallbackData("");
    }
    
    //上传
    ajaxUpload.prototype.ajaxFileUpload = function(uploadId) {
        var _this = this;
        $.ajaxFileUpload({
            fileElementId: uploadId,
            url: this.url,
            secureuri: false,
            data:{"withWH":_this.withWH,"width":10,"height":10},
            dataType: 'json',
            success: function(data, status) {
                _this.setCallbackFn(data);
                _this.callbackFn.call(this, status, _this.el, data);
            },
            error: function(data, status, e) {}
        });
    }

    return ajaxUpload;
})();
(function(){
	var delBtn = $(".ajaxupload-delete-btn");
	var parent = delBtn.parents(".ajaxupload-wrap")
	delBtn.off("click").on("click",function(){
		var img = parent.find('.ajaxupload-image-wrap');
		img.addClass("ajaxupload-hide");
		img.find("input").val('');
        $(this).addClass("ajaxupload-hide");
	});
	delBtn.find(".ajaxupload-image").addClass(".click-view");
})()
