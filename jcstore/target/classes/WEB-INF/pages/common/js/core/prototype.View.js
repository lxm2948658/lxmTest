/**
 * View protype with emitter && handlebars
 * @author daijnma 2016/03/5
 * @description 视图原型
 */
define(function(require, exports, module) {
	
	var events = require("core/EventEmitter2").EventEmitter2;
	var inherits = require("core/inherits");
	
	
    function View(param){
    	born(param.beforeBorn,param.onBorn,param.afterBorn);
    	create.apply(this,[param.beforeCreate,param.onCreate,param.afterCreate]);
    	this.on("render",function(){
    		render(param.beforeRender,param.onRender,param.afterRender);
    	})
    	return this;
    };
    
    /**
     * 继承EventEmitter2的原型
     * */
    inherits(View, events);
    
    /**
     * View 生命周期 初生
     * @param json
     * @return promise 对象
     * @description 此阶段编译完模板
     * */
    function born(fn1,fn2,fn3){
    	if(typeof fn1 === 'function'){  fn1() };
    	if(typeof fn2 === 'function'){  fn2() };
    	if(typeof fn3 === 'function'){  fn3() };
    }
    
    /**
     * View 生命周期 创建完成
     * @param json
     * @return promise 对象
     * @description 此阶段已编译完模板，已准备好数据
     * */
    function create(fn1,fn2,fn3){
    	var _this = this;
    	if(typeof fn1 === 'function'){  fn1() };
    	if(typeof fn2 === 'function'){  fn2() };
    	this.on("createData",function(data){
    		if(typeof fn3 === 'function'){  fn3(data) };
    		this.emit("render");
    	})
    }
    
    /**
     * View 生命周期 渲染完成
     * @param function 回调
     * @return null
     * @description 此阶段已编译完模板，已准备好数据
     * */
    function render(fn1,fn2,fn3){
    	if(typeof fn1 === 'function'){  fn1() };
    	if(typeof fn2 === 'function'){  fn2() };
    	if(typeof fn3 === 'function'){  fn3() };
    }
    
    
    /**
     * View 重绘
     * @param param 重绘修改配置00
     * @param function 回调
     * @return null
     * @description 页面重绘阶段 
     * */
    View.prototype.draw = function (fn){
    	if(typeof fn === 'function'){ return fn() };
    }
    
    /**
     * View 生命周期 销毁完成
     * @param json
     * @return promise 对象
     * */
    View.prototype.destroy = function (fn){
    	//this.beforeDestroy();
    	if(typeof fn === 'function'){ return fn() };
    }
    
    module.exports = View;
});