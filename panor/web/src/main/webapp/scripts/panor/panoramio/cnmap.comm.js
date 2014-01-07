/**
 * Created with JetBrains WebStorm.
 * User: tiwen.wang
 * Date: 13-12-19
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */

/*!
 * jQuery cnmap common Plugin v1.4.0
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2013 Klaus Hartl
 * Released under the MIT license
 */
/**
 * baidu
 */
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as anonymous module.
        define(['jquery'], factory);
    } else {
        // Browser globals.
        factory(jQuery);
    }
}(function ($) {

// 全局命名空间
    $.cnmap = $.cnmap || {};
    $.cnmap.utils = $.cnmap.utils || {};

    var geocoder, geo_listener;

    $.cnmap.utils.getLocalCity = function (callback/*city*/) {
        var url = 'http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js';
        $.getScript(url).done(function () {
            callback.apply(null, [remote_ip_info.city]);
        });
    }

    $.cnmap.GPS = {
        /**
         * GPS point format convert
         * @param input
         */
        convert: function (input) {
            var output;
            if ((typeof input == 'string') && input.constructor == String) {
                var res = input.search(/[0-9]+,[0-9]+,[0-9]+\.[0-9]+/);
                if (res !== -1) {
                    var dms = input.split(",");
                    output = Number(dms[0]) + ( Number(dms[1]) + Number(dms[2]) / 60 ) / 60;
//                    output = dms[0] + "°" + dms[1] + "\'" + dms[2] + "\"";
                } else {
                    res = input.search(/[0-9]+°[0-9]+'([0-9]+|[0-9]+\.[0-9]+)"/);
                    if (res !== -1) {
                        var dms = input.split("°");
                        var degree = Number(dms[0]);
                        dms = dms[1].split("\'");
                        var minute = Number(dms[0]);
                        dms = dms[1].split("\"");
                        var second = Number(dms[0]);
                        output = degree + ( minute + second / 60 ) / 60;
                    }
                }
            } else if ((typeof input == 'number') && input.constructor == Number) {
                var degree = Math.floor(input);
                var minute = Math.floor((input - degree) * 60);
                var second = ((input - degree) * 60 - minute) * 60;
                second = second.toPrecision(6);
                output = degree + "°" + minute + "\'" + second + "\"";
            }
            return output;
        },
        /**
         * return the distance between 2 latLng couple into meters
         * Params :
         *  Lat1, Lng1, Lat2, Lng2
         *  LatLng1, Lat2, Lng2
         *  Lat1, Lng1, LatLng2
         *  LatLng1, LatLng2
         **/
        distanceInMeter: function () {
            var lat1, lat2, lng1, lng2, e, f, g, h;
            if (arguments[0] instanceof google.maps.LatLng) {
                lat1 = arguments[0].lat();
                lng1 = arguments[0].lng();
                if (arguments[1] instanceof google.maps.LatLng) {
                    lat2 = arguments[1].lat();
                    lng2 = arguments[1].lng();
                } else {
                    lat2 = arguments[1];
                    lng2 = arguments[2];
                }
            } else {
                lat1 = arguments[0];
                lng1 = arguments[1];
                if (arguments[2] instanceof google.maps.LatLng) {
                    lat2 = arguments[2].lat();
                    lng2 = arguments[2].lng();
                } else {
                    lat2 = arguments[2];
                    lng2 = arguments[3];
                }
            }
            e = Math.PI * lat1 / 180;
            f = Math.PI * lng1 / 180;
            g = Math.PI * lat2 / 180;
            h = Math.PI * lng2 / 180;
            return 1000 * 6371 * Math.acos(Math.min(Math.cos(e) * Math.cos(g) * Math.cos(f) * Math.cos(h) + Math.cos(e) * Math.sin(f) * Math.cos(g) * Math.sin(h) + Math.sin(e) * Math.sin(g), 1));
        }
    }

    $.cnmap.utils.baidu = {
        geocoder: null,
        init: function () {
            this.geocoder = new BMap.Geocoder();
        },
        convert: function (lat, lng, callback) {
            // require http://developer.baidu.com/map/jsdemo/demo/convertor.js
            var gpsPoint = new BMap.Point(lng, lat); //lng，lat为GPS经纬度
            BMap.Convertor.translate(gpsPoint, 0, callback)
        },
//        reverse: function(lat, lng, callback) {
//            this.convert(lat, lng, function(point) {
//                var nlat = 2*lat - point.lat;
//                var nlng = 2*lng - point.lng;
//                callback.apply(null, [nlat, nlng]);
//            })
//        },
        getGeoPoint: function (address, callback) {
            if(!this.geocoder) {
                this.init();
            }
            this.geocoder.getPoint(address, function (point) {
                callback.apply(null, point);
            })
        }
    };

    $.cnmap.utils.qq = {
        geocoder: null,
        init: function () {
            this.geocoder = new qq.maps.Geocoder();
        },
        getAddress: function(lat, lng, callback) {
            if(!this.geocoder) {
                this.init();
            }
            var latlng = new qq.maps.LatLng(lat, lng);
            this.geocoder.setComplete(callback);
            this.geocoder.getAddress(latlng);
        },
        getLocation: function(address, callback) {
            if(!this.geocoder) {
                this.init();
            }
            this.geocoder.setComplete(callback);
            this.geocoder.getLocation(address);
        }
    }

    $.cnmap.utils.gaode = {
        map: null,
        init: function () {
            //加载地理编码插件
            this.map.plugin(["AMap.Geocoder"], function() { //加载地理编码插件
                geocoder = new AMap.Geocoder({
                    radius: 1000, //以已知坐标为中心点，radius为半径，返回范围内兴趣点和道路信息
                    extensions: "all" //返回地址描述以及附近兴趣点和道路信息，默认“base”
                });
            });
        },
        getAddress: function(lat, lng, callback) {
            if(!geocoder) {
                this.init();
            }
            var latlng = new AMap.LngLat(lng, lat);
//            this.geocoder.regeocode(latlng, callback);
            if(geo_listener) {
                AMap.event.removeListener(geo_listener);
            }
            //返回地理编码结果
            geo_listener = AMap.event.addListener(geocoder, "complete", callback);
            //逆地理编码
            geocoder.getAddress(latlng);
        },
        getLocation: function(address, callback) {
            if(!geocoder) {
                this.init();
            }
//            this.geocoder.geocode(address, callback);
            if(geo_listener) {
                AMap.event.removeListener(geo_listener);
            }
            //返回地理编码结果
            geo_listener = AMap.event.addListener(geocoder, "complete", callback);
            //逆地理编码
            geocoder.getLocation(address);
        }
    }

}))