/**
 * Service protype for http
 * @author daijnma 2016/01/25
 * @description http 接口获取数据类 原型
 */
define(function(require, exports, module) {
	
	var events = require("core/EventEmitter2").EventEmitter2;
	var inherits = require("core/inherits");
	var http = require("common/http");
	var serverMsg = require("utils/State").getServerMsg;
	
    function Service(){
    	this.action="data";
    	this.async = true;
    };
    
    inherits(Service, events);
    
    /**
     * send 方法，发送http
     * @param json
     * @return promise 对象
     * */
    Service.prototype.send = function(callback){
    	var _this = this;
    	var method = this.method;
    	
    	var postData = {
    		url:domainUrl+this.path,
    		method:this.method,
    		async:this.async,
    		dataType:"json"
    	};
    	if(this.data){
    		if(/GET|get/.test(method)){
    			this.data['_t'] = $.now();
    		}
    		postData.data = this.data;
    	};
    	http.ajax(postData)
		.then(function(data){
			var result = data;
			_nowTime = data.nowTime;
			if(callback){
				callback(result);
			};
			if(result.status && (!result.statusMessage)){
				var message = serverMsg(result.status);
				if(message){
					result.statusMessage = message;
				}else{
					console.warn("no current code in utils/State")
				}
			}
			_this.emit(_this.action,result);
		});
		return this;
    };
    /**
     * ready 方法 用于回调
     * @param 回调  function
     * @return null
     * */
    Service.prototype.ready = function(callback){
    	this.on(this.action,function(data){
    		callback(data);
    	});
    }
    
    /**
     * ready 方法 用于回调
     * @param 回调  function
     * @return null
     * */
    Service.prototype.filter = function(action,boo){
    	if(action){
    		this.data = action(this.data);
    	}
    	if(boo){
	    	for(i in this.data){
	    		if((!this.data[i]) || (this.data[i]===null) || (this.data[i]===undefined)){
	    			delete this.data[i]
	    		};
	    	};
    	}
    }
	//    service 原型 增加内置 过滤 方法 filter
	//    service.filter(null,true): 
	//    第一个参数 接受 过滤data的回调
	//    第二个参数 传boolean值，是否过滤空项
    // 过滤  ‘’  null   undefined

    
    module.exports = Service;
});