/**
* 监听滚动 并ajax请求 回填数据
* parma start 开始页数
* parma limit 每次请求条数
* parma api  请求地址
* parma param  附加请求参数 json格式
* parma onPage  每次请求成功回调
* parma beforePage  发起请求之前
* parma onComplete  页面全部加载完
* parma bottom  距离底端XX触发
* parma scroll  值为1无限加载第一页，测试用
* return null
**/
function webScroll(cfg){
		var currentPage = cfg.start||1;
		var limit = cfg.limit||10;
		var api = cfg.api;
		var _param = cfg.param||{};
		var onPage = cfg.onPage;
		var _bottom = cfg.bottom||0;
		var _scroll = cfg.scroll||0;
		var beforePage = cfg.beforePage;
		var onComplete = cfg.onComplete;
		var stop=true;
		var queryTime= new Date().getTime();
		
		var theTemplate = cfg.template;
	    
	    $("#Loading").parent().html("<div id='Loading'></div>");
	    $(window).off("scroll");
		$(window).on("scroll",function(){
		    totalheight = parseFloat($(window).height()) + parseFloat($(window).scrollTop());
		    var boo = ((($(document).height()-_bottom) <= totalheight) && stop==true);
		    if(boo && currentPage>1){
	            stop=false;
	        	_action(_param);
		    }
		});
		
		$(window).off("toScroll").on("toScroll",function(e,param){
	    	_action(param);
		});
		
		function _action(param){
            var postData = {
            	currentPage:currentPage,
            	limit:limit,
            	queryTime:queryTime
        	};
            for(i in param){
            	postData[i] = param[i];
            }
            var lastDate = $.extend({}, postData,param);
            beforepageLoad()
	    	 $.get(
	            api,
	            lastDate,
	        	function(data){
	            	console.log(theTemplate(data.aaData))
	        		var fullPage = Math.ceil(data.iTotalRecords/limit);
	                $("#Loading").before(theTemplate(data.aaData));
	                stop=true;
	                pageLoad();
	                if(_scroll<=0){
		                if(currentPage>=fullPage){
		                	pageLoadComplete(currentPage);
		                }else{
		                	currentPage++;
		                }
	                }
	            },
	            "json"
	        );
	    };
	    function beforepageLoad(){
	    	console.log("beforepageLoad");
	    	if(beforePage){beforePage()};
	    };
	    function pageLoad(){
	    	console.log("pageLoad："+currentPage);
	    	if(onPage){onPage(currentPage)};
	    };
	    function pageLoadComplete(currentPage){
	    	console.log("all complete");
	    	if(onComplete){onComplete(currentPage)}
	    	$(window).off("scroll");
	    };
	    $(window).trigger("toScroll",[_param]);
	};