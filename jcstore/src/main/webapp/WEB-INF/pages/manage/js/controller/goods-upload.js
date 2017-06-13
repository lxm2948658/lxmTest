var filename;
function uploadInputFn(){
    filename = $('#upload_input').val();
    var lastIndex = filename.lastIndexOf('\\');
    $('.filename').html(filename.substring(lastIndex+1));
    if(filename){
        $('#goods_upload').removeAttr('disabled').css('background','#3aa9ff');
        $('#select_file').html('重新选择');
    }
};
define(function(require, exports, module) {
    var dialog = require("common/dialog");
    var filter=require("utils/data.filter");
    var clientService = require("manage/service/client");
    var id=filter.getQueryString("id");
    var $username=$("#username");
    var $clientName=$("#clientName");
    var $companyName=$("#companyName");
    clientService.resource(id).ready(function(data){
        clientService.get(id).ready(function(new_data){
            var aaData=new_data.aaData;
            $username.html(aaData.username);
            $clientName.html(aaData.clientName);
            $companyName.html(aaData.companyName);
        })

    })
    
    //选择文件换颜色
    $(document).on("mouseover",'.uploadinput',function () {
        $('#select_file').css('color','#3aa9ff');
    });
    
    $(document).on("mouseout",'.uploadinput',function () {
    	$('#select_file').css('color','#333');
    });

    $("#goods_upload").click(function(){
        $('.mask').show();
        $.ajaxFileUpload({
            fileElementId: 'upload_input',
            url:domainUrl+"/manage/product/import",
            secureuri: false,
            dataType: 'json',
            data:{
                clientId:id
            },
            success: function(data, status) {
                if(data.status=="1"){
                    $('.mask').hide();
                    var dialog1 = dialog.dialog({
                        title:"系统提示",
                        height:"253",
                        width:"420",
                        content:'<p style="text-align: center;">导入成功，成功导入信息' + data.successNum + '条！</p>',
                        name:"save",
                        button:{
                            "确定":function(){
                                window.location.href = domainUrl+'/manage/clientList';
                            }
                        }
                    }).open()
                }else if(data.status=="20204"){
                    $('.mask').hide();
                    var dialog1 = dialog.dialog({
                        title:"系统提示",
                        height:"253",
                        width:"420",
                        content:'<p style="text-align: center;max-height: 57px; overflow: auto; margin-right: -20px;">导入失败，其中第' + data.errorRowList.join('、') + '行校验不通过！</p>',
                        name:"save",
                        button:{
                            "确定":function(){
                                initButton();
                                dialog1.close();
                            }
                        }
                    }).open()
                }else{
                    $('.mask').hide();
                    var dialog1 = dialog.dialog({
                        title:"系统提示",
                        height:"253",
                        width:"420",
                        content:'<p style="text-align: center;">' + data.statusMsg + '</p>',
                        name:"save",
                        button:{
                            "确定":function(){
                                initButton();
                                dialog1.close();
                            }
                        }
                    }).open()
                }
            },
            error: function(data, status, e) {
                if(status=='error'){
                    var dialog1 = dialog.dialog({
                        title:"系统提示",
                        height:"253",
                        width:"420",
                        content:'<p style="text-align: center;">找不到该文件</p>',
                        name:"save",
                        button:{
                            "确定":function(){
                                initButton();
                                dialog1.close();
                                window.location.reload();
                            }
                        }
                    }).open()
                }
            }
        });
    })

    function initButton() {
        $('#upload_input').val('');
        $('.filename').html('');
        $('#goods_upload').attr('disabled','disabled').css('background','#999');
        $('#select_file').html('选择文件');
    }
});