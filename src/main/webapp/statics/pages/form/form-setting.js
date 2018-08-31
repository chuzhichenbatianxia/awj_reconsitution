(function () {
    var datetimepickerNum = 14;
    for(var i = 1; i < datetimepickerNum; i++) {
        setDateTimePcick(i);
    }
    function setDateTimePcick(i) {
        $('#datetimepicker'+ i).datetimepicker({
            format: 'HH:mm'
        });
        $('#datetimepicker' + i + '_1').datetimepicker({
            format: 'HH:mm',
            useCurrent: false
        });
        $('#datetimepicker'+ i).on("dp.change", function (e) {
            $('#datetimepicker' + i + '_1').data("DateTimePicker").minDate(e.date);
        });
    }
})();