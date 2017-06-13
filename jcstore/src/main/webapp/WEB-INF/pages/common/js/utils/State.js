/**
 * State 状态码
 * @author liujinpeng
 * @description 状态码 和 发送验证码
 */
define(function(require, exports, module) {
	var dialog = require("common/dialog");
	var state={
			getServerMsg:function getServerMsg(string){
				var arr1 = [0,1,10000,10001,10010,10011,10012,10013,11111,10020,10030,10040,10014,10015,10016,10086,10100,10087,10088,10089,10090,10091,10092,10093,10097,10098,10099,10500,10101,10102,10103,10110,10111,10112,10120,10121,10401,
				            10400,10300,10200,10125,101241,101251,101242,101252,10123,10122,10114,10115,10113,10402,20100,20101,20102,20103,20104,20105,20200,20401];
				var arr2 = ['失败','成功','登录验证失败','登录失败，用户名密码错误','用户名已被注册','手机号已被注册','验证码验证失败','手机号不存在','服务器忙','旧密码错误','用户已被冻结','信息包含违规内容','验证码超过次数','密码与旧密码相同','注册俩次密码输入不一致','数据不存在','参数错误','商品已下架','商品库存不足','收货地址不能为空','请获取旧手机验证码','请获取新手机验证码','请获取手机验证码','手机号必填','收货人不能为空','分组超过最大值','此商品已存在','已有相同名字的店铺,请修改','三方登录信息超时，请重新登录','图片验证码错误','该商品非上架商品，无法进行下架操作','订单状态不符，无法操作','订单已更新为其他状态，无法继续当前操作','订单维权未解决，无法继续当前操作','批量退款时，相同交易号的记录不能同时提交','已有记录已操作退款，不能重复退款',
				            '您的操作已过期，请先刷新页面','您的购物车已满，请先去购物车下单购买一些商品，再重新添加新商品','您结算的商品价值过高，请分批下单','收货地址超过10个不能添加','该账号已经被其他微博账号绑定，请先解绑微博或绑定至其他账号','该账号已绑定过微信账号，请先解绑对应的微信账号或绑定其他账号','该账号已绑定新浪微博账号，请先解绑对应的微博账号或绑定其他账号','当前微信号已绑定过贝壳湾账号，请先解绑对应的贝壳湾账号或绑定其他账号','当前微博账号已绑定过贝壳湾账号，请先解绑对应的贝壳湾账号或绑定其他账号','该账号已经被其他QQ账号绑定，请先解绑QQ或绑定至其他账号','待审核记录已发生变更，请刷新列表',
				            '当天订单取消数量超过5个，不允许主动取消订单','当前待支付订单超过5个，不允许继续下单','您的操作已过期，请先刷新页面','超过商品最大数量','收款账户超过上限','提现金额不能低于100元人民币','该申请状态异常，无法操作','提现密码错误','您还没有设置提现密码，提现功能暂不可用','收款账户已存在','该商品非上架商品，无法进行下架操作','验证码输入有误,请重新输入'];
				var num = contains(arr1,string);
				function contains(arr, obj) {  
					obj = +obj;
				    var i = arr.length;  
				    while (i--) {  
				        if (arr[i] === obj) {  
				            return i;  
				        }  
				    }  
				    return false;  
				}  
				return arr2[num];
			},
			account:function(string){
				var arr1 = [1,21,26,36,31]; 
				var arr2 = ['待审核','审核通过','审核驳回','打款失败','提现完成'];
				var num = contains(arr1,string);
				function contains(arr, obj) {  
					obj = +obj;
				    var i = arr.length;  
				    while (i--) {  
				        if (arr[i] === obj) {  
				            return i;  
				        }  
				    }  
				    return false;  
				}  
				return arr2[num];
			},
			account1:function(string){
				var arr1 = [1,21,26,31]; 
				var arr2 = ['待审核','审核成功','审核驳回','退款完成'];
				var num = contains(arr1,string);
				function contains(arr, obj) {  
					obj = +obj;
				    var i = arr.length;  
				    while (i--) {  
				        if (arr[i] === obj) {  
				            return i;  
				        }  
				    }  
				    return false;  
				}  
				return arr2[num];
			},
			code:function code(elem,msg,pos){
				var tag = elem.parent().next()[0].tagName.toLowerCase();
				var s=60;elem.prop('disabled','true');
				elem.addClass("disabled");
				var timer=setInterval(function(){
					s--
					var smsg = ''+s+'s';
					if(pos){
						elem.val(smsg);
					}else{
						elem.parent().next().html(smsg);
					}					
					if(s==0)
					{
						clearInterval(timer);
						if(tag == 'input'){
							elem.parent().next().prop('value','');
							
						}else{
							elem.parent().next().html('');
						}
						elem.prop("disabled",'false');
						elem.removeClass("disabled");
						elem.removeAttr("disabled")
						if(msg){
							elem.val(msg)
						}
						
						s=60;
					}
					
				},1000);
			}
	}
    module.exports = state;
});