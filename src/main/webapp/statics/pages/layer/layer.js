(function () {
    var Layer = new HuaFeng.Layer();
    var Laydate = new HuaFeng.LayDate();
    $('#alert').on('click', function () {
        Layer.alert('密码错误！', function () {
            history.go(-1);
        });
    });
    $('#confirm').on('click', function () {
        Layer.confirm('确定删除吗？',{title: '提示', icon: 7}, function(index){
            Layer.success('删除成功');
        });
    });
    $('#msg').on('click', function () {
        Layer.msg('加载完成');
    });
    $('#success').on('click', function () {
        Layer.success('操作成功');
    });
    $('#warning').on('click', function () {
        Layer.warning('至少选择一个');
    });
    $('#error').on('click', function () {
        Layer.error('提交错误');
    });
    $('#iframe').on('click', function () {
        //iframe层-父子操作
        Layer.openIframe( '../login/login.html', ['450px', '300px']);
    });

    /*日期*/
    Laydate.dateRange('#start', '#end');

})();