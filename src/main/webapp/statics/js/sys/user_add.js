/**
 * Created by Administrator on 2017-7-12.
 */
(function () {
    var change = false;
    $('form').on('change', function () {
        change = true;
    });
    $('#btn-cancel').on('click', function () {
        if (change) {
            layer.confirm('确定返回？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                history.go(-1);
            }, function(){
            }) ;
        } else {
            history.go(-1);
        }
    });

    $('#btn-submit').on('click', function () {
        $('#commentForm').validate({
            submitHandler: function (form) {
                 var url =$('#keyId').val()==''?'user/add':'user/edit';
                 Http.post(url,$(form).serialize(), function(res) {
                     if(res.code==0)
                     {
                         Layer.alert(res.msg,function () {
                         history.go(-1);
                        });
                     }
                     else
                     {
                        Layer.error(res.msg);
                     }
                 }, function(err) {
                    Layer.error(err);
                 });
            }
        });
    });
    $('#userType').on('change',function () {
        setUserType($(this).val());
    });
    $('#schoolName').on('click',function () {
        Layer.openIframe('选择学校', [ctxPath+'/base/school_select.html?v='+Math.random(), 'no'], ['900px','660px'], function (index, layero) {
            var result =$('#layui-layer-iframe'+index)[0].contentWindow.getSelection();
            if(result)
            {
                $('#schoolName').val(result.name);
                $('#schoolCode').val(result.id);
                layer.close(index);
            }
        },function (index, layero) {

        });
    });
})();

function setUserType(type) {
    if($('#userType').val()=='4')
    {
        $('#schoolName').attr('disabled',true);
        $('#schoolName').val('全部');
        $('#schoolCode').val('');
    }
    else
    {
        $('#schoolName').val('');
        $('#schoolName').attr('disabled',false);
    }
}