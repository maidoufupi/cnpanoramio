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

    $window.cnmap.MapService = function(map) {

        this.map = map || this.map;

        var geocoder;

        this.init = function(map, callback) {
            if(map) {
                if(map instanceof Function) {
                    callback = map;
                }else {
                    this.map = map;
                }
            }
            //加载地理编码插件
            this.map.plugin(["AMap.Geocoder"], function () { //加载地理编码插件
                geocoder = new AMap.Geocoder({
                    radius: 1000, //以已知坐标为中心点，radius为半径，返回范围内兴趣点和道路信息
                    extensions: "all" //返回地址描述以及附近兴趣点和道路信息，默认“base”
                });
                if(callback) {
                    callback.apply(geocoder, [geocoder]);
                }

            });
        }

        this.getAddress = function(lat, lng, callback) {
            var point = new AMap.LngLat(lng, lat);

            if (!geocoder) {
                this.init(function() {
                    ga();
                });
            }else {
                ga();
            }
            function ga() {
                AMap.event.addListenerOnce(geocoder, "complete", function(res) {
                    if (res.info == "OK") {
                        callback.apply(undefined, [res.regeocode.formattedAddress]);
                    }
                })
                geocoder.getAddress(point);
            }
        }

        this.getLocation = function (address, callback) {
            if (!geocoder) {
                this.init(function() {
                    ga();
                });
            }else {
                ga();
            }
            function ga() {
                AMap.event.addListenerOnce(geocoder, "complete", function(res) {
                    if (res.info == "OK") {
                        callback.apply(undefined, [res]);
                    }
                })
                geocoder.getLocation(address);
            }
        }
    }

    $window.cnmap.MapService.prototype = $window.cnmap.IMapService;
    $window.cnmap.MapService.factory = function() {
        return new $window.cnmap.MapService();
    }
    return $window.cnmap.MapService;
});