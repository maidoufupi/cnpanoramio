/**
 * Created with JetBrains WebStorm.
 * User: tiwen.wang
 * Date: 13-12-15
 * Time: 下午5:45
 * To change this template use File | Settings | File Templates.
 */
(function ($) {
    "use strict";

    var map;
    var geocoder;

    $.cnmap = $.cnmap || {};

    $.cnmap.initMap = function(mapCanvas, opts) {
           map = new qq.maps.Map(document.getElementById(mapCanvas));
           return map;
    };
})(jQuery)