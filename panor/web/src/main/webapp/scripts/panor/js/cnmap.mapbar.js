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
           map = new Maplet(mapCanvas);
        if(opts.toolbar) {
            map.addControl(new MStandardControl());
        }
           return map;
    };

    $.cnmap.addMarkerInCenter = function(imap) {
        var thismap = imap || map;
        var marker = new MMarker(thismap.getCenter());
        thismap.addOverlay(marker);
    }

    $.cnmap.setCenter = function(lat, lng, imap) {
        var thismap = imap || map;
        var center = new MPoint(lng, lat);
        thismap.centerAndZoom(center, 8);
    }

    $.cnmap.panBy = function(x, y, imap) {
        var thismap = imap || map;
        thismap.moveTo(x, y);
    }
})(jQuery)