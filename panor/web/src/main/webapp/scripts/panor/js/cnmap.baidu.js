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

        map = new BMap.Map(mapCanvas);          // 创建地图实例

        if(opts.scrollzoom) {
            map.enableScrollWheelZoom();
        }
        if(opts.toolbar) {
            map.addControl(new BMap.NavigationControl());
            map.addControl(new BMap.ScaleControl());
            map.addControl(new BMap.MapTypeControl());
        }
        if(opts.overview)  {
            var overviewMapControl = new BMap.OverviewMapControl();
            overviewMapControl.changeView();
            map.addControl(overviewMapControl);
        }
        if(opts.locatecity) {
            var latLng = new BMap.Point(102.8, 25);
            map.centerAndZoom(latLng, 13);
        }

        return map;
    }
})(jQuery)