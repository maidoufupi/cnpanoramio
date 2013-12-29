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
    $.cnmap.initMap = function (mapCanvas, opts) {
        opts = opts || {};

        map = new AMap.Map(mapCanvas, {resizeEnable: true});
        if (opts.toolbar) {
            map.plugin(["AMap.ToolBar"], function () {
                var toolBar = new AMap.ToolBar();
                map.addControl(toolBar);
            });
            map.plugin(["AMap.MapType"], function () {
                //地图类型切换
                var type = new AMap.MapType({
                    defaultType: 0 //使用2D地图
                });
                map.addControl(type);
            });
        }
        return map;
    }

})(jQuery)