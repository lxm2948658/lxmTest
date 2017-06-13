/**
 * filter 过滤器
 * @description 格式处理工具库
 */
define(function(require, exports, module) {
    var filter = {
        /**
         * formatDate:日期格式化
         * 用法：filter.formatDate(time,"YYYY-MM-DD hh:mm:ss")
         * */
        formatDate: function(now, fmt) {
        	var time = new Date(now);
            var o = {
                "M+": time.getMonth() + 1, //月份 
                "D+": time.getDate(), //日 
                "h+": time.getHours(), //小时 
                "m+": time.getMinutes(), //分 
                "s+": time.getSeconds(), //秒 
                "q+": Math.floor((time.getMonth() + 3) / 3), //季度 
                "S": time.getMilliseconds() //毫秒 
            };
            if (/(Y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (time.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        },
        getQueryString: function(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
            return null;
        }
    };
    module.exports = filter;
});