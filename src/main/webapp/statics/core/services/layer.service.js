
if (!HuaFeng) {
    var HuaFeng = {
    };
}


/**
 * @param icon icon的值有：1.正确； 2.错误； 3.疑问； 4.锁； 5:不开心； 6：开心； 7：感叹号。
 *  @param {number} type : 5种层类型——0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
 *
 *  Layer下面有以下方法
 *  var Layer = new HuaFeng.Layer();
 *  Layer.warning('至少选择一项');  //提示信息
 *  Layer.error('密码错误');   //错误信息
 *  Layer.success('操作成功');  //成功信息
 *  Layer.alert('请选择其中一个选项再操作', {title: '提示', icon: 7});   //alert框
 *  Layer.confirm('确定删除？', {title: '提示', icon: 7}, function(index) {});   //confirm框
 *  Layer.confirm('确定删除？', {btn: ['确定', '取消']}, function(index) { Layer.close(index);});
 *  Layer.openIframe('../login/login.html', ['450px', '300px']);  //打开iframe框
* */
var Layer = function () {
};

Layer.prototype = {
    msg: function (content) {
        layer.msg(content);
        return;
    },
    /**
     * @desc：提示信息
     * @param {string} content 提示框内容
     * @example: var Layer = new HuaFeng.Layer(); Layer.warning('至少选择一项');
     * */
    warning: function (content) {
        layer.msg(content, {icon: 7});
        return;
    },
    /**
     * @desc：错误信息
     * @param {string} content 提示框内容
     * @example: var Layer = new HuaFeng.Layer(); Layer.error('密码错误');
     * */
    error: function (content) {
        layer.msg(content, {icon: 2});
        return;
    },
    /**
     * @desc：成功信息
     * @param {string} content 提示框内容
     * @example: var Layer = new HuaFeng.Layer(); Layer.success('操作成功');
     * */
    success: function (content) {
        layer.msg(content, {icon: 1});
        return;
    },
    /**
     * @desc：alert框
     * @param {string} content 提示框内容
     * @param {object} options 配置title,icon等信息
     * @example: var Layer = new HuaFeng.Layer(); Layer.alert('请选择其中一个选项再操作', {title: '提示', icon: 7});
     * @example: var Layer = new HuaFeng.Layer(); Layer.alert('请选择其中一个选项再操作', function() {});
     * @example: var Layer = new HuaFeng.Layer(); Layer.alert('请选择其中一个选项再操作', {title: '提示', icon: 7}, function() {Layer.close();});
     * */
    alert: function (content, options, func) {
        if ('function' === typeof (options)) {
            func = options;
            layer.alert(content, func);
        } else {
            layer.alert(content, options, func);
        }
        return;
    },
    /**
     * @desc：提示框
     * @param {string} content 提示框内容
     * @param {function} func 点击确定按钮之后的操作函数
     * @example: var Layer = new HuaFeng.Layer(); Layer.confirm('确定删除？', {title: '提示', icon: 7}, function(index) {});
     * */
    confirm: function (content, options, func) {
        layer.confirm(content, options, func);
        return;
    },
    /**
     * @desc：关闭提示框
     * @example: var Layer = new HuaFeng.Layer(); Layer.confirm('确定删除？', {btn: ['确定', '取消']}, function(index) { Layer.close(index);});
    * */
    colse: function (index) {
        layer.close(index);
        return;
    },
    /**
     * @desc: 打开iframe层
     * @param {string} title iframe层title名
     * @param {string} content iframe的url链接
     * @param {string or Array} area 弹出层的宽高
     * @param {function} endCallback 弹出层关闭或确认后触发的事件，可不写
     *
     * @example:
     *  var Layer = new HuaFeng.Layer(); Layer.openIframe('添加地区', '../login/login.html', ['450px', '300px']);
     *  or
     *  var Layer = new HuaFeng.Layer(); Layer.openIframe('添加地区', '../login/login.html', ['450px', '300px'], function() {});
     * */
    openIframe: function (title, content, area, yesCallback, endCallback) {
        //iframe层-父子操作
        layer.open({
            title: title,
            type: 2,
            area: area,  //['450px', '300px'],
            fixed: false, //不固定
            maxmin: true,
            btn: ['确定', '取消'],
            yes: yesCallback || function() {},
            content: content,  //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
            end: endCallback || function () {}
        });
        return;
    },
    /**
     * @desc: 打开弹出层
     * @param {string} title 弹出层title名
     * @param {string} content 弹出层内容
     * @param {string or Array} area 弹出层的宽高
     * @param {function} yesCallback 弹出层点击确认按钮后触发的事件，可不写
     *
     * @example:
     *  var Layer = new HuaFeng.Layer(); Layer.openIframe('添加地区', '<div id="add">' + $('#id').html() + '</div>', ['450px', '300px']);
     *  or
     *  var Layer = new HuaFeng.Layer(); Layer.openIframe('添加地区', '<div id="add">' + $('#id').html() + '</div>', ['450px', '300px'], function() {});
     * */
    open: function (title, content, area, yesCallback) {
        layer.open({
            title: title,
            type: 1,
            area: area,
            content: content, //当type为1时，采用HTML方式,   eg:'<div id="add">' + $('#id').html() + '</div>'
            btn: ['确认', '取消'],
            yes: yesCallback
        });
    },
    tip: function (content, icon) {
        layer.msg(content, {
            offset: 'rt',
            area: ['300px', '70px'],
            time: 4000,
            // anim: 6
            icon: icon
        });
        // layer.open({
        //     type: 1,
        //     content: '<div style="padding: 30px 0; text-align: center;">' + content + '</div>',
        //     shade: 0,
        //     offset: 'rt'
        // });
        return;
    }
};

HuaFeng.Layer = Layer;