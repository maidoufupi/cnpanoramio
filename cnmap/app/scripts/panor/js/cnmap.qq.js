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

    $.cnmap.addMarkerInCenter = function(imap) {
        var thismap = imap || map;
        var marker = new qq.maps.Marker();
        marker.setMap(thismap);
        marker.setPosition(thismap.getCenter());
        marker.setVisible(true);
    }

    $.cnmap.setCenter = function(lat, lng, imap) {
        var thismap = imap || map;
        var center = new qq.maps.LatLng(lat, lng);
        thismap.setCenter(center);
    }

    $.cnmap.setZoom = function(zoom, imap) {
        var thismap = imap || map;
        thismap.setZoom(zoom);
    }

    $.cnmap.panBy = function(x, y, imap) {
        var thismap = imap || map;
        thismap.panBy(-x, -y);
    }

    $.cnmap.getCenter = function(imap) {
        var thismap = imap || map;
        return thismap.getCenter();
    }

    $.cnmap.getLat = function(latlng) {
        return latlng.lat;
    }

    $.cnmap.getLng = function(latlng) {
        return latlng.lng;
    }
})(window)