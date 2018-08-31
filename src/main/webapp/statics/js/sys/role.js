(function() {
    var Table = new HuaFeng.Table();

    var $table = $('#table');

    Table.init($table, {
        url: 'role/list',  //查询的接口
        method: 'get',
        //contentType : "application/x-www-form-urlencoded",
        cache: false,
        queryParams: function (params) {  //查询的参数
            return {
                pageSize: params.limit,  //页面大小
                offset: params.offset,   //页码(返回的是数据从第几条开始查询)
                roleName:$('#txt_search_name').val()
            };
        },
        columns: [{
            checkbox: true
        }, {
            field: 'roleId',
            title: '角色ID'
        },  {
            field: 'roleName',
            title: '角色名称'
        },   {
            field: 'status',
            title: '允许删除',
            formatter:function (value, index) {
                return value==0?'不允许':'允许';
            }
        },{
            field: 'remark',
            title: '备注'
        }, {
            field: 'creator',
            title: '创建人'
        }, {
            field: 'createTime',
            title: '创建时间'
        }]
    });

    var buttonInit = {
        add:function () {
            window.location.href = 'role_add.html';
        },
        edit: function () {
            var selects = Table.getSelections($table);
            if (selects.length === 0 || selects.length > 1) {
                layer.msg('请选择一个进行修改', {time: 2000, icon: 7});
                return;
            } else {
                window.location.href = 'role_edit.html?roleId='+selects[0].roleId;
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
                    var tag ='deleteTag='
                    for(var i =0 ; i<selects.length;i++)
                    {
                        data+=selects[i].roleId +",";
                        tag+=selects[i].status +",";
                        if(selects[i].status=='0')
                        {
                            layer.msg('角色('+selects[i].roleName+')不允许删除!',{icon: 2,time:2000})
                            return;
                        }
                    }
                    Http.post("role/delete",data+'&'+tag,function(res) {
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
                Layer.openIframe('权限分配', ['menu_select.html?roleId=' + selects[0].roleId + '&type=1&v='+Math.random(), 'yes'], ['350px','500px'], function (index, layero) {
                    var result =$('#layui-layer-iframe'+index)[0].contentWindow.getSelections();
                    if(result)
                    {
                        var menuIds='menuIds=';
                        for(var i=0;i<result.ids.length;i++)
                        {
                            menuIds+=result.ids[i]+',';
                        }
                        Http.post("role/distribute",'roleId='+selects[0].roleId+'&'+menuIds,function(res) {
                            if(res.code==0)
                            {
                                layer.msg(res.msg, {icon: 1,time:2000});
                            }
                            else
                            {
                                layer.msg(res.msg, {icon: 2,time:2000});
                            }
                        });
                    }
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