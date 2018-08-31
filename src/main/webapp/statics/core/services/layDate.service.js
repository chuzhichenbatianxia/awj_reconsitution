
/**
 * 使用这个插件，需引入<script src="../../core/services/layDate.service.js"></script>
 * */

if (!HuaFeng) {
    var HuaFeng = {
    };
}

var LayDate = function () {
    this.dateRangeFunc = function(startId, endId) {
        var start = {
            elem: startId,
            format: 'YYYY-MM-DD hh:mm:ss',
            min: laydate.now(), //设定最小日期为当前日期
            max: '2099-06-16 23:59:59', //最大日期
            istime: true,
            istoday: false,
            choose: function(datas){
                end.min = datas; //开始日选好后，重置结束日的最小日期
                end.start = datas; //将结束日的初始值设定为开始日
            }
        };
        var end = {
            elem: endId,
            format: 'YYYY-MM-DD hh:mm:ss',
            min: laydate.now(),
            max: '2099-06-16 23:59:59',
            istime: true,
            istoday: false,
            choose: function(datas){
                start.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        return {start: start,end:end};
    };
};

LayDate.prototype = {
    /**
     * @desc：日期范围选择
     * @param {string} startId 开始日期的id
     * @param {string} endId 结束日期的id
     * @example:
     *  html: 开始日：<input id="start" class="laydate-icon">
     *        结束日：<input id="end" class="laydate-icon">
     *  js:  var LayDate = new HuaFeng.LayDate(); LayDate.dateRange('#start', '#end');
     * */
    dateRange: function (startId, endId) {
        var setting = this.dateRangeFunc(startId, endId);
        laydate(setting.start);
        laydate(setting.end);
        return;
    },
    /**
     * @desc：日期
     * @param {string} dateId 日期控件id
     * @example:
     *  html: 开始日：<input id="date" class="laydate-icon">
     *  js:  var LayDate = new HuaFeng.LayDate(); LayDate.date('#date');
     * */
    date: function (dateId) {
        laydate({
            elem: dateId,
            format: 'YYYY-MM-DD'
        });
        return;
    },
    dateTime:function (dateId) {
        laydate({
            elem: dateId,
            format: 'YYYY-MM-DD hh:mm:ss',
            istime: true
        });
        return;
    },
    time: function (dateId) {
        // var start = {
        //     elem: '#start',
        //     format: 'YYYY/MM/DD hh:mm:ss',
        //     min: laydate.now(), //设定最小日期为当前日期
        //     max: '2099-06-16 23:59:59', //最大日期
        //     istime: true,
        //     istoday: false,
        //     choose: function(datas){
        //         end.min = datas; //开始日选好后，重置结束日的最小日期
        //         end.start = datas //将结束日的初始值设定为开始日
        //     }
        // };
        laydate({
            elem: dateId,
            format: 'hh:mm:ss',
            istime: true
        });
        return;
    },
    /**
     * @desc：规范日期格式
     * @example:
     *  js:  var LayDate = new HuaFeng.LayDate(); LayDate.formatDate();  //获取当前时间
     *  js:  var LayDate = new HuaFeng.LayDate(); LayDate.formatDate(1444971065);  //转换时间格式， 1444971065为时间戳格式
     *  @return {
     *      defaultFormat: "1970-02-18 01:22",
     *      localFormat: "1970-02-18T01:22",
     *      longFormat: "1970-02-18 01:22:30",
     *      shortFormat: "1970-02-18",
     *      lastShortFormat: "1970-01-18",
     *      yearToMouthToDayFormat: "1970年02月18日",
     *      yearToMouthFormat: "1970年02月",
     *      mouthToDayFormat: "02.18"
     *  }
     * */
    formatDate: function () {
        var timestamp,
            time = arguments[0];
        timestamp = !!time ? new Date(time) : new Date();
        var year = timestamp.getFullYear(),
            month = timestamp.getMonth() + 1,
            day = timestamp.getDate(),
            hour = timestamp.getHours(),
            minute = timestamp.getMinutes(),
            second = timestamp.getSeconds();
        month  = month >9 ? month: "0" + month;
        day  = day >9 ? day: "0" + day;
        hour  = hour >9 ? hour: "0" + hour;
        minute  = minute >9 ? minute: "0" + minute;
        second  = second >9 ? second: "0" + second;
        return {
            defaultFormat : year + "-" + month + "-" + day + " " + hour + ":" + minute,
            localFormat: year + "-" + month + "-" + day + "T" + hour + ":" + minute,
            longFormat: year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second,
            shortFormat : year + "-" + month + "-" + day,
            lastShortFormat : year + "-" + parseInt(month - 1) + "-" + day,  //上一页
            yearToMouthToDayFormat: year + "年" + month + "月" + day + "日",
            yearToMouthFormat: year + "年" + month + "月",
            mouthToDayFormat: month + "." + day
        };
    }
};

HuaFeng.LayDate = LayDate;