(function() {
    var Table = new HuaFeng.Table();

    var $table = $('#table');

    Table.init($table, {
        url: 'menu/list',  //查询的接口
        method: 'get',
        //contentType : "application/x-www-form-urlencoded",
        cache: false,
        queryParams: function (params) {  //查询的参数
            return {
                pageSize: params.limit,  //页面大小
                offset: params.offset,   //页码(返回的是数据从第几条开始查询)
                parentId:$('#txt_search_parentId').val(),
                name:$('#txt_search_name').val()
            };
        },
        columns: [{
            checkbox: true
        }, {
            field: 'name',
            title: '名称'
        }, {
            field: 'parentName',
            title: '上级菜单'
        }, {
            field: 'type',
            title: '菜单类型',
            formatter:function (value, row, index) {
                switch (value)
                {
                    case 0:
                        return '<span class="label label-primary">目录</span>';
                    case 1:
                        return '<span class="label label-success">页面</span>';
                    case 2:
                        return '<span class="label label-warning">按钮</span>';
                }
            }
        }, {
            field: 'icon',
            title: '菜单图标',
            formatter: function(value, index, row){
                return value == null ? '' : '<i class="'+value+' fa-lg"></i>';
            }
        }, {
            field: 'perms',
            title: '授权标识'
        }, {
            field: 'url',
            title: '菜单URL'
        },{
            field: 'orderNo',
            title: '排序号'
        }]
    });

    var buttonInit = {
        add:function () {
            window.location.href = 'menu_add.html?menuId='+$('#txt_search_parentId').val();
        },
        edit: function () {
            var selects = Table.getSelections($table);
            if (selects.length === 0 || selects.length > 1) {
                layer.msg('请选择一个进行修改', {time: 2000, icon: 7});
                return;
            } else {
                window.location.href = 'menu_edit.html?menuId='+selects[0].menuId;
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
                        data+=selects[i].menuId +",";
                    }
                    Http.post("menu/delete",data,function(res) {
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
    //实现搜索表单内回车自动查询
    $('#formSearch').on('keyup',function(event){
        if(event.keyCode ==13){
            $("#btn_query").trigger("click");
        }
    });
    $('#btn_query').on('click',function () {
        $table.bootstrapTable('refresh');
    });
    $('#txt_search_parentName').on('click',function () {
        Layer.openIframe('选择上级菜单', ['menu_select.html?v='+Math.random(), 'yes'], ['350px','500px'], function (index, layero) {
            var result =$('#layui-layer-iframe'+index)[0].contentWindow.getSelection();
            if(result)
            {
                $('#txt_search_parentName').val(result.name);
                $('#txt_search_parentId').val(result.id);
                layer.close(index);
                $('#btn_query').trigger('click');
            }
        },function (index, layero) {

        });
    });
    $('#btn_clear').on('click',function () {
        $('#txt_search_parentName').val('');
        $('#txt_search_parentId').val('');
        $('#btn_query').trigger('click');
    });
})();