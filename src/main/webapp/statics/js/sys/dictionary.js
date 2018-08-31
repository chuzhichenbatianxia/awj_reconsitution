(function() {
    var Tree = new HuaFeng.Tree();
    var async_tree_api = {
        enable: true,  // true开启 异步加载模式
        url: 'dictionary/tree',
        type: 'GET',
        dataType: 'json',
        dataFilter: function (treeId, parentNode, responseData) {
            if (responseData) {
                return responseData.dictList;
            }
            return responseData;
        }
    };

    var setting = {
        treeId: 'tree',  //用户定义的 zTree 容器的 id 属性值
        async: async_tree_api,
        view: {
            dblClickExpand: true,  //禁止双击展开
            txtSelectedEnable: true
        },
        check: {   //设置可选择
            enable: false
        },
        data: {
            simpleData: {  //根据接口设置idKey和pIdKey，其中idKey为下级名，pIdKey为父级名
                enable: true,
                idKey: 'id',
                pIdKey: 'parentId'
            },
            key:{
                name:'dictText'
            }
        },
        callback: {
            onAsyncSuccess:Tree.expandNode,
            onClick: zTreeOnClick
        }
    };
    Tree.init($('#tree'), setting);



    var Table = new HuaFeng.Table();
    var $table = $('#table');
    Table.init($table, {
        url: 'dictionary/list',  //查询的接口
        method: 'get',
        //contentType : "application/x-www-form-urlencoded",
        cache: false,
        queryParams: function (params) {  //查询的参数
            return {
                pageSize: params.limit,  //页面大小
                offset: params.offset   //页码(返回的是数据从第几条开始查询)
            };
        },
        columns: [{
            checkbox: true
        }, {
            field: 'dictText',
            title: '名称'
        }, {
            field: 'dictValue',
            title: '值'
        }, {
            field: 'orderNo',
            title: '排序'
        }]
    });

    var buttonInit = {
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
                        data+=selects[i].id +",";
                    }
                    Http.post("dictionary/delete",data,function(res) {
                        if(res.code==0)
                        {
                            layer.msg(res.msg, {icon: 1,time:2000});
                            var treeNodes = treeObj.getSelectedNodes();
                            var parentId =treeNodes.length==0?0:treeNodes[0].id;
                            $table.bootstrapTable('refresh',{query:{parentId:parentId}});
                            if(parentId==0)
                            {
                                treeObj.reAsyncChildNodes(null, "refresh");
                            }
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

    var treeObj = $.fn.zTree.getZTreeObj('tree');
    function getSelection() {
        var nodes = treeObj.getSelectedNodes();
        if(nodes.length==0)
        {
            layer.msg('请先选择分类', {icon: 2,time:2000});
            return null;
        }
        return nodes[0];
    }

    $('#btn_add').on('click',function () {
        var result = getSelection();
        if(result!=null)
        {
            var url ='dictionary_add.html?parentId='+result.id+'&level='+(result.id==0?1:2)+'&v='+Math.random();
            Layer.openIframe('添加', [url, 'yes'], ['650px','320px'], function (index, layero) {
                var value =$('#layui-layer-iframe'+index)[0].contentWindow.getValue();
                if(value)
                {
                    Http.post("dictionary/add",value,function(res) {
                        if(res.code==0)
                        {
                            layer.msg(res.msg, {icon: 1,time:2000});
                            $table.bootstrapTable('refresh',{query:{parentId:result.id}});
                            if(result.id=='0')
                            {
                                treeObj.reAsyncChildNodes(null, "refresh");
                            }
                            layer.close(index);
                        }
                        else
                        {
                            layer.msg(res.msg, {icon: 2,time:2000});
                        }
                    });
                }
            },function (index, layero) {

            });
        }
    });

    $('#btn_delete').on('click', function () {
        buttonInit.delete();
    });
    $('#btn_edit').on('click', function () {
        var selects = Table.getSelections($table);
        if (selects.length === 0 || selects.length > 1) {
            layer.msg(selects.length === 0?'请选择一条记录':'只能选择一条记录', {time: 2000, icon: 7});
            return;
        }
        var url ='dictionary_edit.html?id='+selects[0].id+'&v='+Math.random();
        Layer.openIframe('修改', [url, 'yes'], ['650px','320px'], function (index, layero) {
            var value =$('#layui-layer-iframe'+index)[0].contentWindow.getValue();
            if(value)
            {
                Http.post("dictionary/edit",value,function(res) {
                    if(res.code==0)
                    {
                        layer.msg(res.msg, {icon: 1,time:2000});
                        var treeNodes = treeObj.getSelectedNodes();
                        var parentId =treeNodes.length==0?0:treeNodes[0].id;
                        $table.bootstrapTable('refresh',{query:{parentId:parentId}});
                        if(parentId==0)
                        {
                            treeObj.reAsyncChildNodes(null, "refresh");
                        }
                        layer.close(index);
                    }
                    else
                    {
                        layer.msg(res.msg, {icon: 2,time:2000});
                    }
                });
            }
        },function (index, layero) {

        });


    });


    function zTreeOnClick(event, treeId, treeNode) {
        $table.bootstrapTable('refresh',{query:{parentId:treeNode.id}});
    };

})();