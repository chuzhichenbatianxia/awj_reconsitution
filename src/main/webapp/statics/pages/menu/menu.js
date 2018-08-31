(function() {
    var Table = new HuaFeng.Table();

    var $table = $('#table');

    Table.init($table, {
        url: './data.json',  //查询的接口
        method: 'GET',
        cache: false,
        queryParams: function (params) {  //查询的参数
            return {
                pageSize: params.limit,  //页面大小
                page: params.offset   //页码(返回的是数据从第几条开始查询)
            };
        },
        columns: [{
            checkbox: true
        },{
            field: 'id',
            title: 'Item ID'
        }, {
            field: 'name',
            title: 'Item Name',
            sortable: true
        }, {
            field: 'price',
            title: 'Item Price'
        }]
    });

    var buttonInit = {
        edit: function () {
            var selects = Table.getSelections($table);
            if (selects.length === 0 || selects.length > 1) {
                layer.msg('请选择一个进行修改', {time: 5000, icon: 7});
                return;
            } else {
                window.location.href = 'menu_edit.html';
            }
        },
        delete: function () {
            var selects = Table.getSelections($table);
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
        }
    };

    $('#btn_delete').on('click', function () {
        buttonInit.delete();
    });
    $('#btn_edit').on('click', function () {
        buttonInit.edit();
    });
})();