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
           map = new BMap.Map(mapCanvas);          // 创建地图实例
           map.enableScrollWheelZoom();
           map.addControl(new BMap.NavigationControl());
           map.addControl(new BMap.ScaleControl());
           var overviewMapControl = new BMap.OverviewMapControl();
           overviewMapControl.changeView();
           map.addControl(overviewMapControl);
           map.addControl(new BMap.MapTypeControl());

           var latLng = new BMap.Point(102.8, 25);
           map.centerAndZoom(latLng, 13);
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
               $("#preview").html("");
               $("#preview").append(result);
//               $(".imgLiquidFill").imgLiquid({fill: true});
           })
       }
    }
})(jQuery)