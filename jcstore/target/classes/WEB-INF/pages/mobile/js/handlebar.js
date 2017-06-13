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
	// {{refundService 'operationStatus' aaaS }}
	/**
	 * 分转化 元
	 * param String/number
	 * return 00.00
	 * */	
	var filter = {
		_moneyLimit:function(cent){
        	return cent.toFixed(2)
		}	
	}

	Handlebars.registerHelper('yuan', function(fen){
		return filter.Yuan(fen);
	});
	
	Handlebars.registerHelper('_moneyLimit',function(cent){
    	return filter._moneyLimit(cent);
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
