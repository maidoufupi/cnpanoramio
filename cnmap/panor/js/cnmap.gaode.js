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
        }

        if (opts.maptype == "SATELLITE") {
            map.plugin(["AMap.MapType"], function () {
                //地图类型切换
                /* MaptypeOptions	 类型	 说明
                   defaultType  	Number	 初始化默认图层类型。 取值为0：2D地图 取值为1：卫星图 默认值：0
                   showTraffic	    Boolean	 叠加实时交通图层 默认值：false
                   showRoad 	    Boolean	 叠加路网图层 默认值：false
                 */
                var type = new AMap.MapType({
                    defaultType: 1,
                    showRoad: true
                });
                map.addControl(type);
            });
        }else if(opts.maptype) {
            map.plugin(["AMap.MapType"], function () {
                var type = new AMap.MapType({
                    defaultType: 0,
                    showRoad: true
                });
                map.addControl(type);
            });
        }

        if(opts.overview) {
            map.plugin(["AMap.OverView"],function(){
                //加载鹰眼
                var view = new AMap.OverView({
                    isOpen: true
                });
                map.addControl(view);
            });
        }
        return map;
    }

    $.cnmap.addMarkerInCenter = function(imap) {
        var thismap = imap || map;
        var marker = new AMap.Marker();
        marker.setMap(thismap);
        marker.setPosition(thismap.getCenter());
        marker.show();
    }

    $.cnmap.setCenter = function(lat, lng, imap) {
        var thismap = imap || map;
        var center = new AMap.LngLat(lng, lat);
        thismap.setCenter(center);
    }

    $.cnmap.panBy = function(x, y, imap) {
        var thismap = imap || map;
        thismap.panBy(x, y);
    }

    $.cnmap.setZoom = function(zoom, imap) {
        var thismap = imap || map;
        thismap.setZoom(zoom);
    }

})(jQuery)

/*
 HYBRID	This map type displays a transparent layer of major streets on satellite images.
 ROADMAP	This map type displays a normal street map.
 SATELLITE	This map type displays satellite images.
 TERRAIN	This map type displays maps with physical features such as terrain and vegetation.
 */