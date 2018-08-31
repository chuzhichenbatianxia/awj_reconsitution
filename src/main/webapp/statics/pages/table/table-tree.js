(function() {
    var oInit = {};
    $('#table').bootstrapTable({
        // classes: 'table table-hover table-no-bordered',
        height: 500,
        // pagination: true,
        // pageNumber: 1,
        // pageSize: 10,
        // search: true,  //启用搜索栏
        // searchOnEnterKey: true,   //按回车进行搜索
        // searchText: '',  //初始化搜索文字
        // toolbar: '#toolbar',                //工具按钮用哪个容器
        // showColumns: true,  //内容下拉框
        // showRefresh: true,  //显示刷新按钮
        // showExport: true, //显示导出按钮
        // showToggle: true,
        // detailView: true,  //可以显示详细页面模式，表格前面的+号
        // detailFormatter: function (index, row) {  //格式化详细页面模式的视图
        //     return detailFormatter(index, row);
        // },
        clickToSelect: true,  //true时将在点击行时，自动选择rediobox 和 checkbox
        columns: [{
            field: 'state',
            checkbox: true,
            align: 'center',
            valign: 'middle'
        },{
            field: 'name',
            title: '国家',
            sortable: true
        },{
            field: 'creatTime',
            title: '创建时间'
        }],
        onLoadSuccess: function (data) { //加载成功时执行
            console.log(data);
        },
        onLoadError: function (res) { //加载失败时执行
            console.log(res);
        },
        data: [{
            id: 1,
            name: '中国',
            creatTime: '2015-05-06',
            children: [{
                id: 1001,
                name: '广东省',
                creatTime: '2015-05-08',
                children: [{
                    id: 10001,
                    name: '梅州市',
                    creatTime: '2015-05-10'
                }]
            },{
                id: 1002,
                name: '福建省',
                creatTime: '2015-05-08'
            },{
                id: 1003,
                name: '江西省',
                creatTime: '2015-05-08'
            }]
        }, {
            id: 1,
            name: '美国',
            creatTime: '2015-05-06'
        }],
        //注册加载子表的事件。注意下这里的三个参数！
        onExpandRow: function (index, row, $detail) {
            oInit.InitSubTable(index, row, $detail);
        }
    });
    //初始化子表格(无线循环)
    oInit.InitSubTable = function (index, row, $detail) {
        var cur_table = $detail.html('<table></table>').find('table');
        $(cur_table).css({
            padding: '20px'
        });
        $(cur_table).bootstrapTable({
            clickToSelect: true,
            detailView: true,//父子表
            columns: [{
                checkbox: true
            }, {
                field: 'name',
                title: '省'
            }, {
                field: 'creatTime',
                title: '创建时间'
            }],
            data: row.children,
            //无线循环取子表，直到子表里面没有记录
            onExpandRow: function (index, row, $Subdetail) {
                oInit.InitSubTable(index, row, $Subdetail);
            }
        });
    };
    function operateFormatter(value, row, index) {
        return [
            '<a class="next_level" href="javascript:void(0)" title="next_level">下一级</a>'
        ].join('');
    }
    window.operateEvents = {
        'click .next_level': function (e, value, row, index) {
            alert('You click like action, row: ' + JSON.stringify(row));
        }
    };
    function getIdSelections() {
        return $.map($('#table').bootstrapTable('gerSelections'), function (row) {
            return row.id;
        });
    }
    $('#btn_delete').on('click', function () {
        var selects = getSelections();
        if (selects.length === 0) {
            layer.msg('至少选择一个', {time: 5000, icon: 7});
            return;
        } else {
            layer.confirm('确定删除？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                layer.msg('删除成功！', {icon: 1});
            }, function(){
            });
        }
    });
    $('#btn_edit').on('click', function () {
        var selects = getSelections();
        if (selects.length === 0 || selects.length > 1) {
            layer.msg('请选择一个进行修改', {time: 5000, icon: 7});
            return;
        } else {
            // window.open('editTable.html');
            window.location.href = 'editTable.html';
        }
    });
    function getSelections() {
        return $('#table').bootstrapTable('getSelections');
    }
})();