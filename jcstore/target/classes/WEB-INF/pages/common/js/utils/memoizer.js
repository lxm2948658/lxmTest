/**
 * memoizer 记忆
 * @author daijnma
 * @description 记忆键值对，对之前已有处理过的值，直接返回结果
 */
(function (global, factory) {
	if (typeof define === 'function' && define.cmd) {
		// CommonJS. Register as a module
		define(function(require, exports, module) {
			module.exports = factory(); 
		});
	} else {
		// Browser globals
		factory();
	}
}(this, function () { 
	data = {};
	memoizer = function(){
		return new memoizer.prototype.init();
	};
	memoizer.prototype.init=function(){
		return this;
	};
	/* 获取记忆{json}中的值{key}
	 * 
	 * @param String{json} 标记
	 * @param String{key}  值
	 * */
	memoizer.prototype.get=function(json,key){
		if(data[json]){
			return data[json]["k"+key];
		}else{
			return data[json]
		}
	};
	/* 储存记忆{json}中的值{key}的结果{value}
	 * 
	 * @param String{json} 标记
	 * @param String{key}  值
	 * @param String{value}结果
	 * */
	memoizer.prototype.set=function(json,key,value){
		data[json] = data[json] || {};
		data[json]["k"+key] = value;
	}
	memoizer.prototype.init.prototype = memoizer.prototype;
	return memoizer;
}));