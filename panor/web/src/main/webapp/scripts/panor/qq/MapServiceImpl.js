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
            geocoder = new qq.maps.Geocoder();
        }

        this.getAddress = function(lat, lng, callback) {
            var point = new qq.maps.LatLng(lat, lng);

            geocoder.setComplete(function(res) {
                if(res["type"] == "GEO_INFO") {
                    callback.apply(undefined, [res["detail"]["address"]]);
                }
            });

            geocoder.getAddress(point);
        }

        this.getLocation = function (address, callback) {
            geocoder.setComplete(function(res) {
                callback.apply(undefined, [res]);
            })

            geocoder.getLocation(address);
        }
    }

    $window.cnmap.MapService.prototype = $window.cnmap.IMapService;
    $window.cnmap.MapService.factory = function() {
        return new $window.cnmap.MapService();
    }
    return $window.cnmap.MapService;
});