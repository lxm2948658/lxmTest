define(function(require, exports, module) {
	var $ = require("jquery");
	var http = require("common/http");
	var listTable = require("dataTables");
	function deleflash(){
		var div=$(".flash_box")
		if(div.length>0){
			for(var i=0;i<div.length;i++){
				div.eq(i).remove()
			}
		}
	}
  	window.localPage = {};
	window.localCount = {};
	window.DataTableUrl = {};
	jQuery.extend({
		_setPage: function (settings){
			var pageCount;
			var currentPage;
			var url = '';
			var _s=2;
			
			//获取页数
			window.localCount[settings.sInstance] = Math.ceil(settings._iRecordsTotal/settings._iDisplayLength) || 1;
			extend = {
					draw:function(nowPage,Count){
						setTimeout(function(){
							deleflash()
							if(settings.aoData.length==0){
								if(nowPage!=1){
									alert('当前页没有数据，点击‘确定’返回首页！')
									// win.alert('<p class="balert-tips">当前页没有数据，点击‘确定’返回首页！</p>',null,function(){
									// 	extend.broadcast(1);
									// });
								}
							}	
						},0);
						var all =  settings._iRecordsTotal;
	        			var len =  settings._iDisplayLength;
	        			if(window.DataTableUrl[settings.sInstance] == settings.sAjaxSource){
	        				if(nowPage){
	    						currentPage = nowPage;
	    					}else{
	    						currentPage = Math.ceil(settings._iDisplayStart/len)+1;
	    					};
	        			}else{
	        				currentPage = 1;
	        				window.localPage[settings.sInstance] = 1;
	        				window.DataTableUrl[settings.sInstance] = settings.sAjaxSource;
	        			}

	        			
						if(Count){
							pageCount = Count;
						}else{
							pageCount = Math.ceil(all/len);
						}
						if(currentPage>pageCount){
							currentPage=1
						};
						if(pageCount<currentPage){
							pageCount=currentPage
						};
						var DTTTFooter = '<nav class="row DTTTFooter">';
						var showCount = 5 ;
						DTTTFooter += '<div class="col-sm-12">';
						DTTTFooter += '<span class="footer-tips">共'+settings._iRecordsTotal+'条,每页'+settings._iDisplayLength+'条</span>';
						DTTTFooter += '<ul class="pagination" id="searchable_paginate">';
						if(currentPage == 1){
							DTTTFooter += '<li class="paginate_button first disabled"><a class="disabled" id="searchable_first" href="javascript:void(0)">首页</a></li>';
						}else{
							DTTTFooter += '<li class="paginate_button first"><a id="searchable_first" href="javascript:void(0)">首页</a></li>';
							DTTTFooter += '<li class="paginate_button previous"><a id="searchable_previous" href="javascript:void(0)">上一页</a></li>';
						}
						
						if(currentPage < showCount){
							for(m=1;m<currentPage;m++){
								DTTTFooter += '<li class="paginate_button"><a href="javascript:void(0)">'+m+'</a></li>';
							}
							//DTTTFooter += '<a class="paginate_button">1</a>';
						}else{
							DTTTFooter += '<li class="paginate_button"><a href="javascript:void(0)">1</a></li>';
							DTTTFooter += '<li><span class="ellipsis">…</span></li>';
							DTTTFooter += '<li class="paginate_button"><a href="javascript:void(0)">'+eval(currentPage-1)+'</a></li>';
						}		
						DTTTFooter += '<li class="paginate_button current"><a class="current" href="javascript:void(0)">'+currentPage+'</a></li>';
						if(currentPage < (pageCount - showCount)){
							DTTTFooter += '<li class="paginate_button"><a href="javascript:void(0)">'+eval(currentPage+1) +'</a></li>';
							DTTTFooter += '<span class="ellipsis">…</span>';
							DTTTFooter += '<li class="paginate_button"><a href="javascript:void(0)">'+pageCount+'</a></li>';
						}else{
							for(n=1;n<=pageCount-currentPage;n++){
								DTTTFooter += '<li class="paginate_button"><a href="javascript:void(0)">'+ eval(currentPage + n) +'</a></li>';
							}
						}
						
						if(currentPage == pageCount){
							DTTTFooter += '<li class="paginate_button last disabled"><a class="disabled" id="searchable_last" href="javascript:void(0)">尾页</a></li>';	
						}else{
				    		DTTTFooter += '<li class="paginate_button next"><a id="searchable_next" href="javascript:void(0)">下一页</a></li>';
				    		DTTTFooter += '<li class="paginate_button last"><a id="searchable_last" href="javascript:void(0)">尾页</a></li>';	
						}
						DTTTFooter += '<li class="paginate_button input_jump"><input type="text" class="jumoPage"/></li>';
						DTTTFooter += '<li href="javascript:void(0)"><a id="jumpBtn" href="javascript:void(0)">跳转</a></li>';
						DTTTFooter += '</ul>';
						DTTTFooter += '</nav>';
						$(settings.nTable).next(".DTTTFooter").remove();
						$(DTTTFooter).insertAfter($(settings.nTable));
						return {
							currentPage:currentPage,
							pageCount:pageCount
						}
						
					},
					blind:function(){
						var idWrapper = "#"+settings.sInstance+"_wrapper";
						var tableWrap = $(idWrapper);
						var row = $("#searchBtn").parents(".row");
						tableWrap.on('click',".DTTTFooter a",function(e){
							var jjpage;
							$(tableWrap).parent('div').scrollTop(0)
							if($(this).hasClass("disabled")){return false;}
							if(e.target.id == "searchable_first"){
								jjpage = 1;
							}else if(e.target.id == 'searchable_last'){
								jjpage = window.localCount[settings.sInstance];
							}else if(e.target.id == 'searchable_next'){
								jjpage = eval(window.localPage[settings.sInstance] + 1);
							}else if(e.target.id == 'searchable_previous'){
								jjpage = eval(window.localPage[settings.sInstance] - 1);
							}else if(e.target.id == 'jumpBtn'){
								//页面中有多个表格的时候，跳转问题修改
								var p = parseInt($(e.target).parents(".pagination").find(".jumoPage").val());
								if(p){
									jjpage =p
								}else{
									return false;
								}
							}else if(parseInt(e.target.innerHTML)){
								jjpage = parseInt($(this).text());
							}else{
								win.alert('系统错误请联系管理员！');
							};
							extend.broadcast(jjpage);
						});
						tableWrap.on('keyup',".jumoPage",function(e){
							if(e.keyCode == "13"){
								extend.broadcast(parseInt($(this).val()));
							}
							var Number = $(this).val();
							Number = parseInt(Number);
							if(!Number){
								$(this).val('');
								return false;
							}
							if(Number < 0){
								Number = 0;
							}
							if(Number>window.localCount[settings.sInstance]){
								Number = window.localCount[settings.sInstance];
							}
							$(this).val(Number);
						});
						row.find("input").on('keyup',function(e){
							if(e.keyCode == "13"){
								extend.broadcast(1);
							}
						});
						$("#searchBtn").on('click',function(e){
							extend.broadcast(1);
						}).on("redraw",function(){
							row.find("input").val("");
							var select = row.find("select");
							for(i=0;i<select.length;i++){
								select[i].selectedIndex = 0;
							};
							extend.broadcast(1);
						});
						$("#searchClearBtn").on("click",function (e){
							$("#searchBtn").trigger("redraw")
						});
					},
					broadcast:function(_currentPage){
						window.localPage[settings.sInstance] = _currentPage;
						$.fn.dataTableExt.oApi._fnDraw(settings);
					}
			};
			return {
				pageCount:pageCount,
				extend:extend
			}
			
		}
	})

	module.exports = (function(){
	    var listTable = function(cfg){
	        this.el = $(cfg.el);
	        this.onGetData = cfg.onGetData;
	        this.url = cfg.url;
	        this.method = cfg.method || "get";
	        this.addAttr = cfg.addAttr || listTable.prototype.addAttr;
	        this.addAttrCopy = {};
	        this.twolist=cfg.twolist;
	        this.limit = cfg.limit || '20';
	        this.columns = cfg.columns||[];
	        this.data = cfg.data;
	        this.page = (function(boo){
	        	var boo2 = Boolean(boo);
	        	return boo2;
	        })(cfg.page);
	        
	        this.height = cfg.height;
	        this.init();
	    };
	    listTable.prototype.setCopy = function() {
	    	var _this = this;
	    	var mapData = (function(){
	    		  var json = {};
		          if(_this.addAttr){
		        	  return _this.addAttr(json);
		          }else{
		        	return json;  
		          }
		    })();
	    	this.addAttrCopy = mapData;
	    };
	    
	    listTable.prototype.draw = function(page,boo){
	    	var _this = this;
	    	deleflash()
	    	if(!boo){
	    		this.setCopy();
	    	}
	    	if(page){
	    		extend.broadcast(page);
	    	}else{
	    		this.el.fnDraw();
	    	}
	    	if(_this.twolist){
	    		this.el.fnDraw();
	    	}
	        return  ;
	        
	    }
	    
	    listTable.prototype.addAttr = function(json){
	        return json;
	    }

	    listTable.prototype.init = function() {
	        var _this = this;
	        this.setCopy();
	        $("#searchBtn").on('click',function(e){
	        	_this.setCopy();
	        });
	        var myData = {
		            "processing": true,
		            "serverSide": true,
		            "bServerSide":true,
		            "sAjaxSource":this.url,
		            "fnServerData" : function(sSource, aoData, fnCallback) {
		            	var mapData = _this.addAttrCopy;
		                mapData.showCount = aoData[3].value;
		                mapData.iDisplayStart = aoData[4].value;
		                mapData.iDisplayLength = aoData.iDisplayLength;
		                mapData.limit = _this.limit;
		                var myUrl = window.DataTableUrl[_this.el[0].id] ?window.DataTableUrl[_this.el[0].id]:sSource
		                // 页面跳转
		                var _currentPage = window.localPage[_this.el[0].id] ? window.localPage[_this.el[0].id] : 1;
		                if(_currentPage && _currentPage>0){
		                    mapData.currentPage = _currentPage;
		                }else{
		                    mapData.currentPage = (mapData.iDisplayStart/mapData.iDisplayLength)+1;
		                }
		                if(_this.data){
		                	(function(resp){
		                		var data = {
		                				"iTotalDisplayRecords":20,
		                				"status":1,
		                				"iTotalRecords":20,
		                				"statusDes":"SUCCESS",
		                				"statusMsg":"OK",
		                				"sEcho":""
		                			}
		                		data.aaData = resp;
		                		data.iTotalDisplayRecords = data.iTotalRecords;
		                        fnCallback(data);
							})(_this.data);
		                }else{
			                http.ajax({
			                    "type":_this.method,
			                    "url" : myUrl,
			                    "dataType" : "json",
			                    "data":mapData,
			                    "success" : function(resp) {
			                    	if(_this.onGetData){
			                    		_this.onGetData(resp);
			                    	}
			                        resp.iTotalDisplayRecords = resp.iTotalRecords;
			                        fnCallback(resp);
			                    }
			                });		
		                }
		            },
		            "searching": false,
		            "ordering": false,
		            "aLength":false,
		            "bAutoWidth": false,
		            "sDom": "Tft<'row DTTTFooter'<'col-sm-6'i><'col-sm-6'p>>",
		            "paging":   false,
		            "columns": this.columns,
		            "aaSorting": [],
		            "iDisplayLength": this.limit,
		            "iDisplayStart":0,
		            "sLengthMenu": "_MENU_",
		            "oTableTools": {
		                "aButtons": [],
		                "sSwfPath": ""
		            },
		            "language": {
		                "sInfo": "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
		                "sInfoEmpty": "没有记录",
		                "search": "",
		                "sLengthMenu": "",
		                "oPaginate": {
		                    "sFirst": "首页",
		                    "sPrevious": "上一页",
		                    "sNext": "下一页",
		                    "sLast": "尾页"
		                }
		            },
		            "pagingType":"full_numbers",
		            "drawCallback": function( settings ) {
		            	var curpage = window.localPage[settings.sInstance] ? window.localPage[settings.sInstance] : 1;
		            	jQuery._setPage(settings).extend.draw(curpage);
		            	if(!_this.page){
		    	        	_this.el.next(".DTTTFooter").hide();
		    	        }
		                
		            },
		            "initComplete":function(settings){
		            	if(!_this.page){
		    	        	_this.el.next(".DTTTFooter").hide();
		    	        }else{
		    	        	jQuery._setPage(settings).extend.blind();
		    	        }
		            }
		        };
	        if(this.height){
	        	myData.scrollY = this.height;
	        	myData.scrollCollapse=true;
	        }
	        this.el.dataTable(myData);
	        
	    };

	    return listTable;
	})()

});
