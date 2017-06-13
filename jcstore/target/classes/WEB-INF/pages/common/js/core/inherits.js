/**
 * inherits 继承
 * @author daijnma
 * @description 继承函数
 */
define(function(require, exports, module) {
	
	if(typeof Object.create!== 'function'){
        Object.create = function(o){
            var F = function(){};
            F.prototype = o;
            return new F();
       };
	}

    module.exports = function(ctor, superCtor){
    	if (ctor === undefined || ctor === null)
    	    throw new TypeError('The constructor to "inherits" must not be ' +
    	                        'null or undefined');

	  if (superCtor === undefined || superCtor === null)
	    throw new TypeError('The super constructor to "inherits" must not ' +
	                        'be null or undefined');

	  if (superCtor.prototype === undefined)
	    throw new TypeError('The super constructor to "inherits" must ' +
	                        'have a prototype');
	  /*
	   * 原型链链接
	   * */	  
	  ctor.prototype = Object.create(superCtor.prototype, {
	    constructor: {
	      value: ctor,
	      enumerable: false,
	      writable: true,
	      configurable: true
	    }
	  });
	  
    };
});