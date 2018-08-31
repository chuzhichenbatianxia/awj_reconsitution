(function () {
    var Tree = new HuaFeng.Tree();
    var Layer = new HuaFeng.Layer();

    var async_tree_api = {
        enable: true,  // true开启 异步加载模式
        url: './tree.json',
        type: 'GET',
        dataType: 'json',
        otherParam: { 'id': '1', 'name': 'test'},  //请求参数
        // autoParam: ['id=areaValue'],  //将需要作为参数提交的属性名称，制作成Array即可，如['id', 'name']
        dataFilter: function (treeId, parentNode, responseData) {
            if (responseData) {
                responseData = responseData.menuList;
            }
            return responseData;
        }
    };

    var setting = {
        treeId: 'treeDemo',  //用户定义的 zTree 容器的 id 属性值
        async: async_tree_api,
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            // dblClickExpand: false  //禁止双击展开
        },
        check: {   //设置可选择
            enable: true,
            chkStyle: "radio",
            radioType: "all",
            chkboxType: { "Y": "p", "N": "s" }
        },
        data: {
            simpleData: {  //根据接口设置idKey和pIdKey，其中idKey为下级名，pIdKey为父级名
                enable: true,
                idKey: 'areaValue',
                pIdKey: 'parentValue'
            }
        },
        // edit: {
        //     enable: true,
        //     editNameSelectAll: true,
        //     removeTitle: '删除',
        //     renameTitle: '编辑'
        // },
        callback: {
            onAsyncSuccess: Tree.expandNode,
            beforeClick: beforeClick
            // beforeEditName: Tree.checkName,
            // beforeRemove: beforeRemove,
            // beforeRename: Tree.checkName,
            // onRemove: onRemove,
            // onRename: onRename
        }
    };
    Tree.init($('#treeDemo'), setting);
    function beforeClick(treeId, treeNode, clickFlag) {

    }
    /*加入增加按钮*/
    function addHoverDom(treeId, treeNode) {
        var aObj = $('#' + treeNode.tId + '_span');
        if (treeNode.editNameFlag || $('#'+treeNode.tId + '_add').length > 0) {
            return;
        }
        var addStr = '<span class="button add" id="' + treeNode.tId + '_add" title="添加地区"></span>';
        aObj.after(addStr);
        var btn = $('#' + treeNode.tId + '_add');
        if (btn) {
            btn.bind("click", function(){
                addArea();
            });
        }
    }
    /*添加地区*/
    function addArea() {
        Layer.openIframe('添加地区', ['tree_edit.html', 'no'], '580px', function (index, layero) {
            console.info($('#layui-layer-iframe' + index)[0].contentWindow.getValue());  //调用iframe里面的方法
        });
    }
    /*移除增加按钮*/
    function removeHoverDom(treeId, treeNode) {
        $('#' + treeNode.tId + '_add').unbind().remove();
    }

    // function beforeEditName(treeId, treeNode) {
    //     var zTree = $.fn.zTree.getZTreeObj('treeDemo');
    //     zTree.selectNode(treeNode);
    //     setTimeout(function () {
    //         zTree.editName(treeNode);
    //     }, 0);
    //     return false;
    // }
    function beforeRemove(treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj(treeId);
        zTree.selectNode(treeNode);
        layer.confirm('确认删除 节点 -- ' + treeNode.name + ' 吗？', {
            btn: ['确定', '取消']
        }, function () {
            zTree.removeNode(treeNode);
            layer.msg('删除成功！', {icon: 1});
        }, function () {});
        return false;
    }
    function onRemove(e, treeId, treeNode) {
        return layer.confirm('确认删除 节点 -- ' + treeNode.name + ' 吗？', {
            btn: ['确定', '取消']
        }, function () {
            Tree.refreshNode(treeId, treeNode);
        }, function () {

        });
    }
    function beforeRename(treeId, treeNode, newName, isCancel) {
        if (newName.length === 0) {
            setTimeout(function() {
                var zTree = $.fn.zTree.getZTreeObj(treeId);
                zTree.cancelEditName();
                alert('节点名称不能为空.');
            }, 0);
            return false;
        }
        return true;
    }
    function onRename(e, treeId, treeNode, isCancel) {
    }
})();