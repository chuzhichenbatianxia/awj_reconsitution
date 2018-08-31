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
                 var url =$('#keyId').val()==''?'menu/add':'menu/edit';
                 Http.post(url,$(form).serialize(), function(res) {
                     if(res.code==0)
                     {
                         Layer.alert(res.msg,function () {
                         history.go(-1);//window.location.href="menu.html";
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
    $(":radio[name='type']").on('change',function () {
        changeType($(this).val());
    });
    $('#parentName').on('click',function () {
        Layer.openIframe('选择上级菜单', ['menu_select.html?v='+Math.random(), 'yes'], ['350px','500px'], function (index, layero) {
            var result =$('#layui-layer-iframe'+index)[0].contentWindow.getSelection();
            if(result)
            {
                $('#parentName').val(result.name);
                $('#parentId').val(result.id);
                layer.close(index);

            }
        },function (index, layero) {

        });
    });
})();
var $icon = $('#t_icon');
var $perms = $('#t_perms');
var $target = $('#t_target');
var $url = $('#t_url');
function changeType(index) {
    switch (index)
    {
        case '0':
            $url.addClass('hidden');
            $perms.addClass('hidden');
            $target.addClass('hidden');
            $icon.removeClass('hidden');
            break;
        case '1':
            $url.removeClass('hidden');
            $perms.removeClass('hidden');
            $target.removeClass('hidden');
            $icon.removeClass('hidden');
            break;
        case '2':
            $url.addClass('hidden');
            $perms.removeClass('hidden');
            $target.addClass('hidden');
            $icon.addClass('hidden');
            break;
    }
}