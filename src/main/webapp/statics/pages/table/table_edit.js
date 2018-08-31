(function () {
    var Http = new HuaFeng.Http();
    var Layer = new HuaFeng.Layer();

    var change = false;
    $('form').on('change', function () {
        change = true;
    });
    /*表单验证*/
    function validateForm() {
        $('#commentForm').validate({
            rules: {
                schoolPhone: 'isTel'
            },
            submitHandler: function (form) {
                submitForm();
            }
        });
    }
    /*表单数据提交*/
    function submitForm() {
        Http.post('../table/tableData.json', {}, function(res) {
            console.info(res);
        }, function(err) {
            console.info(err);
        });
    }
    $('#btn-submit').on('click', function () {
        validateForm();
    });

    $('#btn-cancel').on('click', function () {
        if (change) {
            Layer.confirm('确定返回？', {title: '提示', icon: 7}, function(index) {
                history.go(-1);
            });
        } else {
            history.go(-1);
        }
    });
})();