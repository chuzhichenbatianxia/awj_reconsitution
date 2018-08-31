/**
 * @summary: ajax请求
 * @param {string}  url  接口名称
 * @param {object}  data  传参
 * @param {function} successCallback：请求成功回调函数
 * @param {function} errorCallback：请求失败回调函数
 * @usage:
 * var Http = new HuaFeng.Http();
 * Http.get(url, data, function(res) {}, function(err) {});  //get请求
 * Http.post(url, data, function(res) {}, function(err) {});  //post请求
 * Http.jsonp(url, data, function(res) {}, function(err) {});  //jsonp请求
 *
 * @warning:
 * 使用前页面请确保已引用
 * <script src="../../libs/layer/layer/layer.js"></script>
 * <script src="../../core/services/ajax.js"></script>
 * */
// var backendUrl = 'http://login.xxy.com:8080/sso'; //本地
var backendUrl = '';
if (!HuaFeng) {
    var HuaFeng = {
    };
}
var Http = function() {
    var Layer = new HuaFeng.Layer();
    /**
     * @param {string} method  请求的方法，默认为GET请求
     * @param {string} url  接口名称
     * @param {object} data  传参
     * @param {string} dataType 预期服务器返回的数据类型， 默认为 JSON
     // * @param {number} timeout 接口请求超时时间，默认为1分钟
     * @param {function} successCallback：请求成功回调函数
     * @param {function} errorCallback：请求失败回调函数
     */
    this.baseAjax = function (method, url, data, successCallback, errorCallback, dataType) {
        $.ajax({
            method: method || 'GET',
            async: false,
            url: backendUrl + url,
            data: data || {},
            dataType:  dataType || "json",
            timeout: 10000,
            statusCode: statusCode()
        }).done(function (res) {
            if (res) {
                if (successCallback) {
                    successCallback(res);
                }
            } else {
                if (errorCallback) {
                    errorCallback(res);
                }
            }
        }).fail(function (reject) {
        });
    };
    /*处理错误的code值*/
    function statusCode() {
        return {
            503: function () {
                Layer.tip('服务器出错', 2);
            },
            400: function () {
                Layer.tip('错误的请求', 2);
            },
            404: function () {
                Layer.tip('页面或接口没找到', 2);
            }
        };
    }
};
Http.prototype = {
    get: function (url, data, successCallback, errorCallback) {
        this.baseAjax('GET', url, data, successCallback, errorCallback);
    },
    post: function (url, data, successCallback, errorCallback) {
        this.baseAjax('POST', url, data, successCallback, errorCallback);
    },
    jsonp: function (url, data, successCallback, errorCallback) {
        this.baseAjax('GET', url, data, successCallback, errorCallback, 'jsonp');
    }
};
HuaFeng.Http = Http;