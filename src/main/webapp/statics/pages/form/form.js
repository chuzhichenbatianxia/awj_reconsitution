(function () {
    var Http = new HuaFeng.Http();

    /*表单验证*/
    function validateForm() {
        $('#commentForm').validate({
            rules: {
                schoolPhone: 'isTel'
            },
            submitHandler: function (form) {
                event.preventDefault();
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
    /*$('#btn-submit').on('click', function () {
        $('#commentForm').validate({
            rules: {
                schoolPhone: 'isTel'
            },
            submitHandler: function (form) {
                event.preventDefault();
                var data = $(form).serialize();
                Http.post('../table/tableData.json', data, function(res) {
                    console.info(res);
                }, function(err) {
                    console.info(err);
                });
            }
        });
    });*/
})();