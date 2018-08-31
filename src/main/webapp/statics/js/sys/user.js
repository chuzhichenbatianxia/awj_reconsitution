(function() {
    var Table = new HuaFeng.Table();

    var $table = $('#table');

    Table.init($table, {
        url: 'user/list',  //查询的接口
        method: 'get',
        //contentType : "application/x-www-form-urlencoded",
        cache: false,
        queryParams: function (params) {  //查询的参数
            return {
                pageSize: params.limit,  //页面大小
                offset: params.offset,   //页码(返回的是数据从第几条开始查询)
                username:$('#txt_search_name').val(),
                account:$('#txt_search_account').val(),
                userType:$('#userType').val()
            };
        },
        columns: [{
            checkbox: true
        }, {
            field: 'userName',
            title: '用户名'
        }, {
            field: 'userType',
            title: '身份',
            formatter:function (value, row, index) {
                switch (value)
                {
                    case 1:
                        return "学生";
                    case 2:
                        return "教师";
                    case 3:
                        return "家长";
                    case 4:
                        return "客服";
                }
            }
        }, {
            field: 'schoolName',
            title: '所属学校'
        }, {
            field: 'account',
            title: '账号'
        }, {
            field: 'status',
            title: '状态',
            formatter:function (value, row, index) {
                return value === 0 ? '<span class="label label-danger">停用</span>' : '<span class="label label-success">正常</span>';
            }
        }, {
            field: 'updateTime',
            title: '修改时间'
        }]
    });

    var buttonInit = {
        add:function () {
            window.location.href = 'user_add.html';
        },
        edit: function () {
            var selects = Table.getSelections($table);
            if (selects.length === 0 || selects.length > 1) {
                layer.msg('请选择一个进行修改', {time: 2000, icon: 7});
                return;
            } else {
                window.location.href = 'user_edit.html?userId='+selects[0].userId;
            }
        },
        delete: function () {
            var selects = Table.getSelections($table);
            if (selects.length === 0) {
                layer.msg('至少选择一个', {time: 2000, icon: 7});
                return;
            } else {
                layer.confirm('确定删除？', {
                    btn: ['确定','取消'] //按钮
                }, function(){
                    var data='ids=';
                    for(var i =0 ; i<selects.length;i++)
                    {
                        data+=selects[i].userId +",";
                    }
                    Http.post("user/delete",data,function(res) {
                        if(res.code==0)
                        {
                            layer.msg(res.msg, {icon: 1,time:2000});
                            $table.bootstrapTable('refresh');
                        }
                        else
                        {
                            layer.msg(res.msg, {icon: 2,time:2000});
                        }
                    });



                }, function(){
                });
            }
        },
        distribute:function () {
            var selects = Table.getSelections($table);
            if (selects.length === 0 || selects.length > 1) {
                layer.msg(selects.length === 0?'请选择一条记录':'只能选择一条记录', {time: 2000, icon: 7});
                return;
            } else {
                Layer.openIframe('角色分配', ['role_select.html?userId=' + selects[0].userId + '&v='+Math.random(), 'yes'], ['600px','200px'], function (index, layero) {
                    var result =$('#layui-layer-iframe'+index)[0].contentWindow.getSelection();
                    Http.post("user/distribute",'userId='+selects[0].userId+'&'+result,function(res) {
                        if(res.code==0)
                        {
                            layer.msg(res.msg, {icon: 1,time:2000});
                        }
                        else
                        {
                            layer.msg(res.msg, {icon: 2,time:2000});
                        }
                    });
                    layer.close(index);
                },function (index, layero) {

                });
            }
        }
    };

    $('#btn_delete').on('click', function () {
        buttonInit.delete();
    });
    $('#btn_edit').on('click', function () {
        buttonInit.edit();
    });
    $('#btn_add').on('click',function () {
        buttonInit.add();
    });
    $('#btn_distribute').on('click',function () {
        buttonInit.distribute();
    });
    //实现搜索表单内回车自动查询
    $('#formSearch').on('keyup',function(event){
        if(event.keyCode ==13){
            $("#btn_query").trigger("click");
        }
    });
    $('#btn_query').on('click',function () {
        $table.bootstrapTable('refresh');
    });
})();