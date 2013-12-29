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
    var marker;
    var template_preview_thumb;

    $.cnmap = $.cnmap || {};
    $.cnmap.explore = {
       initMap: function(mapCanvas) {
           map = new qq.maps.Map(document.getElementById(mapCanvas));
           return map;
       },
       setPanoramioLayer: function() {
           var panoramioLayer = new $.cnmap.PanoramioLayer({suppressInfoWindows: true});
           panoramioLayer.setMap(map);

           if (tmpl) {
               template_preview_thumb = tmpl("template-preview-thumb");
           }
           $(panoramioLayer).bind("data_changed", function(e, data) {
               var result = template_preview_thumb({
                   items: data
               });
//               $(result).find(".imgLiquidFill").imgLiquid({fill: true});
               $("#preview").html("");
               $("#preview").append(result);
//               $(".imgLiquidFill").imgLiquid({fill: true});
           })
       }
    }
})(jQuery)