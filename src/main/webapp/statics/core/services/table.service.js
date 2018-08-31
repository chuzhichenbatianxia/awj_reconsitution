
if (!HuaFeng) {
    var HuaFeng = {
    };
}

var Table = function () {

    /*if (this instanceof Table) {
        loadCssFile('../../libs/bootstrap-table/dist/bootstrap-table.css');
        loadJSFile('../../libs/bootstrap-table/dist/bootstrap-table.js');
        loadJSFile('../../libs/bootstrap-table/dist/extensions/export/bootstrap-table-export.min.js');
        loadJSFile('../../libs/bootstrap-table/dist/locale/bootstrap-table-zh-CN.min.js');
    } else {
        return new Table();
    }
    
    function loadJSFile(src) {
        var scriptObj = document.createElement('script');
        scriptObj.src = src;
        scriptObj.type = 'text/javascript';
        document.getElementsByTagName('head')[0].appendChild(scriptObj);
    }
    function loadCssFile(href) {
        var styleObj = document.createElement('link');
        styleObj.href = href;
        styleObj.type = 'text/css';
        styleObj.rel = 'stylesheet';
        document.getElementsByTagName('head')[0].appendChild(styleObj);
    }*/
};

Table.prototype = {
    options: function (options) {
        var defaultOption = {
            classes: 'table table-hover table-no-bordered',
            pagination: true,
            search: true,  //启用搜索栏
            searchOnEnterKey: true,   //按回车进行搜索
            searchText: '',  //初始化搜索文字
            showColumns: true,  //内容下拉框
            showRefresh: true,  //显示刷新按钮
            // showExport: true, //显示导出按钮
            // showToggle: true,
            clickToSelect: true,  //true时将在点击行时，自动选择rediobox 和 checkbox
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）,server端分页需含有rows字段跟total字段
            onLoadSuccess: function (data) { //加载成功时执行
                // console.log(data);
            },
            onLoadError: function (res) { //加载失败时执行
                // console.log(res);
            }
        };
        return $.extend({}, defaultOption, options);
    },
    init: function ($target, options) {
        $target.bootstrapTable(this.options(options));
        return;
    },
    /*
    * @desc: 返回所选的行，当没有选择任何行的时候返回一个空数组
     * @use: var Table = new HuaFeng.Table();  var selects = Table.getSelections($('#table'));
    * */
    getSelections: function ($target) {
        return $target.bootstrapTable('getSelections');
    },
    /*
     * @desc: 返回所有选择的行，包括搜索过滤前的，当没有选择任何行的时候返回一个空数组
     * @use: var Table = new HuaFeng.Table();  var selects = Table.getAllSelections($('#table'));
     * */
    getAllSelections: function ($target) {
        return $target.bootstrapTable('getAllSelections');
    },
    /*
     * @desc: 加载数据到表格中，旧数据会被替换。
     * @use: var Table = new HuaFeng.Table();  var selects = Table.load($('#table'), data);
     * */
    load: function ($target, data) {
        $target.bootstrapTable('load', data);
        return;
    },
    /*
     * @desc: 隐藏某一列。
     * @use: var Table = new HuaFeng.Table();  var selects = Table.hideColumn($('#table'), 'filed');
     * */
    hideColumn: function ($target, filed) {
        $target.bootstrapTable('hideColumn', filed);
        return;
    },
    /*
     * @desc: 显示某一列。
     * @use: var Table = new HuaFeng.Table();  var selects = Table.showColumn($('#table'), 'filed');
     * */
    showColumn: function ($target, filed) {
        $target.bootstrapTable('showColumn', filed);
        return;
    }
};

HuaFeng.Table = Table;