/**
 * Created by any on 14-4-13.
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

    $window.cnmap.IMapService = {

        /* 地图 */
        map: undefined,

        /**
         * 初始化地图
         *
         * @param map
         * @param callback function(Geocoder)
         */
        init: function(map, callback){},

        /**
         * 根据gps获取地址信息
         *
         * @param lat
         * @param lng
         * @param callback
         */
        getAddress: function(lat, lng, callback) {},

        /**
         * 根据地址获取gps信息
         *
         * @param address
         * @param callback
         */
        getLocation: function (address, callback) {}



    }
});