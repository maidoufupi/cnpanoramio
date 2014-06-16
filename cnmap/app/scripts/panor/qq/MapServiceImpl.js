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

        // 1:行政区划中心点 2:道路中心点 3:道路交叉口 4:估算的门址数据 5:POI（如银科大厦、第三极书局这种类型的） 6:门址
        var gpsTypeZoom = {
            '1': 7,
            '2': 15,
            '3': 17,
            '4': 18,
            '5': 18,
            '6': 19
        };

        this.map = map || this.map;

        var geocoder;

        this.init = function(map, callback) {
            geocoder = new qq.maps.Geocoder();
        };

        this.getAddress = function(lat, lng, callback) {
            var point = new qq.maps.LatLng(lat, lng);

            geocoder.setComplete(function(res) {
                if(res["type"] == "GEO_INFO") {
                    callback.apply(undefined, [res["detail"]["address"]]);
                }
            });

            geocoder.getAddress(point);
        };

        this.getAddrPois = function(lat, lng, callback) {
            var point = new qq.maps.LatLng(lat, lng);
            geocoder.setComplete(function(res) {
                if(res["type"] == "GEO_INFO") {
                    var regeocode = res.detail;
                    var addresses = {};
                    angular.forEach(regeocode.nearPois, function(poi, key) {
                        addresses[poi.address + " " + poi.name] = {
                            poiweight: poi.dist,
                            location: poi.latLng
                        };
                    });
                    callback.apply(undefined, [addresses, regeocode.address]);
                }
            });

            geocoder.getAddress(point);
        };

        this.getLocation = function (address, callback) {
            geocoder.setComplete(function(res) {
                callback.apply(undefined, [res]);
            });

            geocoder.getLocation(address);
        };

        this.getLocPois = function(address, callback) {
            geocoder.setComplete(function(res) {
                var addresses = [];
                if(res.type == "GEO_INFO") {
                    var detail = res.detail;
                    addresses.push({
                        address: detail.address,
                        location: detail.location,
                        similarity: detail.similarity,
                        zoom: gpsTypeZoom[detail.gps_type] || 3
                    });
                    angular.forEach(detail.similarResults, function(similar, key) {
                        addresses.push({
                            address: similar.address,
                            location: similar.location,
                            similarity: similar.similarity,
                            zoom: gpsTypeZoom[detail.gps_type] || 3
                        });
                    });
                }
                callback.apply(undefined, [addresses]);
            });

            geocoder.getLocation(address);
        };

    };

    $window.cnmap.MapService.prototype = $window.cnmap.IMapService;
    $window.cnmap.MapService.factory = function() {
        return new $window.cnmap.MapService();
    };
    return $window.cnmap.MapService;
});