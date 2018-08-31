/*
* 数据接口请求
* */
var Http = new HuaFeng.Http();
Http.get('../table/tableData.json', {}, function (res) {
    console.info(res);
}, function (err) {
    console.info(err)
});


var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
$('#login').on('click', function(){
    parent.layer.tips('Look here', '#iframe', {time: 5000});
    parent.layer.close(index);
});