/**
 * Created with JetBrains WebStorm.
 * User: tiwen.wang
 * Date: 14-1-14
 * Time: 下午4:48
 * To change this template use File | Settings | File Templates.
 */

(function ($) {
    "use strict";

    $.explore = function (map) {
        var listener = qq.maps.event.addListener(
            map,
            'idle',
            setState
        );

        var hashObj = {};

        function setState() {
            var state = {};
            var latlng = this.getCenter(),
                zoom = this.getZoom();

            state['lat'] = latlng.lat;
            state['lng'] = latlng.lng;
            state['zoom'] = zoom;
            $.bbq.pushState(state);
        }

        $(window).bind( 'hashchange', function(e) {
            // Get the hash (fragment) as a string, with any leading # removed. Note that
            // in jQuery 1.4, you should use e.fragment instead of $.param.fragment().
            var url = $.param.fragment();
            var stateObj = $.deparam(url);

            if(stateObj.lat && stateObj.lng) {
                if(hashObj.lat != stateObj.lat ||
                    hashObj.lng != stateObj.lng ||
                    hashObj.zoom != stateObj.zoom ) {
                    hashObj = stateObj;
                    $.cnmap.setCenter(stateObj.lat, stateObj.lng, map);
                    if(stateObj.zoom) {
                        $.cnmap.setZoom(stateObj.zoom, map);
                    }
                }
            }
        })
    }
})(jQuery)