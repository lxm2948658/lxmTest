/**
 * Handlebars  && it's config
 * author daijinma
 * time 2016-2-29
 * Handlebars.js已被设计为在任何ECMAScript 3的环境下工作
 *   Chrome
 *   Firefox
 *   Safari 5+
 *   Opera 11+
 *   IE 6+
 * */
define(function(require, exports, module) {
	var Handlebars = require("handlebars");
	var filter = require("utils/data.filter");
	
	//维权状态
	var refund = {};
	refund['refundStatus'] = ['待商家处理','待买家处理','待商家确认','平台介入','维权结束','无','','已退款'];
	refund['refundType'] = ['仅退款','退货退款'];
	/*维权结果*/
	refund['refundResult'] = ['维权中','买家关闭','商家发货','平台驳回','已退款','已退货退款'];
	/**维权记录*/
	refund['result'] = ['维权中','买家关闭','商家发货','平台驳回','已退款','已退货退款'];
	refund['orderStatus'] = ['待付款','待发货','已发货','已完成','关闭','关闭','关闭'];
	refund['refundHandleStatus'] = ['无','商家同意','商家驳回','平台同意','平台驳回'];
	refund['platformRequester'] = ['无','卖家','买家'];
	refund['receiveType'] = ['未收到货','已收到货'];
	 
	refund['role'] = ['平台','卖家','买家',]
	refund['paytype'] = ['—','微信支付','支付宝支付'];
	/*全部订单*/
	refund['status'] = ['待付款','待发货','','已发货','','','','已完成','已关闭','已关闭','','已关闭'];
	/*维权订单*/
	refund['wqstatus'] = ['待商家处理','待买家处理','待商家确认','平台介入','维权结束','—','申请维权','已退款'];
	/*维权结果*/
	refund['result'] = ['维权中','买家关闭','商家发货','平台驳回','已退款','已退货退款'];
	refund['operationStatus'] = ['买家发起维权申请','买家修改维权申请','商家驳回维权,等待买家处理','商家接受维权申请,等待买家退货',
	                             '平台接受维权申请','买家已退货','买家申请平台介入','商家申请平台介入','买家修改物流信息',
	                             '买家关闭维权','商家发货','商家接受维权申请','系统关闭订单','平台驳回维权申请','商家确认收货'];
	/*关闭原因*/
	refund['gbstatus'] = ['','超时未付款关闭','买家关闭','商家关闭','维权结束关闭','系统关闭','支付异常'];
	/*前台维权订单*/
	refund['orderStatus2'] = ['待付款','待发货','','待收货','','','','已完成','已关闭','已关闭','','已关闭'];
	
	/*关闭原因*/
	refund['closeResult'] = ['','超时未付款关闭','买家关闭','商家关闭','维权结束关闭','系统关闭','支付异常'];
	
	var refundService = function(type,id){
		return refund[type][id];
	};
	// {{refundService 'operationStatus' aaaS }}
	/**
	 * 分转化 元
	 * param String/number
	 * return 00.00
	 * */	
	Handlebars.registerHelper('yuan', function(fen){
		return filter.Yuan(fen);
	});
	
	/**
	 * 维权状态码转化文字
	 * param 维权状态类型 refund
	 * param 维权状态码  id
	 * return 状态文字
	 * */	
	Handlebars.registerHelper('refundService', function(refund,id){
		return refundService(refund,id);
	});
	
	/**
	 * 增加判断 替代 if
	 * {{#ex "{{state}}==='submiting'"}} //
	 *      <i class="icon cross-danger">1</i>
	 * {{else}}
	 *      <i class="icon cross-success">2</i>
	 * {{/ex}}
	 * --------------------------
	 * {{#ex "{{shop.name}}==='刘建星小铺'"}}
	 * 	1
	 * {{else}}
	 *  	2
	 * {{/ex}}
	 * */	
	Handlebars.registerHelper('ex', function(str, options) {
	    var reg = /\{\{.*?\}\}/g;
	    var result = false;
	    var variables = str.match(reg);
	    var context = this;
	    if(/@/g.test(str)){
	    	_variables = variables[0].replace("{{@",'')
	    	_variables = _variables.replace("}}",'')
	    	str = str.replace(variables, options.data[_variables]);
	    }else{
		    $.each(variables, function(i,v){
		      var key = v.replace(/{{|}}/g,"");
		      var arr = key.split(".");
		      var forThis = context;
		      var value = '';
		      for(i=0,len=arr.length;i<len;i++){
		    	  var key = arr[i];
		    	  var obj = forThis[key];
		    	  var type = (typeof obj);
		    	  if(!(type=='object')){
		    		  value = '"'+obj+'"';
		    		  break;
		    	  }else{
		    		  forThis = obj;
		    	  }
		      }
		      str = str.replace(v, value);
		    });	
	    };
	    
	    try{
	      result = eval(str);
	      if (result) {
	        return options.fn(this);
	      } else {
	        return options.inverse(this);
	      }
	    }catch(e){
	      console.log(str,'--Handlerbars Helper "ex" deal with wrong expression!');
	      return options.inverse(this);
	    }
	});
	/**
	 * debug helper
	 * param Value
	 * */	
	Handlebars.registerHelper("debug", function(optionalValue) {  
		console.log("Current Context");
		console.log("====================");
		console.log(this);
		if (optionalValue) {
			console.log("Value");
			console.log("====================");
			console.log(optionalValue);
		}
	});
	/**
	 * debug helper
	 * param Value
	 * */	
	
	
	
	
	
	// 注册一个比较大小的Helper,判断v1是否大于v2 
	// {{#compare a b}} {{a}} {{else}} {{b}} {{/compare}}
	Handlebars.registerHelper("compare",function(v1,v2,string,options){
	  if(eval(''+v1+string+''+v2)){
	    //满足添加继续执行
	    return options.fn(this);
	  }else{
	    //不满足条件执行{{else}}部分
	    return options.inverse(this);
	  }
	});
	//html转义方法 用法如下：
	//{{{htmlEscape content}}}
	Handlebars.registerHelper('htmlEscape', function(data){
		var htmlData = "";
		var arrEntities = {'<':'&lt','>':'&gt','&':'&amp','"':'&quot','\n':'<br/>','\r':'<br/>','\r\n':'<br/>'};
		return data.replace(/(<|>|&|\"|\n|\r|\r\n)/igm,function(all,t){
			return arrEntities[t];
		});
	});
	Handlebars.registerHelper("formatDate", function(time) { 
		return time.substr(0,10);
	});
	
	Handlebars.registerHelper("add", function(a,b) { 
		return ;
	});
	
    module.exports = Handlebars;
});