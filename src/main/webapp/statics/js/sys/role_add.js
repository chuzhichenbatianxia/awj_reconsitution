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
                 var url =$('#keyId').val()==''?'role/add':'role/edit';
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
})();