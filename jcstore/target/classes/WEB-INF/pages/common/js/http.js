//http
define(function(require, exports, module) {
    module.exports = {
        ajax: function(cfg) {
            return $.ajax(cfg);
        },
        get:function(url,boo){
            if(boo){
                var e = new RegExp("?");
                var b = e.test(url);
                var timestamp = new Date().getTime();
                var c = b?'?t=':'&t=';
            }else{
                var c='',timestamp='';
            }
            
            return $.ajax({
                    url:url+c+timestamp,
                    type:"GET",
                    dataType:"json",
                })
        },
        post: function(url, data) {
            return $.ajax({
                url: url,
                type: "POST",
                data: data,
                dataType: "json",
            })
        }
    }
});