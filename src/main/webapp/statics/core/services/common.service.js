if (!HuaFeng) {
    var HuaFeng = {
    };
}

var Common = function () {

};
Common.prototype = {
    highlightNav: function () {
        var nav_list = $('.nav-item');
        var pathToCompare = handle_pathname(window.location.pathname);
        for(var i = 0; i < nav_list.length; i++) {
            nav_sub(i);
        }
        function nav_sub(index) {
            var cur_nav_sub = $(nav_list[index]).find('div.nav-sub-item');
            if (cur_nav_sub.length === 1) {
                var cur_nav_sub_a = $(cur_nav_sub).find('a');
                for(var i = 0; i < cur_nav_sub_a.length; i++) {
                    if (handle_pathname($(cur_nav_sub_a[i]).attr('href')) === pathToCompare) {
                        $(cur_nav_sub).show();
                        $(cur_nav_sub_a[i]).addClass('active');
                    }
                }
            } else {
                var cur_nav_a = $(nav_list[index]).find('a');
                for(var i = 0; i < cur_nav_a.length; i++) {
                    if (handle_pathname($(cur_nav_a[i]).attr('href')) === pathToCompare) {
                        $(nav_list[index]).addClass('active');
                    }
                }
            }
        }
        /*将当前链接进行处理，如/form/form.html，处理后成form/form，用于高亮导航时的比对*/
        function handle_pathname(path) {
            var pathname_split = path.split('/'),
                length = pathname_split.length,
                pathname_split_last = pathname_split[length - 1],
                pathname_split_last_split = pathname_split_last.split('.');
            return pathname_split[length - 2] + '/' + pathname_split_last_split[0].split('_')[0];
        }
    },
    /**
     * @desc: 点击显示或隐藏同级
     * @example:
     * <li class="nav-item">
     *      <a href="javascript:void(0);" class="nav-item-title" data-toggle-target=".nav-sub-item">表单 </a>
     *      <div class="nav-sub-item">
     *          <a href="form/form.html">表单验证</a>
     *      </div>
     * </li>
     * 在触发事件的DOM结构上添加data-toggle-target=""属性，其中例子中.nav-sub-item为需要显示或隐藏的对象class，也可为对象的id，
     * 为id是为#nav-sub-item
     * @notice: 注意事项：使用时触发事件的DOM必须和需要显示隐藏的DOM是同个级
    * */
    changeIcon: function (target) {
        var $_fa = target.find('.fa').last();
        if ($_fa.hasClass('fa-angle-double-down')) {
            $_fa.removeClass('fa-angle-double-down').addClass('fa-angle-double-right');
        } else {
            $_fa.removeClass('fa-angle-double-right').addClass('fa-angle-double-down');
        }
    },
    toggle: function () {
        var _this = this;
        $('[data-toggle-target]').on('click', function () {
            $(this).siblings($(this).attr('data-toggle-target')).toggle();
            _this.changeIcon($(this));
        });
    }
};
HuaFeng.Common = Common;

var hf = new HuaFeng.Common();
hf.highlightNav();
hf.toggle();
