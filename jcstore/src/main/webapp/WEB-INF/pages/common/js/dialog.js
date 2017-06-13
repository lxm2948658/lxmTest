//dialog2
define(function(require, exports, module) {
	var layer = require("layer");
	require("layercss");
	var dialogConfig = {
		"liteTime":3000
	};

	var getData = function(url) {
       var data={};
       var arr = url.split("&");
       for(i=0,len=arr.length;i<len;i++){
       		b = arr[i].split("=");
       		data[b[0]]=b[1];
       }
       return data;
    }
	var dialog = (function(){
		var dialog = function(cfg){
	　　　	return new dialog.prototype.init(cfg);
	　　 };
		
		dialog.prototype = {
	　　　	init : function(cfg){
	　　　		this.cfg = cfg;
				this.title = cfg.title || "系统提示";
				this.isValidForm = cfg.isValidForm,//dialog中有表单验证
				this.ele = cfg.ele;
				this.name = cfg.name
				this.content = cfg.content || $(this.ele).html();
				this.width = cfg.width || 500;
				this.height = cfg.height || 252;
				this.btn = [];
				this.btnFn = [];
				for(var key in cfg.button){
					this.btn.push(key);
					this.btnFn.push(cfg.button[key]);
				}
	　　　　　　	return this;
	　　　　	},
			open:function(fn) {
				var _this = this;
				this.dialogID = layer.open({
					type: 0,
				    title: this.title, //不显示标题
				    area: [this.width+'px', this.height+'px'],//显示宽高
				    content: this.content, //捕获的元素
				    skin:this.name,
				    shade: 0.5, //遮罩透明度
				    moveType: 1, //拖拽风格，0是默认，1是传统拖动
				    shift: 5, //0-6的动画形式，-1不开启
				    btn:this.btn,
				    closeBtn : [0 , false],
				    yes:function(){
				    	var okFn = _this.btnFn[0];
						var isValidForm = _this.isValidForm
						var data = {},
							formData = $(_this.ele).find("form");
						if (formData.length > 0) {
							data = getData(formData.serializeJson());
						}
						//判断是否有验证
						if(!isValidForm){
							okFn && okFn.call(_this, data);
						}
						
						if(isValidForm &&(okFn&&!okFn())){
							return;
						}
						
						layer.close(_this.dialogID);
				    },
				    cancel: function(index){
				    	var cancelFn = _this.btnFn[1];
				    	cancelFn && cancelFn.call(_this);
				    }
				});
				
				
				var heihgt2 = 0;
				var titlewrap = $(".layui-layer-title");
				if($.trim(this.title).length == 0 && this.cfg.newskin){
					titlewrap.css("display","none");
				}else{
					heihgt2 = titlewrap.height()+30;
				}
				
				var heihgt3 = 0;
				var btnwrap = $(".layui-layer-btn");
				if(this.btn.length == 0 && this.cfg.newskin){
					btnwrap.css("display","none");
				}else{
					heihgt3 = btnwrap.height()+60
				}
				
				$("#layui-layer"+this.dialogID+" .layui-layer-content").css("height",(this.height-heihgt2-heihgt3));
				//是否显示关闭按钮
				if(this.cfg.showCloseBtn){
					$(".layui-layer-setwin").css("display","block");
				}
				fn && fn();
				return this;
			},
			close : function(fn){
				layer.close(this.dialogID);
				if(fn && typeof fn ==="function"){fn()};
				return false;
			},
			option : function(key,value){
				var _this = this;
				if(!value){
					return _this[key];
				}else{
					_this[key] = value;
				}
			}
	　　	};
		dialog.prototype.init.prototype = dialog.prototype;
		return dialog;
	})();
	

	module.exports =  {
      "alert":function(msg,title,fn,width,height){
    	  var alert = dialog({
			title:title||"系统消息",
			content:msg,
			width:width || 378,
			height:height || 252,
			name:"text-center",
			button:{
				"确定":function(){
					alert.close();
					if(typeof fn == "string"){
						window.location.href=domainUrl+fn;
					}else if(typeof fn == "function"){
						fn()
					}else if(fn==null){
						return  false;
					}
					
				}
			}
		});
    	  alert.open();
      },
      "confirm":function(msg,title,fnEnsure,fnCancel){
    	  var confirm = dialog({
  			title:title||"系统消息",
  			content:msg,
  			name:"text-center save",
  			button:{
  				"确定":function(){
  					confirm.close();
  					if(fnEnsure){fnEnsure();}
  				},
  				"取消":function(){
  					confirm.close();
  					if(fnCancel){fnCancel();}
  				}
  			}
  		});
    	  confirm.open();
      },
      "lite":function(msg,url,time){
    	var _time = time || dialogConfig.liteTime;
      	new layer.msg(msg, {
      		shade: 0.01, //遮罩透明度
	        time:_time //20s后自动关闭
	    });
      	if(url){
      		setTimeout(function(){
      			window.location.href = domainUrl+url;
      		},_time);
      	}
      },
      "loading":function(msg,time){
    	  var load = layer.load(0, {shade: false});
    	  return load;
       },
       "close":function(id){
    	   layer.close(id);
       },
      "dialog":(function(){
      	return dialog;
      })(),
      "layer":(function(){
      	return layer;
      })()
   };;
});