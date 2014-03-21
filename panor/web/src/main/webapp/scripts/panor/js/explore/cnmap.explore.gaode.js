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
        AMap.event.addListener(
            map,
            'zoomend',
            setState );

        AMap.event.addListener(
            map,
            'moveend',
            setState
        );

        var hashObj = {};
        // changeState状态标志，记录上次动作不超过0.5秒，不能进行state更改的触发
        // 高德地图有setCenter后取getCenter不完全一致问题
        var changeState = false;
        function setState() {

            if(changeState) {
                return;
            }
            var stateObj = {};
            var latlng = this.getCenter(),
                zoom = this.getZoom();

            if(latlng) {
                stateObj['lat'] = latlng.lat;
                stateObj['lng'] = latlng.lng;
                stateObj['zoom'] = zoom;
                if(hashObj.lat != stateObj.lat ||
                    hashObj.lng != stateObj.lng ||
                    hashObj.zoom != stateObj.zoom) {
                    if(this.setState) {
                        clearTimeout(this.setState);
                    }
                    changeState = true;
                    hashObj = stateObj;
                    $.bbq.pushState(stateObj);
                    this.setState = setTimeout(function() {
                        changeState = false;
                    }, 500);

                }
            }
        }

//        $(window).bind( 'hashchange', function(e) {
//            // Get the hash (fragment) as a string, with any leading # removed. Note that
//            // in jQuery 1.4, you should use e.fragment instead of $.param.fragment().
//            if(changeState) {
//                return;
//            }
//            var url = $.param.fragment();
//            var stateObj = $.deparam(url);
//
//            if(stateObj.lat && stateObj.lng) {
//                if(hashObj.lat != stateObj.lat ||
//                    hashObj.lng != stateObj.lng ||
//                    hashObj.zoom != stateObj.zoom ) {
//                    if(this.setState) {
//                        clearTimeout(this.setState);
//                    }
//                    changeState = true;
//                    hashObj = stateObj;
//                    $.cnmap.setCenter(stateObj.lat, stateObj.lng, map);
//                    if(stateObj.zoom) {
//                        $.cnmap.setZoom(stateObj.zoom, map);
//                    }
//                    this.setState = setTimeout(function() {
//                        changeState = false;
//                    }, 500);
//                }
//            }
//        })
    }
})(jQuery);