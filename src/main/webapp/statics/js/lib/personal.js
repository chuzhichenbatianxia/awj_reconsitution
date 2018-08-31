/**
 * Created by Administrator on 2017-7-12.
 */
(function () {
    var $table = $('#table');
    $('#sysMember').on('click',function () {
        Layer.openIframe('个人信息', [ctxPath+'/sys/user_personal.html?v='+Math.random(), 'no'], ['600px','300px'], function (index, layero) {
            var result =window[layero.find('iframe')[0]['name']].getValue();
            if(result)
            {
                Http.post(ctxPath+'/sys/user/editPersonal',result, function(res) {
                    if(res.code==0)
                    {
                        layer.close(index);
                        Layer.alert(res.msg,function () {
                            location.reload();
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
        },function (index, layero) {

        });
    });

    $('#modifyPsw').on('click',function () {
        Layer.openIframe('修改密码', [ctxPath+'/sys/user_password.html?v='+Math.random(), 'no'], ['600px','300px'], function (index, layero) {
            var result =window[layero.find('iframe')[0]['name']].getValue();
            if(result)
            {
                Http.post(ctxPath+'/sys/user/editPassword',result, function(res) {
                    if(res.code==0)
                    {
                        layer.close(index);
                        Layer.success(res.msg);
                    }
                    else
                    {
                        Layer.error(res.msg);
                    }
                }, function(err) {
                    Layer.error(err);
                });
            }
        },function (index, layero) {

        });
    });

    $('#sysLogout').on('click',function () {
        Layer.confirm('确定注销登录？',{title: '提示', icon: 7}, function(index){
            layer.close(index);
            location.href=ctxPath+"/logout"
        });
    });

    var school_lis = $('#schoolLists').find('li');
    for(var i = 0; i < school_lis.length; i++) {
        refreshTable(i);
    }
    function refreshTable(index) {
        $(school_lis[index]).on('click', function () {
            var schoolId = $(this).attr('data-value'),
            schoolName = $(this).find('a').text();
            $('#currentSchool').text(schoolName).attr('data-value', schoolId);
            var currentSchoolId=$('#currentSchool').attr('data-value');
            $.get("/attend/customschool/schoolId?schoolId="+currentSchoolId+"&v="+Math.random(),function(data,status){
                console.log(data);
                if (data==0){
                    //
                    location.reload(true);
                }

            });
        });
    }
})();
