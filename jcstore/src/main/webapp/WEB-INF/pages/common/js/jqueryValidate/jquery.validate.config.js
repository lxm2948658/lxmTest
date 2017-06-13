//index
define(function(require, exports, module) {
	var memoizer = require("utils/memoizer")();
	/*-------------验证插件配置-------------*/
	// 配置错误提示的节点，默认为label，这里配置成 span （errorElement:'span'）
	module.exports.config = function($) {
		$.validator
				.setDefaults({
					errorElement : 'span',
					ignore : ".ignore",
					errorPlacement : function(error, element) {
						// console.log(error);
						var vlidWrap = element.parents(".form-group").find(
								".valid-wrap");
						if (vlidWrap.length > 0) {
							$(vlidWrap).html(error);
						} else {
							error.appendTo(element.parent());
						}
					}
				});
		// 二、默认校验规则
		// (1)required:true 必输字段
		// (2)remote:"check.php" 使用ajax方法调用check.php验证输入值
		// (3)email:true 必须输入正确格式的电子邮件
		// (4)url:true 必须输入正确格式的网址
		// (5)date:true 必须输入正确格式的日期 日期校验ie6出错，慎用
		// (6)dateISO:true 必须输入正确格式的日期(ISO)，例如：2009-06-23，1998/01/22
		// 只验证格式，不验证有效性
		// (7)number:true 必须输入合法的数字(负数，小数)
		// (8)digits:true 必须输入整数
		// (9)creditcard: 必须输入合法的信用卡号
		// (10)equalTo:"#field" 输入值必须和#field相同
		// (11)accept: 输入拥有合法后缀名的字符串（上传文件的后缀）
		// (12)maxlength:5 输入长度最多是5的字符串(汉字算一个字符)
		// (13)minlength:10 输入长度最小是10的字符串(汉字算一个字符)
		// (14)rangelength:[5,10] 输入长度必须介于 5 和 10 之间的字符串")(汉字算一个字符)
		// (15)range:[5,10] 输入值必须介于 5 和 10 之间
		// (16)max:5 输入值不能大于5
		// (17)min:10 输入值不能小于10

		// API
		// $("#signupForm").validate({
		// debug:true //如果这个参数为true，那么表单不会提交，只进行检查，调试时十分方便
		// });
		// $.validator.setDefaults({
		// debug: true
		// })
		// 
		// validator.resetForm() //重置
		// validator.valid() //触发
		//

		// 配置通用的默认提示语
		$.extend($.validator.messages, {
			required : "必填",
			remote : "请修正该字段",
			email : "请输入正确格式的电子邮件",
			url : "请输入合法的网址",
			date : "请输入合法的日期",
			dateISO : "请输入合法的日期 (ISO).",
			number : "请输入合法的数字",
			digits : "只能输入整数",
			creditcard : "请输入合法的信用卡号",
			equalTo : "请再次输入相同的值",
			accept : "请输入拥有合法后缀名的字符串"
		// maxlength: jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
		// minlength: jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
		// rangelength: jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
		// range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
		// max: jQuery.validator.format("请输入一个最大为{0} 的值"),
		// min: jQuery.validator.format("请输入一个最小为{0} 的值")
		});

		/*-------------扩展验证规则 -------------*/
		// 邮箱
		$.validator.addMethod("mail", function(value, element) {
			var mail = /^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$/;
			return this.optional(element) || (mail.test(value));
		}, "邮箱格式不对");

		// 电话验证规则
		$.validator.addMethod("phone", function(value, element) {
			var phone = /^0\d{2,3}-\d{7,8}$/;
			return this.optional(element) || (phone.test(value));
		}, "电话格式如：0371-68787027");

		// 区号验证规则
		$.validator.addMethod("ac", function(value, element) {
			var ac = /^0\d{2,3}$/;
			return this.optional(element) || (ac.test(value));
		}, "区号如：010或0371");

		// 无区号电话验证规则
		$.validator.addMethod("noactel", function(value, element) {
			var noactel = /^\d{7,8}$/;
			return this.optional(element) || (noactel.test(value));
		}, "电话格式如：68787027");

		// 手机验证规则
		$.validator.addMethod("mobile", function(value, element) {
			var mobile = /^1\d{10}$/;
			return this.optional(element) || (mobile.test(value));
		}, "手机格式不对");

		// 邮箱或手机验证规则
		$.validator
				.addMethod(
						"mm",
						function(value, element) {
							var mm = /^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/;
							return this.optional(element) || (mm.test(value));
						}, "格式不对");

		// 电话或手机验证规则
		$.validator
				.addMethod(
						"tm",
						function(value, element) {
							var tm = /(^1[3|4|5|7|8]\d{9}$)|(^\d{3,4}-\d{7,8}$)|(^\d{7,8}$)|(^\d{3,4}-\d{7,8}-\d{1,4}$)|(^\d{7,8}-\d{1,4}$)/;
							return this.optional(element) || (tm.test(value));
						}, "格式不对");

		// 年龄
		$.validator.addMethod("age", function(value, element) {
			var age = /^(?:[1-9][0-9]?|1[01][0-9]|120)$/;
			return this.optional(element) || (age.test(value));
		}, "不能超过120岁");
		// /// 20-60 /^([2-5]\d)|60$/

		// 传真
		$.validator.addMethod("fax", function(value, element) {
			var fax = /^(\d{3,4})?[-]?\d{7,8}$/;
			return this.optional(element) || (fax.test(value));
		}, "传真格式如：0371-68787027");

		$.validator.addMethod("FINES", function(value, element) {
			var reg = /^[0-9]*[0-9][0-9]*$/;
			var boll = false;
			function checkNum() {
				if (reg.test(value) && value <= 500) {
					boll = true
				} else {
					boll = false
				}
				return boll;
			}
			return this.optional(element) || checkNum();
		}, "罚款金额必须是数字，不支持小数点，金额不得超过500");
		// 验证当前值和目标val的值相等 相等返回为 false
		$.validator.addMethod("equalTo2", function(value, element) {
			var returnVal = true;
			var id = $(element).attr("data-rule-equalto2");
			var targetVal = $(id).val();
			if (value === targetVal) {
				returnVal = false;
			}
			return returnVal;
		}, "不能和原始密码相同");

		// 大于指定数
		$.validator.addMethod("gt", function(value, element) {
			var returnVal = false;
			var gt = $(element).data("gt");
			if (value > gt && value != "") {
				returnVal = true;
			}
			return returnVal;
		}, "不能小于0 或空");

		// 汉字
		$.validator.addMethod("chinese", function(value, element) {
			var chinese = /^[\u4E00-\u9FFF]+$/;
			return this.optional(element) || (chinese.test(value));
		}, "格式不对");

		// 指定数字的整数倍
		$.validator.addMethod("times", function(value, element) {
			var returnVal = true;
			var base = $(element).attr('data-rule-times');
			if (value % base != 0) {
				returnVal = false;
			}
			return returnVal;
		}, "必须是发布赏金的整数倍");
		// 身份证
		$.validator.addMethod("companyName", function(value, element) {
			var isIDCard1 = /^[\u4e00-\u9fa5a-zA-Z0-9()（）]{1,30}$/;
			return this.optional(element) || (isIDCard1.test(value));
		}, "企业名称不合法");
		$.validator.addMethod("enterpriser", function(value, element) {
			var isIDCard1 = /^[\u4e00-\u9fa5a-zA-Z]{1,20}$/;
			return this.optional(element) || (isIDCard1.test(value));
		}, "支持输入汉字或英文，长度不超过20个字");
		$.validator.addMethod("telnumber", function(value, element) {
			var isIDCard1 = /^[0-9\-]{1,15}$/;
			return this.optional(element) || (isIDCard1.test(value));
		}, "支持输入阿拉伯数字及符号—，不超过15个字符");

		// 身份证
		$.validator
				.addMethod(
						"idCard",
						function(value, element) {
							var isIDCard1 = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;// (15位)
							var isIDCard2 = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;// (18位)

							return this.optional(element)
									|| (isIDCard1.test(value))
									|| (isIDCard2.test(value));
						}, "格式不对");

		// 企业全称
		$.validator.addMethod("company", function(value, element) {
			var mail = /^[\u4e00-\u9fa5a-zA-Z0-9()]{1,30}$/;
			return this.optional(element) || (mail.test(value));
		}, "支持输入汉子、英文、阿拉伯数字或（），长度不超过30个字");
		
		$.validator.addMethod("freightname", function(value, element) {
			var mail = /^[\u4e00-\u9fa5a-zA-Z0-9]{1,15}$/;
			return this.optional(element) || (mail.test(value));
		}, "支持输入汉子、英文、阿拉伯数字，长度不超过15个字");
		// 验证前台手机号是否注册
		$.validator.addMethod("getFrontMobile", function(value, element) {
			var flag = 1;
			var memoizerValue = memoizer.get("getdealerMobile", value);
			if (memoizerValue) {
				return true
			}
			$.ajax({
				type : "GET",
				url : domainUrl + '/mobile/check',
				async : false, // 同步方法，如果用异步的话，flag永远为1
				data : {
					'mobile' : value
				},
				dataType : "json",
				success : function(msg) {
					if (msg.status != '10010') {
						flag = 0;
					}
				}
			});
			if (flag == 0) {
				memoizer.set("getdealerMobile", value, false);
				return false;
			} else {
				memoizer.set("getdealerMobile", value, true);
				return true;
			}

		}, "手机号未被注册")
		
		$.validator.addMethod("checkuserNumber", function(value, element) {
			var mail = /(^[5-9]$)|(^[1-9][0-9]+$)/;
			return this.optional(element) || (mail.test(value));
		}, "请输入5以上正整数");
		
		$.validator.addMethod("checkmonths", function(value, element) {
			var mail = /^[1-9][0-9]*$/;
			return this.optional(element) || (mail.test(value));
		}, "请输入1以上正整数");
		
		$.validator.addMethod("checkupmonths", function(value, element) {
			var mail = /^[0-9][0-9]*$/;
			return this.optional(element) || (mail.test(value));
		}, "请输入0以上正整数");
		
		$.validator.addMethod("checkupNumber", function(value, element) {
			var number=+$(element).attr("usernumber");  
			var mail = /^[0-9][0-9]*$/;
			function checkNumber(){
				if(value>=number&&mail.test(value)){ 
					return true;
				}else{
					return false;
				}
			}
			return this.optional(element) || checkNumber();
		}, "请输入不小于原使用人数的整数");
		
		$.validator.addMethod("getusername", function(value, element) {
			
			var flag = "0";
			$.ajax({
				type : "get",
				url : domainUrl + '/manage/client/check',
				async : false, // 同步方法，如果用异步的话，flag永远为1
				data : {'username' : value},
				dataType : "json",
				success : function(data) {
					if (data.status == "10010") {
						flag = 0;
					} else {
						flag = 1;
					}
				}
			});
			if(flag == "0"){
				return false;
			}else {
				return true;
			}
		}, "用户名已被注册");
		
		

		$.validator.addMethod("getOldAccountPwd", function(value, element) {
			var flag = "0";
			$.ajax({
				type : "get",
				url : domainUrl + '/dealer/account/withdrawal/checkCode',
				async : false, // 同步方法，如果用异步的话，flag永远为1
				data : {'oldCode' : value},
				dataType : "json",
				success : function(data) {
					if (data.status == "10100") {
						flag = 0;
					} else {
						flag = 1;
					}
				}
			});
			if(flag == "0"){
				return false;
			}else {
				return true;
			}
		}, "旧提现密码错误");

		$.validator.addMethod("password", function(value, element) {
			var password = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/;
			return this.optional(element) || (password.test(value));
		}, "密码格式错误，请输入6~20位字母和数字组合");
		
		
		$.validator.addMethod("checkusername", function(value, element) {
			var password = /^[A-Za-z0-9]{4,20}$/;
			return this.optional(element) || (password.test(value));
		}, "请输入4-20位英文、数字或组合");
		
		
		$.validator
				.addMethod(
						"withDrawaPassword",
						function(value, element) {
							var withDrawaPassword = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6}$/;
							return this.optional(element)
									|| (withDrawaPassword.test(value));
						}, "请输入6位字母与数字的组合")
		$.validator.addMethod("logistics", function(value, element) {
			var withDrawaPassword = /^[A-Za-z0-9]{1,15}$/;
			return this.optional(element) || (withDrawaPassword.test(value));
		}, "请输入正确的运单号码")

		$.validator.addMethod("commission", function(value, element) {
			var mail = /^([1-8][0-9]|[1-9])([.]\d{0,2})?$/;
			return this.optional(element) || (mail.test(value));
		}, "佣金比例请设置在1%-90%之间，支持小数点后2位")

		$.validator.addMethod("price", function(value, element) {
			var mail = /^\d+([.]\d{0,2})?$/;
			return this.optional(element) || (mail.test(value));
		}, "价格格式不正确，支持小数点后2位")

		$.validator.addMethod("maxValue", function(value, element) {
			var boo = ((+value) > (+$(element).data("max")));
			return !boo;
		}, "超过最大数")

		$.validator.addMethod("getdealerMobile", function(value, element) {
			var flag = 1;
			var memoizerValue = memoizer.get("getdealerMobile", value);
			if (memoizerValue) {
				return true
			}
			$.ajax({
				type : "GET",
				url : domainUrl + '/dealer/check?mobile='+value,
				async : false, // 同步方法，如果用异步的话，flag永远为1
				dataType : "json",
				success : function(msg) {
					if (msg.status != '10010') {
						flag = 0;
					}
				}
			});
			if (flag == 0) {
				memoizer.set("getdealerMobile", value, false);
				return false;
			} else {
				memoizer.set("getdealerMobile", value, true);
				return true;
			}

		}, "手机号未被注册");
		
		// 版本控制
		$.validator.addMethod("versionVal", function(value, element) {
			var mail = /^[a-zA-Z0-9\.]*$/;
			return this.optional(element) || (mail.test(value));
		}, '支持输入字母、数字、和"."');
		
		// 是否是http://或https://开头
		$.validator.addMethod("urlHttp", function(value, element) {
			var mail = /^(http:\/\/.+|https:\/\/.+)$/;
			return this.optional(element) || (mail.test($.trim(value)));
		}, "请输入正确的URL")

	};

});
/**
 * validate.resetForm() //清空错误信息 validate.form() //验证整个表单 提示错误
 * validate.form(false) //验证整个表单 不提示 validate.element('.name') //单独验证
 * class='name' 的元素，提示错误信息 validate.element('.name',false) //单独验证 class='name'
 * 的元素，不提示
 * 
 */
