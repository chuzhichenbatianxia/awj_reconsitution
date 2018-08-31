function getValue() {
    return {
        name: '112fdf',
        paf: 'fsdgxcv'
    };
}


/*
(function () {
    var setting = {
        treeId: 'treeDemo',
        treeObj: null,
        async: {
            url: './tree.json',
            type: 'get',
            dataType: 'json'
        },
        view: {
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom,
            dblClickExpand: false  //禁止双击展开
        },
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true,
                idKey: 'areaValue',
                pIdKey: 'parentValue'
            }
        },
        edit: {
            enable: true,
            editNameSelectAll: true,
            removeTitle: '删除',
            renameTitle: '编辑'
        },
        callback: {
            beforeEditName: beforeEditName,
            beforeRemove: beforeRemove,
            beforeRename: beforeRename,
            onRemove: onRemove,
            onRename: onRename
        }
    };
    $.ajax({
        url: './tree.json'
    }).done(function (res) {
        $.fn.zTree.init($('#treeDemo'), setting, res.menuList);
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getNodes();
        for (var i = 0; i < nodes.length; i++) { //设置节点展开
            treeObj.expandNode(nodes[i], true, true, true);
        }
    });
    /!*加入增加按钮*!/
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
                parent.layer.open({
                    title: '添加地区',
                    type: 2,
                    area: ['580px', 'auto'],
                    content: ['editTree.html', 'no'],  //当type为2时，采用页面引用方法
                    // content: '<div id="add">' + $('#id').html() + '</div>',
                    btn: ['确认', '取消'],
                    yes: function (index, layero) {
                        console.info($('#layui-layer-iframe1')[0].contentWindow.getValue());  //调用iframe里面的方法
                        //按钮 “确定”的回调
                        // console.info($('#add input[name=schoolName]').val());
                    }
                });
            });
        }
    }
    /!*移除增加按钮*!/
    function removeHoverDom(treeId, treeNode) {
        $('#' + treeNode.tId + '_add').unbind().remove();
    }

    function beforeEditName(treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj('treeDemo');
        zTree.selectNode(treeNode);
        setTimeout(function () {
            zTree.editName(treeNode);
        }, 0);
        return false;
    }
    function beforeRemove(treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj('treeDemo');
        zTree.selectNode(treeNode);
        return layer.confirm('确认删除 节点 -- ' + treeNode.name + ' 吗？', {
            btn: ['确定', '取消']
        }, function () {

        }, function () {

        });
    }
    function onRemove(e, treeId, treeNode) {
    }
    function beforeRename(treeId, treeNode, newName, isCancel) {
        if (newName.length === 0) {
            setTimeout(function() {
                var zTree = $.fn.zTree.getZTreeObj('treeDemo');
                zTree.cancelEditName();
                alert('节点名称不能为空.');
            }, 0);
            return false;
        }
        return true;
    }
    function onRename(e, treeId, treeNode, isCancel) {
    }
})();*/
