if (!HuaFeng) {
    var HuaFeng = {
    };
}

var Tree = function () {
    this.reloadFlag = false; //是否重新异步请求
};

Tree.prototype = {
    /*刷新当前节点*/
    refreshNode: function (treeId, treeNode) {
        //根据treeId获取zTree对象
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        //获取zTree当前被选中的节点数据集合
        var nodes = treeObj.getSelectedNodes();
        //强行异步加载父节点的子节点[setting.async.enable = true时有效
        treeObj.reAsyncChildNodes(nodes[0], 'refresh', false);
    },
    /*刷新当前选择节点的父节点*/
    refreshParentNode: function (treeId, treeNode) {
        //根据treeId获取zTree对象
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        //获取zTree当前被选中的节点数据集合
        var nodes = treeObj.getSelectedNodes();
        /*根据 zTree 的唯一标识 tId 快速获取节点 JSON 数据对象*/
        var parentNode = treeObj.getNodeByTId(treeNode.parentTId);
        //强行异步加载父节点的子节点[setting.async.enable = true时有效
        treeObj.reAsyncChildNodes(parentNode, 'refresh', false);
    },
    /*展开全部节点*/
    expandNode: function (event, treeId, treeNode) {
        var treeObj = $.fn.zTree.getZTreeObj(treeId);
        var nodes = treeObj.getNodes();
        for (var i = 0; i < nodes.length; i++) { //设置节点展开
            treeObj.expandNode(nodes[0], true, false, true);
        }
        return;
    },
    /**
     * @desc: 编辑节点后检查节点是否为空
     * @example:
     * var Tree = new HuaFeng.Tree(); callback: { beforeRename: Tree.checkName }
     * */
    checkName: function(treeId, treeNode, newName, isCancel) {
        if (newName.length === 0) {
            setTimeout(function() {
                var zTree = $.fn.zTree.getZTreeObj(treeId);
                zTree.cancelEditName();
                alert('节点名称不能为空.');
            }, 0);
            return false;
        }
        return true;
    },
    init: function ($target, setting) {
        $.fn.zTree.init($target, setting);
    }
};

HuaFeng.Tree = Tree;