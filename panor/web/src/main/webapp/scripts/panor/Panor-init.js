/**
 * Created with JetBrains WebStorm.
 * User: tiwen.wang
 * Date: 13-11-30
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */

(function ($) {
    "use strict";

    $.initIndexMap = function (map) {

        var latLng = map.LatLng(25, 102.8);
        map.panTo(25, 102.8, 13);

//        map.InfoWindow({map: map, position: latLng, content:"<img src=\"1.jpg\" style=\"width: 34px; height: 34px;\"></img>"});
        map.setMarker(latLng);

    };

})(jQuery);