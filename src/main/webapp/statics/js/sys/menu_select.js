var single = getQueryString('type')=='1'?false:true;
function getSelections() {
    var treeObj = $.fn.zTree.getZTreeObj("tree");
    var nodes='';
    if(single)
    {
        nodes=treeObj.getSelectedNodes();
        if(nodes.length==0)
        {
            if(!single)
            {
                Layer.alert('请先选择菜单！');
            }
            return null;
        }
    }
    else
    {
        var nodes = treeObj.getCheckedNodes(true);
    }
    var ids=[];
    var names=[];
    for(var i=0;i<nodes.length;i++)
    {
        ids.push(nodes[i].menuId);
        names.push(nodes[i].name);
    }
    return {ids:ids,names:names};
}

function getSelection() {
    var result = getSelections();
    if(result)
    {
        return {id:result.ids[0],name:result.names[0]};
    }
    return null;
}

(function () {
    var Tree = new HuaFeng.Tree();
    var Layer = new HuaFeng.Layer();
    var selectedIds=[];
    var async_tree_api = {
        enable: true,  // true开启 异步加载模式
        url: 'menu/select?roleId='+getQueryString('roleId'),
        type: 'GET',
        dataType: 'json',
        //otherParam: { 'id': '1', 'name': 'test'},  //请求参数
        // autoParam: ['id=areaValue'],  //将需要作为参数提交的属性名称，制作成Array即可，如['id', 'name']
        dataFilter: function (treeId, parentNode, responseData) {
            if (responseData) {
                selectedIds = responseData.selectedMenuId
                return responseData.menuList;
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
            enable: !single
        },
        data: {
            simpleData: {  //根据接口设置idKey和pIdKey，其中idKey为下级名，pIdKey为父级名
                enable: true,
                idKey: 'menuId',
                pIdKey: 'parentId',
                rootPId: 0
            },
            key:{
                url:'nurl'
            }
        },
        callback: {
            onAsyncSuccess: function (event, treeId, treeNode) {
                var treeObj = $.fn.zTree.getZTreeObj(treeId);
                var nodes = treeObj.getNodes();
                for (var i = 0; i < nodes.length; i++) { //设置节点展开
                    treeObj.expandNode(nodes[0], true, false, true);
                }
                var allNodes = treeObj.transformToArray(nodes);
                for(var i=0;i<allNodes.length;i++)
                {
                    if(contains(selectedIds,allNodes[i].menuId))
                    {
                        treeObj.checkNode(allNodes[i],true,false);
                    }
                }
                return;
            }
        }
    };
    Tree.init($('#tree'), setting);
})();

