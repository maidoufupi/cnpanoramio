/**
 * Created by any on 14-4-12.
 */
'use strict';

(function (factory) {

    if (typeof define === 'function' && define.amd) {
        // AMD. Register as anonymous module.
        define([window, 'jquery', 'cnmap'], factory);
    } else {
        // Browser globals.
        factory(window, jQuery);
    }

})(function ($window, $jQuery) {

    $window.cnmap = $window.cnmap || {};

    $window.cnmap.IMapEventListener = {
        opts: {map: undefined},
        /**
         * 添加地图获取location hash值的监听接口
         *
         * @param map
         * @param callback function(lat, lng, zoom)
         */
        addLocationHashListener: function(map, callback) {},

        /**
         * 为地图添加ToolBar控件
         *
         * @param map
         */
        addToolBar: function(map) {},

        /**
         * 设置地图中心坐标
         *
         * @param map
         * @param lat
         * @param lng
         */
        setCenter: function(map, lat, lng){},

        /**
         * 设置地图边界
         *
         * @param map
         * @param sw
         * @param ne
         */
        setBounds: function(map, sw, ne){},

        /**
         * 判断gps坐标是否在地图视野内
         *
         * @param lat
         * @param lng
         * @param map
         */
        inMapView: function(lat, lng, map){},

        /**
         * 创建可移动的marker
         *
         * @param map
         * @param lat
         * @param lng
         */
        createDraggableMarker: function(map, lat, lng){},

        /**
         * 激活marker
         *
         * @param marker
         */
        activeMarker: function(marker){},

        /**
         * 取消激活marker
         *
         * @param marker
         */
        deactiveMarker: function(marker){},

        /**
         * 为marker添加激活状态的监听接口
         *
         * @param marker
         * @param callback function()
         */
        addMarkerActiveListener: function(marker, callback){},

        /**
         * 为marker添加移动结束后的监听接口
         *
         * @param marker
         * @param callback function(lat, lng)
         */
        addDragendListener: function(marker, callback){},

        /**
         * 为地图添加点击监听
         *
         * @param map
         * @param callback function(lat, lng)
         */
        addMapClickListener: function(map, callback) {},

        /**
         * 为目标对象设置位置
         *
         * @param target
         * @param lat
         * @param lng
         */
        setPosition: function(target, lat, lng){},

        /**
         * 为目标对象设置地图并show
         *
         * @param target
         * @param map
         */
        setMap: function(target, map){}
    };

});